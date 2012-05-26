/*
 * Copyright (C) 2011-2012  Christian Roesch
 * 
 * This file is part of micro-debug.
 * 
 * micro-debug is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * micro-debug is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with micro-debug.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.github.croesch.micro_debug.mic1;

import java.io.InputStream;
import java.util.logging.Logger;

import com.github.croesch.micro_debug.annotation.NotNull;
import com.github.croesch.micro_debug.annotation.Nullable;
import com.github.croesch.micro_debug.commons.Printer;
import com.github.croesch.micro_debug.commons.Utils;
import com.github.croesch.micro_debug.error.MacroFileFormatException;
import com.github.croesch.micro_debug.error.MicroFileFormatException;
import com.github.croesch.micro_debug.i18n.Text;
import com.github.croesch.micro_debug.mic1.alu.Alu;
import com.github.croesch.micro_debug.mic1.api.IProcessorInterpreter;
import com.github.croesch.micro_debug.mic1.controlstore.ALUSignalSet;
import com.github.croesch.micro_debug.mic1.controlstore.CBusSignalSet;
import com.github.croesch.micro_debug.mic1.controlstore.JMPSignalSet;
import com.github.croesch.micro_debug.mic1.controlstore.MemorySignalSet;
import com.github.croesch.micro_debug.mic1.controlstore.MicroControlStore;
import com.github.croesch.micro_debug.mic1.controlstore.MicroInstruction;
import com.github.croesch.micro_debug.mic1.controlstore.MicroInstructionDecoder;
import com.github.croesch.micro_debug.mic1.io.Input;
import com.github.croesch.micro_debug.mic1.io.Output;
import com.github.croesch.micro_debug.mic1.mem.Memory;
import com.github.croesch.micro_debug.mic1.mpc.NextMPCCalculator;
import com.github.croesch.micro_debug.mic1.register.Register;
import com.github.croesch.micro_debug.mic1.shifter.Shifter;
import com.github.croesch.micro_debug.settings.Settings;

/**
 * This class represents the CISC-processor being debugged by this program.
 * 
 * @author croesch
 * @since Date: Nov 20, 2011
 */
public final class Mic1 {

  /** the {@link Logger} for this class */
  private static final Logger LOGGER = Logger.getLogger(Mic1.class.getName());

  /** the ALU of the processor */
  @NotNull
  private final Alu alu = new Alu();

  /** the shifter belonging to the ALU of the processor */
  @NotNull
  private final Shifter shifter = new Shifter();

  /** calculator for next MPC value */
  @NotNull
  private NextMPCCalculator mpcCalculator;

  /** store of the micro program */
  @NotNull
  private final MicroControlStore controlStore;

  /** current instruction */
  @Nullable
  private MicroInstruction instruction;

  /** current value of mpc */
  private int oldMpc;

  /** the main memory of the processor */
  @NotNull
  private final Memory memory;

  /** counter for ticks that have been executed */
  private int ticks;

  /** stores the current address of the ijvm-instruction being executed */
  private int lastMacroAddress;

  /** interpreter of this processor */
  @Nullable
  private IProcessorInterpreter interpreter = null;

  /** stores if the processor has been interrupted while executing ticks */
  private boolean interrupted = false;

  /**
   * Constructs a new Mic1-processor, reading the given inputstreams as micro-program and assembler-program.
   * 
   * @since Date: Nov 21, 2011
   * @param micAsm the micro-assembler-program
   * @param asm the assembler-program
   * @throws MacroFileFormatException if the macro assembler program has invalid format
   * @throws MicroFileFormatException if the micro assembler program has invalid format
   */
  public Mic1(final InputStream micAsm, final InputStream asm) throws MacroFileFormatException,
                                                              MicroFileFormatException {

    this.controlStore = new MicroControlStore(micAsm);
    this.memory = new Memory(Settings.MIC1_MEM_MACRO_MAXSIZE.getValue(), asm);

    init();
  }

