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
 * Provides test cases for {@link MicroFileFormatException}.
 * 
 * @author croesch
 * @since Date: Mar 18, 2012
 */
public class MicroFileFormatExceptionTest extends DefaultTestCase {

  @Test
  public void testMicroFileFormatExceptionStringThrowable_Null() {
    printlnMethodName();

    final MicroFileFormatException exception = new MicroFileFormatException(null, null);

    assertThat(exception.getMessage()).isNull();
    assertThat(exception.getCause()).isNull();
  }

  @Test
  public void testMicroFileFormatExceptionString_Null() {
    printlnMethodName();

    final MicroFileFormatException exception = new MicroFileFormatException((String) null);

    assertThat(exception.getMessage()).isNull();
    assertThat(exception.getCause()).isNull();
  }

  @Test
  public void testMicroFileFormatExceptionThrowable_Null() {
    printlnMethodName();

    final MicroFileFormatException exception = new MicroFileFormatException((Throwable) null);

    assertThat(exception.getMessage()).isNull();
    assertThat(exception.getCause()).isNull();
  }

  @Test
  public void testMicroFileFormatExceptionStringThrowable() {
    printlnMethodName();

    final ArrayIndexOutOfBoundsException cause = new ArrayIndexOutOfBoundsException();
    final MicroFileFormatException exception = new MicroFileFormatException("special message", cause);

    assertThat(exception.getMessage()).isEqualTo("special message");
    assertThat(exception.getCause()).isSameAs(cause);
  }

  @Test
  public void testMicroFileFormatExceptionString() {
    printlnMethodName();

    final MicroFileFormatException exception = new MicroFileFormatException(" -> message <- ");

    assertThat(exception.getMessage()).isEqualTo(" -> message <- ");
    assertThat(exception.getCause()).isNull();
  }

  @Test
  public void testMicroFileFormatExceptionThrowable() {
    printlnMethodName();

    final OutOfMemoryError cause = new OutOfMemoryError();
    final MicroFileFormatException exception = new MicroFileFormatException(cause);

    assertThat(exception.getMessage()).isNull();
    assertThat(exception.getCause()).isSameAs(cause);
  }

  @Test
  public void testMicroFileFormatException() {
    printlnMethodName();

    final MicroFileFormatException exception = new MicroFileFormatException();

    assertThat(exception.getMessage()).isNull();
    assertThat(exception.getCause()).isNull();
  }
}
