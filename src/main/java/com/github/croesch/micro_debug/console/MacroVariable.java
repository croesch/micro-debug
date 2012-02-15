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
package com.github.croesch.micro_debug.console;

/**
 * Represents a variable in macro code that is stored on the stack.
 * 
 * @author croesch
 * @since Date: Feb 8, 2012
 */
final class MacroVariable {

  /** the local number of the variable, normally greater than zero */
  private final int number;

  /** the address of the variable in the memory */
  private final int address;

  /** the current value of the variable */
  private int value;

  /**
   * Constructs a new variable with the given values.
   * 
   * @since Date: Feb 8, 2012
   * @param num the local number of the variable
   * @param addr the address where the variable is located in the memory
   * @param val the value of the local variable
   */
  public MacroVariable(final int num, final int addr, final int val) {
    this.number = num;
    this.address = addr;
    this.value = val;
  }

  /**
   * Returns the local number of the variable, normally greater than zero.
   * 
   * @since Date: Feb 8, 2012
   * @return the local number of the variable as offset to the LV.
   */
  public int getNumber() {
    return this.number;
  }

  /**
   * Returns the address where this variable is stored in memory.
   * 
   * @since Date: Feb 8, 2012
   * @return the word address where this variable is stored in the memory.
   */
  public int getAddress() {
    return this.address;
  }

  /**
   * Returns the value of the variable.
   * 
   * @since Date: Feb 8, 2012
   * @return the last stored value of the variable.
   */
  public int getValue() {
    return this.value;
  }

  /**
   * Sets a new value for the variable.
   * 
   * @since Date: Feb 8, 2012
   * @param val the new value for the variable.
   */
  public void setValue(final int val) {
    this.value = val;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + this.address;
    result = prime * result + this.number;
    return result;
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    final MacroVariable other = (MacroVariable) obj;
    if (this.address != other.address) {
      return false;
    }
    if (this.number != other.number) {
      return false;
    }
    return true;
  }
}
