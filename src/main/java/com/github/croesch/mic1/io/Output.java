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
package com.github.croesch.mic1.io;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the connection to the output of the mic1-processor. It can buffer the output until it prints it
 * to its {@link PrintStream} or put each single byte to its {@link PrintStream}.
 * 
 * @author croesch
 * @since Date: Nov 26, 2011
 */
public final class Output {

  /** value of the byte that forces to flush the buffer */
  private static final int LINE_FEED = 10;

  /** <code>true</code>, if the output is buffered until a LF is printed */
  private static boolean buffered = true;

  /** contains the buffered bytes */
  private static List<Byte> buffer = new ArrayList<Byte>();

  /** the print stream to write the output to */
  private static PrintStream out = System.out;

  /**
   * Hides constructor from being invoked. This is a utility class and objects of it don't make sense.
   * 
   * @since Date: Nov 26, 2011
   */
  private Output() {
    throw new AssertionError("called constructor of utility class");
  }

  /**
   * Determines if any output should be buffered before it's written to the {@link PrintStream} or not. If output is
   * buffered, this class will wait writing the bytes to the {@link PrintStream} until a line terminating character is
   * written to the output. If it's not buffered, each byte will be directly written to the {@link PrintStream}.<br />
   * Note: The output'll be flushed when invoking this method.
   * 
   * @since Date: Nov 26, 2011
   * @param buf <code>true</code>, if the output should be buffered
   * @see #flush()
   */
  public static void setBuffered(final boolean buf) {
    buffered = buf;
    flush();
  }

  /**
   * Returns whether this output is buffered or not. See {@link #setBuffered(boolean)} for more information.
   * 
   * @since Date: Dec 3, 2011
   * @return <code>true</code>, if the output is buffered
   * @see #setBuffered(boolean)
   */
  public static boolean isBuffered() {
    return buffered;
  }

  /**
   * Prints a single byte to the {@link PrintStream}. If the output is buffered, the output will be flushed, when
   * {@link #print(byte)} is called with a LF.
   * 
   * @since Date: Nov 26, 2011
   * @param val the byte to write to the {@link PrintStream}
   */
  public static void print(final byte val) {
    if (buffered) {
      buffer.add(Byte.valueOf(val));
      if (val == LINE_FEED) {
        flush();
      }
    } else {
      out.print((char) val);
    }
  }

  /**
   * Empties the buffer and writes everything to the {@link PrintStream}
   * 
   * @since Date: Nov 26, 2011
   */
  public static void flush() {
    while (!buffer.isEmpty()) {
      out.print((char) buffer.remove(0).byteValue());
    }
  }

  /**
   * Sets the {@link PrintStream} to write the data to.
   * 
   * @since Date: Nov 26, 2011
   * @param newOut the new {@link PrintStream}, mustn't be <code>null</code>
   */
  public static void setOut(final PrintStream newOut) {
    if (newOut != null) {
      out = newOut;
    }
  }

}
