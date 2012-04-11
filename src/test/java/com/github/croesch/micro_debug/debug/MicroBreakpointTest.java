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
package com.github.croesch.micro_debug.debug;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

import com.github.croesch.micro_debug.DefaultTestCase;
import com.github.croesch.micro_debug.i18n.Text;

/**
 * Provides test cases for {@link MicroBreakpoint}.
 * 
 * @author croesch
 * @since Date: Jan 30, 2012
 */
public class MicroBreakpointTest extends DefaultTestCase {

  /**
   * Test method for {@link MicroBreakpoint#hashCode()}.
   */
  @Test
  public void testHashCode_Equals() {
    final MicroBreakpoint mbp1 = new MicroBreakpoint(12);
    final MicroBreakpoint mbp1Copy = new MicroBreakpoint(12);
    final MicroBreakpoint mbp2 = new MicroBreakpoint(13);
    final MicroBreakpoint mbp3 = new MicroBreakpoint(-12);
    final MacroBreakpoint mbp4 = new MacroBreakpoint(12);

    assertThat(mbp1).isNotEqualTo("mbp1");
    assertThat(mbp1).isNotEqualTo(null);

    assertThat(mbp1).isEqualTo(mbp1);
    assertThat(mbp1).isEqualTo(mbp1Copy);
    assertThat(mbp1).isNotEqualTo(mbp2);
    assertThat(mbp1).isNotEqualTo(mbp3);
    assertThat(mbp1).isNotEqualTo(mbp4);

    assertThat(mbp1.hashCode()).isEqualTo(mbp1.hashCode());
    assertThat(mbp1.hashCode()).isEqualTo(mbp1Copy.hashCode());
    assertThat(mbp1.hashCode()).isNotEqualTo(mbp2.hashCode());
    assertThat(mbp1.hashCode()).isNotEqualTo(mbp3.hashCode());
    assertThat(mbp1.hashCode()).isNotEqualTo(mbp4.hashCode());

    assertThat(mbp1Copy).isEqualTo(mbp1);
    assertThat(mbp2).isNotEqualTo(mbp1);
    assertThat(mbp3).isNotEqualTo(mbp1);
    assertThat(mbp4).isNotEqualTo(mbp1);

    assertThat(mbp1Copy.hashCode()).isEqualTo(mbp1.hashCode());
    assertThat(mbp2.hashCode()).isNotEqualTo(mbp1.hashCode());
    assertThat(mbp3.hashCode()).isNotEqualTo(mbp1.hashCode());
    assertThat(mbp4.hashCode()).isNotEqualTo(mbp1.hashCode());
  }

  /**
   * Test method for {@link MicroBreakpoint#isConditionMet()}.
   */
  @Test
  public void testIsConditionMet() {
    final MicroBreakpoint mbp = new MicroBreakpoint(12);

    assertThat(mbp.isConditionMet(12, 0, null, null)).isTrue();
    assertThat(mbp.isConditionMet(12, 12, null, null)).isTrue();
    assertThat(mbp.isConditionMet(0, 12, null, null)).isFalse();
    assertThat(mbp.isConditionMet(0, 0, null, null)).isFalse();
    assertThat(mbp.isConditionMet(-12, 0, null, null)).isFalse();
  }

  /**
   * Test method for {@link MicroBreakpoint#MicroBreakpoint(int)} .
   */
  @Test
  public void testMicroBreakpoint() {
    assertThat(new MicroBreakpoint(-2)).isEqualTo(new MicroBreakpoint(-2));
    assertThat(new MicroBreakpoint(-2)).isNotEqualTo(new MicroBreakpoint(0));
    assertThat(new MicroBreakpoint(-2)).isNotEqualTo(new MicroBreakpoint(2));
    assertThat(new MicroBreakpoint(-2)).isNotEqualTo(new MicroBreakpoint(-1));
    assertThat(new MicroBreakpoint(-2)).isNotEqualTo(new MicroBreakpoint(-3));
  }

  /**
   * Test method for {@link MicroBreakpoint#toString()}.
   */
  @Test
  public void testToString() {
    final MicroBreakpoint mbp = new MicroBreakpoint(12);
    assertThat(mbp.toString()).isEqualTo(Text.BREAKPOINT_MICRO.text(mbp.getId(), "0xC"));
  }

  /**
   * Test method for {@link MicroBreakpoint#getLine()}.
   */
  @Test
  public void testGetLine() {
    MicroBreakpoint mbp = new MicroBreakpoint(12);
    assertThat(mbp.getLine()).isEqualTo(12);

    for (int i = -10000; i < 10000; ++i) {
      mbp = new MicroBreakpoint(i);
      assertThat(mbp.getLine()).isEqualTo(i);
    }
  }

  /**
   * Test method for {@link Breakpoint#getId()}.
   */
  @Test
  public void testGetId() {
    final MicroBreakpoint mbp1 = new MicroBreakpoint(12);
    final MicroBreakpoint mbp2 = new MicroBreakpoint(12);
    final MicroBreakpoint mbp3 = new MicroBreakpoint(-12);

    assertThat(mbp1.getId()).isNotEqualTo(mbp2.getId());
    assertThat(mbp1.getId()).isNotEqualTo(mbp3.getId());
    assertThat(mbp2.getId()).isNotEqualTo(mbp3.getId());
  }

}
