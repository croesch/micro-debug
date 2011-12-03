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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Locale;

import com.github.croesch.console.io.Printer;
import com.github.croesch.misc.Utils;

/**
 * Enumeration of all possible command line instructions for the debugger.
 * 
 * @author croesch
 * @since Date: Dec 3, 2011
 */
enum Instruction {

  /** ends the debugger */
  EXIT {
    @Override
    public boolean execute(final String ... params) {
      // simply return to end the program
      return false;
    }
  },

  /** instruction to view a help about the usage of the debugger */
  HELP {
    /** path to the file containing the help text */
    private static final String HELP_FILE = "instruction-help.txt";

    @Override
    public boolean execute(final String ... params) {
      BufferedReader reader = null;
      try {
        reader = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream(HELP_FILE)));
        String line;
        while ((line = reader.readLine()) != null) {
          Printer.println(line);
        }
      } catch (final IOException e) {
        Utils.logThrownThrowable(e);
      } finally {
        if (reader != null) {
          try {
            reader.close();
          } catch (final IOException e) {
            Utils.logThrownThrowable(e);
          }
        }
      }
      return true;
    }
  };

  /** the different ways this argument can be called */
  private final String instruction;

  /**
   * Constructs a new instruction. Its name is used to execute the instruction. For example INSTRUCTION can be executed
   * with: <code>instruction</code> <br>
   * <b>Note:</b> A <code>_</code> in the name will be translated to a <code>-</code>.<br>
   * 
   * @since Date: Dec 3, 2011
   */
  private Instruction() {
    this.instruction = this.name().toLowerCase(Locale.GERMAN).replaceAll("_", "-");
  }

  /**
   * Returns whether this argument can be called with the given {@link String}. Will return <code>false</code>, if the
   * given {@link String} is <code>null</code> or if the {@link Instruction} is a pseudo-argument that cannot be called.
   * 
   * @since Date: Dec 3, 2011
   * @param argStr the {@link String} to test if it's a possible call for this argument
   * @return <code>true</code>, if this argument can be called with the given {@link String}.<br>
   *         For example <code>--argument</code> will return <code>true</code> for the argument <code>ARGUMENT</code>.
   */
  private boolean matches(final String argStr) {
    return argStr != null && argStr.equals(this.instruction);
  }

  /**
   * Returns the {@link Instruction} that matches with the given {@link String}.
   * 
   * @since Date: Aug 13, 2011
   * @param s the {@link String} that is able to call the returned {@link Instruction}.
   * @return the {@link Instruction} that matches the given {@link String}, or <code>null</code> if no argument can be
   *         called with the given {@link String}.
   * @see Instruction#matches(String)
   */
  static Instruction of(final String s) {
    if (s != null) {
      final String instruction = s.toLowerCase();
      for (final Instruction a : values()) {
        if (a.matches(instruction)) {
          return a;
        }
      }
    }
    return null;
  }

  /**
   * Executes the instruction with the given parameters.
   * 
   * @since Date: Dec 3, 2011
   * @param params the parameters of that {@link Instruction}.
   * @return <code>true</code>, if the application can continue<br>
   *         <code>false</code>, if the {@link Instruction} enforces the application to stop.
   */
  public abstract boolean execute(String ... params);

}
