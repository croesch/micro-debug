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

import org.junit.Test;

import com.github.croesch.DefaultTestCase;
import com.github.croesch.mic1.register.Register;

/**
 * Contains test cases for {@link BreakPointManager}.
 * 
 * @author croesch
 * @since Date: Jan 27, 2012
 */
public class BreakPointManagerTest extends DefaultTestCase {

  private BreakPointManager bpm;

  @Override
  protected void setUpDetails() {
    this.bpm = new BreakPointManager();
  }

  @Test
  public void testAddBreakPoint() {
    // shouldn't throw any exception
    this.bpm.addBreakpoint(null, Integer.valueOf(0));
    this.bpm.addBreakpoint(Register.CPP, null);
  }

  @Test
  public void testIsBreakPoint() {
    printMethodName();

    for (final Register r : Register.values()) {
      r.setValue(0);
    }
    assertThat(this.bpm.isBreakPoint()).isFalse();
    for (final Register r : Register.values()) {
      this.bpm.addBreakpoint(r, Integer.valueOf(1));
      assertThat(this.bpm.isBreakPoint()).isFalse();
      r.setValue(1);
      assertThat(this.bpm.isBreakPoint()).isTrue();
      r.setValue(0);
      printStep();
    }
    printLoopEnd();
    for (final Register r : Register.values()) {
      this.bpm.addBreakpoint(r, Integer.valueOf(2));
      assertThat(this.bpm.isBreakPoint()).isFalse();
      r.setValue(1);
      assertThat(this.bpm.isBreakPoint()).isTrue();
      r.setValue(2);
      assertThat(this.bpm.isBreakPoint()).isTrue();
      r.setValue(0);
      printStep();
    }
    printEndOfMethod();
  }
}
