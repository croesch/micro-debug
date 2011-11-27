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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * This class represents the connection to the input of the mic1-processor. It is called buffered, because it reads one
 * line and provides single bytes from the read input to the reader.
 * 
 * @author croesch
 * @since Date: Nov 26, 2011
 */
public final class Input {

  /** the reader that reads from the input stream */
  private static BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

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
      in = new BufferedReader(new InputStreamReader(stream));
      line = null;
    }
  }

  /**
   * Reads a single byte from the given input stream. If the buffer is empty this will include reading a complete line
   * from the input stream.
   * 
   * @since Date: Nov 27, 2011
   * @return the byte value of the read byte, or -1 if the stream doesn't return anything to read.
   */
  public static byte read() {
    if (line == null || line.isEmpty()) {
      refill();
    }
    if (line == null || line.isEmpty()) {
      return -1;
    }
    final byte read = line.getBytes()[0];
    line = line.substring(1);

    return read;
  }

  /**
   * Reads a new line from the input stream to refill the internal buffer.
   * 
   * @since Date: Nov 27, 2011
   */
  private static void refill() {
    try {
      line = in.readLine();
    } catch (final IOException e) {
      e.printStackTrace();
    }
  }

}
