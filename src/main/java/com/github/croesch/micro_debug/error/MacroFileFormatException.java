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
package com.github.croesch.micro_debug.error;

/**
 * Signals that a macro file has not the correct.
 * 
 * @author croesch
 * @since Date: Mar 18, 2012
 */
public class MacroFileFormatException extends FileFormatException {

  /** generated serial version UID */
  private static final long serialVersionUID = 2777373971813105321L;

  /**
   * Constructs a {@link MacroFileFormatException} with <code>null</code> as its error text message.
   * 
   * @since Date: Mar 18, 2012
   */
  public MacroFileFormatException() {
    super();
  }

  /**
   * Constructs an {@code MacroFileFormatException} with the specified detail message and cause.
   * <p>
   * Note that the detail message associated with {@code cause} is <i>not</i> automatically incorporated into this
   * exception's detail message.
   * 
   * @since Date: Mar 18, 2012
   * @param message The detail message (which is saved for later retrieval by the {@link #getMessage()} method)
   * @param cause The cause (which is saved for later retrieval by the {@link #getCause()} method). (A null value is
   *        permitted, and indicates that the cause is nonexistent or unknown.)
   */
  public MacroFileFormatException(final String message, final Throwable cause) {
    super(message, cause);
  }

  /**
   * Constructs an {@code MacroFileFormatException} with the specified detail message.
   * 
   * @since Date: Mar 18, 2012
   * @param message The detail message (which is saved for later retrieval by the {@link #getMessage()} method)
   */
  public MacroFileFormatException(final String message) {
    super(message);
  }

  /**
   * Constructs an {@code MacroFileFormatException} with the specified cause and a detail message of
   * {@code (cause==null ? null : cause.toString())} (which typically contains the class and detail message of
   * {@code cause}).
   * 
   * @since Date: Mar 18, 2012
   * @param cause The cause (which is saved for later retrieval by the {@link #getCause()} method). (A null value is
   *        permitted, and indicates that the cause is nonexistent or unknown.)
   */
  public MacroFileFormatException(final Throwable cause) {
    super(cause);
  }
}
