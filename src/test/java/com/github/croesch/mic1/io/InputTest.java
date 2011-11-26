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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;

import org.junit.Test;

/**
 * TODO Comment here ...
 * 
 * @author croesch
 * @since Date: Nov 26, 2011
 */
public class InputTest {

  //  @Test
  //  public void testSetIn() {
  //    final InputStream in = new ByteArrayInputStream("Test\n".getBytes());
  //    Input.setIn(in);
  //
  //    assertThat(Input.read()).isEqualTo((byte) 'T');
  //    assertThat(Input.read()).isEqualTo((byte) 'e');
  //
  //    Input.setIn(null);
  //    assertThat(Input.read()).isEqualTo((byte) 's');
  //
  //    Input.setIn(System.in);
  //  }

  @Test
  public void testRead() throws IOException {

    //    final InputStream in = System.in;
    final Reader in = new MyStringReader("Test\nnicht gelesen\nHi!");
    //    System.out.println(in.available());
    //    final InputStream in = ClassLoader.getSystemResourceAsStream("mic1/io/test-1.txt");

    System.out.println(in.ready());

    Input.setIn(in);

    assertThat(Input.read()).isEqualTo((byte) 'T');
    assertThat(Input.read()).isEqualTo((byte) 'e');

    final BufferedReader bin = new BufferedReader(in);
    assertThat(bin.readLine()).isEqualTo("nicht gelesen");

    Input.setIn(new InputStreamReader(System.in));
  }

  private class MyStringReader extends StringReader {

    public MyStringReader(final String s) {
      super(s);
    }

    @Override
    public boolean ready() throws IOException {
      return false;
    }
  }

}
