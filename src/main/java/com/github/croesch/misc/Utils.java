package com.github.croesch.misc;

import java.io.IOException;
import java.io.InputStream;

import com.github.croesch.error.FileFormatException;

/**
 * Class with utility methods.
 * 
 * @author croesch
 * @since Date: Nov 23, 2011
 */
public final class Utils {

  /**
   * Hides constructor from being invoked. This is a utility class and objects of it don't make sense.
   * 
   * @since Date: Nov 23, 2011
   */
  private Utils() {
    throw new AssertionError("called constructor of utility class");
  }

  /**
   * Returns whether one of the given numbers is equal to <code>-1</code>.
   * 
   * @since Date: Nov 13, 2011
   * @param values the numbers to check if any is equal to <code>-1</code>.
   * @return <code>true</code>, if at least one of the given values is equal to <code>-1</code>.
   */
  public static boolean isOneValueMinusOne(final int[] values) {
    if (values != null) {
      final int problematicValue = -1;
      for (final int val : values) {
        if (val == problematicValue) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * Reads the first four bytes of the given {@link InputStream} and throws an exception if they aren't equal to the
   * expected magic number.
   * 
   * @since Date: Nov 19, 2011
   * @param in the stream to read the bytes from
   * @param magic the magic number
   * @throws IllegalArgumentException if the given stream is <code>null</code>.
   * @throws FileFormatException if:
   *         <ul>
   *         <li>the stream doesn't contain four bytes</li>
   *         <li>the four bytes are not equal to the given magic number</li>
   *         <li>an {@link IOException} occurs</li>
   *         </ul>
   */
  public static void checkMagicNumber(final InputStream in, final int magic) throws FileFormatException,
                                                                            IllegalArgumentException {
    if (in == null) {
      throw new IllegalArgumentException("Input stream is required.");
    }

    try {

      final int b0 = in.read();
      final int b1 = in.read();
      final int b2 = in.read();
      final int b3 = in.read();

      // we have not enough data to determine a magic number
      if (isOneValueMinusOne(new int[] { b0, b1, b2, b3 })) {
        throw new FileFormatException("File is too small.");
      }

      // build the magic number fetched from the stream
      int magicNumber = b0 << Byte.SIZE * 3;
      magicNumber |= b1 << Byte.SIZE * 2;
      magicNumber |= b2 << Byte.SIZE;
      magicNumber |= b3;

      if (magicNumber != magic) {
        throw new FileFormatException("File has the wrong magic number");
      }

    } catch (final IOException e) {
      throw new FileFormatException(e);
    }
  }

}
