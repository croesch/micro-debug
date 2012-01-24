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

import org.junit.Ignore;

import com.github.croesch.misc.Utils;

/**
 * This class contains utility methods for the tests in this project. It doesn't contain any test methods.
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

  public static void printLoopEnd() {
    System.out.print(" ");
  }

  public static void printStep() {
    System.out.print(".");
  }

  public static void printEndOfMethod() {
    System.out.println();
  }

  public static String getLineSeparator() {
    return Utils.getLineSeparator();
  }
}
