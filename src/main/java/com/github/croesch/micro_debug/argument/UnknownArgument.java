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

import com.github.croesch.micro_debug.annotation.NotNull;
import com.github.croesch.micro_debug.i18n.Text;

/**
 * argument that signalizes an unknown argument
 * 
 * @author croesch
 * @since Date: Feb 28, 2012
 */
public final class UnknownArgument extends AError {

  /**
   * Hide constructor from being invoked.
   * 
   * @since Date: Feb 28, 2012
   */
  private UnknownArgument() {
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
    private static final UnknownArgument INSTANCE = new UnknownArgument();
  }

  /**
   * The singleton instance of this argument.
   * 
   * @since Date: Feb 28, 2012
   * @return the single instance of this argument.
   */
  @NotNull
  public static UnknownArgument getInstance() {
    return LazyHolder.INSTANCE;
  }

  @Override
  @NotNull
  protected Text getErrorText() {
    return Text.UNKNOWN_ARGUMENT;
  }
}
