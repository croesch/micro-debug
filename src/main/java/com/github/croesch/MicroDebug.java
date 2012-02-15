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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import com.github.croesch.commons.Printer;
import com.github.croesch.console.Debugger;
import com.github.croesch.error.FileFormatException;
import com.github.croesch.i18n.Text;
import com.github.croesch.mic1.Mic1;

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

    Printer.println(Text.GREETING);

    if (args == null || args.length == 0) {
      printBorder();
      handleNoArguments();
    } else if (args.length == 1) {
      printBorder();
      handleOneArgument(args[0]);
    } else {
      Printer.println(Text.WELCOME);
      printBorder();
      handleEnoughArguments(args);
    }
  }

  /**
   * Prints text that visualizes a border.
   * 
   * @since Date: Jan 14, 2012
   */
  private static void printBorder() {
    Printer.println(Text.BORDER);
  }

  /**
   * Performs the handling of starting the program with the given number of arguments.
   * 
   * @since Date: Dec 3, 2011
   * @param args the array that contains at least to arguments and is not <code>null</code>.
   */
  private static void handleEnoughArguments(final String[] args) {
    // create the array containing only the variable arguments
    final String[] optionArgs = new String[args.length - 2];
    System.arraycopy(args, 0, optionArgs, 0, optionArgs.length);

    // handle the arguments
    final boolean startApplication = executeTheArguments(Argument.createArgumentList(optionArgs));

    // start the application itself, if the arguments where valid
    LOGGER.finer("starting application: " + startApplication);
    if (startApplication) {
      // create streams to read from the two binary files
      final String ijvmFile = args[args.length - 1];
      final String mic1File = args[args.length - 2];
      LOGGER.config(".ijvm-file: " + ijvmFile);
      LOGGER.config(".mic1-file: " + mic1File);
      final FileInputStream micAsm = createFileInputStream(mic1File);
      final FileInputStream asm = createFileInputStream(ijvmFile);

      // if files where found, try to start application
      if (micAsm != null && asm != null) {
        try {
          new Debugger(new Mic1(micAsm, asm)).run();
        } catch (final FileFormatException e) {
          // the input files were invalid, log this, user has already received information
          LOGGER.finest("started application with wrong file format");
        }
      }
    }

    Argument.releaseAllResources();
  }

  /**
   * Executes all {@link Argument}s in the given {@link Map} with the parameters stored in the map.
   * 
   * @since Date: Dec 3, 2011
   * @param map the map that contains the {@link Argument}s and the {@link String[]} as parameter for the argument.
   * @return <code>true</code> if the application can be started, <code>false</code> otherwise
   */
  private static boolean executeTheArguments(final Map<Argument, String[]> map) {
    boolean startApplication = true;

    for (final Entry<Argument, String[]> argumentEntry : map.entrySet()) {
      final Argument arg = argumentEntry.getKey();
      final String[] params = argumentEntry.getValue();

      LOGGER.fine("Executing argument: " + arg);
      startApplication &= arg.execute(params);
    }

    return startApplication;
  }

  /**
   * Tries to create a {@link FileInputStream}.
   * 
   * @since Date: Dec 3, 2011
   * @param mic1File the path to the file to create the stream from
   * @return the constructed {@link FileInputStream} or <code>null</code>, if the file couldn't be found
   */
  private static FileInputStream createFileInputStream(final String mic1File) {
    try {
      return new FileInputStream(mic1File);
    } catch (final FileNotFoundException e) {
      Printer.printErrorln(Text.FILE_NOT_FOUND.text(mic1File));
      return null;
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

    if (isValidAsOnlyArgument(arg)) {
      // if the help or version should be viewed it's okay to have only one argument
      arg.execute();
    } else {
      Printer.printErrorln(Text.MISSING_IJVM_FILE);
      printTryHelp();
    }
  }

  /**
   * Returns whether the given argument is valid to be the only argument given to the application.
   * 
   * @since Date: Jan 14, 2012
   * @param arg the {@link Argument} to check
   * @return <code>true</code>, whether it is okay to start the application with only the given argument
   */
  private static boolean isValidAsOnlyArgument(final Argument arg) {
    return arg == Argument.HELP || arg == Argument.VERSION;
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
