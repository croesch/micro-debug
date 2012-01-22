/*
 * Copyright (C) 2011-2012  Christian Roesch
 * 
 * This file is part of micro-debug.
 * 
 * micro-debug is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * micro-debug is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with micro-debug.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.github.croesch.misc;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.logging.Logger;

import com.github.croesch.error.FileFormatException;
import com.github.croesch.i18n.Text;

/**
 * Class with utility methods.
 * 
 * @author croesch
 * @since Date: Nov 23, 2011
 */
public final class Utils {

  /** mask to select a byte from an integer */
  private static final int BYTE_MASK = 0xFF;

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
        throw new FileFormatException(Text.WRONG_FORMAT_TOO_SMALL);
      }

      // build the magic number fetched from the stream
      int magicNumber = b0 << Byte.SIZE * 3;
      magicNumber |= b1 << Byte.SIZE * 2;
      magicNumber |= b2 << Byte.SIZE;
      magicNumber |= b3;

      if (magicNumber != magic) {
        throw new FileFormatException(Text.WRONG_FORMAT_MAGIC_NUMBER);
      }

    } catch (final IOException e) {
      throw new FileFormatException(e.getMessage(), e);
    }
  }

  /**
   * Assembles the given bytes to one integer.
   * 
   * @since Date: Nov 23, 2011
   * @param b0 the most significant byte
   * @param b1 the second most significant byte
   * @param b2 the second least significant byte
   * @param b3 the least significant byte
   * @return an integer, assembled by the four bytes
   */
  public static int bytesToInt(final byte b0, final byte b1, final byte b2, final byte b3) {
    int number = b0 << Byte.SIZE * 3;
    number |= (b1 & BYTE_MASK) << Byte.SIZE * 2;
    number |= (b2 & BYTE_MASK) << Byte.SIZE;
    number |= b3 & BYTE_MASK;
    return number;
  }

  /**
   * Logs that the given {@link Throwable} has been thrown.
   * 
   * @param t the thrown {@link Throwable} to log
   * @since Date: Dec 2, 2011
   */
  public static void logThrownThrowable(final Throwable t) {
    final String className = Thread.currentThread().getStackTrace()[2].getClassName();
    final String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
    Logger.getLogger(className).throwing(className, methodName, t);
  }

  /**
   * Returns a string representation of the integer argument as an unsigned integer in base 16.<br>
   * <br>
   * For example: <code>100 &rarr; 0x64</code>
   * 
   * @since Date: Jan 14, 2012
   * @param number the number to represent as hexadecimal string
   * @return the string representation of the given number in upper case and with leading <code>0x</code>
   */
  public static String toHexString(final int number) {
    return "0x" + Integer.toHexString(number).toUpperCase(Locale.GERMAN);
  }

  /**
   * Giving a start value this iterates over all the other given values and returns the minimum of them that is higher
   * than the given start value. So to say the next higher value seen from the starting point.
   * 
   * @since Date: Jan 22, 2012
   * @param start the number to start from
   * @param values the other numbers to iterate over and to search for the next higher value
   * @return the next higher value than the start number<br>
   *         or {@link Integer#MAX_VALUE} if no value is higher than the given start number.
   */
  public static int getNextHigherValue(final int start, final int ... values) {
    int nextHigherValue = Integer.MAX_VALUE;
    for (final int val : values) {
      if (val < nextHigherValue && val > start) {
        nextHigherValue = val;
      }
    }
    return nextHigherValue;
  }
}
