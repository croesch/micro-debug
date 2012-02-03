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
package com.github.croesch.mic1.api;

/**
 * TODO Comment here ...
 * 
 * @author croesch
 * @since Date: Feb 3, 2012
 */
public interface IReadableMemory {

  /**
   * Prints the content of the memory between the given addresses.
   * 
   * @since Date: Jan 29, 2012
   * @param pos1 the address to start (inclusive)
   * @param pos2 the address to end (inclusive)
   */
  void printContent(final int pos1, final int pos2);

  /**
   * Prints the assembler code stored in the memory to the user between the given lines.
   * 
   * @since Date: Jan 26, 2012
   * @param pos1 the first line to print
   * @param pos2 the last line to print
   */
  void printCode(final int pos1, final int pos2);

  /**
   * Prints the given number lines of assembler code stored in the memory to the user around the given line.
   * 
   * @since Date: Jan 26, 2012
   * @param line the line around to print the code
   * @param scope the number of lines to print before and after the given line
   */
  void printCodeAroundLine(final int line, final int scope);

  /**
   * Returns the formatted line.
   * 
   * @since Date: Feb 3, 2012
   * @param line the number of line to fetch.
   * @return the {@link String} representing the given line number
   */
  String getFormattedLine(int line);

  /**
   * Prints the assembler code stored in the memory to the user.
   * 
   * @since Date: Jan 22, 2012
   */
  void printCode();

  /**
   * Returns the word value at the given address.
   * 
   * @since Date: Jan 21, 2012
   * @param addr the address, from where to fetch the word
   * @return the word value read from the memory
   */
  int getWord(final int addr);

  /**
   * Returns a single byte, unsigned, read from the address in the memory.
   * 
   * @since Date: Jan 22, 2012
   * @param addr the byte address of the byte to read from the memory
   * @return the byte from the memory at the given address.
   */
  int getByte(final int addr);

}