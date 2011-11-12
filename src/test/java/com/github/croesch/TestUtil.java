package com.github.croesch;

import org.junit.Ignore;

/**
 * TODO Comment here ...
 * 
 * @author croesch
 * @since Date: Nov 12, 2011
 */
@Ignore
public class TestUtil {

  public static void printMethodName() {
    printMethodName(1);
  }

  public static void printlnMethodName() {
    printMethodName(1);
    System.out.println();
  }

  public static void printlnMethodName(final int lvl) {
    printMethodName(1 + lvl);
    System.out.println();
  }

  public static void printMethodName(final int lvl) {
    System.out.print(Thread.currentThread().getStackTrace()[2 + lvl].getClassName() + "#");
    System.out.print(Thread.currentThread().getStackTrace()[2 + lvl].getMethodName() + " ");
  }

}
