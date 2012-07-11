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
 * Provides test cases for {@link MacroFileFormatException}.
 * 
 * @author croesch
 * @since Date: Mar 18, 2012
 */
public class MacroFileFormatExceptionTest extends DefaultTestCase {

  @Test
  public void testMacroFileFormatExceptionStringThrowable_Null() {
    printlnMethodName();

    final MacroFileFormatException exception = new MacroFileFormatException(null, null);

    assertThat(exception.getMessage()).isNull();
    assertThat(exception.getCause()).isNull();
  }

  @Test
  public void testMacroFileFormatExceptionString_Null() {
    printlnMethodName();

    final MacroFileFormatException exception = new MacroFileFormatException((String) null);

    assertThat(exception.getMessage()).isNull();
    assertThat(exception.getCause()).isNull();
  }

  @Test
  public void testMacroFileFormatExceptionThrowable_Null() {
    printlnMethodName();

    final MacroFileFormatException exception = new MacroFileFormatException((Throwable) null);

    assertThat(exception.getMessage()).isNull();
    assertThat(exception.getCause()).isNull();
  }

  @Test
  public void testMacroFileFormatExceptionStringThrowable() {
    printlnMethodName();

    final ArrayIndexOutOfBoundsException cause = new ArrayIndexOutOfBoundsException();
    final MacroFileFormatException exception = new MacroFileFormatException("special message", cause);

    assertThat(exception.getMessage()).isEqualTo("special message");
    assertThat(exception.getCause()).isSameAs(cause);
  }

  @Test
  public void testMacroFileFormatExceptionString() {
    printlnMethodName();

    final MacroFileFormatException exception = new MacroFileFormatException(" -> message <- ");

    assertThat(exception.getMessage()).isEqualTo(" -> message <- ");
    assertThat(exception.getCause()).isNull();
  }

  @Test
  public void testMacroFileFormatExceptionThrowable() {
    printlnMethodName();

    final OutOfMemoryError cause = new OutOfMemoryError();
    final MacroFileFormatException exception = new MacroFileFormatException(cause);

    assertThat(exception.getMessage()).isNull();
    assertThat(exception.getCause()).isSameAs(cause);
  }

  @Test
  public void testMacroFileFormatException() {
    printlnMethodName();

    final MacroFileFormatException exception = new MacroFileFormatException();

    assertThat(exception.getMessage()).isNull();
    assertThat(exception.getCause()).isNull();
  }
}
