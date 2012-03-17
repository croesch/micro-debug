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
    new FileFormatException(null, null);
  }

  @Test
  public void testFileFormatExceptionString_Null() {
    printlnMethodName();
    new FileFormatException((String) null);
  }

  @Test
  public void testFileFormatExceptionThrowable_Null() {
    printlnMethodName();
    new FileFormatException((Throwable) null);
  }
}