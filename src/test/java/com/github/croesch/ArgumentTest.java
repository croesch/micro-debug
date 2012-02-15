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

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import com.github.croesch.i18n.Text;
import com.github.croesch.mic1.io.Output;

/**
 * Provides several test methods for {@link Argument}.
 * 
 * @author croesch
 * @since Date: Aug 13, 2011
 */
public class ArgumentTest extends DefaultTestCase {

  @Test
  public final void testOf_DifferentValues() {
    printlnMethodName();
    assertThat(Argument.of("--help")).isSameAs(Argument.HELP);
    assertThat(Argument.of("-h")).isSameAs(Argument.HELP);

    assertThat(Argument.of("--version")).isSameAs(Argument.VERSION);
    assertThat(Argument.of("-v")).isSameAs(Argument.VERSION);

    assertThat(Argument.of("--unbuffered-output")).isSameAs(Argument.UNBUFFERED_OUTPUT);
    assertThat(Argument.of("-u")).isSameAs(Argument.UNBUFFERED_OUTPUT);

    assertThat(Argument.of("--output-file")).isSameAs(Argument.OUTPUT_FILE);
    assertThat(Argument.of("-o")).isSameAs(Argument.OUTPUT_FILE);
  }

  @Test
  public final void testOf_Unkown() {
    printlnMethodName();
    assertThat(Argument.of(null)).isNull();
    assertThat(Argument.of("")).isNull();
    assertThat(Argument.of(" ")).isNull();
    assertThat(Argument.of("HELP")).isNull();
    assertThat(Argument.of("-help")).isNull();
    assertThat(Argument.of("--h")).isNull();
  }

  @Test
  public final void testCreateArgumentList_NullDeliversEmptyMap() {
    printlnMethodName();
    assertThat(Argument.createArgumentList(null)).isEmpty();
  }

  @Test
  public final void testCreateArgumentList_EmptyArrayDeliversEmptyMap() {
    printlnMethodName();
    assertThat(Argument.createArgumentList(new String[] {})).isEmpty();
  }

  @Test
  public final void testCreateArgumentList_NullArgumentProducesNoEntry() {
    printlnMethodName();
    assertThat(Argument.createArgumentList(new String[] { null })).isEmpty();
  }

  @Test
  public final void testCreateArgumentList_UnknownArgument() {
    printlnMethodName();
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
    printlnMethodName();
    String[] args = new String[] { "-h" };

    assertThat(Argument.createArgumentList(args).keySet()).containsOnly(Argument.HELP);
    assertThat(Argument.createArgumentList(args).get(Argument.HELP)).isEmpty();

    args = new String[] { "--help" };
    assertThat(Argument.createArgumentList(args).keySet()).containsOnly(Argument.HELP);
    assertThat(Argument.createArgumentList(args).get(Argument.HELP)).isEmpty();
  }

  @Test
  public final void testCreateArgumentList_VersionInArray() {
    printlnMethodName();
    String[] args = new String[] { "-v" };

    assertThat(Argument.createArgumentList(args).keySet()).containsOnly(Argument.VERSION);
    assertThat(Argument.createArgumentList(args).get(Argument.VERSION)).isEmpty();

    args = new String[] { "--version" };
    assertThat(Argument.createArgumentList(args).keySet()).containsOnly(Argument.VERSION);
    assertThat(Argument.createArgumentList(args).get(Argument.VERSION)).isEmpty();
  }

  @Test
  public final void testCreateArgumentList_OutputFileInArray() {
    printlnMethodName();
    String[] args = new String[] { "-o" };

    assertThat(Argument.createArgumentList(args).keySet()).containsOnly(Argument.ERROR_PARAM_NUMBER);
    assertThat(Argument.createArgumentList(args).get(Argument.ERROR_PARAM_NUMBER)).containsOnly("-o");

    args = new String[] { "-o", "2" };
    assertThat(Argument.createArgumentList(args).keySet()).containsOnly(Argument.OUTPUT_FILE);
    assertThat(Argument.createArgumentList(args).get(Argument.OUTPUT_FILE)).containsOnly("2");

    args = new String[] { "--output-file", "2" };
    assertThat(Argument.createArgumentList(args).keySet()).containsOnly(Argument.OUTPUT_FILE);
    assertThat(Argument.createArgumentList(args).get(Argument.OUTPUT_FILE)).containsOnly("2");
  }

  @Test
  public final void testCreateArgumentList() {
    printlnMethodName();
    final String[] args = new String[] { "-h", "-v", null, "--help", "--xxno-argument", "null", "-o" };

    assertThat(Argument.createArgumentList(args)).hasSize(4);
    assertThat(Argument.createArgumentList(args).keySet()).containsOnly(Argument.VERSION, Argument.HELP,
                                                                        Argument.ERROR_UNKNOWN,
                                                                        Argument.ERROR_PARAM_NUMBER);
    assertThat(Argument.createArgumentList(args).get(Argument.VERSION)).isEmpty();
    assertThat(Argument.createArgumentList(args).get(Argument.HELP)).isEmpty();
    assertThat(Argument.createArgumentList(args).get(Argument.ERROR_UNKNOWN)).containsOnly("--xxno-argument", "null");
    assertThat(Argument.createArgumentList(args).get(Argument.ERROR_PARAM_NUMBER)).containsOnly("-o");
  }

