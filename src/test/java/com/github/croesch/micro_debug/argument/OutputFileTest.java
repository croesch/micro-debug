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

import org.junit.Test;

import com.github.croesch.micro_debug.DefaultTestCase;

/**
 * Provides test cases for {@link OutputFile}.
 * 
 * @author croesch
 * @since Date: Feb 28, 2012
 */
public class OutputFileTest extends DefaultTestCase {

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
}
