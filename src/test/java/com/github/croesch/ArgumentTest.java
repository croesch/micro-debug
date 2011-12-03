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

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

import org.junit.Test;

import com.github.croesch.console.Printer;
import com.github.croesch.i18n.Text;
import com.github.croesch.mic1.io.Output;

/**
 * TODO write test, if there's an argument with at least one parameter<br>
 * Provides several test methods for {@link Argument}.
 * 
 * @author croesch
 * @since Date: Aug 13, 2011
 */
public class ArgumentTest {

  @Test
  public final void testOf_DifferentValues() {
    assertThat(Argument.of("--help")).isSameAs(Argument.HELP);
    assertThat(Argument.of("-h")).isSameAs(Argument.HELP);

    assertThat(Argument.of("--version")).isSameAs(Argument.VERSION);
    assertThat(Argument.of("-v")).isSameAs(Argument.VERSION);

    assertThat(Argument.of("--unbuffered-output")).isSameAs(Argument.UNBUFFERED_OUTPUT);
    assertThat(Argument.of("-u")).isSameAs(Argument.UNBUFFERED_OUTPUT);

    //    assertThat(Argument.of("--debug-level")).isSameAs(Argument.DEBUG_LEVEL);
    //    assertThat(Argument.of("-d")).isSameAs(Argument.DEBUG_LEVEL);
  }

  @Test
  public final void testOf_Unkown() {
    assertThat(Argument.of(null)).isNull();
    assertThat(Argument.of("")).isNull();
    assertThat(Argument.of(" ")).isNull();
    assertThat(Argument.of("HELP")).isNull();
    assertThat(Argument.of("-help")).isNull();
    assertThat(Argument.of("--h")).isNull();
  }

  @Test
  public final void testCreateArgumentList_NullDeliversEmptyMap() {
    assertThat(Argument.createArgumentList(null)).isEmpty();
  }

  @Test
  public final void testCreateArgumentList_EmptyArrayDeliversEmptyMap() {
    assertThat(Argument.createArgumentList(new String[] {})).isEmpty();
  }

  @Test
  public final void testCreateArgumentList_NullArgumentProducesNoEntry() {
    assertThat(Argument.createArgumentList(new String[] { null })).isEmpty();
  }

  @Test
  public final void testCreateArgumentList_UnknownArgument() {
    String[] args = new String[] { "" };
    assertThat(Argument.createArgumentList(args).keySet()).containsOnly(Argument.ERROR_UNKNOWN);
    assertThat(Argument.createArgumentList(args).get(Argument.ERROR_UNKNOWN)).containsOnly("");

    args = new String[] { " " };
    assertThat(Argument.createArgumentList(args).keySet()).containsOnly(Argument.ERROR_UNKNOWN);
    assertThat(Argument.createArgumentList(args).get(Argument.ERROR_UNKNOWN)).containsOnly(" ");

    args = new String[] { "-help" };
    assertThat(Argument.createArgumentList(args).keySet()).containsOnly(Argument.ERROR_UNKNOWN);
    assertThat(Argument.createArgumentList(args).get(Argument.ERROR_UNKNOWN)).containsOnly("-help");

    args = new String[] { "-help", "asd" };
    assertThat(Argument.createArgumentList(args).keySet()).containsOnly(Argument.ERROR_UNKNOWN);
    assertThat(Argument.createArgumentList(args).get(Argument.ERROR_UNKNOWN)).containsOnly("asd", "-help");
  }

  @Test
  public final void testCreateArgumentList_HelpInArray() {
    String[] args = new String[] { "-h" };

    assertThat(Argument.createArgumentList(args).keySet()).containsOnly(Argument.HELP);
    assertThat(Argument.createArgumentList(args).get(Argument.HELP)).isEmpty();

    args = new String[] { "--help" };
    assertThat(Argument.createArgumentList(args).keySet()).containsOnly(Argument.HELP);
    assertThat(Argument.createArgumentList(args).get(Argument.HELP)).isEmpty();
  }

  @Test
  public final void testCreateArgumentList_VersionInArray() {
    String[] args = new String[] { "-v" };

    assertThat(Argument.createArgumentList(args).keySet()).containsOnly(Argument.VERSION);
    assertThat(Argument.createArgumentList(args).get(Argument.VERSION)).isEmpty();

    args = new String[] { "--version" };
    assertThat(Argument.createArgumentList(args).keySet()).containsOnly(Argument.VERSION);
    assertThat(Argument.createArgumentList(args).get(Argument.VERSION)).isEmpty();
  }

  //  @Test
  //  public final void testCreateArgumentList_DebugLevelInArray() {
  //    String[] args = new String[] { "-d" };
  //
  //    assertThat(Argument.createArgumentList(args).keySet()).containsOnly(Argument.ERROR_PARAM_NUMBER);
  //    assertThat(Argument.createArgumentList(args).get(Argument.ERROR_PARAM_NUMBER)).containsOnly("-d");
  //
  //    args = new String[] { "-d", "2" };
  //    assertThat(Argument.createArgumentList(args).keySet()).containsOnly(Argument.DEBUG_LEVEL);
  //    assertThat(Argument.createArgumentList(args).get(Argument.DEBUG_LEVEL)).containsOnly("2");
  //
  //    args = new String[] { "--debug-level", "2" };
  //    assertThat(Argument.createArgumentList(args).keySet()).containsOnly(Argument.DEBUG_LEVEL);
  //    assertThat(Argument.createArgumentList(args).get(Argument.DEBUG_LEVEL)).containsOnly("2");
  //  }

