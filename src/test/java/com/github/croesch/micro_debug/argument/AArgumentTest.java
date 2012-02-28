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
package com.github.croesch.micro_debug.argument;

import static org.fest.assertions.Assertions.assertThat;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import com.github.croesch.micro_debug.DefaultTestCase;
import com.github.croesch.micro_debug.i18n.Text;
import com.github.croesch.micro_debug.mic1.io.Output;

/**
 * Provides several test methods for {@link AArgument}.
 * 
 * @author croesch
 * @since Date: Aug 13, 2011
 */
public class AArgumentTest extends DefaultTestCase {

  @Test
  public final void testOf_DifferentValues() {
    printlnMethodName();
    assertThat(AArgument.of("--help")).isSameAs(Help.getInstance());
    assertThat(AArgument.of("-h")).isSameAs(Help.getInstance());

    assertThat(AArgument.of("--version")).isSameAs(Version.getInstance());
    assertThat(AArgument.of("-v")).isSameAs(Version.getInstance());

    assertThat(AArgument.of("--unbuffered-output")).isSameAs(UnbufferedOutput.getInstance());
    assertThat(AArgument.of("-u")).isSameAs(UnbufferedOutput.getInstance());

    assertThat(AArgument.of("--output-file")).isSameAs(OutputFile.getInstance());
    assertThat(AArgument.of("-o")).isSameAs(OutputFile.getInstance());
  }

  @Test
  public final void testOf_Unkown() {
    printlnMethodName();
    assertThat(AArgument.of(null)).isNull();
    assertThat(AArgument.of("")).isNull();
    assertThat(AArgument.of(" ")).isNull();
    assertThat(AArgument.of("HELP")).isNull();
    assertThat(AArgument.of("-help")).isNull();
    assertThat(AArgument.of("--h")).isNull();
  }

  @Test
  public final void testCreateArgumentList_NullDeliversEmptyMap() {
    printlnMethodName();
    assertThat(AArgument.createArgumentList(null)).isEmpty();
  }

  @Test
  public final void testCreateArgumentList_EmptyArrayDeliversEmptyMap() {
    printlnMethodName();
    assertThat(AArgument.createArgumentList(new String[] {})).isEmpty();
  }

  @Test
  public final void testCreateArgumentList_NullArgumentProducesNoEntry() {
    printlnMethodName();
    assertThat(AArgument.createArgumentList(new String[] { null })).isEmpty();
  }

  @Test
  public final void testCreateArgumentList_UnknownArgument() {
    printlnMethodName();
    String[] args = new String[] { "" };
    assertThat(AArgument.createArgumentList(args).keySet()).containsOnly(UnknownArgument.getInstance());
    assertThat(AArgument.createArgumentList(args).get(UnknownArgument.getInstance())).containsOnly("");

    args = new String[] { " " };
    assertThat(AArgument.createArgumentList(args).keySet()).containsOnly(UnknownArgument.getInstance());
    assertThat(AArgument.createArgumentList(args).get(UnknownArgument.getInstance())).containsOnly(" ");

    args = new String[] { "-help" };
    assertThat(AArgument.createArgumentList(args).keySet()).containsOnly(UnknownArgument.getInstance());
    assertThat(AArgument.createArgumentList(args).get(UnknownArgument.getInstance())).containsOnly("-help");

    args = new String[] { "-help", "asd" };
    assertThat(AArgument.createArgumentList(args).keySet()).containsOnly(UnknownArgument.getInstance());
    assertThat(AArgument.createArgumentList(args).get(UnknownArgument.getInstance())).containsOnly("asd", "-help");
  }

  @Test
  public final void testCreateArgumentList_HelpInArray() {
    printlnMethodName();
    String[] args = new String[] { "-h" };

    assertThat(AArgument.createArgumentList(args).keySet()).containsOnly(Help.getInstance());
    assertThat(AArgument.createArgumentList(args).get(Help.getInstance())).isEmpty();

    args = new String[] { "--help" };
    assertThat(AArgument.createArgumentList(args).keySet()).containsOnly(Help.getInstance());
    assertThat(AArgument.createArgumentList(args).get(Help.getInstance())).isEmpty();
  }

