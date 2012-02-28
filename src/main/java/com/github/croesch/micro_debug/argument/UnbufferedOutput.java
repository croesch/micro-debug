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

import com.github.croesch.micro_debug.mic1.io.Output;

/**
 * argument that makes the output to be unbuffered
 * 
 * @author croesch
 * @since Date: Feb 28, 2012
 */
public final class UnbufferedOutput extends AArgument {

  /**
   * Hide constructor from being invoked.
   * 
   * @since Date: Feb 28, 2012
   */
  private UnbufferedOutput() {
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
    private static final UnbufferedOutput INSTANCE = new UnbufferedOutput();
  }

  /**
   * The singleton instance of this argument.
   * 
   * @since Date: Feb 28, 2012
   * @return the single instance of this argument.
   */
  public static UnbufferedOutput getInstance() {
    return LazyHolder.INSTANCE;
  }

  @Override
  public boolean execute(final String ... params) {
    Output.setBuffered(false);
    return true;
  }

  @Override
  protected String name() {
    return "unbuffered-output";
  }
}
