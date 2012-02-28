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

import java.io.IOException;

import org.junit.Test;

import com.github.croesch.micro_debug.DefaultTestCase;

/**
 * Provides test cases for {@link Help}.
 * 
 * @author croesch
 * @since Date: Feb 28, 2012
 */
public class HelpTest extends DefaultTestCase {

  @Test
  public final void testExecuteHelp() throws IOException {
    printlnMethodName();
    assertThat(Help.getInstance().execute()).isFalse();
    assertThat(out.toString()).isEqualTo(getHelpFileText());
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
}
