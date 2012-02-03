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
package com.github.croesch.mic1;

import static org.fest.assertions.Assertions.assertThat;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

import com.github.croesch.DefaultTestCase;
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
    this.bpm.addBreakpoint(null, Integer.valueOf(0));
    this.bpm.addBreakpoint(Register.CPP, null);
  }

  @Test
  public void testIsBreakpoint() {
    printMethodName();

    for (final Register r : Register.values()) {
      r.setValue(0);
    }
    assertThat(this.bpm.isBreakpoint()).isFalse();
    for (final Register r : Register.values()) {
      this.bpm.addBreakpoint(r, Integer.valueOf(1));
      assertThat(this.bpm.isBreakpoint()).isFalse();
      r.setValue(1);
      assertThat(this.bpm.isBreakpoint()).isTrue();
      r.setValue(0);
      printStep();
    }
    printLoopEnd();
    for (final Register r : Register.values()) {
      this.bpm.addBreakpoint(r, Integer.valueOf(2));
      assertThat(this.bpm.isBreakpoint()).isFalse();
      r.setValue(1);
      assertThat(this.bpm.isBreakpoint()).isTrue();
      r.setValue(2);
      assertThat(this.bpm.isBreakpoint()).isTrue();
      r.setValue(0);
      printStep();
    }
    printEndOfMethod();
  }

  @Test
  public void testListBreakpoints() {
    printlnMethodName();

    this.bpm.addBreakpoint(Register.MBRU, Integer.valueOf(16));
    this.bpm.addBreakpoint(Register.MBRU, Integer.valueOf(-48));

    this.bpm.addBreakpoint(Register.CPP, Integer.valueOf(-1));
    this.bpm.addBreakpoint(Register.CPP, Integer.valueOf(Integer.MAX_VALUE));
    this.bpm.addBreakpoint(Register.CPP, Integer.valueOf(Integer.MIN_VALUE));

    this.bpm.addBreakpoint(Register.H, Integer.valueOf(2));
    this.bpm.addBreakpoint(Register.H, Integer.valueOf(2));
    this.bpm.addBreakpoint(Register.H, Integer.valueOf(3));
    this.bpm.addBreakpoint(Register.H, Integer.valueOf(1));

    assertThat(out.toString()).isEmpty();
    this.bpm.listBreakpoints();
    assertThat(out.toString()).matches(Text.BREAKPOINT_REGISTER.text("[0-9]+", Register.MBRU, "0x10")
                                               + getLineSeparator()
                                               + Text.BREAKPOINT_REGISTER.text("[0-9]+", Register.MBRU, "0xFFFFFFD0")
                                               + getLineSeparator()
                                               + Text.BREAKPOINT_REGISTER.text("[0-9]+", Register.CPP, "0xFFFFFFFF")
                                               + getLineSeparator()
                                               + Text.BREAKPOINT_REGISTER.text("[0-9]+", Register.CPP, "0x7FFFFFFF")
                                               + getLineSeparator()
                                               + Text.BREAKPOINT_REGISTER.text("[0-9]+", Register.CPP, "0x80000000")
                                               + getLineSeparator()
                                               + Text.BREAKPOINT_REGISTER.text("[0-9]+", Register.H, "0x2")
                                               + getLineSeparator()
                                               + Text.BREAKPOINT_REGISTER.text("[0-9]+", Register.H, "0x3")
                                               + getLineSeparator()
                                               + Text.BREAKPOINT_REGISTER.text("[0-9]+", Register.H, "0x1")
                                               + getLineSeparator());
  }

  @Test
  public void testRemoveBreakpoint() {
    printlnMethodName();

    this.bpm.addBreakpoint(Register.MBR, Integer.valueOf(16));
    this.bpm.addBreakpoint(Register.MBR, Integer.valueOf(-48));

    assertThat(out.toString()).isEmpty();
    this.bpm.listBreakpoints();
    final Matcher m = Pattern.compile(Text.BREAKPOINT_REGISTER.text("([0-9]+)", Register.MBR, "0x10")
                                              + getLineSeparator()
                                              + Text.BREAKPOINT_REGISTER.text("([0-9]+)", Register.MBR, "0xFFFFFFD0")
                                              + getLineSeparator()).matcher(out.toString());
    assertThat(m.matches());
    this.bpm.removeBreakpoint(Integer.parseInt(m.group(1)));
    out.reset();

    assertThat(out.toString()).isEmpty();
    this.bpm.listBreakpoints();
    assertThat(out.toString()).matches(Text.BREAKPOINT_REGISTER.text("([0-9]+)", Register.MBR, "0xFFFFFFD0")
                                               + getLineSeparator());

    this.bpm.removeBreakpoint(-11);
    out.reset();

    assertThat(out.toString()).isEmpty();
    this.bpm.listBreakpoints();
    assertThat(out.toString()).matches(Text.BREAKPOINT_REGISTER.text("([0-9]+)", Register.MBR, "0xFFFFFFD0")
                                               + getLineSeparator());
  }
}
