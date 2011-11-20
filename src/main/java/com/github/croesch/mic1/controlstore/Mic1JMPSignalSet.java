package com.github.croesch.mic1.controlstore;

/**
 * Represents a set of signals: <code>JMPN</code>, <code>JMPZ</code> and <code>JMPC</code>.<br />
 * The signals determine the behavior of calculation of next address in micro-program to execute.<br />
 * 
 * @author croesch
 * @since Date: Nov 13, 2011
 */
final class Mic1JMPSignalSet extends Mic1SignalSet {

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
  Mic1JMPSignalSet() {
    super(SIZE_OF_SET);
  }

  /**
   * Sets a new value for the signal <code>JMPC</code>.
   * 
   * @since Date: Nov 13, 2011
   * @param value the new value for the signal <code>JMPC</code>.
   */
  public void setJmpC(final boolean value) {
    set(SIGNAL_NUMBER_OF_JMPC, value);
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
   */
  public void setJmpN(final boolean value) {
    set(SIGNAL_NUMBER_OF_JMPN, value);
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
   */
  public void setJmpZ(final boolean value) {
    set(SIGNAL_NUMBER_OF_JMPZ, value);
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
