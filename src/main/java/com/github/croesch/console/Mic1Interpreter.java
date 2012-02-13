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

import com.github.croesch.mic1.Mic1;
import com.github.croesch.mic1.api.IProcessorInterpreter;
import com.github.croesch.mic1.register.Register;

/**
 * Interpreter of a processor, can access and manipulate the processor.
 * 
 * @author croesch
 * @since Date: Feb 13, 2012
 */
public final class Mic1Interpreter implements IProcessorInterpreter {

  /** the processor to interprete */
  private final Mic1 mic1;

  /** the manager for break points */
  private final BreakpointManager bpm = new BreakpointManager();

  /**
   * Constructs an interpreter for the given processor.
   * 
   * @since Date: Feb 13, 2012
   * @param mic the processor to interprete
   */
  public Mic1Interpreter(final Mic1 mic) {
    this.mic1 = mic;
    if (this.mic1 != null) {
      mic.setProcessorInterpreter(this);
    }
  }

  /**
   * Adds a breakpoint for the given {@link Register} and the given value. Debugger will break, if the given
   * {@link Register} has the given value.
   * 
   * @since Date: Jan 27, 2012
   * @param r the {@link Register} to watch for the given value
   * @param value the value that should be a break point if the given {@link Register} has it.
   */
  public void addRegisterBreakpoint(final Register r, final Integer value) {
    this.bpm.addRegisterBreakpoint(r, value);
  }

  /**
   * Adds a breakpoint for the given line number in the micro code.
   * 
   * @since Date: Feb 4, 2012
   * @param line the line number in micro code the debugger should break at
   */
  public void addMicroBreakpoint(final Integer line) {
    this.bpm.addMicroBreakpoint(line);
  }

  /**
   * Adds a breakpoint for the given line number in the macro code.
   * 
   * @since Date: Feb 4, 2012
   * @param line the line number in macro code the debugger should break at
   */
  public void addMacroBreakpoint(final Integer line) {
    this.bpm.addMacroBreakpoint(line);
  }

  /**
   * Removes the breakpoint with the given unique id.
   * 
   * @since Date: Jan 30, 2012
   * @param id the unique id of the breakpoint to remove
   */
  public void removeBreakpoint(final int id) {
    this.bpm.removeBreakpoint(id);
  }

  /**
   * Lists all breakpoints.
   * 
   * @since Date: Jan 28, 2012
   */
  public void listBreakpoints() {
    this.bpm.listBreakpoints();
  }

  /**
   * Returns whether the processor should halt now or if it can continue.
   * 
   * @since Date: Feb 13, 2012
   * @param microLine the number of the line in micro code being executed next
   * @param macroLine the number of the line in macro code being executed next
   * @return <code>true</code> if the processor can continue executing instructions,<br>
   *         <code>false</code> otherwise
   */
  public boolean canContinue(final int microLine, final int macroLine) {
    return !this.bpm.isBreakpoint(microLine, macroLine);
  }

  /**
   * Returns the processor that this is interpreting.
   * 
   * @since Date: Feb 13, 2012
   * @return the instance of the processor this is interpreting.
   */
  public Mic1 getProcessor() {
    return this.mic1;
  }
}
