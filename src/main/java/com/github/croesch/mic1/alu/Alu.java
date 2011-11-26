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
package com.github.croesch.mic1.alu;

/**
 * This class represents an ALU for 32 bit values. It is based on the verilog code for 'interconnection of 1-Bit-ALUs to
 * one 32-bit-ALU' in the script of the lecture 'Rechnertechnik' of Karl Stroetmann.
 * 
 * @author croesch
 * @since Date: Oct 18, 2011
 */
public final class Alu {

  /** the number of one-bit-ALUs that build that ALU */
  private static final int SIZE = 32;

  /** field of one-bit-ALUs that will be set into one row */
  private final OneBitAlu[] oneBitAlus = new OneBitAlu[SIZE];

  // input signals

  /** the first number (A) to put in the ALU */
  private int valueA = 0;

  /** the second number (B) to put in the ALU */
  private int valueB = 0;

  /** the signal F0, determines the calculation method - logically or arithmetically */
  private boolean flag0 = false;

  /** the signal F1, determines the calculation method - logically or arithmetically */
  private boolean flag1 = false;

  /** the signal ENA to determine, whether value of A should be used */
  private boolean enableA = false;

  /** the signal ENB to determine, whether value of B should be used */
  private boolean enableB = false;

  /** the signal INVA to determine, whether value of A should be inverted before calculation */
  private boolean invertA = false;

  /** the signal Carry in, to pass to the full adder, as default carry in */
  private boolean inCarry = false;

  // output signals

  /** the calculated number */
  private int output = 0;

  /** the signal N, whether the calculated number is negative */
  private boolean negative = false;

  /** the signal Z, whether the calculated number is zero */
  private boolean zero = false;

  // constructors

  /**
   * Initialises all the one-bit-ALUs.
   * 
   * @since Date: Oct 18, 2011
   */
  public Alu() {
    for (int i = 0; i < SIZE; ++i) {
      this.oneBitAlus[i] = new OneBitAlu();
    }
  }

  // methods

  /**
   * Returns the calculated output.
   * 
   * @since Date: Oct 18, 2011
   * @return the number calculated by the ALU.
   */
  public int getOut() {
    return this.output;
  }

  /**
   * Returns the whether the calculated output is negative.
   * 
   * @since Date: Oct 18, 2011
   * @return <code>true</code>, if the number calculated by this ALU is less than zero.
   */
  public boolean isN() {
    return this.negative;
  }

  /**
   * Returns the whether the calculated output is zero.
   * 
   * @since Date: Oct 18, 2011
   * @return <code>true</code>, if the number calculated by this ALU is zero.
   */
  public boolean isZ() {
    return this.zero;
  }

  /**
   * Sets the value for the input value A.
   * 
   * @since Date: Oct 18, 2011
   * @param a the value of A, to calculate with
   * @return instance of the {@link Alu} to allow a fluent API.
   */
  public Alu setA(final int a) {
    this.valueA = a;
    return this;
  }

  /**
   * Sets the value for the input value B.
   * 
   * @since Date: Oct 18, 2011
   * @param b the value of B, to calculate with
   * @return instance of the {@link Alu} to allow a fluent API.
   */
  public Alu setB(final int b) {
    this.valueB = b;
    return this;
  }

  /**
   * Sets the value for the signal F0.
   * 
   * @since Date: Oct 18, 2011
   * @param f0 <code>true</code>, if the signal should be set, <code>false</code> otherwise.
   * @return instance of the {@link Alu} to allow a fluent API.
   */
  public Alu setF0(final boolean f0) {
    this.flag0 = f0;
    return this;
  }

  /**
   * Sets the value for the signal F1.
   * 
   * @since Date: Oct 18, 2011
   * @param f1 <code>true</code>, if the signal should be set, <code>false</code> otherwise.
   * @return instance of the {@link Alu} to allow a fluent API.
   */
  public Alu setF1(final boolean f1) {
    this.flag1 = f1;
    return this;
  }

  /**
   * Sets the value for the signal ENA.
   * 
   * @since Date: Oct 18, 2011
   * @param enA <code>true</code>, if the signal should be set, <code>false</code> otherwise.
   * @return instance of the {@link Alu} to allow a fluent API.
   */
  public Alu setEnA(final boolean enA) {
    this.enableA = enA;
    return this;
  }

  /**
   * Sets the value for the signal ENB.
   * 
   * @since Date: Oct 18, 2011
   * @param enB <code>true</code>, if the signal should be set, <code>false</code> otherwise.
   * @return instance of the {@link Alu} to allow a fluent API.
   */
  public Alu setEnB(final boolean enB) {
    this.enableB = enB;
    return this;
  }

  /**
   * Sets the value for the signal INVA.
   * 
   * @since Date: Oct 18, 2011
   * @param invA <code>true</code>, if the signal should be set, <code>false</code> otherwise.
   * @return instance of the {@link Alu} to allow a fluent API.
   */
  public Alu setInvA(final boolean invA) {
    this.invertA = invA;
    return this;
  }

  /**
   * Sets the value for the signal Carry in.
   * 
   * @since Date: Oct 18, 2011
   * @param inC <code>true</code>, if the signal should be set, <code>false</code> otherwise.
   * @return instance of the {@link Alu} to allow a fluent API.
   */
  public Alu setInc(final boolean inC) {
    this.inCarry = inC;
    return this;
  }

  /**
   * Performs calculation of the output signals based on the current values of input signals.
   * 
   * @since Date: Oct 18, 2011
   */
  public void calculate() {
    // carry 'wire', signal that will be passed from ALU to ALU
    boolean carry = this.inCarry;
    // mask, that is used to calculate the A and B values passed to the ALUs and the bit of output to set
    int mask = 1;
    // reset output, so that we can simply set bits that are one
    this.output = 0;

    // iterate over all one-bit-ALUs
    for (int i = 0; i < SIZE; ++i) {
      // setting the signals
      this.oneBitAlus[i].setCarryIn(carry).setF0(this.flag0).setF1(this.flag1);
      this.oneBitAlus[i].setEnA(this.enableA).setEnB(this.enableB).setInvA(this.invertA);
      // calculate the bit of A and B to set in the one-bit-ALU
      this.oneBitAlus[i].setA((this.valueA & mask) == mask).setB((this.valueB & mask) == mask);
      // 'run' the one-bit-alu
      this.oneBitAlus[i].calculate();

      carry = this.oneBitAlus[i].isCarryOut(); // signal for next ALU
      if (this.oneBitAlus[i].isOut()) {
        this.output |= mask; // set bit, if the ALU returns one
      }
      mask <<= 1; // shift mask bit for next iteration
    }

    // set output signals based on the calculated value
    this.zero = this.output == 0;
    this.negative = this.output < 0;
  }
}