  /**
   * Sets the interpreter of this processor.
   * 
   * @since Date: Feb 13, 2012
   * @param ip the interpreter to store.
   */
  public void setProcessorInterpreter(final IProcessorInterpreter ip) {
    this.interpreter = ip;
  }

  /**
   * Initializes the {@link Register}s, the current instructions and the {@link NextMPCCalculator}.
   * 
   * @since Date: Jan 27, 2012
   */
  private void init() {
    initRegisters();

    this.mpcCalculator = new NextMPCCalculator();
    this.ticks = 0;
    this.lastMacroAddress = -1;
    this.oldMpc = -1;
    this.instruction = null;
  }

  /**
   * Resets the processor to its start values, except the debugging options. So {@link Register}s and current
   * instructions will be reset, but breakpoints or tracing options not.
   * 
   * @since Date: Jan 27, 2012
   */
  public void reset() {
    init();

    this.memory.reset();
    Input.reset();
    Output.reset();
  }

  /**
   * Initializes the registers values.
   * 
   * @since Date: Dec 1, 2011
   */
  private void initRegisters() {
    Register.CPP.setValue(Settings.MIC1_REGISTER_CPP_DEFVAL.getValue());
    Register.H.setValue(Settings.MIC1_REGISTER_H_DEFVAL.getValue());
    Register.LV.setValue(Settings.MIC1_REGISTER_LV_DEFVAL.getValue());
    Register.MAR.setValue(Settings.MIC1_REGISTER_MAR_DEFVAL.getValue());
    Register.MBR.setValue(Settings.MIC1_REGISTER_MBR_DEFVAL.getValue());
    Register.MDR.setValue(Settings.MIC1_REGISTER_MDR_DEFVAL.getValue());
    Register.OPC.setValue(Settings.MIC1_REGISTER_OPC_DEFVAL.getValue());
    Register.PC.setValue(Settings.MIC1_REGISTER_PC_DEFVAL.getValue());
    Register.SP.setValue(Settings.MIC1_REGISTER_SP_DEFVAL.getValue());
    Register.TOS.setValue(Settings.MIC1_REGISTER_TOS_DEFVAL.getValue());
  }

  /**
   * Performs the execution of the next instruction.
   * 
   * @since Date: Nov 21, 2011
   */
  void doTick() {
    final boolean assemblerCodeFetchingInstruction = isAssemblerCodeFetchingInstruction();
    if (assemblerCodeFetchingInstruction) {
      this.lastMacroAddress = getNextMacroAddress();
    }

    doClock1();
    doClock2();
    doClock3();

    update(assemblerCodeFetchingInstruction);
    ++this.ticks;
  }

  /**
   * Returns whether the current instruction is the assembler code fetching instruction that invokes a micro method.
   * 
   * @since Date: Jan 29, 2012
   * @return <code>true</code> if this instruction fetches the next assembler byte command to invoke the next micro
   *         method.
   */
  private boolean isAssemblerCodeFetchingInstruction() {
    return getNextMpc() == Settings.MIC1_MICRO_ADDRESS_IJVM.getValue()
    // Going to first instruction needs two invocations of this instruction so check if it's the first one
           && getNextMacroAddress() != Settings.MIC1_REGISTER_PC_DEFVAL.getValue();
  }

  /**
   * If the processor hasn't reached the halt instruction this executes one micro instruction.<br>
   * The number of effectively executed instructions is printed to the user.
   * 
   * @since Date: Jan 16, 2012
   */
  public void microStep() {
    microStep(1);
  }

  /**
   * Executes the given number of micro instructions. If one of them is the halt-instruction, it'll execute less than
   * the given number.<br>
   * The number of effectively executed instructions is printed to the user.
   * 
   * @since Date: Jan 16, 2012
   * @param number micro instructions to execute, if possible.
   */
  public void microStep(final int number) {
    softReset();
    while (this.ticks < number && canContinue()) {
      doTick();
    }
    printTicks();
  }

