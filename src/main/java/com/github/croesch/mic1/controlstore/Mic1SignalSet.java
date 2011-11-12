package com.github.croesch.mic1.controlstore;

/**
 * Represents a set of signals: write, read and fetch.
 * 
 * @author croesch
 * @since Date: Nov 12, 2011
 */
class Mic1SignalSet {

  /** the array of signals to manage */
  private final boolean[] signals;

  /**
   * Constructs a set of signals. All signals are set to <code>false</code> by default.
   * 
   * @since Date: Nov 12, 2011
   * @param size the number of signals, cannot be changed later.
   */
  Mic1SignalSet(final int size) {
    this.signals = new boolean[size];
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
    final Mic1SignalSet other = (Mic1SignalSet) obj;
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
}
