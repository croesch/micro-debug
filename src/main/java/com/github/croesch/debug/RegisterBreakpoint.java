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
package com.github.croesch.debug;

import com.github.croesch.i18n.Text;
import com.github.croesch.mic1.register.Register;
import com.github.croesch.misc.Utils;

/**
 * A breakpoint for a specific value of a register.
 * 
 * @author croesch
 * @since Date: Jan 30, 2012
 */
public final class RegisterBreakpoint extends Breakpoint {

  /** the register that is checked for the value */
  private final Register register;

  /** the value that is the condition for this breakpoint */
  private final int val;

  /**
   * Constructs a breakpoint with the condition that the given {@link Register} has the given value.
   * 
   * @since Date: Jan 30, 2012
   * @param r the {@link Register} to check for the given value
   * @param v the value that the given {@link Register} should have, that is the breakpoint condition
   */
  public RegisterBreakpoint(final Register r, final int v) {
    if (r == null) {
      throw new IllegalArgumentException();
    }
    this.register = r;
    this.val = v;
  }

  @Override
  public boolean isConditionMet() {
    return this.register.getValue() == this.val;
  }

  @Override
  public String toString() {
    return Text.BREAKPOINT_REGISTER.text(getId(), this.register, Utils.toHexString(this.val));
  }

  /**
   * Returns the {@link Register} that is part of the condition.
   * 
   * @since Date: Jan 30, 2012
   * @return the {@link Register} to check for the condition value.
   */
  public Register getRegister() {
    return this.register;
  }

  /**
   * Returns the value that is part of the condition.
   * 
   * @since Date: Jan 30, 2012
   * @return the value to check the given {@link Register} for.
   */
  public int getValue() {
    return this.val;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + this.register.hashCode();
    result = prime * result + this.val;
    return result;
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final RegisterBreakpoint other = (RegisterBreakpoint) obj;
    if (this.register != other.register) {
      return false;
    }
    if (this.val != other.val) {
      return false;
    }
    return true;
  }
}
