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
package com.github.croesch.micro_debug.mic1.alu;

import com.github.croesch.micro_debug.annotation.NotNull;

/**
 * This class represents an ALU for one bit values. It is based on the circuit diagram for '1-Bit-ALU' in the script of
 * the lecture 'Rechnertechnik' of Karl Stroetmann.
 * 
 * @author croesch
 * @since Date: Oct 18, 2011
 */
class OneBitAlu {

  // input signals

  /** the signal A that is a value to calculate with */
  private boolean a;

  /** the signal B that is a value to calculate with */
  private boolean b;

  /** the signal F0 that is a signal to determine calculation method */
  private boolean f0;

  /** the signal F1 that is a signal to determine calculation method */
  private boolean f1;

  /** the signal ENA that is used to determine, whether the value of A is used */
  private boolean enA;

  /** the signal ENB that is used to determine, whether the value of B is used */
  private boolean enB;

  /** the signal INVA that is used to determine, whether the value of A is inverted */
  private boolean invA;

  /** the signal Carry in that is used to define the carry-in-signal for the full adder */
  private boolean carryIn;

  // output signals

  /** the signal Output, may be logically calculated or arithmetically */
  private boolean out;

  /**
   * the signal Carry out, is true if the value for Output has been calculated arithmetically and the value is bigger
   * than two. Note that in that case the output of the ALU is interpreted: Value = 2 * Carry out + Output.
   */
  private boolean carryOut;

  // methods

  /**
   * Returns whether the output signal is set.
   * 
   * @since Date: Oct 18, 2011
   * @return <code>true</code>, if the output signal is set.
   */
  boolean isOut() {
    return this.out;
  }

  /**
   * Returns whether the carry out signal is set.
   * 
   * @since Date: Oct 18, 2011
   * @return <code>true</code>, if the carry out signal is set.
   */
  boolean isCarryOut() {
    return this.carryOut;
  }

  /**
   * Sets the value for the signal A.
   * 
   * @since Date: Oct 18, 2011
   * @param setA <code>true</code>, if the signal should be set, <code>false</code> otherwise.
   * @return instance of the {@link OneBitAlu} to allow a fluent API.
   */
  @NotNull
  OneBitAlu setA(final boolean setA) {
    this.a = setA;
    return this;
  }

  /**
   * Sets the value for the signal B.
   * 
   * @since Date: Oct 18, 2011
   * @param setB <code>true</code>, if the signal should be set, <code>false</code> otherwise.
   * @return instance of the {@link OneBitAlu} to allow a fluent API.
   */
  @NotNull
  OneBitAlu setB(final boolean setB) {
    this.b = setB;
    return this;
  }

  /**
   * Sets the value for the signal F0.
   * 
   * @since Date: Oct 18, 2011
   * @param setF0 <code>true</code>, if the signal should be set, <code>false</code> otherwise.
   * @return instance of the {@link OneBitAlu} to allow a fluent API.
   */
  @NotNull
  OneBitAlu setF0(final boolean setF0) {
    this.f0 = setF0;
    return this;
  }

  /**
   * Sets the value for the signal F1.
   * 
   * @since Date: Oct 18, 2011
   * @param setF1 <code>true</code>, if the signal should be set, <code>false</code> otherwise.
   * @return instance of the {@link OneBitAlu} to allow a fluent API.
   */
  @NotNull
  OneBitAlu setF1(final boolean setF1) {
    this.f1 = setF1;
    return this;
  }

  /**
   * Sets the value for the signal ENA.
   * 
   * @since Date: Oct 18, 2011
   * @param setEnA <code>true</code>, if the signal should be set, <code>false</code> otherwise.
   * @return instance of the {@link OneBitAlu} to allow a fluent API.
   */
  @NotNull
  OneBitAlu setEnA(final boolean setEnA) {
    this.enA = setEnA;
    return this;
  }

  /**
   * Sets the value for the signal ENB.
   * 
   * @since Date: Oct 18, 2011
   * @param setEnB <code>true</code>, if the signal should be set, <code>false</code> otherwise.
   * @return instance of the {@link OneBitAlu} to allow a fluent API.
   */
  @NotNull
  OneBitAlu setEnB(final boolean setEnB) {
    this.enB = setEnB;
    return this;
  }

  /**
   * Sets the value for the signal INVA.
   * 
   * @since Date: Oct 18, 2011
   * @param setInvA <code>true</code>, if the signal should be set, <code>false</code> otherwise.
   * @return instance of the {@link OneBitAlu} to allow a fluent API.
   */
  @NotNull
  OneBitAlu setInvA(final boolean setInvA) {
    this.invA = setInvA;
    return this;
  }

  /**
   * Sets the value for the signal Carry in.
   * 
   * @since Date: Oct 18, 2011
   * @param setCarryIn <code>true</code>, if the signal should be set, <code>false</code> otherwise.
   * @return instance of the {@link OneBitAlu} to allow a fluent API.
   */
  @NotNull
  OneBitAlu setCarryIn(final boolean setCarryIn) {
    this.carryIn = setCarryIn;
    return this;
  }

  /**
   * Performs calculation of the output signals based on the current values of input signals.
   * 
   * @since Date: Oct 18, 2011
   */
  void calculate() {
    // calculate signals of 'wires'
    final boolean aEnabled = this.a && this.enA;
    final boolean aInv = aEnabled ^ this.invA;
    final boolean bEnabled = this.b && this.enB;

    performCalculation(aInv, bEnabled);
  }

  /**
   * Decodes the calculation method and performs the calculation with the given signal values.
   * 
   * @since Date: Oct 18, 2011
   * @param aInv the signal value calculated based on values of A, ENA and INVA.
   * @param bEnabled the signal value calculated based on values of B and ENB
   */
  private void performCalculation(final boolean aInv, final boolean bEnabled) {
    final boolean aAndB = aInv && bEnabled;

    switch (decode(this.f0, this.f1)) {
      case AND:
        this.out = aAndB;
        break;
      case OR:
        this.out = aInv || bEnabled;
        break;
      case NOT:
        this.out = !bEnabled;
        break;
      case SUM:
        final boolean aXorB = aInv ^ bEnabled;
        this.out = aXorB ^ this.carryIn;
        this.carryOut = aAndB || (aXorB && this.carryIn);
        break;
      default:
        // won't happen
        break;
    }
  }

  /** calculation mode of the ALU */
  private enum MODE {
    /** calculate AND operation */
    AND,
    /** calculate OR operation */
    OR,
    /** calculate negation operation */
    NOT,
    /** calculate sum operation */
    SUM;
  }

  /**
   * Decodes two binary signals into one signal that can have four values. Function is equal to the functionality of the
   * 'decoder' in the circuit diagram of the script.
   * 
   * @since Date: Oct 18, 2011
   * @param s0 binary signal one (F0)
   * @param s1 binary signal two (F1)
   * @return {@link MODE} the calculation mode to perform with the ALU.
   */
  @NotNull
  private MODE decode(final boolean s0, final boolean s1) {
    if (s0) {
      if (s1) {
        // we know that: s0 && s1
        return MODE.SUM;
      }

      // we know that: s0 && !s1
      return MODE.NOT;
    }

    if (s1) {
      // we know that: !s0 && s1
      return MODE.OR;
    }
    // we know that: !s0 && !s1
    return MODE.AND;
  }
}
