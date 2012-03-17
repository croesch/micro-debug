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

import java.io.IOException;

/**
 * Signals that a file being read has another format than expected.
 * 
 * @author croesch
 * @since Date: Nov 19, 2011
 */
public class FileFormatException extends IOException {

  /** generated serial version UID */
  private static final long serialVersionUID = 4654303879465622547L;

  /**
   * Constructs an {@code FileFormatException} with {@code null} as its error detail message.
   * 
   * @since Date: Nov 19, 2011
   */
  public FileFormatException() {
    super();
  }

  /**
   * Constructs an {@code FileFormatException} with the specified detail message and cause.
   * <p>
   * Note that the detail message associated with {@code cause} is <i>not</i> automatically incorporated into this
   * exception's detail message.
   * 
   * @since Date: Nov 19, 2011
   * @param message The detail message (which is saved for later retrieval by the {@link #getMessage()} method)
   * @param cause The cause (which is saved for later retrieval by the {@link #getCause()} method). (A null value is
   *        permitted, and indicates that the cause is nonexistent or unknown.)
   */
  public FileFormatException(final String message, final Throwable cause) {
    super(message);
    initCause(cause);
  }

  /**
   * Constructs an {@code FileFormatException} with the specified detail message.
   * 
   * @since Date: Feb 10, 2012
   * @param message The detail message (which is saved for later retrieval by the {@link #getMessage()} method)
   */
  public FileFormatException(final String message) {
    super(message);
  }

  /**
   * Constructs an {@code FileFormatException} with the specified cause and a detail message of
   * {@code (cause==null ? null : cause.toString())} (which typically contains the class and detail message of
   * {@code cause}).
   * 
   * @since Date: Nov 19, 2011
   * @param cause The cause (which is saved for later retrieval by the {@link #getCause()} method). (A null value is
   *        permitted, and indicates that the cause is nonexistent or unknown.)
   */
  public FileFormatException(final Throwable cause) {
    super(getMessageOrNull(cause));
    initCause(cause);
  }

  /**
   * Returns the cause of the given {@link Throwable}, or <code>null</code> if the given {@link Throwable} is
   * <code>null</code>.
   * 
   * @since Date: Mar 18, 2012
   * @param cause the {@link Throwable} that caused this exception.
   * @return the message {@link String} of the cause,<br>
   *         or <code>null</code> if the given cause is <code>null</code>.
   */
  private static String getMessageOrNull(final Throwable cause) {
    if (cause == null) {
      return null;
    }
    return cause.getMessage();
  }
}
