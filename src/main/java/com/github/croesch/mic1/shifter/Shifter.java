package com.github.croesch.mic1.shifter;

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
    if (this.sll8) {
      if (this.sra1) {
        throw new IllegalStateException();
      } else {
        return (this.input << SLL8_NUMBER_OF_BITS_SHIFTED);
      }
    } else {
      if (this.sra1) {
        return this.input >> SRA1_NUMBER_OF_BITS_SHIFTED;
      } else {
        return this.input;
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
