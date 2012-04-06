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
package com.github.croesch.micro_debug.mic1.controlstore;

import com.github.croesch.micro_debug.annotation.NotNull;

/**
 * Represents a set of signals: <code>JMPN</code>, <code>JMPZ</code> and <code>JMPC</code>.<br />
 * The signals determine the behavior of calculation of next address in micro-program to execute.<br />
 * 
 * @author croesch
 * @since Date: Nov 13, 2011
 */
public final class JMPSignalSet extends SignalSet {

  /** the number of the signal JMPC */
  private static final int SIGNAL_NUMBER_OF_JMPC = 0;

  /** the number of the signal JMPN */
  private static final int SIGNAL_NUMBER_OF_JMPN = 1;

  /** the number of the signal JMPZ */
  private static final int SIGNAL_NUMBER_OF_JMPZ = 2;

  /** the number of signals this set contains */
  private static final int SIZE_OF_SET = 3;

  /**
   * Constructs a new signal set. Containing the signals <code>JMPN</code>, <code>JMPZ</code> and <code>JMPC</code>. All
   * signals are not set after creation.
   * 
   * @since Date: Nov 13, 2011
   */
  JMPSignalSet() {
    super(SIZE_OF_SET);
  }

  /**
   * Sets a new value for the signal <code>JMPC</code>.
   * 
   * @since Date: Nov 13, 2011
   * @param value the new value for the signal <code>JMPC</code>.
   * @return instance of this object for fluent API.
   */
  @NotNull
  public JMPSignalSet setJmpC(final boolean value) {
    set(SIGNAL_NUMBER_OF_JMPC, value);
    return this;
  }

  /**
   * Returns whether the signal <code>JMPC</code> is set.
   * 
   * @since Date: Nov 13, 2011
   * @return <code>true</code>, if the signal <code>JMPC</code> is set.
   */
  public boolean isJmpC() {
    return is(SIGNAL_NUMBER_OF_JMPC);
  }

  /**
   * Sets a new value for the signal <code>JMPN</code>.
   * 
   * @since Date: Nov 13, 2011
   * @param value the new value for the signal <code>JMPN</code>.
   * @return instance of this object for fluent API.
   */
  @NotNull
  public JMPSignalSet setJmpN(final boolean value) {
    set(SIGNAL_NUMBER_OF_JMPN, value);
    return this;
  }

  /**
   * Returns whether the signal <code>JMPN</code> is set.
   * 
   * @since Date: Nov 13, 2011
   * @return <code>true</code>, if the signal <code>JMPN</code> is set.
   */
  public boolean isJmpN() {
    return is(SIGNAL_NUMBER_OF_JMPN);
  }

  /**
   * Sets a new value for the signal <code>JMPZ</code>.
   * 
   * @since Date: Nov 13, 2011
   * @param value the new value for the signal <code>JMPZ</code>.
   * @return instance of this object for fluent API.
   */
  @NotNull
  public JMPSignalSet setJmpZ(final boolean value) {
    set(SIGNAL_NUMBER_OF_JMPZ, value);
    return this;
  }

  /**
   * Returns whether the signal <code>JMPZ</code> is set.
   * 
   * @since Date: Nov 13, 2011
   * @return <code>true</code>, if the signal <code>JMPZ</code> is set.
   */
  public boolean isJmpZ() {
    return is(SIGNAL_NUMBER_OF_JMPZ);
  }
}
