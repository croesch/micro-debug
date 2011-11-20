package com.github.croesch.mic1.register;

/**
 * This enumeration contains the registers of the processor. The registers are described in the script of Karl
 * Stroetmann.
 * 
 * @author croesch
 * @since Date: Nov 19, 2011
 */
public enum Register {

  /**
   * the <b>M</b>emory <b>A</b>ddress <b>R</b>egister - if something is read/written from/to the memory this register
   * defines the address in the memory where to read/write from/to
   */
  MAR,

  /**
   * the <b>M</b>emory <b>D</b>ata <b>R</b>egister - it is the register that is connected to the memory. If data is read
   * from memory it'll be written into MDR. If data is written to the memory, MDR contains the data that'll be written.
   */
  MDR,

  /**
   * the <b>P</b>rogram <b>C</b>ounter - if something is read from the program-memory this register defines the address
   * in the program-memory where to read from.
   */
  PC,

  /**
   * the <b>M</b>emory <b>B</b>yte <b>R</b>egister - it is the register that is connected to the program-memory. If data
   * is read from program-memory it'll be written into MBR. For security reasons one cannot change contents of the
   * program-memory.<br />
   * Since a single instruction is only 8-bit wide, this register is in the script of Karl Stroetmann only a
   * 8-bit-register. For simulation we assume that we have a 32-bit-register. So this register contains the
   * sign-extended 8-bit-value, written to the register MBR.<br />
   * This register fills the content of {@link Register#MBRU}. In the script of Karl Stroetmann are these two registers
   * only one, but for simulation purpose we are using both registers to build the functionality of the MBR register.
   */
  MBR {

    /** the mask to build a byte of a integer */
    private static final int BYTE_MASK = 0xff;

    /** the mask to fetch the sign bit of a byte */
    private static final int SIGN_BIT_OF_BYTE = 0x80;

    /** the mask to sign-extend a byte to an integer */
    private static final int NEGATIVE_SIGN_EXTENSION = 0xffffff00;

    /**
     * The given value is logically only an 8-bit-value. It will be set to the {@link Register#MBRU} and this registers
     * value will be sign-extended, so the first 24 bits are defined by the highest bit of the given 8-bit-value.
     * 
     * @param val the 8-bit-value.
     */
    @Override
    public void setValue(final int val) {
      final int v = val & BYTE_MASK;
      if ((v & SIGN_BIT_OF_BYTE) == SIGN_BIT_OF_BYTE) {
        super.setValue(v | NEGATIVE_SIGN_EXTENSION);
      } else {
        super.setValue(v);
      }
      MBRU.setValue(v);
    }
  },

  /**
   * this register is not a real register an exists only for simulation purpose.<br />
   * It will be filled with the 8-bit-value of the MBR and the highest 24 bits are filled with zeros.
   */
  MBRU,

  /** The <b>S</b>tack <b>P</b>ointer */
  SP,

  /**
   * The <b>L</b>ocal <b>V</b>ariable pointer - it points to the first variable of a method or if parameters are given,
   * to the first parameter
   */
  LV,

  /** The <b>C</b>onstant <b>P</b>ool <b>P</b>ointer - it points to the begin of the constant pool */
  CPP,

  /** The <b>T</b>op <b>O</b>f <b>S</b>tack - it contains the word that is the top of stack */
  TOS,

  /**
   * the <b>O</b>ld <b>P</b>rogram <b>C</b>ounter - it is a additional register and has no certain function. But it can
   * be used to store the value of PC in goto-functions.
   */
  OPC,

  /** this register is a additional register to help storing values. */
  H;

  /** the value stored by this register */
  private int value = 0;

  /**
   * Sets the value for that register.
   * 
   * @since Date: Nov 19, 2011
   * @param val the new value for the register
   */
  public void setValue(final int val) {
    this.value = val;
  }

  /**
   * Gets the value currently stored in the register.
   * 
   * @since Date: Nov 19, 2011
   * @return the value of the register.
   */
  public final int getValue() {
    return this.value;
  }
}
