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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

import com.github.croesch.DefaultTestCase;
import com.github.croesch.commons.Settings;
import com.github.croesch.debug.BreakpointManager;
import com.github.croesch.i18n.Text;
import com.github.croesch.mic1.register.Register;

/**
 * Contains test cases for {@link BreakpointManager}.
 * 
 * @author croesch
 * @since Date: Jan 27, 2012
 */
public class BreakpointManagerTest extends DefaultTestCase {

  private BreakpointManager bpm;

  @Override
  protected void setUpDetails() {
    this.bpm = new BreakpointManager();
  }

  @Test
  public void testAddBreakPoint() {
    printlnMethodName();

    // shouldn't throw any exception
    this.bpm.addRegisterBreakpoint(null, Integer.valueOf(0));
    this.bpm.addRegisterBreakpoint(Register.CPP, null);
    this.bpm.addMicroBreakpoint(null);
    this.bpm.addMacroBreakpoint(null);
  }

  @Test
  public void testIsBreakpoint() {
    printMethodName();

    for (final Register r : Register.values()) {
      r.setValue(0);
    }
    assertThat(this.bpm.isBreakpoint(0, 0)).isFalse();
    for (final Register r : Register.values()) {
      this.bpm.addRegisterBreakpoint(r, Integer.valueOf(1));
      assertThat(this.bpm.isBreakpoint(0, 0)).isFalse();
      r.setValue(1);
      assertThat(this.bpm.isBreakpoint(0, 0)).isTrue();
      r.setValue(0);
      printStep();
    }
    printLoopEnd();
    for (final Register r : Register.values()) {
      this.bpm.addRegisterBreakpoint(r, Integer.valueOf(2));
      assertThat(this.bpm.isBreakpoint(0, 0)).isFalse();
      r.setValue(1);
      assertThat(this.bpm.isBreakpoint(0, 0)).isTrue();
      r.setValue(2);
      assertThat(this.bpm.isBreakpoint(0, 0)).isTrue();
      r.setValue(0);
      printStep();
    }
    printLoopEnd();
    for (int i = -17; i < 43; ++i) {
      this.bpm.addMicroBreakpoint(i);
      assertThat(this.bpm.isBreakpoint(i + 1, 0)).isFalse();
      assertThat(this.bpm.isBreakpoint(i, 0)).isTrue();
      assertThat(this.bpm.isBreakpoint(i, i)).isTrue();
      printStep();
    }
    printLoopEnd();
    for (int i = -17; i < 43; ++i) {
      this.bpm.addMacroBreakpoint(i);
      assertThat(this.bpm.isBreakpoint(370, i)).isFalse();
      assertThat(this.bpm.isBreakpoint(-41, i)).isFalse();
      assertThat(this.bpm.isBreakpoint(Settings.MIC1_MICRO_ADDRESS_IJVM.getValue(), i)).isTrue();
      printStep();
    }
    printEndOfMethod();
  }