  @Test
  public final void testCreateArgumentList2() {
    printlnMethodName();
    final String[] args = new String[] { "-h", "-v", null, "--xxno-argument", "null", "-dx", "-d", "--output-file" };

    assertThat(Argument.createArgumentList(args)).hasSize(4);
    assertThat(Argument.createArgumentList(args).keySet()).containsOnly(Argument.VERSION, Argument.HELP,
                                                                        Argument.ERROR_UNKNOWN,
                                                                        Argument.ERROR_PARAM_NUMBER);
    assertThat(Argument.createArgumentList(args).get(Argument.HELP)).isEmpty();
    assertThat(Argument.createArgumentList(args).get(Argument.VERSION)).isEmpty();
    assertThat(Argument.createArgumentList(args).get(Argument.ERROR_UNKNOWN)).containsOnly("--xxno-argument", "null",
                                                                                           "-dx", "-d");
    assertThat(Argument.createArgumentList(args).get(Argument.ERROR_PARAM_NUMBER)).containsOnly("--output-file");
  }

  @Test
  public final void testExecuteVersion() {
    printlnMethodName();
    assertThat(Argument.VERSION.execute()).isFalse();
    assertThat(out.toString()).isEqualTo(Text.VERSION.text() + getLineSeparator());
  }

  @Test
  public final void testExecuteHelp() throws IOException {
    printlnMethodName();
    assertThat(Argument.HELP.execute()).isFalse();
    assertThat(out.toString()).isEqualTo(getHelpFileText());
  }

  @Test
  public final void testExecuteUnbufferedOutput() {
    printlnMethodName();
    assertThat(Argument.UNBUFFERED_OUTPUT.execute()).isTrue();
    assertThat(Output.isBuffered()).isFalse();

    Output.setBuffered(true);
    assertThat(Output.isBuffered()).isTrue();
    assertThat(Argument.UNBUFFERED_OUTPUT.execute()).isTrue();
    assertThat(Output.isBuffered()).isFalse();

    assertThat(Argument.UNBUFFERED_OUTPUT.execute()).isTrue();
    assertThat(Output.isBuffered()).isFalse();

    Output.setBuffered(true);
  }

  @Test
  public final void testExecuteUnknownArgument() throws IOException {
    printlnMethodName();
    assertThat(Argument.ERROR_UNKNOWN.execute()).isTrue();
    assertThat(out.toString()).isEmpty();

    assertThat(Argument.ERROR_UNKNOWN.execute(new String[] {})).isTrue();
    assertThat(out.toString()).isEmpty();

    assertThat(Argument.ERROR_UNKNOWN.execute(new String[] { null })).isTrue();
    assertThat(out.toString()).isEmpty();

    assertThat(Argument.ERROR_UNKNOWN.execute(new String[] { "bla" })).isFalse();
    assertThat(out.toString()).isEqualTo(Text.ERROR.text(Text.UNKNOWN_ARGUMENT.text("bla")) + getLineSeparator()
                                                 + getHelpFileText());

    out.reset();

    assertThat(Argument.ERROR_UNKNOWN.execute(new String[] { "bla", "--bla", "-wow" })).isFalse();
    assertThat(out.toString()).isEqualTo(Text.ERROR.text(Text.UNKNOWN_ARGUMENT.text("bla")) + getLineSeparator()
                                                 + Text.ERROR.text(Text.UNKNOWN_ARGUMENT.text("--bla"))
                                                 + getLineSeparator()
                                                 + Text.ERROR.text(Text.UNKNOWN_ARGUMENT.text("-wow"))
                                                 + getLineSeparator() + getHelpFileText());
  }

  @Test
  public final void testExecuteParameterNumber() throws IOException {
    printlnMethodName();
    assertThat(Argument.ERROR_PARAM_NUMBER.execute()).isTrue();
    assertThat(out.toString()).isEmpty();

    assertThat(Argument.ERROR_PARAM_NUMBER.execute((String[]) null)).isTrue();
    assertThat(out.toString()).isEmpty();

    assertThat(Argument.ERROR_PARAM_NUMBER.execute(new String[] {})).isTrue();
    assertThat(out.toString()).isEmpty();

    assertThat(Argument.ERROR_PARAM_NUMBER.execute(new String[] { null })).isTrue();
    assertThat(out.toString()).isEmpty();

    assertThat(Argument.ERROR_PARAM_NUMBER.execute(new String[] { "-o" })).isFalse();
    assertThat(out.toString()).isEqualTo(Text.ERROR.text(Text.ARGUMENT_WITH_WRONG_PARAM_NUMBER.text("-o"))
                                                 + getLineSeparator() + getHelpFileText());
  }

  @Test
  public void testErrorArgs() {
    printlnMethodName();
    assertThat(Argument.of("--error-unknown")).isNull();
    assertThat(Argument.of("--error-param-number")).isNull();
    assertThat(Argument.of("-e")).isNull();
  }

  @Test
  public void testExecuteOutputFileAndReleaseResources() {
    printlnMethodName();
    Argument.releaseAllResources();
    Argument.OUTPUT_FILE.execute(System.getProperty("java.io.tmpdir") + "/asd");
    Argument.releaseAllResources();
    assertThat(new File(System.getProperty("java.io.tmpdir") + "/asd").delete()).isTrue();
    Argument.releaseAllResources();
    Argument.OUTPUT_FILE.releaseResources();
  }
}