  /**
   * If the processor hasn't reached the halt instruction this executes one macro instruction.<br>
   * The number of effectively executed micro instructions is printed to the user.
   * 
   * @since Date: Jan 29, 2012
   */
  public void step() {
    step(1);
  }

  /**
   * Executes the given number of macro instructions. If one of the executed micro instructions is the halt-instruction,
   * it'll execute less than the given number of macro instructions.<br>
   * The number of effectively executed micro instructions is printed to the user.
   * 
   * @since Date: Jan 29, 2012
   * @param steps the number of macro instructions to execute
   */
  public void step(final int steps) {
    int step = 0;

    softReset();
    while (step < steps && canContinue()) {
      doTick();
      if (!isFirstTick() && isAssemblerCodeFetchingInstruction()) {
        ++step;
      }
    }
    printTicks();
  }

  /**
   * Returns whether the processor is executing its first tick.
   * 
   * @since Date: Jan 29, 2012
   * @return <code>true</code> if the current tick is the first tick
   */
  private boolean isFirstTick() {
    return this.ticks == 0;
  }

  /**
   * Returns whether the debugger can continue executing instructions or if it should stop.
   * 
   * @since Date: Jan 27, 2012
   * @return <code>true</code>, if the debugger can continue executing instructions,<br>
   *         <code>false</code> otherwise
   */
  private boolean canContinue() {
    return !isInterrupted() && !isHaltInstruction() && doesInterpreterAllowToContinue();
  }

  /**
   * Returns whether the interpreter allows to continue.
   * 
   * @since Date: May 26, 2012
   * @return <code>true</code>, if it's the first tick or the interpreter allows to continue,<br>
   *         or <code>false</code> if it's not the first tick and the interpreter doesn't allow to continue.
   */
  private boolean doesInterpreterAllowToContinue() {
    return isFirstTick()
           || this.interpreter == null
           || this.interpreter.canContinue(getNextMpc(), getNextMacroAddress(), this.instruction,
                                           this.controlStore.getInstruction(getNextMpc()));
  }

  /**
   * Performs the execution of the first part of a instruction - clock number one.
   * 
   * @since Date: Nov 21, 2011
   */
  private void doClock1() {
    // fetching instruction
    fetchNextInstruction();
    final ALUSignalSet aluSignals = this.instruction.getAluSignals();
    // setting the signals
    this.alu.setEnA(aluSignals.isEnA()).setEnB(aluSignals.isEnB());
    this.alu.setF0(aluSignals.isF0()).setF1(aluSignals.isF1());
    this.alu.setInc(aluSignals.isInc()).setInvA(aluSignals.isInvA());
    // set A and B-Bus
    this.alu.setA(Register.H.getValue());
    this.alu.setB(this.instruction.getbBusSelect().getValue());
    // run ALU
    this.alu.calculate();

    // setting signals
    this.shifter.setSLL8(aluSignals.isSLL8());
    this.shifter.setSRA1(aluSignals.isSRA1());
    // setting input from ALU
    this.shifter.setInput(this.alu.getOut());
    // run shifter
    this.shifter.calculate();
  }

  /**
   * Fetches the next instruction from the {@link MicroControlStore} and stores the address from where the instruction
   * has been fetched.
   * 
   * @since Date: Jan 14, 2012
   */
  private void fetchNextInstruction() {
    this.instruction = this.controlStore.getInstruction(getNextMpc());
    this.oldMpc = getNextMpc();
  }

  /**
   * Performs the execution of the first part of a instruction - clock number two.
   * 
   * @since Date: Nov 21, 2011
   */
  private void doClock2() {
    setValueIntoRegisters(this.shifter.getOutput(), this.instruction.getCBusSignals());

    // set N and Z
    this.mpcCalculator.setN(this.alu.isN());
    this.mpcCalculator.setZ(this.alu.isZ());

    // fill MBR and MDR
    this.memory.fillRegisters(Register.MDR, Register.MBR);
  }