  @Test
  public void testListBreakpoints() {
    printlnMethodName();

    this.bpm.addRegisterBreakpoint(Register.MBRU, Integer.valueOf(16));
    this.bpm.addRegisterBreakpoint(Register.MBRU, Integer.valueOf(-48));

    this.bpm.addMicroBreakpoint(37);

    this.bpm.addRegisterBreakpoint(Register.CPP, Integer.valueOf(-1));
    this.bpm.addRegisterBreakpoint(Register.CPP, Integer.valueOf(Integer.MAX_VALUE));
    this.bpm.addRegisterBreakpoint(Register.CPP, Integer.valueOf(Integer.MIN_VALUE));

    this.bpm.addMicroBreakpoint(42);
    this.bpm.addMicroBreakpoint(42);

    this.bpm.addRegisterBreakpoint(Register.H, Integer.valueOf(2));
    this.bpm.addRegisterBreakpoint(Register.H, Integer.valueOf(2));
    this.bpm.addRegisterBreakpoint(Register.H, Integer.valueOf(3));
    this.bpm.addRegisterBreakpoint(Register.H, Integer.valueOf(1));

    this.bpm.addMacroBreakpoint(37);

    assertThat(out.toString()).isEmpty();
    this.bpm.listBreakpoints();
    assertThat(out.toString()).matches(Text.BREAKPOINT_REGISTER.text("[0-9]+", Register.MBRU, "0x10")
                                               + getLineSeparator()
                                               + Text.BREAKPOINT_REGISTER.text("[0-9]+", Register.MBRU, "0xFFFFFFD0")
                                               + getLineSeparator() + Text.BREAKPOINT_MICRO.text("[0-9]+", "0x25")
                                               + getLineSeparator()
                                               + Text.BREAKPOINT_REGISTER.text("[0-9]+", Register.CPP, "0xFFFFFFFF")
                                               + getLineSeparator()
                                               + Text.BREAKPOINT_REGISTER.text("[0-9]+", Register.CPP, "0x7FFFFFFF")
                                               + getLineSeparator()
                                               + Text.BREAKPOINT_REGISTER.text("[0-9]+", Register.CPP, "0x80000000")
                                               + getLineSeparator() + Text.BREAKPOINT_MICRO.text("[0-9]+", "0x2A")
                                               + getLineSeparator()
                                               + Text.BREAKPOINT_REGISTER.text("[0-9]+", Register.H, "0x2")
                                               + getLineSeparator()
                                               + Text.BREAKPOINT_REGISTER.text("[0-9]+", Register.H, "0x3")
                                               + getLineSeparator()
                                               + Text.BREAKPOINT_REGISTER.text("[0-9]+", Register.H, "0x1")
                                               + getLineSeparator() + Text.BREAKPOINT_MACRO.text("[0-9]+", "0x25")
                                               + getLineSeparator());
  }

  @Test
  public void testRemoveBreakpoint() {
    printlnMethodName();

    this.bpm.addRegisterBreakpoint(Register.MBR, Integer.valueOf(16));
    this.bpm.addRegisterBreakpoint(Register.MBR, Integer.valueOf(-48));
    this.bpm.addMicroBreakpoint(12);
    this.bpm.addMacroBreakpoint(13);
    this.bpm.addMacroBreakpoint(13);
    this.bpm.addMacroBreakpoint(13);

    assertThat(out.toString()).isEmpty();
    this.bpm.listBreakpoints();
    final Matcher m = Pattern.compile(Text.BREAKPOINT_REGISTER.text("([0-9]+)", Register.MBR, "0x10")
                                              + getLineSeparator()
                                              + Text.BREAKPOINT_REGISTER.text("([0-9]+)", Register.MBR, "0xFFFFFFD0")
                                              + getLineSeparator() + Text.BREAKPOINT_MICRO.text("([0-9]+)", "0xC")
                                              + getLineSeparator() + Text.BREAKPOINT_MACRO.text("[0-9]+", "0xD")
                                              + getLineSeparator()).matcher(out.toString());
    assertThat(m.matches()).isTrue();
    this.bpm.removeBreakpoint(Integer.parseInt(m.group(1)));
    out.reset();

    assertThat(out.toString()).isEmpty();
    this.bpm.listBreakpoints();
    assertThat(out.toString()).matches(Text.BREAKPOINT_REGISTER.text("([0-9]+)", Register.MBR, "0xFFFFFFD0")
                                               + getLineSeparator() + Text.BREAKPOINT_MICRO.text("[0-9]+", "0xC")
                                               + getLineSeparator() + Text.BREAKPOINT_MACRO.text("[0-9]+", "0xD")
                                               + getLineSeparator());

    this.bpm.removeBreakpoint(-11);
    out.reset();

    assertThat(out.toString()).isEmpty();
    this.bpm.listBreakpoints();
    assertThat(out.toString()).matches(Text.BREAKPOINT_REGISTER.text("([0-9]+)", Register.MBR, "0xFFFFFFD0")
                                               + getLineSeparator() + Text.BREAKPOINT_MICRO.text("[0-9]+", "0xC")
                                               + getLineSeparator() + Text.BREAKPOINT_MACRO.text("[0-9]+", "0xD")
                                               + getLineSeparator());

    this.bpm.removeBreakpoint(Integer.parseInt(m.group(3)));
    out.reset();
    this.bpm.listBreakpoints();
    assertThat(out.toString()).matches(Text.BREAKPOINT_REGISTER.text("([0-9]+)", Register.MBR, "0xFFFFFFD0")
                                               + getLineSeparator() + Text.BREAKPOINT_MACRO.text("[0-9]+", "0xD")
                                               + getLineSeparator());
  }
}
