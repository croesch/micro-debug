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

import org.junit.Test;

import com.github.croesch.micro_debug.DefaultTestCase;
import com.github.croesch.micro_debug.i18n.Text;

/**
 * Provides test cases for {@link XMLPropertiesProvider}.
 * 
 * @author croesch
 * @since Date: Feb 23, 2012
 */
public class XMLPropertiesProviderTest extends DefaultTestCase {

  private final XMLPropertiesProvider propProvider = XMLPropertiesProvider.getInstance();

  @Test
  public void testInstanceSame() {
    printlnMethodName();
    assertThat(XMLPropertiesProvider.getInstance()).isSameAs(XMLPropertiesProvider.getInstance());
    assertThat(XMLPropertiesProvider.getInstance()).isSameAs(this.propProvider);
  }

  @Test
  public void testReplacePlaceholdersInString_NothingToReplace() {
    printlnMethodName();
    final String line = "alfred";

    String replaced = XMLPropertiesProvider.replacePlaceholdersInString(line);
    assertThat(replaced).isEqualTo(line);

    replaced = XMLPropertiesProvider.replacePlaceholdersInString(line, (Object[]) null);
    assertThat(replaced).isEqualTo(line);

    replaced = XMLPropertiesProvider.replacePlaceholdersInString(line, new Object[] { null });
    assertThat(replaced).isEqualTo(line);

    replaced = XMLPropertiesProvider.replacePlaceholdersInString(line, "null");
    assertThat(replaced).isEqualTo(line);

    replaced = XMLPropertiesProvider.replacePlaceholdersInString(line, "asd", "nbsdz", "$$&(=?!");
    assertThat(replaced).isEqualTo(line);
  }

  @Test
  public void testReplacePlaceholdersInString_EscapedPlaceholders() {
    printlnMethodName();
    final String line = "$lf {{0} r{}e{d} {-1} {{2}}";
    final String after = "$lf {0} r{}e{d} {-1} {2}}";

    String replaced = XMLPropertiesProvider.replacePlaceholdersInString(line);
    assertThat(replaced).isEqualTo(after);

    replaced = XMLPropertiesProvider.replacePlaceholdersInString(line, (Object[]) null);
    assertThat(replaced).isEqualTo(after);

    replaced = XMLPropertiesProvider.replacePlaceholdersInString(line, new Object[] { null });
    assertThat(replaced).isEqualTo(after);

    replaced = XMLPropertiesProvider.replacePlaceholdersInString(line, "null");
    assertThat(replaced).isEqualTo(after);

    replaced = XMLPropertiesProvider.replacePlaceholdersInString(line, "asd", "nbsdz", "$$&(=?!");
    assertThat(replaced).isEqualTo(after);
  }

  @Test
  public void testReplacePlaceholdersInString() {
    printlnMethodName();
    final String line = "{0}: [{1}] {2}{3}";

    String replaced = XMLPropertiesProvider.replacePlaceholdersInString(line, "$", " $ ", "  $  ");
    assertThat(replaced).isEqualTo("$: [ $ ]   $  {3}");

    replaced = XMLPropertiesProvider.replacePlaceholdersInString(line, "a", "b", "c", "d");
    assertThat(replaced).isEqualTo("a: [b] cd");

    replaced = XMLPropertiesProvider.replacePlaceholdersInString(line, "a", "b", "c", "d", "e", "f");
    assertThat(replaced).isEqualTo("a: [b] cd");

    replaced = XMLPropertiesProvider.replacePlaceholdersInString(line, "{0}");
    assertThat(replaced).isEqualTo(line);

    replaced = XMLPropertiesProvider.replacePlaceholdersInString(line, "{{0}");
    assertThat(replaced).isEqualTo(line);

    replaced = XMLPropertiesProvider.replacePlaceholdersInString(line);
    assertThat(replaced).isEqualTo(line);

    replaced = XMLPropertiesProvider.replacePlaceholdersInString(line, (Object[]) null);
    assertThat(replaced).isEqualTo(line);

    replaced = XMLPropertiesProvider.replacePlaceholdersInString(line, new Object[] { null });
    assertThat(replaced).isEqualTo("null: [{1}] {2}{3}");

    replaced = XMLPropertiesProvider.replacePlaceholdersInString(line, new Object[] { new Object() {
      @Override
      public String toString() {
        return null;
      }
    } });
    assertThat(replaced).isEqualTo("null: [{1}] {2}{3}");
  }

  @Test
  public void testGet_NullPath() {
    printlnMethodName();
    assertThat(this.propProvider.get(null, null)).isEqualTo(null);
    assertThat(this.propProvider.get(null, "")).isEqualTo(null);
    assertThat(this.propProvider.get(null, "asd")).isEqualTo(null);
  }

  @Test
  public void testGet_NullKey() {
    printlnMethodName();
    final String file = "lang/text";
    assertThat(this.propProvider.get(file, null)).isEqualTo(null);
    assertThat(this.propProvider.get(file, "")).isEqualTo("!!missing-key=!!");
    assertThat(this.propProvider.get(file, "asd")).isEqualTo("!!missing-key=asd!!");
  }

  @Test
  public void testGet() {
    printlnMethodName();
    final String file = "lang/text";
    assertThat(this.propProvider.get(file, Text.BORDER.name())).isEqualTo("b o r d e r");
    assertThat(this.propProvider.get(file, Text.TRY_HELP.name())).isEqualTo("OVERRIDDEN");
  }
}
