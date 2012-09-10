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
import com.github.croesch.micro_debug.settings.Settings;

/**
 * Provides test cases for {@link MacroBreakpoint}.
 * 
 * @author croesch
 * @since Date: Jan 30, 2012
 */
public class MacroBreakpointTest extends DefaultTestCase {

  /**
   * Test method for {@link MacroBreakpoint#hashCode()}.
   */
  @Test
  public void testHashCode_Equals() {
    final MacroBreakpoint mbp1 = new MacroBreakpoint(12);
    final MacroBreakpoint mbp1Copy = new MacroBreakpoint(12);
    final MacroBreakpoint mbp2 = new MacroBreakpoint(13);
    final MacroBreakpoint mbp3 = new MacroBreakpoint(-12);
    final MicroBreakpoint mbp4 = new MicroBreakpoint(12);

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
   * Test method for {@link MacroBreakpoint#isConditionMet()}.
   */
  @Test
  public void testIsConditionMet() {
    final MacroBreakpoint mbp = new MacroBreakpoint(12);

    assertThat(mbp.isConditionMet(Settings.MIC1_MICRO_ADDRESS_IJVM.getValue(), 12, null, null)).isTrue();
    assertThat(mbp.isConditionMet(Settings.MIC1_MICRO_ADDRESS_IJVM.getValue() - 1, 12, null, null)).isFalse();
    assertThat(mbp.isConditionMet(Settings.MIC1_MICRO_ADDRESS_IJVM.getValue() + 1, 12, null, null)).isFalse();
    assertThat(mbp.isConditionMet(Settings.MIC1_MICRO_ADDRESS_IJVM.getValue(), 0, null, null)).isFalse();
    assertThat(mbp.isConditionMet(Settings.MIC1_MICRO_ADDRESS_IJVM.getValue(), -12, null, null)).isFalse();
  }

  /**
   * Test method for {@link MacroBreakpoint#MacroBreakpoint(int)} .
   */
  @Test
  public void testMacroBreakpoint() {
    assertThat(new MacroBreakpoint(-2)).isEqualTo(new MacroBreakpoint(-2));
    assertThat(new MacroBreakpoint(-2)).isNotEqualTo(new MacroBreakpoint(0));
    assertThat(new MacroBreakpoint(-2)).isNotEqualTo(new MacroBreakpoint(2));
    assertThat(new MacroBreakpoint(-2)).isNotEqualTo(new MacroBreakpoint(-1));
    assertThat(new MacroBreakpoint(-2)).isNotEqualTo(new MacroBreakpoint(-3));
  }

  /**
   * Test method for {@link MacroBreakpoint#toString()}.
   */
  @Test
  public void testToString() {
    final MacroBreakpoint mbp = new MacroBreakpoint(12);
    assertThat(mbp.toString()).isEqualTo(Text.BREAKPOINT_MACRO.text(mbp.getId(), "0xC"));
  }

  /**
   * Test method for {@link MacroBreakpoint#getLine()}.
   */
  @Test
  public void testGetLine() {
    MacroBreakpoint mbp = new MacroBreakpoint(12);
    assertThat(mbp.getLine()).isEqualTo(12);

    for (int i = -10000; i < 10000; ++i) {
      mbp = new MacroBreakpoint(i);
      assertThat(mbp.getLine()).isEqualTo(i);
    }
  }

  /**
   * Test method for {@link Breakpoint#getId()}.
   */
  @Test
  public void testGetId() {
    final MacroBreakpoint mbp1 = new MacroBreakpoint(12);
    final MacroBreakpoint mbp2 = new MacroBreakpoint(12);
    final MacroBreakpoint mbp3 = new MacroBreakpoint(-12);

    assertThat(mbp1.getId()).isNotEqualTo(mbp2.getId());
    assertThat(mbp1.getId()).isNotEqualTo(mbp3.getId());
    assertThat(mbp2.getId()).isNotEqualTo(mbp3.getId());
  }

  @Test
  public void testIsMacroBreakpoint() {
    assertThat(new MacroBreakpoint(12).isMacroBreakpoint()).isTrue();
    assertThat(new MacroBreakpoint(5).isMacroBreakpoint()).isTrue();
    assertThat(new MacroBreakpoint(-12).isMacroBreakpoint()).isTrue();
  }

  @Test
  public void testIsMicroBreakpoint() {
    assertThat(new MacroBreakpoint(12).isMicroBreakpoint()).isFalse();
    assertThat(new MacroBreakpoint(5).isMicroBreakpoint()).isFalse();
    assertThat(new MacroBreakpoint(-12).isMicroBreakpoint()).isFalse();
  }
}
