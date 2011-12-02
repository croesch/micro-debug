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

import java.util.EnumMap;
import java.util.Locale;
import java.util.Map;

import com.github.croesch.i18n.Text;

/**
 * Enumeration of all possible command line arguments for the debugger.
 * 
 * @author croesch
 * @since Date: Aug 13, 2011
 */
enum Argument {

  /** argument to define the debug level of the program */
  DEBUG_LEVEL (1) {
    @Override
    public boolean execute() {
      // TODO Auto-generated method stub
      return true;
    }
  },

  /** argument to view a help about usage of the debugger */
  HELP {
    @Override
    public boolean execute() {
      // TODO Auto-generated method stub
      return false;
    }
  },

  /** argument that signalizes an unknown argument */
  ERROR_UNKNOWN {
    @Override
    public boolean execute() {
      // TODO Auto-generated method stub
      return false;
    }
  },

  /** argument that signalizes an argument with the wrong number of parameters */
  ERROR_PARAM_NUMBER {
    @Override
    public boolean execute() {
      // TODO Auto-generated method stub
      return false;
    }
  },

  /** argument to view the version of the debugger */
  VERSION {
    @Override
    public boolean execute() {
      System.out.println(Text.VERSION);
      return false;
    }
  };

  /** the different ways this argument can be called */
  private final String[] args = new String[2];

  /** the number of parameters that argument requires */
  private final int numOfParams;

  /**
   * Constructs a new argument with its name as long argument and its first letter as short argument. For example
   * ARGUMENT will result in:
   * <ul>
   * <li><code>--argument</code></li>
   * <li><code>-a</code></li>
   * </ul>
   * <br>
   * <b>Note:</b> A <code>_</code> in the name will be translated to a <code>-</code>.<br>
   * This argument will require no parameters.
   * 
   * @since Date: Aug 13, 2011
   * @see #Argument(int)
   */
  private Argument() {
    this(0);
  }

  /**
   * Constructs a new argument with its name as long argument and its first letter as short argument. For example
   * ARGUMENT will result in:
   * <ul>
   * <li><code>--argument</code></li>
   * <li><code>-a</code></li>
   * </ul>
   * <br>
   * <b>Note:</b> A <code>_</code> in the name will be translated to a <code>-</code>. <br>
   * This argument will require the passed number of parameters.
   * 
   * @param nop the number of parameters this argument requires
   * @since Date: Aug 17, 2011
   * @see #Argument()
   */
  private Argument(final int nop) {
    this.args[0] = "--" + this.name().toLowerCase(Locale.getDefault()).replaceAll("_", "-");
    this.args[1] = "-" + this.name().substring(0, 1).toLowerCase(Locale.getDefault());
    this.numOfParams = nop;
  }

  /**
   * Returns whether this argument can be called with m. Will return <code>false</code>, if m is <code>null</code>.
   * 
   * @since Date: Aug 13, 2011
   * @param m the string to test if it's a possible call for this argument
   * @return <code>true</code>, if this argument can be called with m. For example <code>--argument</code> will return
   *         <code>true</code> for the argument <code>ARGUMENT</code>.
   */
  private boolean matches(final String m) {
    return m != null && (m.equals(this.args[0]) || m.equals(this.args[1]));
  }

  /**
   * Returns the number of parameters this argument requires.
   * 
   * @since Date: Aug 17, 2011
   * @return the number of parameters for that argument.
   */
  private int getNumberOfParameters() {
    return this.numOfParams;
  }

  /**
   * Returns the {@link Argument} that matches with the given {@link String}.
   * 
   * @since Date: Aug 13, 2011
   * @param s the {@link String} that is able to call the returned {@link Argument}.
   * @return the {@link Argument} that matches the given {@link String}, or <code>null</code> if no argument can be
   *         called with the given {@link String}.
   * @see Argument#matches(String)
   */
  static Argument of(final String s) {
    for (final Argument a : values()) {
      if (a.matches(s)) {
        return a;
      }
    }
    return null;
  }

  /**
   * Converts a given array of {@link String}s into a {@link Map} that contains an entry for each valid {@link Argument}
   * and the possible parameters belonging to it.
   * 
   * @since Date: Aug 17, 2011
   * @param args the array of {@link String}s
   * @return the {@link Map} that contains pairs of {@link Argument}s and arrays of strings that contain all parameters
   *         for that argument.
   */
  static Map<Argument, String[]> createArgumentList(final String[] args) {
    final Map<Argument, String[]> map = new EnumMap<Argument, String[]>(Argument.class);
    if (args != null) {
      for (int i = 0; i < args.length; ++i) {
        if (args[i] != null) {
          final Argument arg = of(args[i]);
          if (arg == null) {
            addErrorArgument(map, ERROR_UNKNOWN, args[i]);
          } else {
            final String[] params = new String[arg.getNumberOfParameters()];
            if (i + arg.getNumberOfParameters() < args.length) {
              for (int j = 0; j < arg.getNumberOfParameters(); ++j) {
                params[j] = args[j + i + 1];
              }
              i += arg.getNumberOfParameters();
              map.put(arg, params);
            } else {
              addErrorArgument(map, Argument.ERROR_PARAM_NUMBER, args[i]);
            }
          }
        }
      }
    }
    return map;
  }

  /**
   * Appends the given value to the array belonging to the given argument in the given map.
   * 
   * @since Date: Dec 2, 2011
   * @param map the map, that'll contain a key argument and an array that contains at least the given value
   * @param arg the argument that is the key in the given map
   * @param value the new value to append to the array, belonging to the key in the map.
   */
  private static void addErrorArgument(final Map<Argument, String[]> map, final Argument arg, final String value) {
    if (!map.containsKey(arg)) {
      // key not in map -> create it
      map.put(arg, new String[] { value });
    } else {
      // key already in map -> append value to the array
      final String[] newArray = appendValueToArray(map.get(arg), value);
      map.put(arg, newArray);
    }
  }

  /**
   * Appends a value to the given array and returns the new created array.
   * 
   * @since Date: Dec 2, 2011
   * @param old the array to append the value to
   * @param value the value to append
   * @return the new array, containing elements from the old array and the new value.
   */
  private static String[] appendValueToArray(final String[] old, final String value) {
    final String[] newArray = new String[old.length + 1];
    // copy array
    for (int i = 0; i < old.length; ++i) {
      newArray[i] = old[i];
    }
    // set new value
    newArray[old.length] = value;

    return newArray;
  }

  /**
   * Executes commands that result in the specific argument.
   * 
   * @since Date: Dec 2, 2011
   * @return <code>false</code>, if the argument enforces the application to stop
   */
  public abstract boolean execute();
}
