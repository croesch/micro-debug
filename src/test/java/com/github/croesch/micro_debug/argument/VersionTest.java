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
import com.github.croesch.micro_debug.i18n.Text;

/**
 * Provides test cases for {@link Version}.
 * 
 * @author croesch
 * @since Date: Feb 28, 2012
 */
public class VersionTest extends DefaultTestCase {

  @Test
  public final void testExecuteVersion() {
    printlnMethodName();
    assertThat(Version.getInstance().execute()).isFalse();
    assertThat(out.toString()).isEqualTo(Text.VERSION.text() + getLineSeparator());
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
}
