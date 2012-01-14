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

import com.github.croesch.console.io.Printer;
import com.github.croesch.error.FileFormatException;
import com.github.croesch.i18n.Text;
import com.github.croesch.mic1.alu.Alu;
import com.github.croesch.mic1.controlstore.Mic1ALUSignalSet;
import com.github.croesch.mic1.controlstore.Mic1CBusSignalSet;
import com.github.croesch.mic1.controlstore.Mic1ControlStore;
import com.github.croesch.mic1.controlstore.Mic1Instruction;
import com.github.croesch.mic1.controlstore.Mic1InstructionDecoder;
import com.github.croesch.mic1.controlstore.Mic1JMPSignalSet;
import com.github.croesch.mic1.controlstore.Mic1MemorySignalSet;
import com.github.croesch.mic1.mem.Memory;
import com.github.croesch.mic1.mpc.NextMPCCalculator;
import com.github.croesch.mic1.register.Register;
import com.github.croesch.mic1.shifter.Shifter;
import com.github.croesch.misc.Settings;
import com.github.croesch.misc.Utils;

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
  private final NextMPCCalculator mpcCalculator = new NextMPCCalculator();

  /** store of the micro program */
  private final Mic1ControlStore controlStore;

  /** current instruction */
  private Mic1Instruction instruction = null;

  /** current value of mpc */
  private int oldMpc = -1;

  /** the main memory of the processor */
  private final Memory memory;

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

    //TODO implement maximum size of memory as argument
    final int maxSize = 0x10000;
    this.memory = createMemory(asm, maxSize);

    if (this.controlStore == null || this.memory == null) {
      // inform the caller about the problem
      throw new FileFormatException();
    }

    initRegisters();
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
      Printer.printErrorln(Text.WRONG_FORMAT_IJVM.text(e.getMessage()));
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
      Printer.printErrorln(Text.WRONG_FORMAT_MIC1.text(e.getMessage()));
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
   * Performs one execution of the processor.
   * 
   * @since Date: Nov 21, 2011
   */
  public void doTick() {
    doClock1();
    doClock2();
    doClock3();
  }

  /**
   * Performs the execution of the first part of a instruction - clock number one.
   * 
   * @since Date: Nov 21, 2011
   */
  private void doClock1() {
    // fetching instruction
    fetchNextInstruction();
    final Mic1ALUSignalSet aluSignals = this.instruction.getAluSignals();
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
  private static void setValueIntoRegisters(final int value, final Mic1CBusSignalSet cBusSignals) {
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
    int ticks = 0;
    while (!isHaltInstruction()) {
      doTick();
      ++ticks;
    }
    return ticks;
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
}
