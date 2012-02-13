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
package com.github.croesch.ui.api;

import com.github.croesch.mic1.controlstore.MicroInstruction;
import com.github.croesch.mic1.register.Register;

/**
 * View for the {@link com.github.croesch.mic1.Mic1}.
 * 
 * @author croesch
 * @since Date: Jan 15, 2012
 */
public interface Mic1View {

  /**
   * Lists the values of all {@link Register}s.
   * 
   * @since Date: Jan 15, 2012
   */
  void listAllRegisters();

  /**
   * Lists the value of a single {@link Register}.
   * 
   * @since Date: Jan 15, 2012
   * @param r the {@link Register} to print with its value.
   */
  void listRegister(Register r);

  /**
   * Performs to trace all {@link Register}s.
   * 
   * @since Date: Jan 15, 2012
   */
  void traceRegister();

  /**
   * Performs to trace the given {@link Register}.
   * 
   * @since Date: Jan 15, 2012
   * @param r the {@link Register} to trace.
   */
  void traceRegister(Register r);

  /**
   * Performs to not trace any {@link Register}.
   * 
   * @since Date: Jan 15, 2012
   */
  void untraceRegister();

  /**
   * Performs to not trace the given {@link Register} anymore.
   * 
   * @since Date: Jan 15, 2012
   * @param r the {@link Register} not being traced anymore.
   */
  void untraceRegister(Register r);

  /**
   * Returns whether the given {@link Register} is currently traced.
   * 
   * @since Date: Jan 15, 2012
   * @param r the {@link Register} to check, if it's traced
   * @return <code>true</code>, if the {@link Register} is currently traced<br>
   *         <code>false</code> otherwise.
   */
  boolean isTracing(Register r);

  /**
   * Returns whether the micro code is currently traced.
   * 
   * @since Date: Jan 21, 2012
   * @return <code>true</code>, if the micro code is currently traced<br>
   *         <code>false</code> otherwise.
   */
  boolean isTracingMicro();

  /**
   * Performs to trace the micro code.
   * 
   * @since Date: Jan 21, 2012
   */
  void traceMicro();

  /**
   * Performs to not trace the micro code anymore.
   * 
   * @since Date: Jan 21, 2012
   */
  void untraceMicro();

  /**
   * Performs to trace the macro code.
   * 
   * @since Date: Feb 03, 2012
   */
  void traceMacro();

  /**
   * Performs to not trace the macro code anymore.
   * 
   * @since Date: Feb 03, 2012
   */
  void untraceMacro();

  /**
   * Tells the view to update itself.
   * 
   * @since Date: Jan 15, 2012
   * @param currentInstruction the instruction that is now executed
   * @param macroCodeLine the formatted macro code line being executed, or <code>null</code> if no new macro code line
   *        has been reached
   */
  void update(MicroInstruction currentInstruction, String macroCodeLine);

  /**
   * Start tracing the value of the local variable with the given number. This will create a variable based on the
   * current LV value so that we can differentiate the variable if we return from this method.
   * 
   * @since Date: Feb 8, 2012
   * @param varNum the local number of this variable as an offset to the LV.
   */
  void traceLocalVariable(final int varNum);

  /**
   * Ends tracing the value of the local variable with the given number. This will remove the variable based on the
   * current LV value so that we don't stop tracing a variable that is traced outside the current method.
   * 
   * @since Date: Feb 8, 2012
   * @param varNum the local number of this variable as an offset to the LV.
   */
  void untraceLocalVariable(final int varNum);

  /**
   * Returns whether we are tracing the value of the local variable with the given number. This will also check the
   * value of the LV, so that we are sure we are really tracing this variable and not a variable in a different method
   * with the same local number.
   * 
   * @since Date: Feb 8, 2012
   * @param varNum the local number of this variable as an offset to the LV.
   * @return <code>true</code> if we are tracing the local variable with the given number in the current macro code
   *         method,<br>
   *         <code>false</code> otherwise
   */
  boolean isTracingLocalVariable(final int varNum);
}
