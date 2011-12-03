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
package com.github.croesch.console.io;

import static org.fest.assertions.Assertions.assertThat;

import java.io.StringReader;

import org.junit.Test;

/**
 * Provides test cases for {@link Reader}.
 * 
 * @author croesch
 * @since Date: Dec 3, 2011
 */
public class ReaderTest {

  @Test
  public void testSetReader() {
    Reader.setReader(new StringReader("111111\n11111\n111111"));
    assertThat(Reader.readLine()).isEqualTo("111111");

    Reader.setReader(new StringReader("2222222\n22222222\n22222"));
    assertThat(Reader.readLine()).isEqualTo("2222222");

    Reader.setReader(null);
    assertThat(Reader.readLine()).isEqualTo("22222222");
  }

  @Test
  public void testReadLine() {
    Reader.setReader(new StringReader("asd"));
    assertThat(Reader.readLine()).isEqualTo("asd");
    assertThat(Reader.readLine()).isNull();

    Reader.setReader(new StringReader("asd\nbsd"));
    assertThat(Reader.readLine()).isEqualTo("asd");
    assertThat(Reader.readLine()).isEqualTo("bsd");
    assertThat(Reader.readLine()).isNull();
  }

}
