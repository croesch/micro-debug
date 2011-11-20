/*
 *
 *  Mic1Instruction.java
 *
 *  mic1 microarchitecture simulator 
 *  Copyright (C) 1999, Prentice-Hall, Inc. 
 * 
 *  This program is free software; you can redistribute it and/or modify 
 *  it under the terms of the GNU General Public License as published by 
 *  the Free Software Foundation; either version 2 of the License, or 
 *  (at your option) any later version. 
 * 
 *  This program is distributed in the hope that it will be useful, but 
 *  WITHOUT ANY WARRANTY; without even the implied warranty of 
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General 
 *  Public License for more details. 
 * 
 *  You should have received a copy of the GNU General Public License along with 
 *  this program; if not, write to: 
 * 
 *    Free Software Foundation, Inc. 
 *    59 Temple Place - Suite 330 
 *    Boston, MA 02111-1307, USA. 
 * 
 *  A copy of the GPL is available online the GNU web site: 
 * 
 *    http://www.gnu.org/copyleft/gpl.html
 * 
 */
package com.github.croesch.mic1.controlstore;

import com.github.croesch.mic1.Register;

/**
 * Mic1Instruction This class represents an instruction which might appear in the Mic-1 control store. To Do We should
 * probably create a separate class for Mic1Reader and move the read method from here to the Mic1Reader class.
 * Modification History Name Date Comment ---------------- ---------- ---------------------------------------- Ray Ontko
 * 1998.09.01 Created
 */
public final class Mic1Instruction {

  /** bit mask for the value of address */
  private static final int ADDRESS_MASK = 0x1ff;

  /** MIR[35:27] - bits that used for calculation of next MPC - basic address */
  private final int nextAddress;

  /** MIR[26:24]: set of bits that are basic for calculation of next MPC */
  private final Mic1JMPSignalSet jmpSignals = new Mic1JMPSignalSet();

  /** MIR[23:16]: set of bits that are responsible for the behavior of the ALU and the shifter */
  private final Mic1ALUSignalSet aluSignals = new Mic1ALUSignalSet();

  /** MIR[15:7]: set of bits that are responsible for the registers that are filled with the C-Bus value */
  private final Mic1CBusSignalSet cBusSignals = new Mic1CBusSignalSet();

  /** MIR[6:4]: set of bits that are responsible for communication with external memory (main memory and program memory) */
  private final Mic1MemorySignalSet memorySignals = new Mic1MemorySignalSet();

  /** responsible which register's value is written on the B-Bus */
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
  public Mic1Instruction(final int addr,
                         final Mic1JMPSignalSet jmpSet,
                         final Mic1ALUSignalSet aluSet,
                         final Mic1CBusSignalSet cBusSet,
                         final Mic1MemorySignalSet memSet,
                         final Register b) {
    this.nextAddress = addr & ADDRESS_MASK;
    this.bBusSelect = b;

    this.jmpSignals.copyOf(jmpSet);
    this.aluSignals.copyOf(aluSet);
    this.cBusSignals.copyOf(cBusSet);
    this.memorySignals.copyOf(memSet);
  }

  @Override
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
    final Mic1Instruction other = (Mic1Instruction) obj;
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
   * Returns the {@link Mic1JMPSignalSet} of this instruction.
   * 
   * @since Date: Nov 13, 2011
   * @return a copy of the {@link Mic1JMPSignalSet} of this instruction.
   */
  public Mic1JMPSignalSet getJmpSignals() {
    final Mic1JMPSignalSet set = new Mic1JMPSignalSet();
    set.copyOf(this.jmpSignals);
    return set;
  }

  /**
   * Returns the {@link Mic1ALUSignalSet} of this instruction.
   * 
   * @since Date: Nov 13, 2011
   * @return a copy of the {@link Mic1ALUSignalSet} of this instruction.
   */
  public Mic1ALUSignalSet getAluSignals() {
    final Mic1ALUSignalSet set = new Mic1ALUSignalSet();
    set.copyOf(this.aluSignals);
    return set;
  }

  /**
   * Returns the {@link Mic1CBusSignalSet} of this instruction.
   * 
   * @since Date: Nov 13, 2011
   * @return a copy of the {@link Mic1CBusSignalSet} of this instruction.
   */
  public Mic1CBusSignalSet getCBusSignals() {
    final Mic1CBusSignalSet set = new Mic1CBusSignalSet();
    set.copyOf(this.cBusSignals);
    return set;
  }

  /**
   * Returns the {@link Mic1MemorySignalSet} of this instruction.
   * 
   * @since Date: Nov 13, 2011
   * @return a copy of the {@link Mic1MemorySignalSet} of this instruction.
   */
  public Mic1MemorySignalSet getMemorySignals() {
    final Mic1MemorySignalSet set = new Mic1MemorySignalSet();
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
   * @return the {@link Mic1BBusRegister} that defines the register being written on the B-Bus.
   */
  public Register getbBusSelect() {
    return this.bBusSelect;
  }
}
