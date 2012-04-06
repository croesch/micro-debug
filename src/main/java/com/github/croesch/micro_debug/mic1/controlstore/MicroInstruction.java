/*
 * Copyright (C) 1999, Prentice-Hall, Inc.
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
package com.github.croesch.micro_debug.mic1.controlstore;

import com.github.croesch.micro_debug.annotation.NotNull;
import com.github.croesch.micro_debug.annotation.Nullable;
import com.github.croesch.micro_debug.mic1.register.Register;

/**
 * Represents an instruction in the micro code. Based on the implementation of <em>Ray Ontko</em>. <br>
 * <br>
 * MicroInstruction This class represents an instruction which might appear in the Mic-1 control store. <br>
 * Ray Ontko<br>
 * 1998.09.01 Created
 */
public final class MicroInstruction {

  /** bit mask for the value of address */
  private static final int ADDRESS_MASK = 0x1ff;

  /** MIR[35:27] - bits that used for calculation of next MPC - basic address */
  private final int nextAddress;

  /** MIR[26:24]: set of bits that are basic for calculation of next MPC */
  @NotNull
  private final JMPSignalSet jmpSignals = new JMPSignalSet();

  /** MIR[23:16]: set of bits that are responsible for the behavior of the ALU and the shifter */
  @NotNull
  private final ALUSignalSet aluSignals = new ALUSignalSet();

  /** MIR[15:7]: set of bits that are responsible for the registers that are filled with the C-Bus value */
  @NotNull
  private final CBusSignalSet cBusSignals = new CBusSignalSet();

  /** MIR[6:4]: set of bits that are responsible for communication with external memory (main memory and program memory) */
  @NotNull
  private final MemorySignalSet memorySignals = new MemorySignalSet();

  /** responsible which register's value is written on the B-Bus */
  @Nullable
  private final Register bBusSelect;

  /**
   * Constructs a single mic1-instruction.
   * 
   * @since Date: Nov 13, 2011
   * @param addr contains the MIR[35:27]. Only the lowest nine bits are fetched, it contains the value that is used to
   *        calculate next value of MPC.
   * @param jmpSet MIR[26:24]: set of bits that are basic for calculation of next MPC
   * @param aluSet MIR[23:16]: set of bits that are responsible for the behavior of the ALU and the shifter
   * @param cBusSet MIR[15:7]: set of bits that are responsible for the registers that are filled with the C-Bus value
   * @param memSet MIR[6:4]: set of bits that are responsible for communication with external memory (main memory and
   *        program memory) details see the comments of the fields or the script of Karl Stroetmann.
   * @param b contains the MIR[3:0]. Only the lowest four bits are fetched, it contains the bits that are used to define
   *        which register's value is written to the B-Bus.
   */
  public MicroInstruction(final int addr,
                          final JMPSignalSet jmpSet,
                          final ALUSignalSet aluSet,
                          final CBusSignalSet cBusSet,
                          final MemorySignalSet memSet,
                          final Register b) {
    this.nextAddress = addr & ADDRESS_MASK;
    this.bBusSelect = b;

    this.jmpSignals.copyOf(jmpSet);
    this.aluSignals.copyOf(aluSet);
    this.cBusSignals.copyOf(cBusSet);
    this.memorySignals.copyOf(memSet);
  }

  @Override
  @NotNull
  public String toString() {
    return Integer.toBinaryString(this.nextAddress) + "_" + this.jmpSignals + "_" + this.aluSignals + "_"
           + this.cBusSignals + "_" + this.memorySignals + "_" + this.bBusSelect;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result *= prime;
    if (this.bBusSelect != null) {
      result += this.bBusSelect.hashCode();
    }
    result = prime * result + this.nextAddress;
    result = prime * result + this.aluSignals.hashCode();
    result = prime * result + this.jmpSignals.hashCode();
    result = prime * result + this.cBusSignals.hashCode();
    result = prime * result + this.memorySignals.hashCode();
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
    final MicroInstruction other = (MicroInstruction) obj;
    if (this.bBusSelect != other.bBusSelect) {
      return false;
    }
    if (this.nextAddress != other.nextAddress) {
      return false;
    }
    if (!this.jmpSignals.equals(other.jmpSignals)) {
      return false;
    }
    if (!this.aluSignals.equals(other.aluSignals)) {
      return false;
    }
    if (!this.cBusSignals.equals(other.cBusSignals)) {
      return false;
    }
    return this.memorySignals.equals(other.memorySignals);
  }

  /**
   * Returns the {@link JMPSignalSet} of this instruction.
   * 
   * @since Date: Nov 13, 2011
   * @return a copy of the {@link JMPSignalSet} of this instruction.
   */
  @NotNull
  public JMPSignalSet getJmpSignals() {
    final JMPSignalSet set = new JMPSignalSet();
    set.copyOf(this.jmpSignals);
    return set;
  }

  /**
   * Returns the {@link ALUSignalSet} of this instruction.
   * 
   * @since Date: Nov 13, 2011
   * @return a copy of the {@link ALUSignalSet} of this instruction.
   */
  @NotNull
  public ALUSignalSet getAluSignals() {
    final ALUSignalSet set = new ALUSignalSet();
    set.copyOf(this.aluSignals);
    return set;
  }

  /**
   * Returns the {@link CBusSignalSet} of this instruction.
   * 
   * @since Date: Nov 13, 2011
   * @return a copy of the {@link CBusSignalSet} of this instruction.
   */
  @NotNull
  public CBusSignalSet getCBusSignals() {
    final CBusSignalSet set = new CBusSignalSet();
    set.copyOf(this.cBusSignals);
    return set;
  }

  /**
   * Returns the {@link MemorySignalSet} of this instruction.
   * 
   * @since Date: Nov 13, 2011
   * @return a copy of the {@link MemorySignalSet} of this instruction.
   */
  @NotNull
  public MemorySignalSet getMemorySignals() {
    final MemorySignalSet set = new MemorySignalSet();
    set.copyOf(this.memorySignals);
    return set;
  }

  /**
   * Returns the next address of this instruction.
   * 
   * @since Date: Nov 13, 2011
   * @return the address that is basic for calculation of next MPC address.
   */
  public int getNextAddress() {
    return this.nextAddress;
  }

  /**
   * Returns the value that defines the register that'll be written on the B-Bus.
   * 
   * @since Date: Nov 13, 2011
   * @return the {@link Register} that defines the register being written on the B-Bus.
   */
  @Nullable
  public Register getbBusSelect() {
    return this.bBusSelect;
  }

  /**
   * Returns whether this instruction simply points to another one without changing something.
   * 
   * @since Date: Jan 14, 2012
   * @return <code>true</code>, if this instruction is simply like <code>goto 0x..</code>
   */
  public boolean isNopOrHalt() {
    if (this.jmpSignals.isAnythingSet()) {
      return false;
    }
    if (this.aluSignals.isAnythingSet()) {
      return false;
    }
    if (this.cBusSignals.isAnythingSet()) {
      return false;
    }
    if (this.memorySignals.isAnythingSet()) {
      return false;
    }
    return true;
  }
}
