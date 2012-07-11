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
package com.github.croesch.micro_debug.mic1.mem;

import com.github.croesch.micro_debug.annotation.NotNull;
import com.github.croesch.micro_debug.commons.Utils;
import com.github.croesch.micro_debug.mic1.register.Register;

/**
 * Argument that belongs to an {@link IJVMCommand}.
 * 
 * @author croesch
 * @since Date: Jan 22, 2012
 */
public enum IJVMCommandArgument {

  /** represents a single byte */
  BYTE (1),
  /** represents a label defined in the assembler code */
  LABEL (2) {
    @Override
    public String represent(final int addr, final int value, final Memory mem) {
      return Utils.toHexString(addr + signExtend(value));
    }

    /**
     * Returns the sign extended value of the given input number.
     * 
     * @since Date: Feb 3, 2012
     * @param num the value to sign extend
     * @return the sign extended number
     */
    private int signExtend(final int num) {
      if ((num & (SIGN_MASK_2)) != 0) {
        return num | ~BYTE_MASK_2;
      }
      return num;
    }
  },
  /** represents a constant */
  CONST (1),
  /** represents a variable */
  VARNUM (1) {
    @Override
    public String represent(final int addr, final int value, final Memory mem) {
      return String.valueOf(value);
    }
  },
  /** represents an offset */
  OFFSET (2) {
    @Override
    public String represent(final int addr, final int value, final Memory mem) {
      final int cons = mem.getWord(Register.CPP.getValue() + value);
      return value + "[=" + Utils.toHexString(cons) + "]";
    }
  },
  /** represents an index */
  INDEX (2) {
    @Override
    public String represent(final int addr, final int value, final Memory mem) {
      final int cons = mem.getWord(Register.CPP.getValue() + value);
      return value + "[=" + Utils.toHexString(cons) + "]";
    }
  };

  /** mask to select one byte */
  private static final int BYTE_MASK_1 = 0xFF;

  /** mask to select two bytes */
  private static final int BYTE_MASK_2 = 0xFFFF;

  /** mask to select sign bit of two bytes */
  private static final int SIGN_MASK_2 = 0x8000;

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
  public final int getNumberOfBytes() {
    return this.bytes;
  }

  /**
   * Returns the {@link String} representing the given value of this argument.
   * 
   * @since Date: Jan 23, 2012
   * @param addr the address of the command this argument belongs to
   * @param value the value of the argument to represent as {@link String}
   * @param mem the memory to fetch values from
   * @return the {@link String} representing the given value for this argument.
   */
  @NotNull
  public final String getRepresentationOfArgument(final int addr, final int value, final Memory mem) {
    return represent(addr, maskValue(value), mem);
  }

  /**
   * Ensures that the given number is only as big as expected.
   * 
   * @since Date: Jan 23, 2012
   * @param value the input number to mask
   * @return the number that is as big as {@link #getNumberOfBytes()} indicates
   */
  private int maskValue(final int value) {
    switch (getNumberOfBytes()) {
      case 1:
        return value & BYTE_MASK_1;
      case 2:
        return value & BYTE_MASK_2;
      default:
        return value;
    }
  }

  /**
   * Returns the {@link String} representation of the given value that is expected to have the correct size. By default
   * this will return the hexadecimal representation of the given number.
   * 
   * @since Date: Jan 23, 2012
   * @param addr the address of the command this argument belongs to
   * @param value the number to represent
   * @param mem the memory to fetch values from
   * @return the string representation of the given value for that argument.
   */
  @NotNull
  public String represent(final int addr, final int value, final Memory mem) {
    return Utils.toHexString(value);
  }
}
