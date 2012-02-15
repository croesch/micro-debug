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
package com.github.croesch.micro_debug.mic1.mpc;

/**
 * This class represents a calculator for the next micro-program-counter (MPC). It is based on the verilog code for
 * 'calculation of MPC' in the script of the lecture 'Rechnertechnik' of Karl Stroetmann.
 * 
 * @author croesch
 * @since Date: Nov 7, 2011
 */
public final class NextMPCCalculator {

  /** mask to select lowest nine bits */
  private static final int LOW_NINE_BITS = 0x1FF;

  /** mask to select bit number eight */
  private static final int BIT_EIGHT = 0x100;

  /** mask to select lowest eight bits */
  private static final int LOW_EIGHT_BITS = 0xFF;

  // input signals

  /** the value of the register MBR */
  private byte mbr = 0;

  /** the value of the part in the MIR[Addr] */
  private int addr = 0;

  /** whether the control line JMPC (MIR[26]) is set */
  private boolean jmpC;

  /** whether the control line JMPN (MIR[25]) is set */
  private boolean jmpN;

  /** whether the control line JMPZ (MIR[24]) is set */
  private boolean jmpZ;

  /** the value of the control line N, fetched from the ALU */
  private boolean n;

  /** the value of the control line Z, fetched from the ALU */
  private boolean z;

  // input signals

  /** the calculated 9-bit-value for the value of the next MPC */
  private int mpc = 0;

  // methods

  /**
   * Performs calculation of the output signals based on the current values of input signals.
   * 
   * @since Date: Nov 7, 2011
   */
  public void calculate() {
    if (this.jmpC) {
      // MPC[7:0] = MBR | Addr[7:0];
      this.mpc = (this.mbr | this.addr) & LOW_EIGHT_BITS;
    } else {
      // MPC[7:0] = Addr[7:0];
      this.mpc = this.addr & LOW_EIGHT_BITS;
    }
    if ((this.jmpN && this.n) || (this.jmpZ && this.z)) {
      // MPC[8] = 1;
      this.mpc |= BIT_EIGHT;
    } else {
      // MPC[8] = Addr[8];
      this.mpc |= this.addr & BIT_EIGHT;
    }
  }

  /**
   * Returns the calculated 9-bit-value. This is the value for the next MPC.
   * 
   * @since Date: Nov 7, 2011
   * @return the 9-bit-value for the MPC
   */
  public int getMpc() {
    return this.mpc;
  }

  /**
   * Sets the value of the register MBR.
   * 
   * @since Date: Nov 7, 2011
   * @param mbrValue the byte fetched from the register MBR
   */
  public void setMbr(final byte mbrValue) {
    this.mbr = mbrValue;
  }

  /**
   * Sets the value of the Addr (MIR[35:27]).
   * 
   * @since Date: Nov 7, 2011
   * @param newAddr the value of the Addr fetched from the current control word
   */
  public void setAddr(final int newAddr) {
    this.addr = newAddr & LOW_NINE_BITS;
  }

  /**
   * Sets the value of the JMPC (MIR[26]).
   * 
   * @since Date: Nov 7, 2011
   * @param jmpCValue the value of JMPC fetched from the current control word
   */
  public void setJmpC(final boolean jmpCValue) {
    this.jmpC = jmpCValue;
  }

  /**
   * Sets the value of the JMPN (MIR[25]).
   * 
   * @since Date: Nov 7, 2011
   * @param jmpNValue the value of JMPN fetched from the current control word
   */
  public void setJmpN(final boolean jmpNValue) {
    this.jmpN = jmpNValue;
  }

  /**
   * Sets the value of the JMPZ (MIR[24]).
   * 
   * @since Date: Nov 7, 2011
   * @param jmpZValue the value of JMPZ fetched from the current control word
   */
  public void setJmpZ(final boolean jmpZValue) {
    this.jmpZ = jmpZValue;
  }

  /**
   * Sets the value of the N - value fetched from the ALU.
   * 
   * @since Date: Nov 7, 2011
   * @param nValue the value of N, fetched from the ALU
   */
  public void setN(final boolean nValue) {
    this.n = nValue;
  }

  /**
   * Sets the value of the Z - value fetched from the ALU.
   * 
   * @since Date: Nov 7, 2011
   * @param zValue the value of Z, fetched from the ALU
   */
  public void setZ(final boolean zValue) {
    this.z = zValue;
  }
}
