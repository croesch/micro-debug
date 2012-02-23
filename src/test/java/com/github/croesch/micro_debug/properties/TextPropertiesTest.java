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
package com.github.croesch.micro_debug.properties;

import static org.fest.assertions.Assertions.assertThat;

import java.util.Locale;

import org.junit.Test;

import com.github.croesch.micro_debug.DefaultTestCase;

/**
 * Provides test cases for {@link TextProperties}.
 * 
 * @author croesch
 * @since Date: Jan 24, 2012
 */
public class TextPropertiesTest extends DefaultTestCase {

  @Test
  public void testEqualsAndHashCode() {
    assertThat(new TextProperties("asd", Locale.getDefault()))
      .isEqualTo(new TextProperties("asd", Locale.getDefault()));

    assertThat(new TextProperties("asd", Locale.getDefault())).isEqualTo(new TextProperties("", Locale.getDefault()));

    assertThat(new TextProperties("asd", Locale.getDefault()).hashCode()).isEqualTo(new TextProperties("asd", Locale
                                                                                      .getDefault()).hashCode());

    assertThat(new TextProperties("asd", Locale.getDefault()).hashCode()).isEqualTo(new TextProperties("", Locale
                                                                                      .getDefault()).hashCode());
  }

  @Test
  public void testGetProperty_WrongFormat() {
    printlnMethodName();
    final String file = "lang/false-format";
    assertThat(new TextProperties(file, Locale.getDefault()).getProperty("border")).isEqualTo("!!missing-key=border!!");
  }

  @Test
  public void testXyGetProperty() {
    printlnMethodName();
    final String file = "xy";
    assertThat(new TextProperties(file, Locale.getDefault()).getProperty("border")).isEqualTo("!!missing-key=border!!");
    assertThat(new TextProperties(file, Locale.getDefault()).propertyNames().hasMoreElements()).isFalse();
  }

  @Test
  public void testLangTextGetProperty() {
    printlnMethodName();
    final String file = "lang/text";
    assertThat(new TextProperties(file, new Locale("test", "tst", "asd")).getProperty("border"))
      .isEqualTo("b o r d e r");
    assertThat(new TextProperties(file, new Locale("test", "tst", "asd")).getProperty("BORDER"))
      .isEqualTo("b o r d e r");
    assertThat(new TextProperties(file, new Locale("test")).getProperty("try-help"))
      .isEqualTo("..no one will ever see..");
    assertThat(new TextProperties(file, new Locale("test", "tst")).getProperty("try-help")).isEqualTo("OVERRIDDEN");
    assertThat(new TextProperties(file, new Locale("")).getProperty("try-help")).isNotEqualTo("OVERRIDDEN");
    assertThat(new TextProperties(file, new Locale("")).getProperty("try-help"))
      .isNotEqualTo("..no one will ever see..");
    assertThat(new TextProperties(file, new Locale("pdf")).getProperty("try-help")).isNotEqualTo("OVERRIDDEN");
    assertThat(new TextProperties(file, new Locale("pdf")).getProperty("try-help"))
      .isNotEqualTo("..no one will ever see..");
  }
}
