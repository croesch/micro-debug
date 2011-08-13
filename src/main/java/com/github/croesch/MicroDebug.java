package com.github.croesch;

/**
 * TODO Comment here ...
 * 
 * @author croesch
 * @since Date: Aug 13, 2011
 */
public final class MicroDebug {

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
   * TODO Comment here ...
   * 
   * @since Date: Aug 13, 2011
   * @param args the arguments of the program
   */
  public static void main(final String[] args) {
    for (int i = 0; i < args.length; ++i) {
      System.out.println(args[i]);
      System.out.println(Argument.of(args[i]));
    }
  }

}
