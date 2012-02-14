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
package com.github.croesch.mic1;

import com.github.croesch.commons.Utils;

/**
 * Represents a code container that is e.g. able to print the code.
 * 
 * @author croesch
 * @since Date: Feb 5, 2012
 */
public abstract class AbstractCodeContainer {

  /**
   * Prints the whole code to the user.
   * 
   * @since Date: Jan 22, 2012
   */
  public final void printCode() {
    printCode(Integer.MIN_VALUE, Integer.MAX_VALUE);
  }

  /**
   * Prints the given number lines of code around the given line to the user.
   * 
   * @since Date: Jan 26, 2012
   * @param line the line around to print the code
   * @param scope the number of lines to print before and after the given line
   */
  public final void printCodeAroundLine(final int line, final int scope) {
    printCode(line - scope, line + scope);
  }

  /**
   * Prints the code between the given lines to the user.
   * 
   * @since Date: Jan 26, 2012
   * @param pos1 the first line to print
   * @param pos2 the last line to print
   */
  public final void printCode(final int pos1, final int pos2) {
    // correct arguments
    final int start = Math.max(getFirstPossibleCodeAddress(), Math.min(pos1, pos2));
    final int end = Math.min(getLastPossibleCodeAddress(), Math.max(pos1, pos2));

    for (int i = start; i <= end; ++i) {
      i += printCodeLine(i);
    }
  }

  /**
   * Returns the address where the first possible code is stored.
   * 
   * @since Date: Jan 26, 2012
   * @return the number of the first code line.
   */
  protected abstract int getFirstPossibleCodeAddress();

  /**
   * Returns the address where the last possible code is stored.
   * 
   * @since Date: Jan 26, 2012
   * @return the number of the last code line.
   */
  protected abstract int getLastPossibleCodeAddress();

  /**
   * Prints the line with the given number to the user. Returns the number of code lines to skip after this line, in
   * case of reading arguments.
   * 
   * @since Date: Jan 22, 2012
   * @param line the line number of the code instruction to print
   * @return the number of lines that can be skipped after printing this line
   */
  protected abstract int printCodeLine(int line);

  /**
   * Formats the given number to a hexadecimal number and returns an right aligned string with the given width.
   * 
   * @since Date: Jan 22, 2012
   * @param number the number to format
   * @param width the width the formatted string should at least have
   * @return the formatted string containing the hexadecimal value of the given number and at least <i>width</i>
   *         characters.
   */
  protected final String formatIntToHex(final int number, final int width) {
    return String.format("%" + width + "s", Utils.toHexString(number));
  }
}
