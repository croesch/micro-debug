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

import com.github.croesch.micro_debug.datatypes.DebugMode;
import com.github.croesch.micro_debug.mic1.controlstore.MicroInstruction;

/**
 * Represents a breakpoint in the debugger.
 * 
 * @author croesch
 * @since Date: Jan 30, 2012
 */
abstract class Breakpoint {

  /** the unique id of this breakpoint */
  private int id = 0;

  /** the currently highest unique id .. to calculate the next id */
  private static int highestId = 0;

  /**
   * Constructs a new {@link Breakpoint} with a unique id.
   * 
   * @since Date: Jan 30, 2012
   */
  Breakpoint() {
    incrementHighestId();
    this.id = highestId;
  }

  /**
   * Increments the highest id of the breakpoint by one.
   * 
   * @since Date: Jul 11, 2012
   */
  private static void incrementHighestId() {
    ++highestId;
  }

  /**
   * Returns the unique id of this breakpoint.
   * 
   * @since Date: Jan 30, 2012
   * @return the unique id of this breakpoint.
   */
  final int getId() {
    return this.id;
  }

  /**
   * Returns whether the condition of this breakpoint is met and the debugger should stop now.
   * 
   * @param debuggingMode
   * @since Date: Sep 10, 2012
   * @param mode the current mode of debugging
   * @param microLine the number of the line in micro code being executed next
   * @param macroLine the number of the line in macro code being executed next
   * @param currentInstruction the current (last executed) {@link MicroInstruction}
   * @param nextInstruction the next (to be executed) {@link MicroInstruction}
   * @return <code>true</code>, if the condition of this breakpoint is met and the debugger should stop.
   */
  boolean shouldBreak(final DebugMode mode,
                      final int microLine,
                      final int macroLine,
                      final MicroInstruction currentInstruction,
                      final MicroInstruction nextInstruction) {
    return isBreakpointForMode(mode) && isConditionMet(microLine, macroLine, currentInstruction, nextInstruction);
  }

  /**
   * Returns whether the condition of this breakpoint is met.
   * 
   * @param debuggingMode
   * @since Date: Jan 30, 2012
   * @param microLine the number of the line in micro code being executed next
   * @param macroLine the number of the line in macro code being executed next
   * @param currentInstruction the current (last executed) {@link MicroInstruction}
   * @param nextInstruction the next (to be executed) {@link MicroInstruction}
   * @return <code>true</code>, if the condition of this breakpoint is met.
   */
  abstract boolean isConditionMet(int microLine,
                                  int macroLine,
                                  MicroInstruction currentInstruction,
                                  MicroInstruction nextInstruction);

  /**
   * Returns whether this breakpoint is a breakpoint for the given debugging mode.
   * 
   * @since Date: Sep 10, 2012
   * @param mode the current debugging mode
   * @return <code>true</code> if this breakpoint is a breakpoint for the given debugging mode.
   */
  abstract boolean isBreakpointForMode(DebugMode mode);
}
