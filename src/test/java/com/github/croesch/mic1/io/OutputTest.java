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
package com.github.croesch.mic1.io;

import static org.fest.assertions.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Provides test cases for {@link Output}.
 * 
 * @author croesch
 * @since Date: Nov 26, 2011
 */
public class OutputTest {

  private static ByteArrayOutputStream out = new ByteArrayOutputStream();

  @BeforeClass
  public static void init() {
    Output.setOut(new PrintStream(out));
  }

  @AfterClass
  public static void tearDown() {
    Output.setOut(System.out);
  }

  @Before
  public void before() {
    Output.setBuffered(true);
  }

  @After
  public void after() {
    out.reset();
  }

  @Test
  public void testSetBuffered_Unbuffered() throws IOException {
    Output.print((byte) '0');
    Output.print((byte) '1');
    assertThat(out.toString()).isEmpty();
    Output.setBuffered(false);
    assertThat(out.toString()).isEqualTo("01");
    out.reset();
    Output.print((byte) '2');
    assertThat(out.toString()).isEqualTo("2");
  }

  @Test
  public void testPrint() throws IOException {
    Output.print((byte) '9');
    Output.print((byte) '8');
    assertThat(out.toString()).isEmpty();
    Output.print((byte) 10);
    assertThat(out.toString()).isEqualTo("98\n");
    out.reset();

    Output.print((byte) '7');
    Output.print((byte) '6');
    assertThat(out.toString()).isEmpty();
    Output.print((byte) 10);
    assertThat(out.toString()).isEqualTo("76\n");
  }

  @Test
  public void testPrint_LF() throws IOException {
    Output.print((byte) '\n');
    assertThat(out.toString()).isEqualTo("\n");
    Output.print((byte) '\n');
    assertThat(out.toString()).isEqualTo("\n\n");
    Output.print((byte) '\n');
    assertThat(out.toString()).isEqualTo("\n\n\n");
  }

  @Test
  public void testFlush() throws IOException {
    Output.print((byte) 'a');
    Output.print((byte) 'b');
    Output.print((byte) 'c');
    Output.print((byte) 'c');
    assertThat(out.toString()).isEmpty();
    Output.flush();
    assertThat(out.toString()).isEqualTo("abcc");
    out.reset();

    Output.print((byte) 'b');
    Output.print((byte) 'c');
    Output.print((byte) 'a');
    Output.print((byte) 'c');
    assertThat(out.toString()).isEmpty();
    Output.flush();
    assertThat(out.toString()).isEqualTo("bcac");
  }
}