  @Test
  public final void testCreateArgumentList_VersionInArray() {
    printlnMethodName();
    String[] args = new String[] { "-v" };

    assertThat(AArgument.createArgumentList(args).keySet()).containsOnly(Version.getInstance());
    assertThat(AArgument.createArgumentList(args).get(Version.getInstance())).isEmpty();

    args = new String[] { "--version" };
    assertThat(AArgument.createArgumentList(args).keySet()).containsOnly(Version.getInstance());
    assertThat(AArgument.createArgumentList(args).get(Version.getInstance())).isEmpty();
  }

  @Test
  public final void testCreateArgumentList_OutputFileInArray() {
    printlnMethodName();
    String[] args = new String[] { "-o" };

    assertThat(AArgument.createArgumentList(args).keySet()).containsOnly(WrongParameterNumberArgument.getInstance());
    assertThat(AArgument.createArgumentList(args).get(WrongParameterNumberArgument.getInstance())).containsOnly("-o");

    args = new String[] { "-o", "2" };
    assertThat(AArgument.createArgumentList(args).keySet()).containsOnly(OutputFile.getInstance());
    assertThat(AArgument.createArgumentList(args).get(OutputFile.getInstance())).containsOnly("2");

    args = new String[] { "--output-file", "2" };
    assertThat(AArgument.createArgumentList(args).keySet()).containsOnly(OutputFile.getInstance());
    assertThat(AArgument.createArgumentList(args).get(OutputFile.getInstance())).containsOnly("2");
  }

  @Test
  public final void testCreateArgumentList() {
    printlnMethodName();
    final String[] args = new String[] { "-h", "-v", null, "--help", "--xxno-argument", "null", "-o" };

    assertThat(AArgument.createArgumentList(args)).hasSize(4);
    assertThat(AArgument.createArgumentList(args).keySet()).containsOnly(Version.getInstance(), Help.getInstance(),
                                                                         UnknownArgument.getInstance(),
                                                                         WrongParameterNumberArgument.getInstance());
    assertThat(AArgument.createArgumentList(args).get(Version.getInstance())).isEmpty();
    assertThat(AArgument.createArgumentList(args).get(Help.getInstance())).isEmpty();
    assertThat(AArgument.createArgumentList(args).get(UnknownArgument.getInstance())).containsOnly("--xxno-argument",
                                                                                                   "null");
    assertThat(AArgument.createArgumentList(args).get(WrongParameterNumberArgument.getInstance())).containsOnly("-o");
  }

  @Test
  public final void testCreateArgumentList2() {
    printlnMethodName();
    final String[] args = new String[] { "-h", "-v", null, "--xxno-argument", "null", "-dx", "-d", "--output-file" };

    assertThat(AArgument.createArgumentList(args)).hasSize(4);
    assertThat(AArgument.createArgumentList(args).keySet()).containsOnly(Version.getInstance(), Help.getInstance(),
                                                                         UnknownArgument.getInstance(),
                                                                         WrongParameterNumberArgument.getInstance());
    assertThat(AArgument.createArgumentList(args).get(Help.getInstance())).isEmpty();
    assertThat(AArgument.createArgumentList(args).get(Version.getInstance())).isEmpty();
    assertThat(AArgument.createArgumentList(args).get(UnknownArgument.getInstance())).containsOnly("--xxno-argument",
                                                                                                   "null", "-dx", "-d");
    assertThat(AArgument.createArgumentList(args).get(WrongParameterNumberArgument.getInstance()))
      .containsOnly("--output-file");
  }

  @Test
  public final void testExecuteVersion() {
    printlnMethodName();
    assertThat(Version.getInstance().execute()).isFalse();
    assertThat(out.toString()).isEqualTo(Text.VERSION.text() + getLineSeparator());
  }

