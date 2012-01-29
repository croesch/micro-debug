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
package com.github.croesch.i18n;

import static org.fest.assertions.Assertions.assertThat;

import java.util.Locale;

import org.junit.Test;

import com.github.croesch.DefaultTestCase;

/**
 * Provides test cases for {@link TextProperties}.
 * 
 * @author croesch
 * @since Date: Jan 24, 2012
 */
public class TextPropertiesTest extends DefaultTestCase {

  @Test
  public void test() {
    printlnMethodName();
    assertThat(new TextProperties(new Locale("test", "tst", "asd")).getProperty("border")).isEqualTo("b o r d e r");
    assertThat(new TextProperties(new Locale("test")).getProperty("try-help")).isEqualTo("..no one will ever see..");
    assertThat(new TextProperties(new Locale("test", "tst")).getProperty("try-help")).isEqualTo("OVERRIDDEN");
    assertThat(new TextProperties(new Locale("")).getProperty("try-help")).isNotEqualTo("OVERRIDDEN");
    assertThat(new TextProperties(new Locale("")).getProperty("try-help")).isNotEqualTo("..no one will ever see..");
    assertThat(new TextProperties(new Locale("pdf")).getProperty("try-help")).isNotEqualTo("OVERRIDDEN");
    assertThat(new TextProperties(new Locale("pdf")).getProperty("try-help")).isNotEqualTo("..no one will ever see..");
  }
}
