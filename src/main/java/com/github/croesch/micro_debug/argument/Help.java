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
package com.github.croesch.micro_debug.argument;

import java.io.InputStream;
import java.io.InputStreamReader;

import com.github.croesch.micro_debug.commons.Printer;
import com.github.croesch.micro_debug.commons.Utils;

/**
 * Argument to view a help about usage of the debugger
 * 
 * @author croesch
 * @since Date: Feb 28, 2012
 */
public final class Help extends AArgument {

  /** path to the file containing the help text */
  private static final String HELP_FILE = "help.txt";

  /**
   * Hide constructor from being invoked.
   * 
   * @since Date: Feb 28, 2012
   */
  private Help() {
    // hidden constructor
  }

  /**
   * Class that holds the singleton of this argument.
   * 
   * @author croesch
   * @since Date: Feb 28, 2012
   */
  private static class LazyHolder {
    /** the single instance of the argument */
    private static final Help INSTANCE = new Help();
  }

  /**
   * The singleton instance of this argument.
   * 
   * @since Date: Feb 28, 2012
   * @return the single instance of this argument.
   */
  public static Help getInstance() {
    return LazyHolder.INSTANCE;
  }

  @Override
  public boolean execute(final String ... params) {
    final InputStream fileStream = Utils.class.getClassLoader().getResourceAsStream(HELP_FILE);
    Printer.printReader(new InputStreamReader(fileStream));
    return false;
  }

  @Override
  protected String name() {
    return "help";
  }
}
