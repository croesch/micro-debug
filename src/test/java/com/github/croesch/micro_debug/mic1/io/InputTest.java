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
package com.github.croesch.micro_debug.mic1.io;

import static org.fest.assertions.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

import com.github.croesch.micro_debug.DefaultTestCase;
import com.github.croesch.micro_debug.i18n.Text;

/**
 * Provides test cases for {@link Input}.
 * 
 * @author croesch
 * @since Date: Nov 26, 2011
 */
public class InputTest extends DefaultTestCase {

  @Test
  public void testSetIn() {
    printlnMethodName();

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
    printlnMethodName();

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
    printlnMethodName();

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

  @Test
  public void testReset() {
    printlnMethodName();

    Input.setIn(new ByteArrayInputStream("Test\nNOT\nHi!".getBytes()));
    assertThat(Input.read()).isEqualTo("T".getBytes()[0]);
    Input.reset();
    assertThat(Input.read()).isEqualTo("N".getBytes()[0]);
    Input.reset();
    assertThat(Input.read()).isEqualTo("H".getBytes()[0]);
    Input.reset();
    assertThat(Input.read()).isEqualTo((byte) -1);
  }

  @Test
  public void testQuietness() throws IOException {
    printlnMethodName();

    Input.setIn(new ByteArrayInputStream("Test\n".getBytes()));
    assertThat(out.toString()).isEmpty();
    assertThat(Input.read()).isEqualTo("T".getBytes()[0]);
    assertThat(out.toString()).isEqualTo(Text.INPUT_MIC1.text());
    out.reset();
    assertThat(Input.read()).isEqualTo("e".getBytes()[0]);

    Input.setQuiet(true);
    Input.setIn(new ByteArrayInputStream("Test\n".getBytes()));
    assertThat(Input.read()).isEqualTo("T".getBytes()[0]);
    assertThat(out.toString()).isEmpty();

    Input.setQuiet(false);
    Input.setIn(new ByteArrayInputStream("Test\n".getBytes()));
    assertThat(Input.read()).isEqualTo("T".getBytes()[0]);
    assertThat(out.toString()).isEqualTo(Text.INPUT_MIC1.text());
  }
}
