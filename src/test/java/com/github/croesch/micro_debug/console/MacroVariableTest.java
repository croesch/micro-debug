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
package com.github.croesch.micro_debug.console;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

import com.github.croesch.micro_debug.DefaultTestCase;

/**
 * Provides test cases for {@link MacroVariable}.
 * 
 * @author croesch
 * @since Date: Feb 8, 2012
 */
public class MacroVariableTest extends DefaultTestCase {

  private MacroVariable v1;

  private MacroVariable v1c;

  private MacroVariable v2;

  private MacroVariable v3;

  private MacroVariable v4;

  private MacroVariable v5;

  private MacroVariable v6;

  private MacroVariable v7;

  private MacroVariable v8;

  @Override
  protected void setUpDetails() throws Exception {
    this.v1 = new MacroVariable(2, 3, 4);
    this.v1c = new MacroVariable(2, 3, 4);
    this.v2 = new MacroVariable(10, 3, 4);
    this.v3 = new MacroVariable(2, -12, 4);
    this.v4 = new MacroVariable(2, 3, 27);
    this.v5 = new MacroVariable(19, 47, 4);
    this.v6 = new MacroVariable(2, 42, -11);
    this.v7 = new MacroVariable(1, 3, 2);
    this.v8 = new MacroVariable(3, 4, 2);
  }

  @Test
  public void testEquals() {
    printlnMethodName();

    assertThat(this.v1).isEqualTo(this.v1);

    assertThat(this.v1).isEqualTo(this.v1c);
    assertThat(this.v1c).isEqualTo(this.v1);
    this.v1c.setValue(242);
    assertThat(this.v1).isEqualTo(this.v1c);
    assertThat(this.v1c).isEqualTo(this.v1);

    assertThat(this.v1).isNotEqualTo(null);
    assertThat(this.v1).isNotEqualTo("v1");

    assertThat(this.v1c).isNotEqualTo(null);
    assertThat(this.v1c).isNotEqualTo("v1");

    assertThat(this.v1).isNotEqualTo(this.v2);
    assertThat(this.v1).isNotEqualTo(this.v3);
    assertThat(this.v1).isEqualTo(this.v4);
    assertThat(this.v1).isNotEqualTo(this.v5);
    assertThat(this.v1).isNotEqualTo(this.v6);
    assertThat(this.v1).isNotEqualTo(this.v7);
    assertThat(this.v1).isNotEqualTo(this.v8);

    assertThat(this.v1c).isNotEqualTo(this.v2);
    assertThat(this.v1c).isNotEqualTo(this.v3);
    assertThat(this.v1c).isEqualTo(this.v4);
    assertThat(this.v1c).isNotEqualTo(this.v5);
    assertThat(this.v1c).isNotEqualTo(this.v6);
    assertThat(this.v1c).isNotEqualTo(this.v7);
    assertThat(this.v1c).isNotEqualTo(this.v8);
  }

  @Test
  public void testHashCode() {
    printlnMethodName();

    assertThat(this.v1.hashCode()).isEqualTo(this.v1.hashCode());

    assertThat(this.v1.hashCode()).isEqualTo(this.v1c.hashCode());
    assertThat(this.v1c.hashCode()).isEqualTo(this.v1.hashCode());
    this.v1c.setValue(242);
    assertThat(this.v1.hashCode()).isEqualTo(this.v1c.hashCode());
    assertThat(this.v1c.hashCode()).isEqualTo(this.v1.hashCode());

    assertThat(this.v1.hashCode()).isNotEqualTo("v1".hashCode());
    assertThat(this.v1c.hashCode()).isNotEqualTo("v1".hashCode());

    assertThat(this.v1.hashCode()).isNotEqualTo(this.v2.hashCode());
    assertThat(this.v1.hashCode()).isNotEqualTo(this.v3.hashCode());
    assertThat(this.v1.hashCode()).isEqualTo(this.v4.hashCode());
    assertThat(this.v1.hashCode()).isNotEqualTo(this.v5.hashCode());
    assertThat(this.v1.hashCode()).isNotEqualTo(this.v6.hashCode());
    assertThat(this.v1.hashCode()).isNotEqualTo(this.v7.hashCode());
    assertThat(this.v1.hashCode()).isNotEqualTo(this.v8.hashCode());

    assertThat(this.v1c.hashCode()).isNotEqualTo(this.v2.hashCode());
    assertThat(this.v1c.hashCode()).isNotEqualTo(this.v3.hashCode());
    assertThat(this.v1c.hashCode()).isEqualTo(this.v4.hashCode());
    assertThat(this.v1c.hashCode()).isNotEqualTo(this.v5.hashCode());
    assertThat(this.v1c.hashCode()).isNotEqualTo(this.v6.hashCode());
    assertThat(this.v1c.hashCode()).isNotEqualTo(this.v7.hashCode());
    assertThat(this.v1c.hashCode()).isNotEqualTo(this.v8.hashCode());
  }

  @Test
  public void testGetNumber() {
    printlnMethodName();

    assertThat(this.v1.getNumber()).isEqualTo(2);
    assertThat(this.v1c.getNumber()).isEqualTo(2);
    assertThat(this.v2.getNumber()).isEqualTo(10);
    assertThat(this.v3.getNumber()).isEqualTo(2);
    assertThat(this.v4.getNumber()).isEqualTo(2);
    assertThat(this.v5.getNumber()).isEqualTo(19);
    assertThat(this.v6.getNumber()).isEqualTo(2);
    assertThat(this.v7.getNumber()).isEqualTo(1);
    assertThat(this.v8.getNumber()).isEqualTo(3);
  }

  @Test
  public void testGetAddress() {
    printlnMethodName();

    assertThat(this.v1.getAddress()).isEqualTo(3);
    assertThat(this.v1c.getAddress()).isEqualTo(3);
    assertThat(this.v2.getAddress()).isEqualTo(3);
    assertThat(this.v3.getAddress()).isEqualTo(-12);
    assertThat(this.v4.getAddress()).isEqualTo(3);
    assertThat(this.v5.getAddress()).isEqualTo(47);
    assertThat(this.v6.getAddress()).isEqualTo(42);
    assertThat(this.v7.getAddress()).isEqualTo(3);
    assertThat(this.v8.getAddress()).isEqualTo(4);
  }

  @Test
  public void testGetValue() {
    printlnMethodName();

    assertThat(this.v1.getValue()).isEqualTo(4);
    assertThat(this.v1c.getValue()).isEqualTo(4);
    assertThat(this.v2.getValue()).isEqualTo(4);
    assertThat(this.v3.getValue()).isEqualTo(4);
    assertThat(this.v4.getValue()).isEqualTo(27);
    assertThat(this.v5.getValue()).isEqualTo(4);
    assertThat(this.v6.getValue()).isEqualTo(-11);
    assertThat(this.v7.getValue()).isEqualTo(2);
    assertThat(this.v8.getValue()).isEqualTo(2);

    this.v5.setValue(377);

    assertThat(this.v1.getValue()).isEqualTo(4);
    assertThat(this.v1c.getValue()).isEqualTo(4);
    assertThat(this.v2.getValue()).isEqualTo(4);
    assertThat(this.v3.getValue()).isEqualTo(4);
    assertThat(this.v4.getValue()).isEqualTo(27);
    assertThat(this.v5.getValue()).isEqualTo(377);
    assertThat(this.v6.getValue()).isEqualTo(-11);
    assertThat(this.v7.getValue()).isEqualTo(2);
    assertThat(this.v8.getValue()).isEqualTo(2);
  }
}