  /**
   * Performs the execution of the first part of a instruction - clock number three.
   * 
   * @since Date: Nov 21, 2011
   */
  private void doClock3() {
    // fetch address and MBR for calculation of mpc
    this.mpcCalculator.setAddr(this.instruction.getNextAddress());
    this.mpcCalculator.setMbr((byte) Register.MBR.getValue());

    // fetch signals for calculation of mpc
    final JMPSignalSet jmpSignals = this.instruction.getJmpSignals();
    this.mpcCalculator.setJmpC(jmpSignals.isJmpC());
    this.mpcCalculator.setJmpN(jmpSignals.isJmpN());
    this.mpcCalculator.setJmpZ(jmpSignals.isJmpZ());
    // calculate next mpc
    this.mpcCalculator.calculate();

    // fetch signals for memory operations
    final MemorySignalSet memSignals = this.instruction.getMemorySignals();
    this.memory.setFetch(memSignals.isFetch());
    this.memory.setRead(memSignals.isRead());
    this.memory.setWrite(memSignals.isWrite());
    // fetch values of PC, MAR and MDR and set it to the memory
    this.memory.setByteAddress(Register.PC.getValue());
    this.memory.setWordAddress(Register.MAR.getValue());
    this.memory.setWordValue(Register.MDR.getValue());
    // let the memory do its work
    this.memory.doTick();
  }

  /**
   * Sets the value of the C-Bus into the selected registers.
   * 
   * @since Date: Nov 21, 2011
   * @param value the value of the C-Bus.
   * @param cBusSignals the signals that determine, which registers are selected.
   */
  private static void setValueIntoRegisters(final int value, final CBusSignalSet cBusSignals) {
    if (cBusSignals.isCpp()) {
      Register.CPP.setValue(value);
    }
    if (cBusSignals.isH()) {
      Register.H.setValue(value);
    }
    if (cBusSignals.isLv()) {
      Register.LV.setValue(value);
    }
    if (cBusSignals.isMar()) {
      Register.MAR.setValue(value);
    }
    if (cBusSignals.isMdr()) {
      Register.MDR.setValue(value);
    }
    if (cBusSignals.isOpc()) {
      Register.OPC.setValue(value);
    }
    if (cBusSignals.isPc()) {
      Register.PC.setValue(value);
    }
    if (cBusSignals.isSp()) {
      Register.SP.setValue(value);
    }
    if (cBusSignals.isTos()) {
      Register.TOS.setValue(value);
    }
  }

  /**
   * Executes all instructions until the end of the program.
   * 
   * @since Date: Jan 14, 2012
   * @return the number of ticks that this method executed.
   */
  public int run() {
    softReset();
    while (canContinue()) {
      doTick();
    }
    printTicks();

    return this.ticks;
  }

  /**
   * Prints the currently executed number of ticks, if its greater than zero.
   * 
   * @since Date: Jan 21, 2012
   */
  private void printTicks() {
    if (this.ticks > 0) {
      Printer.println(Text.TICKS.text(this.ticks));
    }
  }

  /**
   * Resets the counter of executed ticks to zero and resets the interrupted flag.
   * 
   * @since Date: Jan 16, 2012
   */
  private void softReset() {
    this.ticks = 0;
    this.interrupted = false;
  }

  /**
   * Returns whether the current instruction is the halt-instruction. If the instruction only is a goto that points to
   * itself, then this is considered to be a halt instruction.<br>
   * If the instruction points to an address where no instruction is defined, then this will also be handled as a
   * halt-instruction.
   * 
   * @since Date: Jan 14, 2012
   * @return <code>true</code>, if the current instruction causes the processor to halt
   */
  public boolean isHaltInstruction() {
    boolean halt = false;

    // instruction should be >null< only at the beginning of the program
    if (this.instruction != null && this.instruction.isNopOrHalt() && getNextMpc() == this.oldMpc) {
      // regular halt condition
      halt = true;
    } else if (this.controlStore.getInstruction(getNextMpc()) == null) {
      // instruction points to an undefined position
      LOGGER.warning("instruction at " + Utils.toHexString(this.oldMpc) + " ["
                     + MicroInstructionDecoder.decode(this.instruction) + "] points to an undefined address: "
                     + Utils.toHexString(getNextMpc()));
      halt = true;
    }

    if (halt && this.instruction != null) {
      LOGGER.finer("found halt: (" + MicroInstructionDecoder.decode(this.instruction) + ")");
    }
    return halt;
  }

