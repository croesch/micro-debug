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
package com.github.croesch.commons;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.github.croesch.i18n.Text;

/**
 * The interface to read information from the console (or from the given {@link java.io.Reader}).
 * 
 * @author croesch
 * @since Date: Dec 3, 2011
 */
public final class Reader {

  /** the {@link java.io.Reader} to read the information from */
  private static BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

  /**
   * Hides constructor from being invoked. This is a utility class and objects of it don't make sense.
   * 
   * @since Date: Dec 3, 2011
   */
  private Reader() {
    throw new AssertionError("called constructor of utility class");
  }

  /**
   * Sets the {@link java.io.Reader} to read the information from. Will be buffered internally, so don't need to be a
   * {@link BufferedReader}.
   * 
   * @since Date: Dec 3, 2011
   * @param reader the new {@link java.io.Reader} for this component, not <code>null</code>.
   */
  public static void setReader(final java.io.Reader reader) {
    if (reader != null) {
      in = new BufferedReader(reader);
    }
  }

  /**
   * Reads a line from the current {@link java.io.Reader} and returns it. If an exception is thrown this will return
   * <code>null</code>.
   * 
   * @since Date: Dec 3, 2011
   * @return the line read from the {@link java.io.Reader} or<br>
   *         <code>null</code>, if an {@link IOException} occurred.
   */
  public static String readLine() {
    Printer.print(Text.INPUT_DEBUGGER);
    try {
      return in.readLine();
    } catch (final IOException e) {
      Utils.logThrownThrowable(e);
      return null;
    }
  }

}
