/*
 * Copyright (C) 2011-2012 Christian Roesch
 * This file is part of micro-debug.
 * micro-debug is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * micro-debug is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with micro-debug. If not, see <http://www.gnu.org/licenses/>.
 */
package com.github.croesch.mic1.io;

import java.io.IOException;
import java.io.InputStream;

import com.github.croesch.misc.Utils;

/**
 * This class represents the connection to the input of the mic1-processor. It is called buffered, because it reads one
 * line and provides single bytes from the read input to the reader.
 * 
 * @author croesch
 * @since Date: Nov 26, 2011
 */
public final class Input {

  /** the input stream to read data from */
  private static InputStream in = System.in;

  /** the current line */
  private static String line = "";

  /**
   * Hides constructor from being invoked. This is a utility class and objects of it don't make sense.
   * 
   * @since Date: Nov 26, 2011
   */
  private Input() {
    throw new AssertionError("called constructor of utility class");
  }

  /**
   * Sets the input stream for the component. The processor will now read from the given stream.
   * 
   * @since Date: Nov 27, 2011
   * @param stream the new input stream, mustn't be <code>null</code>
   */
  public static void setIn(final InputStream stream) {
    if (stream != null) {
      in = stream;
      line = null;
    }
  }

  /**
   * Reads a single byte from the given input stream. If the buffer is empty this will include reading a complete line
   * from the input stream.
   * 
   * @since Date: Nov 27, 2011
   * @return the byte value of the read byte,<br>
   *         or <code>-1</code> if the stream doesn't return anything to read.
   */
  public static byte read() {
    if (line == null || line.equals("")) {
      // the buffer is empty, so read the next line
      readLine();
    }
    if (line == null || line.equals("")) {
      // if there is still no data, return -1
      return -1;
    }
    // read first byte and remove it from buffer
    final byte read = line.getBytes()[0];
    line = line.substring(1);

    return read;
  }

  /**
   * Reads a line from the input stream to refill the internal buffer.
   * 
   * @since Date: Nov 27, 2011
   */
  private static void readLine() {
    try {
      final StringBuilder sb = new StringBuilder();

      boolean endOfLine = false;
      while (!endOfLine) {
        final int read = in.read();
        if (read == -1) {
          // the end of the stream has been reached
          endOfLine = true;
        } else {
          // we have read data from the stream, append it
          sb.append((char)read);
          // if the LF is read, update the flag
          endOfLine = read == '\n';
        }
      }
      // set the buffer to the read data
      line = sb.toString();
    } catch (final IOException e) {
      Utils.logThrownThrowable(e);
    }
  }

}