  /**
   * Tells the view to update itself.
   * 
   * @since Date: Jan 15, 2012
   * @param macroCodeFetching <code>true</code> if the next macro code instruction has being fetched,<br>
   *        <code>false</code> otherwise
   */
  private void update(final boolean macroCodeFetching) {
    this.interpreter.tickDone(this.instruction, macroCodeFetching);
  }

  /**
   * Sets the value of the memory at the given address to the given value.
   * 
   * @since Date: Jan 21, 2012
   * @param addr the address, where to set the new value
   * @param val the new value
   */
  public void setMemoryValue(final int addr, final int val) {
    this.memory.setWord(addr, val);
  }

  /**
   * Returns the value of the memory at the given address.
   * 
   * @since Date: Jan 21, 2012
   * @param addr the address to read the value from the memory.
   * @return the value fetched from the memory at the given address
   */
  public int getMemoryValue(final int addr) {
    return this.memory.getWord(addr);
  }

  /**
   * Returns the main memory.
   * 
   * @since Date: Feb 13, 2012
   * @return the {@link Memory} of this processor.
   */
  @NotNull
  public Memory getMemory() {
    return this.memory;
  }

  /**
   * Returns the address of the last executed code line.
   * 
   * @since Date: Feb 13, 2012
   * @return the address of the last executed code line.
   */
  public int getLastMacroAddress() {
    return this.lastMacroAddress;
  }

  /**
   * Returns the address of the code line that'll be executed next.
   * 
   * @since Date: May 11, 2012
   * @return the address of the code line that'll be executed next.
   */
  public int getNextMacroAddress() {
    return Register.PC.getValue();
  }

  /**
   * Returns the address of the last executed micro code line.
   * 
   * @since Date: Feb 14, 2012
   * @return the address of the last executed micro code line.
   */
  public int getOldMpc() {
    return this.oldMpc;
  }

  /**
   * Returns the address of the micro code line that'll be executed by the next tick.
   * 
   * @since Date: May 11, 2012
   * @return the address of the micro code line that'll be executed by the next tick.
   */
  public int getNextMpc() {
    return this.mpcCalculator.getMpc();
  }

  /**
   * Returns the control store of micro instructions.
   * 
   * @since Date: Feb 14, 2012
   * @return {@link MicroControlStore} storing the micro code of the processor
   */
  @NotNull
  public MicroControlStore getControlStore() {
    return this.controlStore;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + this.controlStore.hashCode();
    result = prime * result + this.memory.hashCode();
    result = prime * result + this.mpcCalculator.hashCode();
    result = prime * result + this.oldMpc;
    return result;
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final Mic1 other = (Mic1) obj;
    if (!this.controlStore.equals(other.controlStore)) {
      return false;
    }
    if (!this.memory.equals(other.memory)) {
      return false;
    }
    if (!this.mpcCalculator.equals(other.mpcCalculator)) {
      return false;
    }
    if (this.oldMpc != other.oldMpc) {
      return false;
    }
    return true;
  }

  /**
   * Interrupts the processor.
   * 
   * @since Date: May 26, 2012
   */
  public void interrupt() {
    this.interrupted = true;
  }

  /**
   * Returns whether the processor is interrupted.
   * 
   * @since Date: May 26, 2012
   * @return <code>true</code>, if the processor has been interrupted while executing ticks,<br>
   *         or <code>false</code> if the processor hasn't been interrupted while executing the last ticks.
   */
  public boolean isInterrupted() {
    return this.interrupted;
  }
}
