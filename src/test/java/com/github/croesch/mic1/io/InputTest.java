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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

/**
 * Provides test cases for {@link Input}.
 * 
 * @author croesch
 * @since Date: Nov 26, 2011
 */
public class InputTest {

  @Test
  public void testSetIn() {
    final InputStream in = new ByteArrayInputStream("Test".getBytes());
    Input.setIn(in);

    assertThat(Input.read()).isEqualTo((byte) 'T');
    assertThat(Input.read()).isEqualTo((byte) 'e');

    Input.setIn(null);
    assertThat(Input.read()).isEqualTo((byte) 's');
    assertThat(Input.read()).isEqualTo((byte) 't');

    Input.setIn(System.in);
  }

  /**
   * Tests that processor reads a line, then somebody else reads a line and then the processor again reads a line.
   */
  @Test
  public void testRead() throws IOException {

    final ByteArrayInputStream in = new ByteArrayInputStream("Test\nNOT\nHi!".getBytes());
    Input.setIn(in);

    assertThat(Input.read()).isEqualTo((byte) 'T');
    assertThat(Input.read()).isEqualTo((byte) 'e');

    assertThat(in.read()).isEqualTo('N');
    assertThat(in.read()).isEqualTo('O');
    assertThat(in.read()).isEqualTo('T');

    assertThat(Input.read()).isEqualTo((byte) 's');
    assertThat(Input.read()).isEqualTo((byte) 't');

    assertThat(in.read()).isEqualTo('\n');

    assertThat(Input.read()).isEqualTo((byte) '\n');
    assertThat(Input.read()).isEqualTo((byte) 'H');
    assertThat(Input.read()).isEqualTo((byte) 'i');
    assertThat(Input.read()).isEqualTo((byte) '!');

    assertThat(Input.read()).isEqualTo((byte) -1);

    Input.setIn(System.in);
  }

  @Test
  public void testRead_IOExc() throws IOException {
    final InputStream in = new InputStream() {
      @Override
      public int read() throws IOException {
        throw new IOException();
      }
    };

    Input.setIn(in);
    assertThat(Input.read()).isEqualTo((byte) -1);
    Input.setIn(System.in);

  }
}
