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
package com.github.croesch.console;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.github.croesch.commons.Printer;
import com.github.croesch.debug.Breakpoint;
import com.github.croesch.debug.MacroBreakpoint;
import com.github.croesch.debug.MicroBreakpoint;
import com.github.croesch.debug.RegisterBreakpoint;
import com.github.croesch.i18n.Text;
import com.github.croesch.mic1.register.Register;

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
  private final List<Breakpoint> breakPoints = new ArrayList<Breakpoint>();

  /**
   * Returns whether any break point condition is met.
   * 
   * @since Date: Jan 27, 2012
   * @param microLine the number of the line in micro code being executed next
   * @param macroLine the number of the line in macro code being executed next
   * @return <code>true</code> if a break point is met,<br>
   *         <code>false</code> otherwise
   */
  boolean isBreakpoint(final int microLine, final int macroLine) {
    for (final Breakpoint bp : this.breakPoints) {
      if (bp.isConditionMet(microLine, macroLine)) {
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
  void addRegisterBreakpoint(final Register r, final Integer val) {
    if (r != null && val != null) {
      final Breakpoint bp = new RegisterBreakpoint(r, val);
      if (this.breakPoints.contains(bp)) {
        LOGGER.fine("adding '" + Text.BREAKPOINT_REGISTER.text("", r, val) + "' that already exists..");
      } else {
        this.breakPoints.add(bp);
      }
    }
  }

  /**
   * Adds a breakpoint for the given line number in the micro code.
   * 
   * @since Date: Feb 4, 2012
   * @param line the line number in micro code the debugger should break at
   */
  void addMicroBreakpoint(final Integer line) {
    if (line != null) {
      final Breakpoint bp = new MicroBreakpoint(line.intValue());
      if (this.breakPoints.contains(bp)) {
        LOGGER.fine("adding '" + Text.BREAKPOINT_MICRO.text("", line) + "' that already exists..");
      } else {
        this.breakPoints.add(bp);
      }
    }
  }

  /**
   * Adds a breakpoint for the given line number in the macro code.
   * 
   * @since Date: Feb 4, 2012
   * @param line the line number in macro code the debugger should break at
   */
  void addMacroBreakpoint(final Integer line) {
    if (line != null) {
      final Breakpoint bp = new MacroBreakpoint(line.intValue());
      if (this.breakPoints.contains(bp)) {
        LOGGER.fine("adding '" + Text.BREAKPOINT_MACRO.text("", line) + "' that already exists..");
      } else {
        this.breakPoints.add(bp);
      }
    }
  }

  /**
   * Removes the breakpoint with the given unique id.
   * 
   * @since Date: Jan 30, 2012
   * @param id the unique id of the breakpoint to remove
   */
  void removeBreakpoint(final int id) {
    boolean removed = false;
    for (final Breakpoint bp : this.breakPoints) {
      if (bp.getId() == id) {
        removed = this.breakPoints.remove(bp);
        break;
      }
    }

    if (!removed) {
      LOGGER.fine("couldn't remove breakpoint #" + id);
    }
  }

  /**
   * Lists all breakpoints.
   * 
   * @since Date: Jan 28, 2012
   */
  void listBreakpoints() {
    for (final Breakpoint bp : this.breakPoints) {
      Printer.println(bp);
    }
  }
}
