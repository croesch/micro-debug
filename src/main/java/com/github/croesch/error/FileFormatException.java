package com.github.croesch.error;

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
    super(message, cause);
  }

  /**
   * Constructs an {@code FileFormatException} with the specified detail message.
   * 
   * @since Date: Nov 19, 2011
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
    super(cause);
  }
}
