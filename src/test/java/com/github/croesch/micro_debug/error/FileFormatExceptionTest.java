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

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

import com.github.croesch.micro_debug.DefaultTestCase;

/**
 * Provides test cases for {@link FileFormatException}.
 * 
 * @author croesch
 * @since Date: Mar 18, 2012
 */
public class FileFormatExceptionTest extends DefaultTestCase {

  @Test
  public void testFileFormatExceptionStringThrowable_Null() {
    printlnMethodName();

    final FileFormatException exception = new FileFormatException(null, null);

    assertThat(exception.getMessage()).isNull();
    assertThat(exception.getCause()).isNull();
  }

  @Test
  public void testFileFormatExceptionString_Null() {
    printlnMethodName();

    final FileFormatException exception = new FileFormatException((String) null);

    assertThat(exception.getMessage()).isNull();
    assertThat(exception.getCause()).isNull();
  }

  @Test
  public void testFileFormatExceptionThrowable_Null() {
    printlnMethodName();

    final FileFormatException exception = new FileFormatException((Throwable) null);

    assertThat(exception.getMessage()).isNull();
    assertThat(exception.getCause()).isNull();
  }

  @Test
  public void testFileFormatExceptionStringThrowable() {
    printlnMethodName();

    final ArrayIndexOutOfBoundsException cause = new ArrayIndexOutOfBoundsException();
    final FileFormatException exception = new FileFormatException("special message", cause);

    assertThat(exception.getMessage()).isEqualTo("special message");
    assertThat(exception.getCause()).isSameAs(cause);
  }

  @Test
  public void testFileFormatExceptionString() {
    printlnMethodName();

    final FileFormatException exception = new FileFormatException(" -> message <- ");

    assertThat(exception.getMessage()).isEqualTo(" -> message <- ");
    assertThat(exception.getCause()).isNull();
  }

  @Test
  public void testFileFormatExceptionThrowable() {
    printlnMethodName();

    final OutOfMemoryError cause = new OutOfMemoryError();
    final FileFormatException exception = new FileFormatException(cause);

    assertThat(exception.getMessage()).isNull();
    assertThat(exception.getCause()).isSameAs(cause);
  }

  @Test
  public void testFileFormatException() {
    printlnMethodName();

    final FileFormatException exception = new FileFormatException();

    assertThat(exception.getMessage()).isNull();
    assertThat(exception.getCause()).isNull();
  }
}
