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
 * Represents a set of signals: write, read and fetch.
 * 
 * @author croesch
 * @since Date: Nov 12, 2011
 */
class SignalSet {

  /** the array of signals to manage */
  private final boolean[] signals;

  /**
   * Constructs a set of signals. All signals are set to <code>false</code> by default.
   * 
   * @since Date: Nov 12, 2011
   * @param size the number of signals, cannot be changed later.
   */
  SignalSet(final int size) {
    this.signals = new boolean[size];
  }

  /**
   * Returns the number of signals this set contains.
   * 
   * @since Date: Jan 14, 2012
   * @return the number of signals this set contains, greater or equal than zero
   * @see SignalSet#SignalSet(int)
   */
  public final int getSize() {
    return this.signals.length;
  }

  /**
   * Returns whether any of the signals is set.
   * 
   * @since Date: Jan 14, 2012
   * @return <code>true</code>, if at least one of the signals is set,<br>
   *         <code>false</code> otherwise.
   */
  public final boolean isAnythingSet() {
    for (int i = 0; i < this.signals.length; ++i) {
      if (this.signals[i]) {
        return true;
      }
    }
    return false;
  }

  /**
   * Returns whether the specific signal is set.
   * 
   * @since Date: Nov 12, 2011
   * @param i the number of the signal to check
   * @return <code>true</code>, if signal number <code>i</code> is set, <code>false</code> otherwise
   */
  boolean is(final int i) {
    return this.signals[i];
  }

  /**
   * Sets the specific signal to the given value.
   * 
   * @since Date: Nov 12, 2011
   * @param i the number of the signal to set
   * @param value the new value for signal number <code>i</code>
   */
  void set(final int i, final boolean value) {
    this.signals[i] = value;
  }

  /**
   * Copies all signals from the given set to this set. If the given set is <code>null</code> or the number of signals
   * are different to the signals in this object, nothing is done.
   * 
   * @since Date: Nov 13, 2011
   * @param set the set to fetch the signals from
   */
  public void copyOf(final SignalSet set) {
    if (set != null && this.signals.length == set.signals.length) {
      for (int i = 0; i < this.signals.length; ++i) {
        this.signals[i] = set.signals[i];
      }
    }
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    for (final boolean val : this.signals) {
      result = prime * result + Boolean.valueOf(val).hashCode();
    }
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
    final SignalSet other = (SignalSet) obj;
    if (this.signals.length != other.signals.length) {
      return false;
    }
    for (int i = 0; i < this.signals.length; ++i) {
      if (this.signals[i] != other.signals[i]) {
        return false;
      }
    }
    return true;
  }

  @Override
  @NotNull
  public String toString() {
    final StringBuilder s = new StringBuilder();
    for (final boolean b : this.signals) {
      if (b) {
        s.append(1);
      } else {
        s.append(0);
      }
    }
    return s.toString();
  }
}
