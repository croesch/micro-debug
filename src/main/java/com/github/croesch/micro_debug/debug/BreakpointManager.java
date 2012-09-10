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

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.github.croesch.micro_debug.annotation.NotNull;
import com.github.croesch.micro_debug.commons.Printer;
import com.github.croesch.micro_debug.datatypes.DebugMode;
import com.github.croesch.micro_debug.i18n.Text;
import com.github.croesch.micro_debug.mic1.controlstore.MicroInstruction;
import com.github.croesch.micro_debug.mic1.register.Register;

/**
 * A manager for break points in the debugger.
 * 
 * @author croesch
 * @since Date: Jan 27, 2012
 */
public final class BreakpointManager {

  /** the {@link Logger} for this class */
  private static final Logger LOGGER = Logger.getLogger(Text.class.getName());

  /** contains a list of values for each register that are set to be a breakpoint */
  @NotNull
  private final List<Breakpoint> breakPoints = new ArrayList<Breakpoint>();

  /** the mode of debugging - micro, macro code or both */
  @NotNull
  private DebugMode debugMode = DebugMode.BOTH;

  /**
   * Returns whether any break point condition is met.
   * 
   * @since Date: Jan 27, 2012
   * @param microLine the number of the line in micro code being executed next
   * @param macroLine the number of the line in macro code being executed next
   * @param currentInstruction the current (last executed) {@link MicroInstruction}
   * @param nextInstruction the next (to be executed) {@link MicroInstruction}
   * @return <code>true</code> if a break point is met,<br>
   *         <code>false</code> otherwise
   */
  public boolean isBreakpoint(final int microLine,
                              final int macroLine,
                              final MicroInstruction currentInstruction,
                              final MicroInstruction nextInstruction) {
    for (final Breakpoint bp : this.breakPoints) {
      if (bp.shouldBreak(this.debugMode, microLine, macroLine, currentInstruction, nextInstruction)) {
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
  public void addRegisterBreakpoint(final Register r, final Integer val) {
    if (r != null && val != null) {
      final Breakpoint bp = new RegisterBreakpoint(r, val.intValue());
      if (this.breakPoints.contains(bp)) {
        logAlreadyExistingBreakpoint(Text.BREAKPOINT_REGISTER.text("", r, val.intValue()));
      } else {
        this.breakPoints.add(bp);
      }
    }
  }

  /**
   * Adds a breakpoint for the given {@link Register} on write access.
   * 
   * @since Date: Apr 11, 2012
   * @param r the {@link Register} to watch for being written
   */
  public void addRegisterBreakpoint(final Register r) {
    if (r != null) {
      final Breakpoint bp = new RegisterWriteBreakpoint(r);
      if (this.breakPoints.contains(bp)) {
        logAlreadyExistingBreakpoint(Text.BREAKPOINT_WRITE_REGISTER.text("", r));
      } else {
        this.breakPoints.add(bp);
      }
    }
  }

  /**
   * Returns whether there is a breakpoint for the given {@link Register} on write access.
   * 
   * @since Date: Jun 2, 2012
   * @param r the {@link Register} to check if it's currently being watched
   * @return <code>true</code> if the manager contains a breakpoint for the register on write access,<or>
   *         <code>false</code> otherwise
   */
  public boolean isRegisterBreakpoint(final Register r) {
    return r != null && this.breakPoints.contains(new RegisterWriteBreakpoint(r));
  }

  /**
   * Returns whether there is a breakpoint for the given {@link Register} and the given value.
   * 
   * @since Date: Jul 14, 2012
   * @param r the {@link Register} to check if it's currently being watched
   * @param val the value to check, if the debugger would break if the given {@link Register} has it.
   * @return <code>true</code> if the manager contains a breakpoint for the register and the given value,<or>
   *         <code>false</code> otherwise
   */
  public boolean isRegisterBreakpoint(final Register r, final Integer val) {
    return r != null && val != null && this.breakPoints.contains(new RegisterBreakpoint(r, val.intValue()));
  }

  /**
   * Removes the breakpoint for the given {@link Register} on write access. If the breakpoint has been set or not, after
   * calling this method, the breakpoint is definitely <em>not</em> set.
   * 
   * @since Date: Apr 11, 2012
   * @param r the {@link Register} to <em>not</em> watch anymore for being written
   */
  public void removeRegisterBreakpoint(final Register r) {
    if (r != null) {
      this.breakPoints.remove(new RegisterWriteBreakpoint(r));
    }
  }

  /**
   * Removes the breakpoint for the given {@link Register} and the given value. If the breakpoint has been set or not,
   * after calling this method, the breakpoint is definitely <em>not</em> set.
   * 
   * @since Date: Jul 14, 2012
   * @param r the {@link Register} to <em>not</em> watch anymore for being written
   * @param val the value the debugger should <em>not</em> break anymore if the given {@link Register} has it.
   */
  public void removeRegisterBreakpoint(final Register r, final Integer val) {
    if (r != null && val != null) {
      this.breakPoints.remove(new RegisterBreakpoint(r, val.intValue()));
    }
  }

  /**
   * Adds a breakpoint for the given line number in the micro code.
   * 
   * @since Date: Feb 4, 2012
   * @param line the line number in micro code the debugger should break at
   */
  public void addMicroBreakpoint(final Integer line) {
    if (line != null) {
      final Breakpoint bp = new MicroBreakpoint(line.intValue());
      if (this.breakPoints.contains(bp)) {
        logAlreadyExistingBreakpoint(Text.BREAKPOINT_MICRO.text("", line));
      } else {
        this.breakPoints.add(bp);
      }
    }
  }

  /**
   * Returns whether there is a breakpoint for the given line number in the micro code.
   * 
   * @since Date: Apr 18, 2012
   * @param line the line number in micro code to check for a breakpoint
   * @return <code>true</code> if the manager contains a breakpoint for the micro code in the given line,<or>
   *         <code>false</code> otherwise
   */
  public boolean isMicroBreakpoint(final Integer line) {
    return line != null && this.breakPoints.contains(new MicroBreakpoint(line.intValue()));
  }

  /**
   * Removes a breakpoint for the given line number in the micro code.
   * 
   * @since Date: Apr 18, 2012
   * @param line the line number in micro code the debugger shouldn't break at anymore,<br>
   *        if there has been a breakpoint or not, the debugger won't stop at the given line after calling this
   */
  public void removeMicroBreakpoint(final Integer line) {
    if (line != null) {
      this.breakPoints.remove(new MicroBreakpoint(line.intValue()));
    }
  }

  /**
   * Adds a breakpoint for the given line number in the macro code.
   * 
   * @since Date: Feb 4, 2012
   * @param line the line number in macro code the debugger should break at
   */
  public void addMacroBreakpoint(final Integer line) {
    if (line != null) {
      final Breakpoint bp = new MacroBreakpoint(line.intValue());
      if (this.breakPoints.contains(bp)) {
        logAlreadyExistingBreakpoint(Text.BREAKPOINT_MACRO.text("", line));
      } else {
        this.breakPoints.add(bp);
      }
    }
  }

  /**
   * Returns whether there is a breakpoint for the given line number in the macro code.
   * 
   * @since Date: Apr 18, 2012
   * @param line the line number in macro code to check for a breakpoint
   * @return <code>true</code> if the manager contains a breakpoint for the macro code in the given line,<or>
   *         <code>false</code> otherwise
   */
  public boolean isMacroBreakpoint(final Integer line) {
    return line != null && this.breakPoints.contains(new MacroBreakpoint(line.intValue()));
  }

  /**
   * Removes a breakpoint for the given line number in the macro code.
   * 
   * @since Date: Apr 18, 2012
   * @param line the line number in macro code the debugger shouldn't break at anymore,<br>
   *        if there has been a breakpoint or not, the debugger won't stop at the given line after calling this
   */
  public void removeMacroBreakpoint(final Integer line) {
    if (line != null) {
      this.breakPoints.remove(new MacroBreakpoint(line.intValue()));
    }
  }

  /**
   * Logs that a breakpoint is being added that already existed. The duplicate breakpoint is described by the given
   * {@link String}.
   * 
   * @since Date: Apr 11, 2012
   * @param description the {@link String} that describes the duplicate breakpoint
   */
  private void logAlreadyExistingBreakpoint(final String description) {
    LOGGER.fine("adding '" + description + "' that already exists..");
  }

  /**
   * Removes the breakpoint with the given unique id.
   * 
   * @since Date: Jan 30, 2012
   * @param id the unique id of the breakpoint to remove
   */
  public void removeBreakpoint(final int id) {
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
   * Removes all breakpoints that are available. After calling this operation no breakpoint is set.
   * 
   * @since Date: Sep 10, 2012
   */
  public void removeAllBreakpoints() {
    this.breakPoints.clear();
  }

  /**
   * Lists all breakpoints.
   * 
   * @since Date: Jan 28, 2012
   */
  public void listBreakpoints() {
    for (final Breakpoint bp : this.breakPoints) {
      Printer.println(bp);
    }
  }

  /**
   * Sets the new {@link DebugMode}.
   * 
   * @since Date: Sep 10, 2012
   * @param mode the new {@link DebugMode}.
   */
  public void setDebuggingMode(final DebugMode mode) {
    this.debugMode = mode;
  }
}
