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
package com.github.croesch.micro_debug.mic1.mem;

import java.util.ArrayList;
import java.util.List;

import com.github.croesch.micro_debug.annotation.NotNull;

/**
 * Wrapper class for a ijvm command and its arguments.
 * 
 * @author croesch
 * @since Date: Jan 22, 2012
 */
public final class IJVMCommand {

  /** the name of this command */
  @NotNull
  private final String name;

  /** the list of arguments */
  @NotNull
  private final List<IJVMCommandArgument> args = new ArrayList<IJVMCommandArgument>();

  /**
   * Constructs a new command with the given name and arguments.
   * 
   * @since Date: Jan 22, 2012
   * @param n the name of the command,<br>
   *        may not be <code>null</code><br>
   *        mustn't contain only whitespaces
   * @param arguments the required {@link IJVMCommandArgument}s, for this command<br>
   *        may be omitted, if this command needs no arguments.
   */
  public IJVMCommand(final String n, final IJVMCommandArgument ... arguments) {
    if (n == null) {
      throw new IllegalArgumentException();
    }
    this.name = n.trim();
    if (this.name.equals("")) {
      throw new IllegalArgumentException();
    }

    for (final IJVMCommandArgument arg : arguments) {
      if (arg != null) {
        this.args.add(arg);
      }
    }
  }

  @Override
  @NotNull
  public String toString() {
    final StringBuilder sb = new StringBuilder(this.name);
    for (final IJVMCommandArgument arg : this.args) {
      sb.append(" " + arg.name());
    }
    return sb.toString();
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + this.args.hashCode();
    result = prime * result + this.name.hashCode();
    return result;
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final IJVMCommand other = (IJVMCommand) obj;
    if (!this.args.equals(other.args)) {
      return false;
    }
    if (!this.name.equals(other.name)) {
      return false;
    }
    return true;
  }

  /**
   * Returns the name of this command as viewed to the user.
   * 
   * @since Date: Jan 22, 2012
   * @return the {@link String} representing the name of this command.
   */
  @NotNull
  public String getName() {
    return this.name;
  }

  /**
   * Returns a {@link List} of arguments that are required for this command.
   * 
   * @since Date: Jan 22, 2012
   * @return a {@link List} containing the required {@link IJVMCommandArgument}s for this command. Ensured to be not
   *         <code>null</code> and to contain no <code>null</code>-values.
   */
  @NotNull
  public List<IJVMCommandArgument> getArgs() {
    return new ArrayList<IJVMCommandArgument>(this.args);
  }
}
