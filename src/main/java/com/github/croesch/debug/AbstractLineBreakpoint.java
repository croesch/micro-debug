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
 * An abstract breakpoint that represents a breakpoint at a specific line number in the code.
 * 
 * @author croesch
 * @since Date: Feb 4, 2012
 */
abstract class AbstractLineBreakpoint extends Breakpoint {

  /** the line that is the condition for this breakpoint */
  private final int line;

  /**
   * Constructs a breakpoint for the given line number in a code.
   * 
   * @since Date: Feb 4, 2012
   * @param l the line number this breakpoint should make the debugger to break at
   */
  AbstractLineBreakpoint(final int l) {
    this.line = l;
  }

  /**
   * Returns the line that is the condition.
   * 
   * @since Date: Feb 4, 2012
   * @return the line in code, where this breakpoint lies.
   */
  final int getLine() {
    return this.line;
  }

  @Override
  public final int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + this.line;
    result = prime * result + getClass().hashCode();
    return result;
  }

  @Override
  public final boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final AbstractLineBreakpoint other = (AbstractLineBreakpoint) obj;
    if (this.line != other.line) {
      return false;
    }
    return true;
  }
}
