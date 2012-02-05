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
package com.github.croesch.debug;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

import com.github.croesch.DefaultTestCase;
import com.github.croesch.i18n.Text;
import com.github.croesch.mic1.register.Register;

/**
 * Provides test cases for {@link RegisterBreakpoint}.
 * 
 * @author croesch
 * @since Date: Jan 30, 2012
 */
public class RegisterBreakpointTest extends DefaultTestCase {

  /**
   * Test method for {@link RegisterBreakpoint#hashCode()}.
   */
  @Test
  public void testHashCode_Equals() {
    final RegisterBreakpoint rbp1 = new RegisterBreakpoint(Register.H, 12);
    final RegisterBreakpoint rbp1Copy = new RegisterBreakpoint(Register.H, 12);
    final RegisterBreakpoint rbp2 = new RegisterBreakpoint(Register.H, 13);
    final RegisterBreakpoint rbp3 = new RegisterBreakpoint(Register.CPP, 12);
    final RegisterBreakpoint rbp4 = new RegisterBreakpoint(Register.LV, 14);

    assertThat(rbp1).isNotEqualTo("rbp1");
    assertThat(rbp1).isNotEqualTo(null);

    assertThat(rbp1).isEqualTo(rbp1);
    assertThat(rbp1).isEqualTo(rbp1Copy);
    assertThat(rbp1).isNotEqualTo(rbp2);
    assertThat(rbp1).isNotEqualTo(rbp3);
    assertThat(rbp1).isNotEqualTo(rbp4);

    assertThat(rbp1.hashCode()).isEqualTo(rbp1.hashCode());
    assertThat(rbp1.hashCode()).isEqualTo(rbp1Copy.hashCode());
    assertThat(rbp1.hashCode()).isNotEqualTo(rbp2.hashCode());
    assertThat(rbp1.hashCode()).isNotEqualTo(rbp3.hashCode());
    assertThat(rbp1.hashCode()).isNotEqualTo(rbp4.hashCode());

    assertThat(rbp1Copy).isEqualTo(rbp1);
    assertThat(rbp2).isNotEqualTo(rbp1);
    assertThat(rbp3).isNotEqualTo(rbp1);
    assertThat(rbp4).isNotEqualTo(rbp1);

    assertThat(rbp1Copy.hashCode()).isEqualTo(rbp1.hashCode());
    assertThat(rbp2.hashCode()).isNotEqualTo(rbp1.hashCode());
    assertThat(rbp3.hashCode()).isNotEqualTo(rbp1.hashCode());
    assertThat(rbp4.hashCode()).isNotEqualTo(rbp1.hashCode());
  }

  /**
   * Test method for {@link RegisterBreakpoint#isConditionMet()}.
   */
  @Test
  public void testIsConditionMet() {
    final RegisterBreakpoint rbp = new RegisterBreakpoint(Register.H, 12);

    Register.H.setValue(12);
    assertThat(rbp.isConditionMet(0, 0)).isTrue();

    Register.H.setValue(12);
    assertThat(rbp.isConditionMet(0, 0)).isTrue();

    Register.H.setValue(13);
    assertThat(rbp.isConditionMet(0, 0)).isFalse();

    Register.CPP.setValue(12);
    assertThat(rbp.isConditionMet(0, 0)).isFalse();
  }

  /**
   * Test method for {@link RegisterBreakpoint#RegisterBreakpoint(Register, int)} .
   */
  @Test(expected = IllegalArgumentException.class)
  public void testRegisterBreakpoint() {
    new RegisterBreakpoint(null, 2);
  }

  /**
   * Test method for {@link RegisterBreakpoint#toString()}.
   */
  @Test
  public void testToString() {
    final RegisterBreakpoint rbp = new RegisterBreakpoint(Register.H, 12);
    assertThat(rbp.toString()).isEqualTo(Text.BREAKPOINT_REGISTER.text(rbp.getId(), "H", "0xC"));
  }

  /**
   * Test method for {@link RegisterBreakpoint#getRegister()}.
   */
  @Test
  public void testGetRegister() {
    final RegisterBreakpoint rbp = new RegisterBreakpoint(Register.H, 12);
    assertThat(rbp.getRegister()).isEqualTo(Register.H);
  }

  /**
   * Test method for {@link RegisterBreakpoint#getValue()}.
   */
  @Test
  public void testGetValue() {
    final RegisterBreakpoint rbp = new RegisterBreakpoint(Register.H, 12);
    assertThat(rbp.getValue()).isEqualTo(12);
  }

  /**
   * Test method for {@link Breakpoint#getId()}.
   */
  @Test
  public void testGetId() {
    final RegisterBreakpoint rbp1 = new RegisterBreakpoint(Register.H, 12);
    final RegisterBreakpoint rbp2 = new RegisterBreakpoint(Register.H, 12);
    final RegisterBreakpoint rbp3 = new RegisterBreakpoint(Register.H, 13);

    assertThat(rbp1.getId()).isNotEqualTo(rbp2.getId());
    assertThat(rbp1.getId()).isNotEqualTo(rbp3.getId());
    assertThat(rbp2.getId()).isNotEqualTo(rbp3.getId());
  }

}
