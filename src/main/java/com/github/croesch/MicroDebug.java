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
package com.github.croesch;

import java.util.Map;
import java.util.logging.Logger;

import com.github.croesch.console.Printer;
import com.github.croesch.i18n.Text;

/**
 * TODO Comment here ...
 * 
 * @author croesch
 * @since Date: Aug 13, 2011
 */
public final class MicroDebug {

  /** the {@link Logger} for this class */
  private static final Logger LOGGER = Logger.getLogger(MicroDebug.class.getName());

  /**
   * Hides constructor from being invoked. This class is a utility class and no one should be able to produce objects of
   * it.
   * 
   * @since Date: Aug 13, 2011
   */
  private MicroDebug() {
    // do nothing
  }

  /**
   * Starts the debugger. First handles the given arguments and then starts the debugging of the processor.
   * 
   * @since Date: Aug 13, 2011
   * @param args the arguments of the program
   */
  public static void main(final String[] args) {

    if (args == null || args.length == 0) {
      handleNoArguments();
    } else if (args.length == 1) {
      handleOneArgument(args[0]);
    } else {
      handleEnoughArguments(args);
    }
  }

  /**
   * Performs the handling of starting the program with the given number of arguments.
   * 
   * @since Date: Dec 3, 2011
   * @param args the array that contains at least to arguments and is not <code>null</code>.
   */
  private static void handleEnoughArguments(final String[] args) {
    final String[] optionArgs = new String[args.length - 2];
    System.arraycopy(args, 0, optionArgs, 0, optionArgs.length);

    boolean startApplication = true;

    final Map<Argument, String[]> map = Argument.createArgumentList(optionArgs);
    for (final Argument arg : map.keySet()) {
      LOGGER.fine("Executing argument: " + arg);
      startApplication &= arg.execute(map.get(arg));
    }

    LOGGER.finer("starting application: " + startApplication);
    if (startApplication) {
      final String ijvmFile = args[args.length - 1];
      final String mic1File = args[args.length - 2];
      LOGGER.config(".ijvm-file: " + ijvmFile);
      LOGGER.config(".mic1-file: " + mic1File);
      // TODO .. implement ..
    }
  }

  /**
   * Performs the handling of passing only one argument to the program.
   * 
   * @since Date: Dec 3, 2011
   * @param argument the argument passed to the program
   */
  private static void handleOneArgument(final String argument) {
    final Argument arg = Argument.of(argument);
    if (arg == Argument.HELP || arg == Argument.VERSION) {
      // if the help or version should be viewed it's okay to have only one argument
      arg.execute(null);
    } else {
      Printer.printErrorln(Text.MISSING_IJVM_FILE);
      printTryHelp();
    }
  }

  /**
   * Prints the {@link Text#TRY_HELP} to the user. Signalizes to view the help of the application for usage and
   * arguments.
   * 
   * @since Date: Dec 3, 2011
   */
  private static void printTryHelp() {
    Printer.println(Text.TRY_HELP);
  }

  /**
   * Performs the handling of passing no arguments to the program.
   * 
   * @since Date: Dec 3, 2011
   */
  private static void handleNoArguments() {
    Printer.printErrorln(Text.MISSING_MIC1_FILE);
    Printer.printErrorln(Text.MISSING_IJVM_FILE);
    printTryHelp();
  }
}
