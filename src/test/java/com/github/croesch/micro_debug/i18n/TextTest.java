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
package com.github.croesch.micro_debug.i18n;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

import com.github.croesch.micro_debug.DefaultTestCase;

/**
 * Provides test cases for {@link Text}.
 * 
 * @author croesch
 * @since Date: Jan 24, 2012
 */
public class TextTest extends DefaultTestCase {

  @Test
  public void testText() {
    printlnMethodName();
    assertThat(Text.MACRO_CODE_LINE.text("$", " $ ", "  $  ")).isEqualTo("$: [ $ ]   $  {3}");
    assertThat(Text.MACRO_CODE_LINE.text("a", "b", "c", "d")).isEqualTo("a: [b] cd");
    assertThat(Text.MACRO_CODE_LINE.text("{0}")).isEqualTo("{0}: [{1}] {2}{3}");
    assertThat(Text.MACRO_CODE_LINE.text("{{0}")).isEqualTo("{0}: [{1}] {2}{3}");
    assertThat(Text.MACRO_CODE_LINE.text((Object[]) null)).isEqualTo("{0}: [{1}] {2}{3}");
    assertThat(Text.MACRO_CODE_LINE.text(new Object[] { null })).isEqualTo("null: [{1}] {2}{3}");
    assertThat(Text.MACRO_CODE_LINE.text(new Object[] { new Object() {
      @Override
      public String toString() {
        return null;
      }
    } })).isEqualTo("null: [{1}] {2}{3}");
    assertThat(Text.BORDER.text("asd")).isEqualTo("b o r d e r");
  }

  @Test
  public void test() {
    printlnMethodName();
    assertThat(Text.BORDER.text()).isEqualTo("b o r d e r");
    assertThat(Text.TRY_HELP.text()).isEqualTo("OVERRIDDEN");
  }
}
