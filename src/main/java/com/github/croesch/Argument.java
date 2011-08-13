package com.github.croesch;

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
  VERSION;

  /** the different ways this argument can be called */
  private final String[] args = new String[2];

  /**
   * Constructs a new argument with its name as long argument and its first letter as short argument. For example
   * ARGUMENT will result in:
   * <ul>
   * <li><code>--argument</code></li>
   * <li><code>-a</code></li>
   * </ul>
   * <br>
   * <b>Note:</b> A <code>_</code> in the name will be translated to a <code>-</code>.
   * 
   * @since Date: Aug 13, 2011
   */
  private Argument() {
    this.args[0] = "--" + this.name().toLowerCase().replaceAll("_", "-");
    this.args[1] = "-" + this.name().substring(0, 1).toLowerCase();
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
}
