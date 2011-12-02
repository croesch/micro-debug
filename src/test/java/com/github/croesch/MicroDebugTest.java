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
package com.github.croesch;

import static org.fest.assertions.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.Test;

import com.github.croesch.i18n.Text;

/**
 * Contains tests for {@link MicroDebug}.
 * 
 * @author croesch
 * @since Date: Dec 2, 2011
 */
public class MicroDebugTest {

  @Test
  public final void testMain_Version() {
    final PrintStream oldOut = System.out;
    final ByteArrayOutputStream out = new ByteArrayOutputStream();
    System.setOut(new PrintStream(out));

    MicroDebug.main(new String[] { "-v" });
    assertThat(out.toString()).isEqualTo(Text.VERSION.text() + "\n");

    out.reset();

    MicroDebug.main(new String[] { "--version" });
    assertThat(out.toString()).isEqualTo(Text.VERSION.text() + "\n");

    System.setOut(oldOut);
  }
}
