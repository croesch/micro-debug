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
package com.github.croesch.console;

import static org.fest.assertions.Assertions.assertThat;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

import org.junit.Test;

import com.github.croesch.console.io.Printer;

/**
 * Provides test cases for {@link Instruction}.
 * 
 * @author croesch
 * @since Date: Dec 3, 2011
 */
public class InstructionTest {

  @Test
  public final void testOf() {
    assertThat(Instruction.of("help")).isSameAs(Instruction.HELP);
    assertThat(Instruction.of("exit")).isSameAs(Instruction.EXIT);
    assertThat(Instruction.of("Help")).isSameAs(Instruction.HELP);
    assertThat(Instruction.of("EXIT")).isSameAs(Instruction.EXIT);

    assertThat(Instruction.of(null)).isNull();
    assertThat(Instruction.of("")).isNull();
    assertThat(Instruction.of(" ")).isNull();
    assertThat(Instruction.of("--help")).isNull();
    assertThat(Instruction.of("--exit")).isNull();
  }

  @Test
  public final void testExecuteExit() throws IOException {
    final ByteArrayOutputStream out = new ByteArrayOutputStream();
    Printer.setPrintStream(new PrintStream(out));

    assertThat(Instruction.EXIT.execute()).isFalse();
    assertThat(Instruction.EXIT.execute("asd")).isFalse();
    assertThat(Instruction.EXIT.execute("asd", "asd")).isFalse();

    assertThat(out.toString()).isEmpty();
    Printer.setPrintStream(System.out);
  }

  @Test
  public final void testExecuteHelp() throws IOException {
    final ByteArrayOutputStream out = new ByteArrayOutputStream();
    Printer.setPrintStream(new PrintStream(out));

    assertThat(Instruction.HELP.execute("asd")).isTrue();
    assertThat(Instruction.HELP.execute("asd", "asd")).isTrue();
    out.reset();

    assertThat(Instruction.HELP.execute()).isTrue();

    final StringBuilder sb = new StringBuilder();
    BufferedReader reader = null;
    try {
      reader = new BufferedReader(new InputStreamReader(getClass().getClassLoader()
        .getResourceAsStream("instruction-help.txt")));
      String line;
      while ((line = reader.readLine()) != null) {
        sb.append(line).append("\n");
      }
    } finally {
      if (reader != null) {
        reader.close();
      }
    }

    assertThat(out.toString()).isEqualTo(sb.toString());

    Printer.setPrintStream(System.out);
  }

}
