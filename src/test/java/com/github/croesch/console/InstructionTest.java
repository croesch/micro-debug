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
import com.github.croesch.i18n.Text;
import com.github.croesch.mic1.Mic1;

/**
 * Provides test cases for {@link Instruction}.
 * 
 * @author croesch
 * @since Date: Dec 3, 2011
 */
public class InstructionTest {

  @Test
  public final void testOf() {
    assertThat(Instruction.of("Help")).isSameAs(Instruction.HELP);

    for (final Instruction ins : Instruction.values()) {
      assertThat(Instruction.of(ins.name())).isSameAs(ins);
      assertThat(Instruction.of(ins.name().toLowerCase())).isSameAs(ins);
      assertThat(Instruction.of("--" + ins.name())).isNull();
      assertThat(Instruction.of("--" + ins.name().toLowerCase())).isNull();
    }

    assertThat(Instruction.of(null)).isNull();
    assertThat(Instruction.of("")).isNull();
    assertThat(Instruction.of(" ")).isNull();
  }

  @Test
  public final void testExecuteExit() throws IOException {
    final ByteArrayOutputStream out = new ByteArrayOutputStream();
    Printer.setPrintStream(new PrintStream(out));

    assertThat(Instruction.EXIT.execute(null)).isFalse();
    assertThat(Instruction.EXIT.execute(null, "asd")).isFalse();
    assertThat(Instruction.EXIT.execute(null, "asd", "asd")).isFalse();

    assertThat(out.toString()).isEmpty();
    Printer.setPrintStream(System.out);
  }

  @Test
  public final void testExecuteHelp() throws IOException {
    final ByteArrayOutputStream out = new ByteArrayOutputStream();
    Printer.setPrintStream(new PrintStream(out));

    assertThat(Instruction.HELP.execute(null, "asd")).isTrue();
    assertThat(Instruction.HELP.execute(null, "asd", "asd")).isTrue();
    out.reset();

    assertThat(Instruction.HELP.execute(null)).isTrue();

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

  @Test(expected = NullPointerException.class)
  public final void testExecuteRun_NPE() throws IOException {
    Instruction.RUN.execute(null, "asd");
  }

  @Test
  public final void testExecuteRun() throws IOException {
    Mic1 processor = new Mic1(ClassLoader.getSystemResourceAsStream("mic1/hi.mic1"),
                              ClassLoader.getSystemResourceAsStream("mic1/hi.ijvm"));
    final ByteArrayOutputStream out = new ByteArrayOutputStream();
    Printer.setPrintStream(new PrintStream(out));

    assertThat(Instruction.RUN.execute(processor, "asd")).isTrue();
    assertThat(Instruction.RUN.execute(processor, "asd", "asd")).isTrue();
    out.reset();

    processor = new Mic1(ClassLoader.getSystemResourceAsStream("mic1/hi.mic1"),
                         ClassLoader.getSystemResourceAsStream("mic1/hi.ijvm"));
    assertThat(Instruction.RUN.execute(processor)).isTrue();
    assertThat(out.toString()).isEqualTo(Text.TICKS.text(14) + "\n");

    out.reset();
    assertThat(Instruction.RUN.execute(processor)).isTrue();
    assertThat(out.toString()).isEqualTo(Text.TICKS.text(0) + "\n");

    Printer.setPrintStream(System.out);
  }

}
