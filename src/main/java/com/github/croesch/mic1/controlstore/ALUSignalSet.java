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
package com.github.croesch.mic1.controlstore;

/**
 * Represents a set of signals: <code>SLL8</code>, <code>SRA1</code>, <code>F0</code>, <code>F1</code>, <code>ENA</code>
 * , <code>ENB</code>, <code>INVA</code> and <code>INC</code>.<br />
 * The signals determine the behavior of ALU and Shifter.<br />
 * 
 * @author croesch
 * @since Date: Nov 12, 2011
 */
public final class ALUSignalSet extends SignalSet {

  /** the number of the signal SLL8 */
  private static final int SIGNAL_NUMBER_OF_SLL8 = 0;

  /** the number of the signal SRA1 */
  private static final int SIGNAL_NUMBER_OF_SRA1 = 1;

  /** the number of the signal F0 */
  private static final int SIGNAL_NUMBER_OF_F0 = 2;

  /** the number of the signal F1 */
  private static final int SIGNAL_NUMBER_OF_F1 = 3;

  /** the number of the signal ENA */
  private static final int SIGNAL_NUMBER_OF_ENA = 4;

  /** the number of the signal ENB */
  private static final int SIGNAL_NUMBER_OF_ENB = 5;

  /** the number of the signal INVA */
  private static final int SIGNAL_NUMBER_OF_INVA = 6;

  /** the number of the signal INC */
  private static final int SIGNAL_NUMBER_OF_INC = 7;

  /** the number of signals this set contains */
  private static final int SIZE_OF_SET = 8;

  /**
   * Constructs a new signal set. Containing the signals <code>SLL8</code>, <code>SRA1</code>, <code>F0</code>,
   * <code>F1</code>, <code>ENA</code> , <code>ENB</code>, <code>INVA</code> and <code>INC</code>. All signals are not
   * set after creation.
   * 
   * @since Date: Nov 12, 2011
   */
  ALUSignalSet() {
    super(SIZE_OF_SET);
  }

  /**
   * Sets a new value for the signal <code>SLL8</code>.
   * 
   * @since Date: Nov 12, 2011
   * @param value the new value for the signal <code>SLL8</code>.
   * @return instance of this object for fluent API.
   */
  public ALUSignalSet setSLL8(final boolean value) {
    set(SIGNAL_NUMBER_OF_SLL8, value);
    return this;
  }

  /**
   * Returns whether the signal <code>SLL8</code> is set.
   * 
   * @since Date: Nov 12, 2011
   * @return <code>true</code>, if the signal <code>SLL8</code> is set.
   */
  public boolean isSLL8() {
    return is(SIGNAL_NUMBER_OF_SLL8);
  }

  /**
   * Sets a new value for the signal <code>SRA1</code>.
   * 
   * @since Date: Nov 12, 2011
   * @param value the new value for the signal <code>SRA1</code>.
   * @return instance of this object for fluent API.
   */
  public ALUSignalSet setSRA1(final boolean value) {
    set(SIGNAL_NUMBER_OF_SRA1, value);
    return this;
  }

  /**
   * Returns whether the signal <code>SRA1</code> is set.
   * 
   * @since Date: Nov 12, 2011
   * @return <code>true</code>, if the signal <code>SRA1</code> is set.
   */
  public boolean isSRA1() {
    return is(SIGNAL_NUMBER_OF_SRA1);
  }

  /**
   * Sets a new value for the signal <code>F0</code>.
   * 
   * @since Date: Nov 12, 2011
   * @param value the new value for the signal <code>F0</code>.
   * @return instance of this object for fluent API.
   */
  public ALUSignalSet setF0(final boolean value) {
    set(SIGNAL_NUMBER_OF_F0, value);
    return this;
  }

  /**
   * Returns whether the signal <code>F0</code> is set.
   * 
   * @since Date: Nov 12, 2011
   * @return <code>true</code>, if the signal <code>F0</code> is set.
   */
  public boolean isF0() {
    return is(SIGNAL_NUMBER_OF_F0);
  }

  /**
   * Sets a new value for the signal <code>F1</code>.
   * 
   * @since Date: Nov 12, 2011
   * @param value the new value for the signal <code>F1</code>.
   * @return instance of this object for fluent API.
   */
  public ALUSignalSet setF1(final boolean value) {
    set(SIGNAL_NUMBER_OF_F1, value);
    return this;
  }

  /**
   * Returns whether the signal <code>F1</code> is set.
   * 
   * @since Date: Nov 12, 2011
   * @return <code>true</code>, if the signal <code>F1</code> is set.
   */
  public boolean isF1() {
    return is(SIGNAL_NUMBER_OF_F1);
  }

  /**
   * Sets a new value for the signal <code>ENA</code>.
   * 
   * @since Date: Nov 12, 2011
   * @param value the new value for the signal <code>ENA</code>.
   * @return instance of this object for fluent API.
   */
  public ALUSignalSet setEnA(final boolean value) {
    set(SIGNAL_NUMBER_OF_ENA, value);
    return this;
  }

  /**
   * Returns whether the signal <code>ENA</code> is set.
   * 
   * @since Date: Nov 12, 2011
   * @return <code>true</code>, if the signal <code>ENA</code> is set.
   */
  public boolean isEnA() {
    return is(SIGNAL_NUMBER_OF_ENA);
  }

  /**
   * Sets a new value for the signal <code>ENB</code>.
   * 
   * @since Date: Nov 12, 2011
   * @param value the new value for the signal <code>ENB</code>.
   * @return instance of this object for fluent API.
   */
  public ALUSignalSet setEnB(final boolean value) {
    set(SIGNAL_NUMBER_OF_ENB, value);
    return this;
  }

  /**
   * Returns whether the signal <code>ENB</code> is set.
   * 
   * @since Date: Nov 12, 2011
   * @return <code>true</code>, if the signal <code>ENB</code> is set.
   */
  public boolean isEnB() {
    return is(SIGNAL_NUMBER_OF_ENB);
  }

  /**
   * Sets a new value for the signal <code>INVA</code>.
   * 
   * @since Date: Nov 12, 2011
   * @param value the new value for the signal <code>INVA</code>.
   * @return instance of this object for fluent API.
   */
  public ALUSignalSet setInvA(final boolean value) {
    set(SIGNAL_NUMBER_OF_INVA, value);
    return this;
  }

  /**
   * Returns whether the signal <code>INVA</code> is set.
   * 
   * @since Date: Nov 12, 2011
   * @return <code>true</code>, if the signal <code>INVA</code> is set.
   */
  public boolean isInvA() {
    return is(SIGNAL_NUMBER_OF_INVA);
  }

  /**
   * Sets a new value for the signal <code>INC</code>.
   * 
   * @since Date: Nov 12, 2011
   * @param value the new value for the signal <code>INC</code>.
   * @return instance of this object for fluent API.
   */
  public ALUSignalSet setInc(final boolean value) {
    set(SIGNAL_NUMBER_OF_INC, value);
    return this;
  }

  /**
   * Returns whether the signal <code>INC</code> is set.
   * 
   * @since Date: Nov 12, 2011
   * @return <code>true</code>, if the signal <code>INC</code> is set.
   */
  public boolean isInc() {
    return is(SIGNAL_NUMBER_OF_INC);
  }
}
