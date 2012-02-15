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
package com.github.croesch.micro_debug.mic1.alu;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

import com.github.croesch.micro_debug.DefaultTestCase;

/**
 * Provides test cases for {@link OneBitAlu}.
 * 
 * @author croesch
 * @since Date: Oct 18, 2011
 */
public class OneBitAluTest extends DefaultTestCase {

  @Test
  public void test_A_And_B() {
    final OneBitAlu alu = new OneBitAlu();
    alu.setF0(false);
    alu.setF1(false);
    alu.setCarryIn(false);

    for (int i = 0; i < 2; ++i) {
      for (int j = 0; j < 2; ++j) {
        for (int k = 0; k < 2; ++k) {
          for (int l = 0; l < 2; ++l) {
            for (int m = 0; m < 2; ++m) {
              final boolean invA = i == 0;
              final boolean a = j == 0;
              final boolean enA = k == 0;
              final boolean b = l == 0;
              final boolean enB = m == 0;
              alu.setInvA(invA).setA(a).setEnA(enA).setB(b).setEnB(enB);
              alu.calculate();
              //              System.out.println("1-bit-alu and: INVA=" + invA + ",A=" + a + ",ENA=" + enA + ",B=" + b + ",ENB=" + enB
              //                                 + " -> out=" + alu.isOut());
              assertThat(alu.isCarryOut()).isFalse();
              assertThat(alu.isOut()).isEqualTo((b && enB) && (invA ^ (a && enA)));
            }
          }
        }
      }
    }
  }

  @Test
  public void test_A_Or_B() {
    final OneBitAlu alu = new OneBitAlu();
    alu.setF0(false);
    alu.setF1(true);
    alu.setCarryIn(false);

    for (int i = 0; i < 2; ++i) {
      for (int j = 0; j < 2; ++j) {
        for (int k = 0; k < 2; ++k) {
          for (int l = 0; l < 2; ++l) {
            for (int m = 0; m < 2; ++m) {
              final boolean invA = i == 0;
              final boolean a = j == 0;
              final boolean enA = k == 0;
              final boolean b = l == 0;
              final boolean enB = m == 0;
              alu.setInvA(invA).setA(a).setEnA(enA).setB(b).setEnB(enB);
              alu.calculate();
              //              System.out.println("1-bit-alu or: INVA=" + invA + ",A=" + a + ",ENA=" + enA + ",B=" + b + ",ENB=" + enB
              //                                 + " -> out=" + alu.isOut());
              assertThat(alu.isCarryOut()).isFalse();
              assertThat(alu.isOut()).isEqualTo((b && enB) || (invA ^ (a && enA)));
            }
          }
        }
      }
    }
  }

  @Test
  public void test_Not_B() {
    final OneBitAlu alu = new OneBitAlu();
    alu.setF0(true);
    alu.setF1(false);
    alu.setCarryIn(false);

    for (int i = 0; i < 2; ++i) {
      for (int j = 0; j < 2; ++j) {
        for (int k = 0; k < 2; ++k) {
          for (int l = 0; l < 2; ++l) {
            for (int m = 0; m < 2; ++m) {
              final boolean invA = i == 0;
              final boolean a = j == 0;
              final boolean enA = k == 0;
              final boolean b = l == 0;
              final boolean enB = m == 0;
              alu.setInvA(invA).setA(a).setEnA(enA).setB(b).setEnB(enB);
              alu.calculate();
              //              System.out.println("1-bit-alu not: INVA=" + invA + ",A=" + a + ",ENA=" + enA + ",B=" + b + ",ENB=" + enB
              //                                 + " -> out=" + alu.isOut());
              assertThat(alu.isCarryOut()).isFalse();
              assertThat(alu.isOut()).isEqualTo(!(b && enB));
            }
          }
        }
      }
    }
  }

  @Test
  public void test_Plus_B() {
    final OneBitAlu alu = new OneBitAlu();
    alu.setF0(true);
    alu.setF1(true);

    for (int h = 0; h < 2; ++h) {
      for (int i = 0; i < 2; ++i) {
        for (int j = 0; j < 2; ++j) {
          for (int k = 0; k < 2; ++k) {
            for (int l = 0; l < 2; ++l) {
              for (int m = 0; m < 2; ++m) {
                final boolean carryIn = h == 0;
                final boolean invA = i == 0;
                final boolean a = j == 0;
                final boolean enA = k == 0;
                final boolean b = l == 0;
                final boolean enB = m == 0;
                alu.setCarryIn(carryIn).setInvA(invA).setA(a).setEnA(enA).setB(b).setEnB(enB);
                alu.calculate();
                //                System.out
                //                  .println("1-bit-alu add: cIn=" + carryIn + ",INVA=" + invA + ",A=" + a + ",ENA=" + enA + ",B=" + b
                //                           + ",ENB=" + enB + " -> out=" + alu.isOut() + ",cOut=" + alu.isCarryOut());
                final boolean addA = invA ^ (a && enA);
                final boolean addB = b && enB;
                assertThat(alu.isCarryOut()).isEqualTo((addA && addB) || ((addA ^ addB) && carryIn));
                assertThat(alu.isOut()).isEqualTo((addA ^ addB) ^ carryIn);
              }
            }
          }
        }
      }
    }
  }
}
