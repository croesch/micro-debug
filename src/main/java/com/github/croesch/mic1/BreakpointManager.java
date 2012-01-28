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

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.logging.Logger;

import com.github.croesch.i18n.Text;
import com.github.croesch.mic1.register.Register;
import com.github.croesch.misc.Printer;
import com.github.croesch.misc.Utils;

/**
 * A manager for break points in the debugger.
 * 
 * @author croesch
 * @since Date: Jan 27, 2012
 */
final class BreakpointManager {

  /** the {@link Logger} for this class */
  private static final Logger LOGGER = Logger.getLogger(Text.class.getName());

  /** contains a list of values for each register that are set to be a breakpoint */
  private final EnumMap<Register, List<Integer>> registerBreakPoints = new EnumMap<Register, List<Integer>>(Register.class);

  /**
   * Constructs the break point manager.
   * 
   * @since Date: Jan 27, 2012
   */
  public BreakpointManager() {
    for (final Register r : Register.values()) {
      this.registerBreakPoints.put(r, new ArrayList<Integer>());
    }
  }

  /**
   * Returns whether any break point condition is met.
   * 
   * @since Date: Jan 27, 2012
   * @return <code>true</code> if a break point is met,<br>
   *         <code>false</code> otherwise
   */
  boolean isBreakpoint() {
    for (final Register r : Register.values()) {
      if (this.registerBreakPoints.get(r).contains(Integer.valueOf(r.getValue()))) {
        return true;
      }
    }
    return false;
  }

  /**
   * Adds a breakpoint for the given {@link Register} and the given value.
   * 
   * @since Date: Jan 27, 2012
   * @param r the {@link Register} to watch for the given value
   * @param val the value the debugger should break if the given {@link Register} has it.
   */
  void addBreakpoint(final Register r, final Integer val) {
    if (r != null && val != null) {
      if (this.registerBreakPoints.get(r).contains(val)) {
        LOGGER.fine("adding '" + Text.BREAKPOINT_REGISTER.text(r, val) + "' that already exists..");
      } else {
        this.registerBreakPoints.get(r).add(Integer.valueOf(val));
      }
    }
  }

  /**
   * Lists all breakpoints.
   * 
   * @since Date: Jan 28, 2012
   */
  void listBreakpoints() {
    for (final Register r : Register.values()) {
      for (final int i : this.registerBreakPoints.get(r)) {
        Printer.println(Text.BREAKPOINT_REGISTER.text(r, Utils.toHexString(i)));
      }
    }
  }
}
