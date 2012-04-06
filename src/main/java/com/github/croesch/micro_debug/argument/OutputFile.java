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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

import com.github.croesch.micro_debug.annotation.NotNull;
import com.github.croesch.micro_debug.annotation.Nullable;
import com.github.croesch.micro_debug.commons.Utils;
import com.github.croesch.micro_debug.mic1.io.Output;

/**
 * argument to specify a file to append the output of the debugger to
 * 
 * @author croesch
 * @since Date: Feb 28, 2012
 */
public final class OutputFile extends AArgument {

  /** the stream that has been opened by this instance */
  @Nullable
  private transient PrintStream stream = null;

  /**
   * Hide constructor from being invoked.
   * 
   * @since Date: Feb 28, 2012
   */
  private OutputFile() {
    super(1);
  }

  /**
   * Class that holds the singleton of this argument.
   * 
   * @author croesch
   * @since Date: Feb 28, 2012
   */
  private static class LazyHolder {
    /** the single instance of the argument */
    private static final OutputFile INSTANCE = new OutputFile();
  }

  /**
   * The singleton instance of this argument.
   * 
   * @since Date: Feb 28, 2012
   * @return the single instance of this argument.
   */
  @NotNull
  public static OutputFile getInstance() {
    return LazyHolder.INSTANCE;
  }

  @Override
  public boolean execute(final String ... params) {
    releaseResources();
    try {
      this.stream = new PrintStream(new FileOutputStream(new File(params[0]), true));
      Output.setOut(this.stream);
    } catch (final FileNotFoundException e) {
      Utils.logThrownThrowable(e);
    }
    return true;
  }

  /**
   * Releases important references.
   * 
   * @since Date: Feb 15, 2012
   */
  void releaseResources() {
    if (this.stream != null) {
      Output.setOut(System.out);
      this.stream.close();
      this.stream = null;
    }
  }

  @Override
  @NotNull
  protected String name() {
    return "output-file";
  }
}
