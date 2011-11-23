package com.github.croesch.mic1.controlstore;

import java.io.IOException;
import java.io.InputStream;

import com.github.croesch.mic1.register.Register;
import com.github.croesch.misc.Utils;

/**
 * Is able to read bytes from {@link InputStream}s and to construct {@link Mic1Instruction}s with the given values.
 * 
 * @author croesch
 * @since Date: Nov 7, 2011
 */
public final class Mic1InstructionReader {

  /**
   * the list of registers that can be written on the B-bus. The last four bits of a instruction generate the index in
   * this array. For example if the last four bits are 0010, then {@link Register#MBR} is returned.
   */
  private static final Register[] B_BUS_REGISTER = new Register[] { Register.MDR,
                                                                   Register.PC,
                                                                   Register.MBR,
                                                                   Register.MBRU,
                                                                   Register.SP,
                                                                   Register.LV,
                                                                   Register.CPP,
                                                                   Register.TOS,
                                                                   Register.OPC };

  /** mask for the last four bits: 0000 1111 */
  private static final int BIT5678 = 0x0F;

  /** mask for the first bit: 1000 0000 */
  private static final int BIT1 = 0x80;

  /** mask for the second bit: 0100 0000 */
  private static final int BIT2 = 0x40;

  /** mask for the third bit: 0010 0000 */
  private static final int BIT3 = 0x20;

  /** mask for the fourth bit: 0001 0000 */
  private static final int BIT4 = 0x10;

  /** mask for the fifth bit: 0000 1000 */
  private static final int BIT5 = 0x08;

  /** mask for the sixth bit: 0000 0100 */
  private static final int BIT6 = 0x04;

  /** mask for the seventh bit: 0000 0010 */
  private static final int BIT7 = 0x02;

  /** mask for the eighth bit: 0000 0001 */
  private static final int BIT8 = 0x01;

  /** the magic number that is needed at the begin of a binary mic1-file */
  public static final int MIC1_MAGIC_NUMBER = 0x12345678;

  // constructors

  /**
   * Hides constructor from being invoked. This is a utility class and objects of it don't make sense.
   * 
   * @since Date: Nov 10, 2011
   */
  private Mic1InstructionReader() {
    throw new AssertionError("called constructor of utility class");
  }

  // methods

  /**
   * Reads five bytes from the given {@link InputStream} and constructs one {@link Mic1Instruction}. Invoke several
   * times to read the whole data in the stream.
   * 
   * @since Date: Nov 10, 2011
   * @param in the inputstream to fetch five bytes from
   * @return a {@link Mic1Instruction} constructed from the bytes read or <code>null</code> if there are less than five
   *         bytes to read from the inputstream.
   * @throws IOException if something went wrong reading the given {@link InputStream}.
   */
  public static Mic1Instruction read(final InputStream in) throws IOException {
    final int b0 = in.read();
    final int b1 = in.read();
    final int b2 = in.read();
    final int b3 = in.read();
    final int b4 = in.read();

    // we have reached the end of data, so return null
    if (Utils.isOneValueMinusOne(new int[] { b0, b1, b2, b3, b4 })) {
      return null;
    }

    // b0:           abcd efgh b1: ijkl mnop
    // nextAddress: a bcde fghi
    // need to shift b0 to the left and insert msb of b1
    final int bitNumberOfSecondByte = 7;
    final int nextAddress = (b0 << 1) | ((b1 & BIT1) >> bitNumberOfSecondByte);

    final Mic1JMPSignalSet jmpSet = new Mic1JMPSignalSet();
    jmpSet.setJmpC((b1 & BIT2) > 0);
    jmpSet.setJmpN((b1 & BIT3) > 0);
    jmpSet.setJmpZ((b1 & BIT4) > 0);

    final Mic1ALUSignalSet aluSet = new Mic1ALUSignalSet();
    aluSet.setSLL8((b1 & BIT5) > 0);
    aluSet.setSRA1((b1 & BIT6) > 0);
    aluSet.setF0((b1 & BIT7) > 0);
    aluSet.setF1((b1 & BIT8) > 0);
    aluSet.setEnA((b2 & BIT1) > 0);
    aluSet.setEnB((b2 & BIT2) > 0);
    aluSet.setInvA((b2 & BIT3) > 0);
    aluSet.setInc((b2 & BIT4) > 0);

    final Mic1CBusSignalSet cBusSet = new Mic1CBusSignalSet();
    cBusSet.setH((b2 & BIT5) > 0);
    cBusSet.setOpc((b2 & BIT6) > 0);
    cBusSet.setTos((b2 & BIT7) > 0);
    cBusSet.setCpp((b2 & BIT8) > 0);
    cBusSet.setLv((b3 & BIT1) > 0);
    cBusSet.setSp((b3 & BIT2) > 0);
    cBusSet.setPc((b3 & BIT3) > 0);
    cBusSet.setMdr((b3 & BIT4) > 0);
    cBusSet.setMar((b3 & BIT5) > 0);

    final Mic1MemorySignalSet memSet = new Mic1MemorySignalSet();
    memSet.setWrite((b3 & BIT6) > 0);
    memSet.setRead((b3 & BIT7) > 0);
    memSet.setFetch((b3 & BIT8) > 0);

    final int b = (b4 >> 4) & BIT5678;

    return new Mic1Instruction(nextAddress, jmpSet, aluSet, cBusSet, memSet, decodeBBusBits(b));
  }

  /**
   * Decodes the value of b into the register that should be written on the B-Bus.
   * 
   * @since Date: Nov 12, 2011
   * @param b the four-bit-value to decode
   * @return the {@link Mic1BBusRegister} that should be written on the B-Bus.
   */
  private static Register decodeBBusBits(final int b) {
    if (b < 0 || b >= B_BUS_REGISTER.length) {
      return null;
    }
    return B_BUS_REGISTER[b];
  }
}
