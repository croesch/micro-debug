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

import com.github.croesch.commons.Printer;
import com.github.croesch.commons.Reader;
import com.github.croesch.i18n.Text;
import com.github.croesch.mic1.Mic1;

/**
 * Class that handles the core of debugging. Reads user input and handles the instructions.
 * 
 * @author croesch
 * @since Date: Dec 3, 2011
 */
public final class Debugger implements Runnable {

  /** {@link String} that separates instruction from parameters and each parameter to another */
  private static final String SEPARATING_STRING = " ";

  /** the interpreter to debug the processor with */
  private final Mic1Interpreter interpreter;

  /**
   * Constructs a debugger for the given processor.
   * 
   * @since Date: Dec 3, 2011
   * @param mic1 the processor to debug.
   */
  public Debugger(final Mic1 mic1) {
    this.interpreter = new Mic1Interpreter(mic1);
  }

  /**
   * Starts the debugger and returns when the debugger ended his work.
   */
  public void run() {
    boolean canContinue = true;
    while (canContinue) {
      // prompt user
      final String[] usersInstruction = askUserWhatToDo().split(SEPARATING_STRING);
      // check if this was a valid instruction
      final UserInstruction in = UserInstruction.of(usersInstruction[0]);
      if (in != null) {
        // valid instruction -> copy parameters
        final String[] params = new String[usersInstruction.length - 1];
        System.arraycopy(usersInstruction, 1, params, 0, params.length);
        // execute the instruction, returns whether program can continue
        canContinue = in.execute(this.interpreter, params);
      } else {
        // unknown instruction -> inform user
        Printer.printErrorln(Text.UNKNOWN_INSTRUCTION.text(usersInstruction[0]));
      }
    }
  }

  /**
   * Reads a line from the users input and returns it.
   * 
   * @since Date: Dec 3, 2011
   * @return the line provided by the {@link Reader}.
   */
  private String askUserWhatToDo() {
    return Reader.readLine();
  }
}
