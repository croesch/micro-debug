package com.github.croesch;

import java.util.EnumMap;
import java.util.Map;

/**
 * Enumeration of all possible command line arguments for the debugger.
 * 
 * @author croesch
 * @since Date: Aug 13, 2011
 */
enum Argument {

  /** argument to view a help about usage of the debugger */
  HELP,

  /** argument to view the version of the debugger */
  VERSION,

  /** argument to define the debug level of the program */
  DEBUG_LEVEL (1);

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
    this.args[0] = "--" + this.name().toLowerCase().replaceAll("_", "-");
    this.args[1] = "-" + this.name().substring(0, 1).toLowerCase();
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
    return m != null && (m.equals(args[0]) || m.equals(args[1]));
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
          if (arg != null) {
            final String[] params = new String[arg.getNumberOfParameters()];
            if (i + arg.getNumberOfParameters() < args.length) {
              for (int j = 0; j < arg.getNumberOfParameters(); ++j) {
                params[j] = args[j + i + 1];
              }
              map.put(arg, params);
            }
          }
        }
      }
    }
    return map;
  }
}
