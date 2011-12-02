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

import java.util.Map;
import java.util.logging.Logger;

/**
 * TODO Comment here ...
 * 
 * @author croesch
 * @since Date: Aug 13, 2011
 */
public final class MicroDebug {

  /** the {@link Logger} for this class */
  private static final Logger LOGGER = Logger.getLogger(MicroDebug.class.getName());

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
   * Starts the debugger. First handles the given arguments and then starts the debugging of the processor.
   * 
   * @since Date: Aug 13, 2011
   * @param args the arguments of the program
   */
  public static void main(final String[] args) {
    boolean startApplication = true;

    final Map<Argument, String[]> map = Argument.createArgumentList(args);
    for (final Argument arg : map.keySet()) {
      LOGGER.fine("Executing argument: " + arg);
      startApplication &= arg.execute();
    }

    LOGGER.finer("starting application: " + startApplication);
    if (startApplication) {
      // TODO .. implement ..
    }
  }
}
