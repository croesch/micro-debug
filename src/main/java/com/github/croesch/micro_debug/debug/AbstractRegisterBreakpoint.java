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
package com.github.croesch.micro_debug.debug;

import com.github.croesch.micro_debug.annotation.NotNull;
import com.github.croesch.micro_debug.datatypes.DebugMode;
import com.github.croesch.micro_debug.mic1.register.Register;

/**
 * An abstract breakpoint that checks a specific register.
 * 
 * @author croesch
 * @since Date: Apr 11, 2012
 */
abstract class AbstractRegisterBreakpoint extends Breakpoint {

  /** the register that is checked for the breakpoint condition */
  @NotNull
  private final Register register;

  /**
   * Constructs this abstract register breakpoint that checks the given register for the breakpoint condition.
   * 
   * @since Date: Apr 11, 2012
   * @param r the {@link Register} to be checked for the breakpoint condition by
   *        {@link #isConditionMet(int, int, com.github.croesch.micro_debug.mic1.controlstore.MicroInstruction, com.github.croesch.micro_debug.mic1.controlstore.MicroInstruction)}
   *        , may not be <code>null</code>.
   */
  AbstractRegisterBreakpoint(final Register r) {
    if (r == null) {
      throw new IllegalArgumentException();
    }
    this.register = r;
  }

  /**
   * Returns the {@link Register} that is part of the condition.
   * 
   * @since Date: Jan 30, 2012
   * @return the {@link Register} to check for the condition value.
   */
  @NotNull
  Register getRegister() {
    return this.register;
  }

  @Override
  boolean isBreakpointForMode(final DebugMode mode) {
    return mode != DebugMode.MACRO;
  }
}
