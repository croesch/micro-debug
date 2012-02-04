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

/**
 * Represents a breakpoint in the debugger.
 * 
 * @author croesch
 * @since Date: Jan 30, 2012
 */
public abstract class Breakpoint {

  /** the unique id of this breakpoint */
  private int id = 0;

  /** the currently highest unique id .. to calculate the next id */
  private static int highestId = 0;

  /**
   * Constructs a new {@link Breakpoint} with a unique id.
   * 
   * @since Date: Jan 30, 2012
   */
  public Breakpoint() {
    this.id = ++highestId;
  }

  /**
   * Returns the unique id of this breakpoint.
   * 
   * @since Date: Jan 30, 2012
   * @return the unique id of this breakpoint.
   */
  public final int getId() {
    return this.id;
  }

  /**
   * Returns whether the condition of this breakpoint is met and the debugger should stop now.
   * 
   * @since Date: Jan 30, 2012
   * @param microLine the number of the line in micro code being executed next
   * @param macroLine the number of the line in macro code being executed next
   * @return <code>true</code>, if the condition of this breakpoint is met and the debugger should stop.
   */
  public abstract boolean isConditionMet(int microLine, int macroLine);
}
