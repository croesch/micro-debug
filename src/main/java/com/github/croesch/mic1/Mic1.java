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
package com.github.croesch.mic1;

import java.io.InputStream;
import java.util.logging.Logger;

import com.github.croesch.commons.Printer;
import com.github.croesch.commons.Settings;
import com.github.croesch.commons.Utils;
import com.github.croesch.error.FileFormatException;
import com.github.croesch.i18n.Text;
import com.github.croesch.mic1.alu.Alu;
import com.github.croesch.mic1.controlstore.ALUSignalSet;
import com.github.croesch.mic1.controlstore.CBusSignalSet;
import com.github.croesch.mic1.controlstore.Mic1ControlStore;
import com.github.croesch.mic1.controlstore.Mic1Instruction;
import com.github.croesch.mic1.controlstore.Mic1InstructionDecoder;
import com.github.croesch.mic1.controlstore.Mic1JMPSignalSet;
import com.github.croesch.mic1.controlstore.Mic1MemorySignalSet;
import com.github.croesch.mic1.io.Input;
import com.github.croesch.mic1.io.Output;
import com.github.croesch.mic1.mem.Memory;
import com.github.croesch.mic1.mpc.NextMPCCalculator;
import com.github.croesch.mic1.register.Register;
import com.github.croesch.mic1.shifter.Shifter;
import com.github.croesch.ui.TraceManager;
import com.github.croesch.ui.api.Mic1View;

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
  private final Alu alu = new Alu();

  /** the shifter belonging to the ALU of the processor */
  private final Shifter shifter = new Shifter();

  /** calculator for next MPC value */
  private NextMPCCalculator mpcCalculator;

  /** store of the micro program */
  private final Mic1ControlStore controlStore;

  /** current instruction */
  private Mic1Instruction instruction;

  /** current value of mpc */
  private int oldMpc;

  /** the main memory of the processor */
  private final Memory memory;

  /** the view that is able to present details of this processor to the user */
  private final Mic1View view;

  /** the manager for break points */
  private final BreakpointManager bpm = new BreakpointManager();

  /** counter for ticks that have been executed */
  private int ticks;

  /** stores the current address of the ijvm-instruction being executed */
  private int lastMacroAddress;

  /**
   * Constructs a new Mic1-processor, reading the given inputstreams as micro-program and assembler-program.
   * 
   * @since Date: Nov 21, 2011
   * @param micAsm the micro-assembler-program
   * @param asm the assembler-program
   * @throws FileFormatException if one of the streams contains a file with the wrong file format
   */
  public Mic1(final InputStream micAsm, final InputStream asm) throws FileFormatException {

    this.controlStore = createMic1ControlStore(micAsm);
    this.memory = createMemory(asm, Settings.MIC1_MEM_MACRO_MAXSIZE.getValue());

    if (this.controlStore == null || this.memory == null) {
      // inform the caller about the problem
      throw new FileFormatException();
    }

    this.view = new TraceManager(this.memory);

    init();
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
   * Tries to create the memory of the processor and prints an error if one occurred.
   * 
   * @since Date: Dec 3, 2011
   * @param asm the input stream to pass to the {@link Memory#Memory(int, InputStream)}
   * @param maxSize the maximum size of the memory
   * @return the constructed memory, or <code>null</code> if an error occurred
   * @see Memory#Memory(int, InputStream)
   */
  private Memory createMemory(final InputStream asm, final int maxSize) {
    try {
      return new Memory(maxSize, asm);
    } catch (final FileFormatException e) {
      LOGGER.severe(e.getMessage());
      Printer.printErrorln(Text.WRONG_FORMAT_IJVM.text());
      return null;
    }
  }

  /**
   * Tries to create the control store of the processor and prints an error if one occurred.
   * 
   * @since Date: Dec 3, 2011
   * @param micAsm the input stream to pass to the {@link Mic1ControlStore#Mic1ControlStore(InputStream)}
   * @return the constructed control store, or <code>null</code> if an error occurred
   * @see Mic1ControlStore#Mic1ControlStore(InputStream)
   */
  private Mic1ControlStore createMic1ControlStore(final InputStream micAsm) {
    try {
      return new Mic1ControlStore(micAsm);
    } catch (final FileFormatException e) {
      LOGGER.severe(e.getMessage());
      Printer.printErrorln(Text.WRONG_FORMAT_MIC1.text());
      return null;
    }
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
      this.lastMacroAddress = Register.PC.getValue();
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
    return this.mpcCalculator.getMpc() == Settings.MIC1_MICRO_ADDRESS_IJVM.getValue()
    // Going to first instruction needs two invocations of this instruction so check if it's the first one
           && Register.PC.getValue() != Settings.MIC1_REGISTER_PC_DEFVAL.getValue();
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
    resetTicks();
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

    resetTicks();
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
    return !isHaltInstruction()
           && (isFirstTick() || !this.bpm.isBreakpoint(this.mpcCalculator.getMpc(), Register.PC.getValue()));
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
   * Fetches the next instruction from the {@link Mic1ControlStore} and stores the address from where the instruction
   * has been fetched.
   * 
   * @since Date: Jan 14, 2012
   */
  private void fetchNextInstruction() {
    this.instruction = this.controlStore.getInstruction(this.mpcCalculator.getMpc());
    this.oldMpc = this.mpcCalculator.getMpc();
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
    final Mic1JMPSignalSet jmpSignals = this.instruction.getJmpSignals();
    this.mpcCalculator.setJmpC(jmpSignals.isJmpC());
    this.mpcCalculator.setJmpN(jmpSignals.isJmpN());
    this.mpcCalculator.setJmpZ(jmpSignals.isJmpZ());
    // calculate next mpc
    this.mpcCalculator.calculate();

    // fetch signals for memory operations
    final Mic1MemorySignalSet memSignals = this.instruction.getMemorySignals();
    this.memory.setFetch(memSignals.isFetch());
    this.memory.setRead(memSignals.isRead());
    this.memory.setWrite(memSignals.isWrite());
    // fetch values of PC, MAR and MDR and set it to the memory
    this.memory.setByteAddress(Register.PC.getValue());
    this.memory.setWordAddress(Register.MAR.getValue());
    this.memory.setWordValue(Register.MDR.getValue());
    // let the memory do its work
    this.memory.poke();
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
    resetTicks();
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
   * Resets the counter of executed ticks to zero.
   * 
   * @since Date: Jan 16, 2012
   */
  private void resetTicks() {
    this.ticks = 0;
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
    if (this.instruction != null && this.instruction.isNopOrHalt() && this.mpcCalculator.getMpc() == this.oldMpc) {
      // regular halt condition
      halt = true;
    } else if (this.controlStore.getInstruction(this.mpcCalculator.getMpc()) == null) {
      // instruction points to an undefined position
      LOGGER.warning("instruction at " + Utils.toHexString(this.oldMpc) + " ["
                     + Mic1InstructionDecoder.decode(this.instruction) + "] points to an undefined address: "
                     + Utils.toHexString(this.mpcCalculator.getMpc()));
      halt = true;
    }

    if (halt && this.instruction != null) {
      LOGGER.finer("found halt: (" + Mic1InstructionDecoder.decode(this.instruction) + ")");
    }
    return halt;
  }

  /**
   * Lists the values of all {@link Register}s.
   * 
   * @since Date: Jan 15, 2012
   */
  public void listAllRegisters() {
    this.view.listAllRegisters();
  }

  /**
   * Lists the value of a single {@link Register}.
   * 
   * @since Date: Jan 15, 2012
   * @param r the {@link Register} to print with its value.
   */
  public void listSingleRegister(final Register r) {
    this.view.listRegister(r);
  }

  /**
   * Performs to trace the local variable with the given number.
   * 
   * @since Date: Feb 8, 2012
   * @param num the number of the local variable in the given macro code method as an offset to the LV.
   */
  public void traceLocalVariable(final int num) {
    this.view.traceLocalVariable(num);
  }

  /**
   * Performs to trace the macro code.
   * 
   * @since Date: Feb 3, 2012
   */
  public void traceMacro() {
    this.view.traceMacro();
  }

  /**
   * Performs to not trace the macro code.
   * 
   * @since Date: Feb 3, 2012
   */
  public void untraceMacro() {
    this.view.untraceMacro();
  }

  /**
   * Performs to trace the micro code.
   * 
   * @since Date: Jan 21, 2012
   */
  public void traceMicro() {
    this.view.traceMicro();
  }

  /**
   * Performs to trace all {@link Register}s.
   * 
   * @since Date: Jan 15, 2012
   */
  public void traceRegister() {
    this.view.traceRegister();
  }

  /**
   * Performs to trace the given {@link Register}.
   * 
   * @since Date: Jan 15, 2012
   * @param r the {@link Register} to trace.
   */
  public void traceRegister(final Register r) {
    this.view.traceRegister(r);
  }

  /**
   * Performs to not trace the local variable with the given number.
   * 
   * @since Date: Feb 8, 2012
   * @param num the number of the local variable in the given macro code method as an offset to the LV.
   */
  public void untraceLocalVariable(final int num) {
    this.view.untraceLocalVariable(num);
  }

  /**
   * Performs to not trace the micro code.
   * 
   * @since Date: Jan 21, 2012
   */
  public void untraceMicro() {
    this.view.untraceMicro();
  }

  /**
   * Performs to not trace any {@link Register}.
   * 
   * @since Date: Jan 15, 2012
   */
  public void untraceRegister() {
    this.view.untraceRegister();
  }

  /**
   * Performs to not trace the given {@link Register} anymore.
   * 
   * @since Date: Jan 15, 2012
   * @param r the {@link Register} not being traced anymore.
   */
  public void untraceRegister(final Register r) {
    this.view.untraceRegister(r);
  }

  /**
   * Returns whether the given {@link Register} is currently traced.
   * 
   * @since Date: Jan 15, 2012
   * @param r the {@link Register} to check, if it's traced
   * @return <code>true</code>, if the {@link Register} is currently traced<br>
   *         <code>false</code> otherwise.
   */
  public boolean isTracing(final Register r) {
    return this.view.isTracing(r);
  }

  /**
   * Tells the view to update itself.
   * 
   * @since Date: Jan 15, 2012
   * @param macroCodeFetching <code>true</code> if the next macro code instruction has being fetched,<br>
   *        <code>false</code> otherwise
   */
  private void update(final boolean macroCodeFetching) {
    if (macroCodeFetching) {
      this.view.update(this.instruction, this.lastMacroAddress);
    } else {
      this.view.update(this.instruction, -1);
    }
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
   * Prints the whole ijvm code to the user.
   * 
   * @since Date: Jan 23, 2012
   */
  public void printMacroCode() {
    this.memory.printCode();
  }

  /**
   * Prints the given number of lines of code around the current line to the user.
   * 
   * @since Date: Jan 26, 2012
   * @param scope the number of lines to print before and after the current line
   */
  public void printMacroCode(final int scope) {
    this.memory.printCodeAroundLine(Math.max(0, this.lastMacroAddress), scope);
  }

  /**
   * Prints the whole ijvm code to the user. Between the given line numbers.
   * 
   * @since Date: Jan 26, 2012
   * @param from the first line to print
   * @param to the last line to print
   */
  public void printMacroCode(final int from, final int to) {
    this.memory.printCode(from, to);
  }

  /**
   * Prints the whole micro code to the user.
   * 
   * @since Date: Feb 5, 2012
   */
  public void printMicroCode() {
    this.controlStore.printCode();
  }

  /**
   * Prints the given number of lines of micro code around the current line to the user.
   * 
   * @since Date: Feb 5, 2012
   * @param scope the number of lines to print before and after the current line
   */
  public void printMicroCode(final int scope) {
    this.controlStore.printCodeAroundLine(Math.max(0, this.oldMpc), scope);
  }

  /**
   * Prints the micro code to the user. Between the given line numbers.
   * 
   * @since Date: Feb 5, 2012
   * @param from the first line to print
   * @param to the last line to print
   */
  public void printMicroCode(final int from, final int to) {
    this.controlStore.printCode(from, to);
  }

  /**
   * Adds a breakpoint for the given {@link Register} and the given value. Debugger will break, if the given
   * {@link Register} has the given value.
   * 
   * @since Date: Jan 27, 2012
   * @param r the {@link Register} to watch for the given value
   * @param value the value that should be a break point if the given {@link Register} has it.
   */
  public void addRegisterBreakpoint(final Register r, final Integer value) {
    this.bpm.addRegisterBreakpoint(r, value);
  }

  /**
   * Adds a breakpoint for the given line number in the micro code.
   * 
   * @since Date: Feb 4, 2012
   * @param line the line number in micro code the debugger should break at
   */
  public void addMicroBreakpoint(final Integer line) {
    this.bpm.addMicroBreakpoint(line);
  }

  /**
   * Adds a breakpoint for the given line number in the macro code.
   * 
   * @since Date: Feb 4, 2012
   * @param line the line number in macro code the debugger should break at
   */
  public void addMacroBreakpoint(final Integer line) {
    this.bpm.addMacroBreakpoint(line);
  }

  /**
   * Removes the breakpoint with the given unique id.
   * 
   * @since Date: Jan 30, 2012
   * @param id the unique id of the breakpoint to remove
   */
  public void removeBreakpoint(final int id) {
    this.bpm.removeBreakpoint(id);
  }

  /**
   * Lists all breakpoints.
   * 
   * @since Date: Jan 28, 2012
   */
  public void listBreakpoints() {
    this.bpm.listBreakpoints();
  }

  /**
   * Prints the content of the memory between the given addresses.
   * 
   * @since Date: Jan 29, 2012
   * @param pos1 the address to start (inclusive)
   * @param pos2 the address to end (inclusive)
   */
  public void printContent(final int pos1, final int pos2) {
    this.memory.printContent(pos1, pos2);
  }

  /**
   * Prints the content of the stack. Technical speaking it prints the content of the memory between the initial stack
   * pointer value and the current value of the stack (inclusive edges).
   * 
   * @since Date: Feb 5, 2012
   */
  public void printStack() {
    this.memory.printStack();
  }
}
