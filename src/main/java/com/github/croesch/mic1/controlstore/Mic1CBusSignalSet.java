package com.github.croesch.mic1.controlstore;

/**
 * Represents a set of signals: <code>H</code> , <code>CPP</code> , <code>MAR</code> , <code>MDR</code> ,
 * <code>OPC</code> , <code>PC</code> , <code>SP</code> , <code>TOS</code> and <code>LV</code>.<br />
 * The signals determine whether the result of ALU/Shifter should be written into the specific register.<br />
 * 
 * @author croesch
 * @since Date: Nov 12, 2011
 */
public final class Mic1CBusSignalSet extends Mic1SignalSet {

  /** the number of the signal H */
  private static final int SIGNAL_NUMBER_OF_H = 0;

  /** the number of the signal OPC */
  private static final int SIGNAL_NUMBER_OF_OPC = 1;

  /** the number of the signal TOS */
  private static final int SIGNAL_NUMBER_OF_TOS = 2;

  /** the number of the signal CPP */
  private static final int SIGNAL_NUMBER_OF_CPP = 3;

  /** the number of the signal LV */
  private static final int SIGNAL_NUMBER_OF_LV = 4;

  /** the number of the signal SP */
  private static final int SIGNAL_NUMBER_OF_SP = 5;

  /** the number of the signal PC */
  private static final int SIGNAL_NUMBER_OF_PC = 6;

  /** the number of the signal MDR */
  private static final int SIGNAL_NUMBER_OF_MDR = 7;

  /** the number of the signal MAR */
  private static final int SIGNAL_NUMBER_OF_MAR = 8;

  /** the number of signals this set contains */
  private static final int SIZE_OF_SET = 9;

  /**
   * Constructs a new signal set. Containing the signals <code>H</code> , <code>CPP</code> , <code>MAR</code> ,
   * <code>MDR</code> , <code>OPC</code> , <code>PC</code> , <code>SP</code> , <code>TOS</code> and <code>LV</code>. All
   * signals are not set after creation.
   * 
   * @since Date: Nov 12, 2011
   */
  Mic1CBusSignalSet() {
    super(SIZE_OF_SET);
  }

  /**
   * Sets a new value for the signal <code>H</code>.
   * 
   * @since Date: Nov 12, 2011
   * @param value the new value for the signal <code>H</code>.
   */
  public void setH(final boolean value) {
    set(SIGNAL_NUMBER_OF_H, value);
  }

  /**
   * Returns whether the signal <code>H</code> is set.
   * 
   * @since Date: Nov 12, 2011
   * @return <code>true</code>, if the signal <code>H</code> is set.
   */
  public boolean isH() {
    return is(SIGNAL_NUMBER_OF_H);
  }

  /**
   * Sets a new value for the signal <code>OPC</code>.
   * 
   * @since Date: Nov 12, 2011
   * @param value the new value for the signal <code>OPC</code>.
   */
  public void setOpc(final boolean value) {
    set(SIGNAL_NUMBER_OF_OPC, value);
  }

  /**
   * Returns whether the signal <code>OPC</code> is set.
   * 
   * @since Date: Nov 12, 2011
   * @return <code>true</code>, if the signal <code>OPC</code> is set.
   */
  public boolean isOpc() {
    return is(SIGNAL_NUMBER_OF_OPC);
  }

  /**
   * Sets a new value for the signal <code>TOS</code>.
   * 
   * @since Date: Nov 12, 2011
   * @param value the new value for the signal <code>TOS</code>.
   */
  public void setTos(final boolean value) {
    set(SIGNAL_NUMBER_OF_TOS, value);
  }

  /**
   * Returns whether the signal <code>TOS</code> is set.
   * 
   * @since Date: Nov 12, 2011
   * @return <code>true</code>, if the signal <code>TOS</code> is set.
   */
  public boolean isTos() {
    return is(SIGNAL_NUMBER_OF_TOS);
  }

  /**
   * Sets a new value for the signal <code>CPP</code>.
   * 
   * @since Date: Nov 12, 2011
   * @param value the new value for the signal <code>CPP</code>.
   */
  public void setCpp(final boolean value) {
    set(SIGNAL_NUMBER_OF_CPP, value);
  }

  /**
   * Returns whether the signal <code>CPP</code> is set.
   * 
   * @since Date: Nov 12, 2011
   * @return <code>true</code>, if the signal <code>CPP</code> is set.
   */
  public boolean isCpp() {
    return is(SIGNAL_NUMBER_OF_CPP);
  }

  /**
   * Sets a new value for the signal <code>LV</code>.
   * 
   * @since Date: Nov 12, 2011
   * @param value the new value for the signal <code>LV</code>.
   */
  public void setLv(final boolean value) {
    set(SIGNAL_NUMBER_OF_LV, value);
  }

  /**
   * Returns whether the signal <code>LV</code> is set.
   * 
   * @since Date: Nov 12, 2011
   * @return <code>true</code>, if the signal <code>LV</code> is set.
   */
  public boolean isLv() {
    return is(SIGNAL_NUMBER_OF_LV);
  }

  /**
   * Sets a new value for the signal <code>SP</code>.
   * 
   * @since Date: Nov 12, 2011
   * @param value the new value for the signal <code>SP</code>.
   */
  public void setSp(final boolean value) {
    set(SIGNAL_NUMBER_OF_SP, value);
  }

  /**
   * Returns whether the signal <code>SP</code> is set.
   * 
   * @since Date: Nov 12, 2011
   * @return <code>true</code>, if the signal <code>SP</code> is set.
   */
  public boolean isSp() {
    return is(SIGNAL_NUMBER_OF_SP);
  }

  /**
   * Sets a new value for the signal <code>PC</code>.
   * 
   * @since Date: Nov 12, 2011
   * @param value the new value for the signal <code>PC</code>.
   */
  public void setPc(final boolean value) {
    set(SIGNAL_NUMBER_OF_PC, value);
  }

  /**
   * Returns whether the signal <code>PC</code> is set.
   * 
   * @since Date: Nov 12, 2011
   * @return <code>true</code>, if the signal <code>PC</code> is set.
   */
  public boolean isPc() {
    return is(SIGNAL_NUMBER_OF_PC);
  }

  /**
   * Sets a new value for the signal <code>MDR</code>.
   * 
   * @since Date: Nov 12, 2011
   * @param value the new value for the signal <code>MDR</code>.
   */
  public void setMdr(final boolean value) {
    set(SIGNAL_NUMBER_OF_MDR, value);
  }

  /**
   * Returns whether the signal <code>MDR</code> is set.
   * 
   * @since Date: Nov 12, 2011
   * @return <code>true</code>, if the signal <code>MDR</code> is set.
   */
  public boolean isMdr() {
    return is(SIGNAL_NUMBER_OF_MDR);
  }

  /**
   * Sets a new value for the signal <code>MAR</code>.
   * 
   * @since Date: Nov 12, 2011
   * @param value the new value for the signal <code>MAR</code>.
   */
  public void setMar(final boolean value) {
    set(SIGNAL_NUMBER_OF_MAR, value);
  }

  /**
   * Returns whether the signal <code>MAR</code> is set.
   * 
   * @since Date: Nov 12, 2011
   * @return <code>true</code>, if the signal <code>MAR</code> is set.
   */
  public boolean isMar() {
    return is(SIGNAL_NUMBER_OF_MAR);
  }
}
