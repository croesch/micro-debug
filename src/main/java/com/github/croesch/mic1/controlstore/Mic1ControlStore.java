package com.github.croesch.mic1.controlstore;

import java.io.IOException;
import java.io.InputStream;

import com.github.croesch.mic1.FileFormatException;

/**
 * The store for {@link Mic1Instruction}s.
 * 
 * @author croesch
 * @since Date: Nov 19, 2011
 */
public final class Mic1ControlStore {

  /** the number of micro code instructions that are stored in this store */
  private static final int INSTRUCTIONS_PER_STORE = 512;

  /** number of bits to shift the first part of the magic number to the left */
  private static final int BITS_TO_SHIFT_FOR_MN1 = 24;

  /** number of bits to shift the second part of the magic number to the left */
  private static final int BITS_TO_SHIFT_FOR_MN2 = 16;

  /** number of bits to shift the third part of the magic number to the left */
  private static final int BITS_TO_SHIFT_FOR_MN3 = 8;

  /** the store of 512 micro code instructions */
  private final Mic1Instruction[] store = new Mic1Instruction[INSTRUCTIONS_PER_STORE];

  /**
   * Constructs a {@link Mic1ControlStore} with the {@link Mic1Instruction} fetched from the given stream. If the magic
   * number is incorrect, or if there are too few or too many bytes to read, a {@link FileFormatException} will be
   * thrown.
   * 
   * @since Date: Nov 19, 2011
   * @param in the stream to read the instructions from
   * @throws FileFormatException if there are not enough or too many bytes in the stream
   */
  public Mic1ControlStore(final InputStream in) throws FileFormatException {
    checkMagicNumber(in);

    boolean eof = false;
    // read the instructions from the stream
    for (int i = 0; !eof; ++i) {

      Mic1Instruction instr = null;
      try {
        instr = Mic1InstructionReader.read(in);
      } catch (final IOException e) {
        throw new FileFormatException(e);
      }

      if (instr == null) {
        // reached the end of input stream
        if (i == 0) {
          // only the magic number has been found
          throw new FileFormatException("File is empty.");
        }
        eof = true;
      } else if (i >= this.store.length) {
        // more instructions to read than capacity in the store
        throw new FileFormatException("File is too big.");
      } else {
        // save the instruction
        this.store[i] = instr;
      }
    }
  }

  /**
   * Reads the first four bytes of the given {@link InputStream} and throws an exception if they aren't equal to the
   * {@link Mic1InstructionReader#MIC1_MAGIC_NUMBER}.
   * 
   * @since Date: Nov 19, 2011
   * @param in the stream to read the bytes from
   * @throws FileFormatException if:
   *         <ul>
   *         <li>the stream is <code>null</code></li>
   *         <li>the stream doesn't contain four bytes</li>
   *         <li>the four bytes are not equalt to the {@link Mic1InstructionReader#MIC1_MAGIC_NUMBER}</li>
   *         <li>an {@link IOException} occurs</li>
   *         </ul>
   */
  private void checkMagicNumber(final InputStream in) throws FileFormatException {
    if (in == null) {
      throw new IllegalArgumentException("Input stream is required.");
    }

    try {

      final int b0 = in.read();
      final int b1 = in.read();
      final int b2 = in.read();
      final int b3 = in.read();

      // we have not enough data to determine a magic number
      if (Mic1InstructionReader.isOneValueMinusOne(new int[] { b0, b1, b2, b3 })) {
        throw new FileFormatException("File is too small.");
      }

      // build the magic number fetched from the stream
      int magicNumber = b0 << BITS_TO_SHIFT_FOR_MN1;
      magicNumber |= b1 << BITS_TO_SHIFT_FOR_MN2;
      magicNumber |= b2 << BITS_TO_SHIFT_FOR_MN3;
      magicNumber |= b3;

      if (magicNumber != Mic1InstructionReader.MIC1_MAGIC_NUMBER) {
        throw new FileFormatException("File has the wrong magic number");
      }

    } catch (final IOException e) {
      throw new FileFormatException(e);
    }
  }

  /**
   * Returns the instruction from the store that is stored under the given mpc (address).
   * 
   * @since Date: Nov 20, 2011
   * @param mpc the address of the instruction to fetch - only the least nine bits will be used.
   * @return the {@link Mic1Instruction} that is stored at the given address, or <code>null</code> if there is no
   *         instruction at the given address.
   */
  public Mic1Instruction getInstruction(final int mpc) {
    final int nineBitMask = 0x1FF;
    return this.store[mpc & nineBitMask];
  }
}