  @Test
  public final void testCreateArgumentList() {
    final String[] args = new String[] { "-h", "-v", null, "--help", "--xxno-argument", "null" };

    assertThat(Argument.createArgumentList(args)).hasSize(3);
    assertThat(Argument.createArgumentList(args).keySet()).containsOnly(Argument.VERSION, Argument.HELP,
                                                                        Argument.ERROR_UNKNOWN);
    assertThat(Argument.createArgumentList(args).get(Argument.VERSION)).isEmpty();
    assertThat(Argument.createArgumentList(args).get(Argument.HELP)).isEmpty();
    assertThat(Argument.createArgumentList(args).get(Argument.ERROR_UNKNOWN)).containsOnly("--xxno-argument", "null");
  }

  @Test
  public final void testCreateArgumentList2() {
    final String[] args = new String[] { "-h", "-v", null, "--xxno-argument", "null", "-dx", "-d" };

    assertThat(Argument.createArgumentList(args)).hasSize(3);
    assertThat(Argument.createArgumentList(args).keySet()).containsOnly(Argument.VERSION, Argument.HELP,
                                                                        Argument.ERROR_UNKNOWN);
    assertThat(Argument.createArgumentList(args).get(Argument.HELP)).isEmpty();
    assertThat(Argument.createArgumentList(args).get(Argument.VERSION)).isEmpty();
    assertThat(Argument.createArgumentList(args).get(Argument.ERROR_UNKNOWN)).containsOnly("--xxno-argument", "null",
                                                                                           "-dx", "-d");
  }

  @Test
  public final void testExecuteVersion() {
    final ByteArrayOutputStream out = new ByteArrayOutputStream();
    Printer.setPrintStream(new PrintStream(out));

    assertThat(Argument.VERSION.execute(null)).isFalse();
    assertThat(out.toString()).isEqualTo(Text.VERSION.text() + "\n");
  }

  @Test
  public final void testExecuteHelp() throws IOException {
    final ByteArrayOutputStream out = new ByteArrayOutputStream();
    Printer.setPrintStream(new PrintStream(out));

    assertThat(Argument.HELP.execute(null)).isFalse();

    final StringBuilder sb = new StringBuilder();
    BufferedReader reader = null;
    try {
      reader = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream("help.txt")));
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
  }

  @Test
  public final void testExecuteUnbufferedOutput() {
    assertThat(Argument.UNBUFFERED_OUTPUT.execute(null)).isTrue();
    assertThat(Output.isBuffered()).isFalse();

    Output.setBuffered(true);
    assertThat(Output.isBuffered()).isTrue();
    assertThat(Argument.UNBUFFERED_OUTPUT.execute(null)).isTrue();
    assertThat(Output.isBuffered()).isFalse();

    assertThat(Argument.UNBUFFERED_OUTPUT.execute(null)).isTrue();
    assertThat(Output.isBuffered()).isFalse();

    Output.setBuffered(true);
  }

  @Test
  public final void testExecuteUnknownArgument() {
    final ByteArrayOutputStream out = new ByteArrayOutputStream();
    Printer.setPrintStream(new PrintStream(out));

    assertThat(Argument.ERROR_UNKNOWN.execute(null)).isTrue();
    assertThat(out.toString()).isEmpty();

    assertThat(Argument.ERROR_UNKNOWN.execute(new String[] {})).isTrue();
    assertThat(out.toString()).isEmpty();

    assertThat(Argument.ERROR_UNKNOWN.execute(new String[] { null })).isTrue();
    assertThat(out.toString()).isEmpty();

    assertThat(Argument.ERROR_UNKNOWN.execute(new String[] { "bla" })).isFalse();
    assertThat(out.toString()).isEqualTo(Text.ERROR.text(Text.UNKNOWN_ARGUMENT.text("bla")) + "\n");

    out.reset();

    assertThat(Argument.ERROR_UNKNOWN.execute(new String[] { "bla", "--bla", "-wow" })).isFalse();
    assertThat(out.toString()).isEqualTo(Text.ERROR.text(Text.UNKNOWN_ARGUMENT.text("bla")) + "\n"
                                                 + Text.ERROR.text(Text.UNKNOWN_ARGUMENT.text("--bla")) + "\n"
                                                 + Text.ERROR.text(Text.UNKNOWN_ARGUMENT.text("-wow")) + "\n");
  }

  @Test
  public void testErrorArgs() {
    assertThat(Argument.of("--error-unknown")).isNull();
    assertThat(Argument.of("--error-param-number")).isNull();
    assertThat(Argument.of("-e")).isNull();
  }
}
