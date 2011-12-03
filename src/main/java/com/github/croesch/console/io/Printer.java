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
package com.github.croesch.console.io;

import java.io.PrintStream;

import com.github.croesch.i18n.Text;

/**
 * The interface to print information to the console.
 * 
 * @author croesch
 * @since Date: Dec 2, 2011
 */
public final class Printer {

  /** the current {@link PrintStream} to write the output to */
  private static PrintStream out = System.out;

  /**
   * Hides constructor from being invoked. This is a utility class and objects of it don't make sense.
   * 
   * @since Date: Dec 2, 2011
   */
  private Printer() {
    throw new AssertionError("called constructor of utility class");
  }

  /**
   * Writes the given text to the {@link PrintStream} and adds a LF.
   * 
   * @since Date: Dec 2, 2011
   * @param line the text to be printed to the {@link PrintStream}. <code>null</code> -values will be ignored.
   */
  public static void println(final String line) {
    if (line != null) {
      out.println(line);
    }
  }

  /**
   * Writes the text representation of the given object to the {@link PrintStream} and adds a LF.
   * 
   * @since Date: Dec 2, 2011
   * @param obj the object, whose text representation should be printed to the {@link PrintStream}. <code>null</code>
   *        -values will be ignored.
   */
  public static void println(final Object obj) {
    if (obj != null) {
      out.println(obj);
    }
  }

  /**
   * Writes the text representation of the given object as an error to the {@link PrintStream} and adds a LF. If the
   * text representation of the object contains LFs, this will produce several different lines as output.
   * 
   * @since Date: Dec 2, 2011
   * @param obj the object, whose text representation should be printed to the {@link PrintStream} as an error.
   */
  public static void printErrorln(final Object obj) {
    if (obj != null) {
      for (final String line : obj.toString().split("\n")) {
        out.println(Text.ERROR.text(line));
      }
    }
  }

  /**
   * Sets the new {@link PrintStream} for the printer. From now on, output will be printed to the given stream.
   * 
   * @since Date: Dec 2, 2011
   * @param newOut the new {@link PrintStream} to set, <code>null</code>-values will be ignored.
   */
  public static void setPrintStream(final PrintStream newOut) {
    if (newOut != null) {
      out = newOut;
    }
  }
}
