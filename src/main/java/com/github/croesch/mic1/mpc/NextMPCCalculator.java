package com.github.croesch.mic1.mpc;

import java.util.BitSet;

/**
 * This class represents a calculator for the next micro-program-counter (MPC). It is based on the verilog code for
 * 'calculation of MPC' in the script of the lecture 'Rechnertechnik' of Karl Stroetmann.
 * 
 * @author croesch
 * @since Date: Nov 7, 2011
 */
public final class NextMPCCalculator {

  /** size of MBR in bits */
  private static final int BITS_IN_MBR = 8;

  /** the highest bit of MPC */
  private static final int HIGHEST_MPC_BIT = BITS_IN_MBR;

  /** the number of bits in the address */
  private static final int MAX_SIZE_OF_ADDR = HIGHEST_MPC_BIT + 1;

  // input signals

  /** the value of the register MBR */
  private byte mbr = 0;

  /** the value of the part in the MIR[Addr] */
  private final BitSet addr = new BitSet(9);

  /** whether the control line JMPC (MIR[26]) is set */
  private boolean jmpC;

  /** whether the control line JMPN (MIR[25]) is set */
  private boolean jmpN;

  /** whether the control line JMPZ (MIR[24]) is set */
  private boolean jmpZ;

  /** the value of the control line N, fetched from the ALU */
  private boolean n;

  /** the value of the control line Z, fetched from the ALU */
  private boolean z;

  // input signals

  /** the calculated 9-bit-value for the value of the next MPC */
  private final BitSet mpc = new BitSet(HIGHEST_MPC_BIT + 1);

  // methods

  /**
   * Performs calculation of the output signals based on the current values of input signals.
   * 
   * @since Date: Nov 7, 2011
   */
  public void calculate() {
    if (this.jmpC) {
      // MPC[7:0] = MBR | Addr[7:0];
      for (int i = 0; i < BITS_IN_MBR; ++i) {
        // check if bit number i is set in the value of MBR
        final boolean isBitIOfMBRSet = (this.mbr & 1 << i) == 1 << i;
        this.mpc.set(i, this.addr.get(i) || isBitIOfMBRSet);
      }
    } else {
      // MPC[7:0] = Addr[7:0];
      for (int i = 0; i < BITS_IN_MBR; ++i) {
        this.mpc.set(i, this.addr.get(i));
      }
    }
    if ((this.jmpN && this.n) || (this.jmpZ && this.z)) {
      // MPC[8] = 1;
      this.mpc.set(HIGHEST_MPC_BIT);
    } else {
      // MPC[8] = Addr[8];
      this.mpc.set(HIGHEST_MPC_BIT, this.addr.get(HIGHEST_MPC_BIT));
    }
  }

  /**
   * Returns the calculated 9-bit-value. This is the value for the next MPC.
   * 
   * @since Date: Nov 7, 2011
   * @return the 9-bit-value for the MPC
   */
  public BitSet getMpc() {
    return this.mpc;
  }

  /**
   * Sets the value of the register MBR.
   * 
   * @since Date: Nov 7, 2011
   * @param mbrValue the byte fetched from the register MBR
   */
  public void setMbr(final byte mbrValue) {
    this.mbr = mbrValue;
  }

  /**
   * Sets the value of the Addr (MIR[35:27]).
   * 
   * @since Date: Nov 7, 2011
   * @param newAddr the value of the Addr fetched from the current control word
   */
  public void setAddr(final BitSet newAddr) {
    if (newAddr.length() > MAX_SIZE_OF_ADDR) {
      throw new IllegalArgumentException();
    }
    this.addr.clear();
    this.addr.or(newAddr);
  }

  /**
   * Sets the value of the JMPC (MIR[26]).
   * 
   * @since Date: Nov 7, 2011
   * @param jmpCValue the value of JMPC fetched from the current control word
   */
  public void setJmpC(final boolean jmpCValue) {
    this.jmpC = jmpCValue;
  }

  /**
   * Sets the value of the JMPN (MIR[25]).
   * 
   * @since Date: Nov 7, 2011
   * @param jmpNValue the value of JMPN fetched from the current control word
   */
  public void setJmpN(final boolean jmpNValue) {
    this.jmpN = jmpNValue;
  }

  /**
   * Sets the value of the JMPZ (MIR[24]).
   * 
   * @since Date: Nov 7, 2011
   * @param jmpZValue the value of JMPZ fetched from the current control word
   */
  public void setJmpZ(final boolean jmpZValue) {
    this.jmpZ = jmpZValue;
  }

  /**
   * Sets the value of the N - value fetched from the ALU.
   * 
   * @since Date: Nov 7, 2011
   * @param nValue the value of N, fetched from the ALU
   */
  public void setN(final boolean nValue) {
    this.n = nValue;
  }

  /**
   * Sets the value of the Z - value fetched from the ALU.
   * 
   * @since Date: Nov 7, 2011
   * @param zValue the value of Z, fetched from the ALU
   */
  public void setZ(final boolean zValue) {
    this.z = zValue;
  }
}