  @Test
  public final void testExecuteHelp() throws IOException {
    printlnMethodName();
    assertThat(Help.getInstance().execute()).isFalse();
    assertThat(out.toString()).isEqualTo(getHelpFileText());
  }

  @Test
  public final void testExecuteUnbufferedOutput() {
    printlnMethodName();
    assertThat(UnbufferedOutput.getInstance().execute()).isTrue();
    assertThat(Output.isBuffered()).isFalse();

    Output.setBuffered(true);
    assertThat(Output.isBuffered()).isTrue();
    assertThat(UnbufferedOutput.getInstance().execute()).isTrue();
    assertThat(Output.isBuffered()).isFalse();

    assertThat(UnbufferedOutput.getInstance().execute()).isTrue();
    assertThat(Output.isBuffered()).isFalse();

    Output.setBuffered(true);
  }

  @Test
  public final void testExecuteUnknownArgument() throws IOException {
    printlnMethodName();
    assertThat(UnknownArgument.getInstance().execute()).isTrue();
    assertThat(out.toString()).isEmpty();

    assertThat(UnknownArgument.getInstance().execute(new String[] {})).isTrue();
    assertThat(out.toString()).isEmpty();

    assertThat(UnknownArgument.getInstance().execute(new String[] { null })).isTrue();
    assertThat(out.toString()).isEmpty();

    assertThat(UnknownArgument.getInstance().execute(new String[] { "bla" })).isFalse();
    assertThat(out.toString()).isEqualTo(Text.ERROR.text(Text.UNKNOWN_ARGUMENT.text("bla")) + getLineSeparator()
                                                 + getHelpFileText());

    out.reset();

    assertThat(UnknownArgument.getInstance().execute(new String[] { "bla", "--bla", "-wow" })).isFalse();
    assertThat(out.toString()).isEqualTo(Text.ERROR.text(Text.UNKNOWN_ARGUMENT.text("bla")) + getLineSeparator()
                                                 + Text.ERROR.text(Text.UNKNOWN_ARGUMENT.text("--bla"))
                                                 + getLineSeparator()
                                                 + Text.ERROR.text(Text.UNKNOWN_ARGUMENT.text("-wow"))
                                                 + getLineSeparator() + getHelpFileText());
  }

  @Test
  public final void testExecuteParameterNumber() throws IOException {
    printlnMethodName();
    assertThat(WrongParameterNumberArgument.getInstance().execute()).isTrue();
    assertThat(out.toString()).isEmpty();

    assertThat(WrongParameterNumberArgument.getInstance().execute((String[]) null)).isTrue();
    assertThat(out.toString()).isEmpty();

    assertThat(WrongParameterNumberArgument.getInstance().execute(new String[] {})).isTrue();
    assertThat(out.toString()).isEmpty();

    assertThat(WrongParameterNumberArgument.getInstance().execute(new String[] { null })).isTrue();
    assertThat(out.toString()).isEmpty();

    assertThat(WrongParameterNumberArgument.getInstance().execute(new String[] { "-o" })).isFalse();
    assertThat(out.toString()).isEqualTo(Text.ERROR.text(Text.ARGUMENT_WITH_WRONG_PARAM_NUMBER.text("-o"))
                                                 + getLineSeparator() + getHelpFileText());
  }

  @Test
  public void testErrorArgs() {
    printlnMethodName();
    assertThat(AArgument.of("--error-unknown")).isNull();
    assertThat(AArgument.of("--error-param-number")).isNull();
    assertThat(AArgument.of("-e")).isNull();
  }

  @Test
  public void testExecuteOutputFileAndReleaseResources() {
    printlnMethodName();
    AArgument.releaseAllResources();
    OutputFile.getInstance().execute(System.getProperty("java.io.tmpdir") + "/asd");
    AArgument.releaseAllResources();
    assertThat(new File(System.getProperty("java.io.tmpdir") + "/asd").delete()).isTrue();
    AArgument.releaseAllResources();
    OutputFile.getInstance().releaseResources();
  }
}
