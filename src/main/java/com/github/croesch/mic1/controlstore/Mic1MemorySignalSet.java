package com.github.croesch.mic1.controlstore;

/**
 * Represents a set of signals: <code>write</code>, <code>read</code> and <code>fetch</code>.<br />
 * The signal <code>write</code> determines whether the content of the register MDR should be written to the memory to
 * the address defined by MAR.<br />
 * The signal <code>read</code> determines whether content of the memory at the address defined by MAR should be written
 * into the register MDR.<br />
 * The signal <code>read</code> determines whether content of the program memory at the address defined by PC should be
 * written into the register MBR.<br />
 * 
 * @author croesch
 * @since Date: Nov 12, 2011
 */
public final class Mic1MemorySignalSet extends Mic1SignalSet {

  /** the number of signals this set contains */
  private static final int SIZE_OF_SET = 3;

  /**
   * Constructs a new signal set. Containing the signals <code>write</code>, <code>read</code> and <code>fetch</code>.
   * All signals are not set after creation.
   * 
   * @since Date: Nov 12, 2011
   */
  Mic1MemorySignalSet() {
    super(SIZE_OF_SET);
  }

  /**
   * Returns whether the signal <code>write</code> is set.
   * 
   * @since Date: Nov 12, 2011
   * @return <code>true</code>, if the signal <code>write</code> is set.
   */
  public boolean isWrite() {
    return is(0);
  }

  /**
   * Sets a new value for the signal <code>write</code>.
   * 
   * @since Date: Nov 12, 2011
   * @param write the new value for the signal <code>write</code>.
   * @return instance of this object for fluent API.
   */
  Mic1MemorySignalSet setWrite(final boolean write) {
    set(0, write);
    return this;
  }

  /**
   * Returns whether the signal <code>read</code> is set.
   * 
   * @since Date: Nov 12, 2011
   * @return <code>true</code>, if the signal <code>read</code> is set.
   */
  public boolean isRead() {
    return is(1);
  }

  /**
   * Sets a new value for the signal <code>read</code>.
   * 
   * @since Date: Nov 12, 2011
   * @param read the new value for the signal <code>read</code>.
   * @return instance of this object for fluent API.
   */
  Mic1MemorySignalSet setRead(final boolean read) {
    set(1, read);
    return this;
  }

  /**
   * Returns whether the signal <code>fetch</code> is set.
   * 
   * @since Date: Nov 12, 2011
   * @return <code>true</code>, if the signal <code>fetch</code> is set.
   */
  public boolean isFetch() {
    return is(2);
  }

  /**
   * Sets a new value for the signal <code>fetch</code>.
   * 
   * @since Date: Nov 12, 2011
   * @param fetch the new value for the signal <code>fetch</code>.
   * @return instance of this object for fluent API.
   */
  Mic1MemorySignalSet setFetch(final boolean fetch) {
    set(2, fetch);
    return this;
  }
}
