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

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Locale;

import com.github.croesch.i18n.Text;
import com.github.croesch.mic1.Mic1;
import com.github.croesch.mic1.register.Register;
import com.github.croesch.misc.Parameter;
import com.github.croesch.misc.Printer;
import com.github.croesch.misc.Utils;

/**
 * Enumeration of all possible command line instructions for the debugger.
 * 
 * @author croesch
 * @since Date: Dec 3, 2011
 */
enum UserInstruction {

  /** ends the debugger */
  EXIT {
    @Override
    public boolean execute(final Mic1 processor, final String ... params) {
      // simply return to end the program
      return false;
    }
  },

  /** instruction to view a help about the usage of the debugger */
  HELP {
    /** path to the file containing the help text */
    private static final String HELP_FILE = "instruction-help.txt";

    @Override
    public boolean execute(final Mic1 processor, final String ... params) {
      final InputStream fileStream = Utils.class.getClassLoader().getResourceAsStream(HELP_FILE);
      Printer.printReader(new InputStreamReader(fileStream));
      return true;
    }
  },

  /** list the values of all or a single register */
  LS_REG {
    @Override
    public boolean execute(final Mic1 processor, final String ... params) {
      if (getSize(params) == 0) {
        processor.listAllRegisters();
      } else {
        final Register r = (Register) Parameter.REGISTER.getValue(params[0]);
        if (r == null) {
          processor.listAllRegisters();
        } else {
          processor.listSingleRegister(r);
        }
      }
      return true;
    }
  },

  /** runs the program to the end */
  RUN {
    @Override
    public boolean execute(final Mic1 processor, final String ... params) {
      final int ticks = processor.run();
      Printer.println(Text.TICKS.text(ticks));
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
  private UserInstruction() {
    this.instruction = this.name().toLowerCase(Locale.GERMAN).replaceAll("_", "-");
  }

  /**
   * Returns whether this argument can be called with the given {@link String}. Will return <code>false</code>, if the
   * given {@link String} is <code>null</code> or if the {@link UserInstruction} is a pseudo-argument that cannot be
   * called.
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
   * Returns the {@link UserInstruction} that matches with the given {@link String}.
   * 
   * @since Date: Aug 13, 2011
   * @param s the {@link String} that is able to call the returned {@link UserInstruction}.
   * @return the {@link UserInstruction} that matches the given {@link String}, or<br>
   *         <code>null</code> if no {@link UserInstruction} can be called with the given {@link String}.
   * @see UserInstruction#matches(String)
   */
  static UserInstruction of(final String s) {
    if (s != null) {
      final String instruction = s.toLowerCase(Locale.GERMAN);
      for (final UserInstruction a : values()) {
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
   * @param processor the processor to operate on, is not needed for every {@link UserInstruction}.
   * @param params the parameters of that {@link UserInstruction}.
   * @return <code>true</code>, if the application can continue<br>
   *         <code>false</code>, if the {@link UserInstruction} enforces the application to stop.
   */
  public abstract boolean execute(Mic1 processor, String ... params);

  /**
   * Returns the size of the given array or <code>0</code>, if the array is <code>null</code>.
   * 
   * @since Date: Jan 15, 2012
   * @param array the array to determine the size of
   * @return the size of the given array or zero, if the given array is <code>null</code>.
   */
  protected static int getSize(final Object[] array) {
    if (array == null) {
      return 0;
    }
    return array.length;
  }
}