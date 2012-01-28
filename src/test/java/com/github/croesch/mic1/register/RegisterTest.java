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
package com.github.croesch.mic1.register;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

import com.github.croesch.DefaultTestCase;
import com.github.croesch.mic1.register.Register;

/**
 * Provides test cases for {@link Register}.
 * 
 * @author croesch
 * @since Date: Nov 19, 2011
 */
public class RegisterTest extends DefaultTestCase {

  private static int[] TEST_VALUES = new int[] { Integer.MIN_VALUE,
                                                -1289237,
                                                -1273,
                                                -42,
                                                -1,
                                                0,
                                                1,
                                                42,
                                                242179,
                                                127380127,
                                                Integer.MAX_VALUE };

  @Override
  protected void setUpDetails() {
    for (final Register r : Register.values()) {
      r.setValue(0);
    }
  }

  @Test
  public void testSetValue() {
    printMethodName();

    final int oldValue = 4711;

    Register old = Register.H;
    for (final Register r : Register.values()) {
      // reset value of the register
      old.setValue(oldValue);

      // register MBR is a special case
      if (r != Register.MBR) {
        // set different values and test that they are returned but the old register isn't affected
        for (final int i : TEST_VALUES) {
          r.setValue(i);
          assertThat(r.getValue()).isEqualTo(i);
          assertThat(old.getValue()).isEqualTo(oldValue);
          printStep();
        }
        old = r;
        printLoopEnd();
      }
    }

    printEndOfMethod();
  }

  @Test
  public void testSetValue_MBR() {
    // test sign extension
    Register.MBR.setValue(0x00ff);
    assertThat(Register.MBR.getValue()).isEqualTo(0xffffffff);
    assertThat(Register.MBRU.getValue()).isEqualTo(0xff);

    Register.MBR.setValue(0x007f);
    assertThat(Register.MBR.getValue()).isEqualTo(0x7f);
    assertThat(Register.MBRU.getValue()).isEqualTo(0x7f);

    // test if value is read as byte
    Register.MBR.setValue(0xff7f);
    assertThat(Register.MBR.getValue()).isEqualTo(0x7f);
    assertThat(Register.MBRU.getValue()).isEqualTo(0x7f);

    Register.MBR.setValue(0xabcdeff);
    assertThat(Register.MBR.getValue()).isEqualTo(0xffffffff);
    assertThat(Register.MBRU.getValue()).isEqualTo(0xff);

    // test if value is read as byte
    Register.MBR.setValue(0xff00);
    assertThat(Register.MBR.getValue()).isZero();
    assertThat(Register.MBRU.getValue()).isZero();

    Register.MBR.setValue(0xff80);
    assertThat(Register.MBR.getValue()).isEqualTo(0xffffff80);
    assertThat(Register.MBRU.getValue()).isEqualTo(0x80);
  }
}
