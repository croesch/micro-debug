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
package com.github.croesch.mic1.mem;

import com.github.croesch.misc.Utils;

/**
 * Argument that belongs to an {@link IJVMCommand}.
 * 
 * @author croesch
 * @since Date: Jan 22, 2012
 */
enum IJVMCommandArgument {

  /** represents a single byte */
  BYTE (1),
  /** represents a label defined in the assembler code */
  LABEL (2),
  /** represents a constant */
  CONST (1),
  /** represents a variable */
  VARNUM (1),
  /** represents an offset */
  OFFSET (2),
  /** represents an index */
  INDEX (2);

  /** the number of bytes this argument is build of */
  private int bytes;

  /**
   * Constructs this argument with the given number of bytes.
   * 
   * @since Date: Jan 23, 2012
   * @param b the number of bytes this argument needs
   */
  private IJVMCommandArgument(final int b) {
    this.bytes = b;
  }

  /**
   * Returns the number of bytes needed to build this argument.
   * 
   * @since Date: Jan 23, 2012
   * @return number of bytes needed to build this argument
   */
  int getNumberOfBytes() {
    return this.bytes;
  }

  /**
   * Returns the {@link String} representing the given value of this argument.<br>
   * TODO make this dependent on the arguments
   * 
   * @since Date: Jan 23, 2012
   * @param value the value of the argument to represent as {@link String}
   * @return the {@link String} representing the given value for this argument.
   */
  String getRepresentationOfArgument(final int value) {
    return Utils.toHexString(value);
  }
}
