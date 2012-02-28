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

import org.junit.Test;

import com.github.croesch.micro_debug.DefaultTestCase;

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
  public void testErrorArgs() {
    printlnMethodName();
    assertThat(AArgument.of("--ERROR")).isNull();
    assertThat(AArgument.of("--error")).isNull();
    assertThat(AArgument.of("--error-unknown")).isNull();
    assertThat(AArgument.of("--error-param-number")).isNull();
    assertThat(AArgument.of("-e")).isNull();
    assertThat(AArgument.of("-E")).isNull();
  }
}
