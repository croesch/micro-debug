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
package com.github.croesch.micro_debug.mic1.shifter;

/**
 * This class represents a shifter for 32 bit values. It is based on the description for the shifter of the
 * CISC-processor in the script of the lecture 'Rechnertechnik' of Karl Stroetmann.
 * 
 * @author croesch
 * @since Date: Nov 7, 2011
 */
public final class Shifter {

  /** the number of bits to shift with SLL8 to the left side */
  private static final int SLL8_NUMBER_OF_BITS_SHIFTED = 8;

  /** the number of bits to shift with SRA1 to the right side */
  private static final int SRA1_NUMBER_OF_BITS_SHIFTED = 1;

  // input signals

  /** the control line with the name (SLL8) for the shifter */
  private boolean sll8 = false;

  /** the control line with the name (SRA1) for the shifter */
  private boolean sra1 = false;

  /** the 32-bit value that is set to the shifter */
  private int input = 0;

  // output signals

  /** the calculated value */
  private int output = 0;

  // methods

  /**
   * Sets the value for the control line SLL8.
   * 
   * @since Date: Nov 7, 2011
   * @param valueForSll8 <code>true</code>, if the control bit SLL8 is set
   */
  public void setSLL8(final boolean valueForSll8) {
    this.sll8 = valueForSll8;
  }

  /**
   * Sets the value for the control line SRA1.
   * 
   * @since Date: Nov 7, 2011
   * @param valueForSra1 <code>true</code>, if the control bit SRA1 is set
   */
  public void setSRA1(final boolean valueForSra1) {
    this.sra1 = valueForSra1;
  }

  /**
   * Returns the calculated output.
   * 
   * @since Date: Nov 7, 2011
   * @return the input of the shifter, possible shifted.
   */
  public int getOutput() {
    return this.output;
  }

  /**
   * Performs calculation of the output signals based on the current values of input signals.
   * 
   * @since Date: Nov 21, 2011
   */
  public void calculate() {
    if (this.sll8) {
      if (this.sra1) {
        throw new IllegalStateException();
      } else {
        this.output = (this.input << SLL8_NUMBER_OF_BITS_SHIFTED);
      }
    } else {
      if (this.sra1) {
        this.output = this.input >> SRA1_NUMBER_OF_BITS_SHIFTED;
      } else {
        this.output = this.input;
      }
    }
  }

  /**
   * Sets the value for the input of the shifter. This 32-bit number will be shifted, depending on the values of the
   * control lines.
   * 
   * @since Date: Nov 7, 2011
   * @param value 32-bit input value for the shifter
   */
  public void setInput(final int value) {
    this.input = value;
  }
}
