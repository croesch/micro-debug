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
package com.github.croesch.micro_debug.mic1.api;

import com.github.croesch.micro_debug.mic1.controlstore.MicroInstruction;

/**
 * Interpreter of a processor, can access and manipulate the processor.
 * 
 * @author croesch
 * @since Date: Feb 13, 2012
 */
public interface IProcessorInterpreter {

  /**
   * Returns whether the processor should halt now or if it can continue.
   * 
   * @since Date: Feb 13, 2012
   * @param microLine the number of the line in micro code being executed next
   * @param macroLine the number of the line in macro code being executed next
   * @param currentInstruction the current (last executed) {@link MicroInstruction}
   * @param nextInstruction the next (to be executed) {@link MicroInstruction}
   * @return <code>true</code> if the processor can continue executing instructions,<br>
   *         <code>false</code> otherwise
   */
  boolean canContinue(final int microLine,
                      final int macroLine,
                      MicroInstruction currentInstruction,
                      MicroInstruction nextInstruction);

  /**
   * Informs the interpreter that the processor has done one tick.
   * 
   * @since Date: Feb 13, 2012
   * @param instruction the executed micro instruction
   * @param macroCodeFetching <code>true</code> if the next macro code instruction has being fetched,<br>
   *        <code>false</code> otherwise
   */
  void tickDone(MicroInstruction instruction, boolean macroCodeFetching);

}
