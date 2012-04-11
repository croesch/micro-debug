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
import com.github.croesch.micro_debug.commons.Utils;
import com.github.croesch.micro_debug.i18n.Text;
import com.github.croesch.micro_debug.mic1.controlstore.MicroInstruction;

/**
 * A breakpoint for a specific line in micro code.
 * 
 * @author croesch
 * @since Date: Feb 4, 2012
 */
final class MicroBreakpoint extends AbstractLineBreakpoint {

  /**
   * Constructs a breakpoint with the condition that the given line of micro code is executed.
   * 
   * @since Date: Feb 4, 2012
   * @param l the line that should be executed so that this breakpoint is activated
   */
  MicroBreakpoint(final int l) {
    super(l);
  }

  @Override
  public boolean isConditionMet(final int microLine,
                                final int macroLine,
                                final MicroInstruction currentInstruction,
                                final MicroInstruction nextInstruction) {
    return microLine == getLine();
  }

  @Override
  @NotNull
  public String toString() {
    return Text.BREAKPOINT_MICRO.text(getId(), Utils.toHexString(getLine()));
  }
}
