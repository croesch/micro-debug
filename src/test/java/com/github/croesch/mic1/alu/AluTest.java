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
package com.github.croesch.mic1.alu;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

/**
 * Provides test cases for {@link Alu}, based on the table 'calculable functions of the ALU' of the script of Karl
 * Stroetmann.
 * 
 * @author croesch
 * @since Date: Oct 18, 2011
 */
public class AluTest {

  private static final int[] TEST_VALUES = new int[] { Integer.MIN_VALUE,
                                                      -2008,
                                                      -1000,
                                                      -16,
                                                      -1,
                                                      0,
                                                      1,
                                                      6,
                                                      32,
                                                      128,
                                                      256,
                                                      4096,
                                                      Integer.MAX_VALUE };

  private static final boolean[] BOOL_VALUES = new boolean[] { true, false };

  @Test
  public void testA() {
    final Alu alu = new Alu();
    alu.setF0(false).setF1(true).setEnA(true).setEnB(false).setInvA(false);
    for (final boolean inc : BOOL_VALUES) {
      for (final int a : TEST_VALUES) {
        for (final int b : TEST_VALUES) {
          alu.setA(a).setB(b).setInc(inc).calculate();
          final int result = a;
          assertThat(alu.getOut()).isEqualTo(result);
          assertThat(alu.isN()).isEqualTo(result < 0);
          assertThat(alu.isZ()).isEqualTo(result == 0);
        }
      }
    }
  }

  @Test
  public void testAAdded() {
    final Alu alu = new Alu();
    alu.setF0(true).setF1(true).setEnA(true).setEnB(false).setInvA(false).setInc(false);
    for (final int a : TEST_VALUES) {
      for (final int b : TEST_VALUES) {
        alu.setA(a).setB(b).calculate();
        final int result = a;
        assertThat(alu.getOut()).isEqualTo(result);
        assertThat(alu.isN()).isEqualTo(result < 0);
        assertThat(alu.isZ()).isEqualTo(result == 0);
      }
    }
  }

  @Test
  public void testZeroAdded1() {
    final Alu alu = new Alu();
    alu.setF0(true).setF1(true).setEnA(false).setEnB(false).setInvA(true).setInc(true);
    for (final int a : TEST_VALUES) {
      for (final int b : TEST_VALUES) {
        alu.setA(a).setB(b).calculate();
        final int result = 0;
        assertThat(alu.getOut()).isEqualTo(result);
        assertThat(alu.isN()).isFalse();
        assertThat(alu.isZ()).isTrue();
      }
    }
  }

  @Test
  public void testZeroAdded2() {
    final Alu alu = new Alu();
    alu.setF0(true).setF1(true).setEnA(false).setEnB(false).setInvA(false).setInc(false);
    for (final int a : TEST_VALUES) {
      for (final int b : TEST_VALUES) {
        alu.setA(a).setB(b).calculate();
        final int result = 0;
        assertThat(alu.getOut()).isEqualTo(result);
        assertThat(alu.isN()).isFalse();
        assertThat(alu.isZ()).isTrue();
      }
    }
  }

  @Test
  public void testMinusOneAdded() {
    final Alu alu = new Alu();
    alu.setF0(true).setF1(true).setEnA(false).setEnB(false).setInvA(true).setInc(false);
    for (final int a : TEST_VALUES) {
      for (final int b : TEST_VALUES) {
        alu.setA(a).setB(b).calculate();
        final int result = -1;
        assertThat(alu.getOut()).isEqualTo(result);
        assertThat(alu.isN()).isTrue();
        assertThat(alu.isZ()).isFalse();
      }
    }
  }

  @Test
  public void testBAdded1() {
    final Alu alu = new Alu();
    alu.setF0(true).setF1(true).setEnA(false).setEnB(true).setInvA(true).setInc(true);
    for (final int a : TEST_VALUES) {
      for (final int b : TEST_VALUES) {
        alu.setA(a).setB(b).calculate();
        final int result = b;
        assertThat(alu.getOut()).isEqualTo(result);
        assertThat(alu.isN()).isEqualTo(result < 0);
        assertThat(alu.isZ()).isEqualTo(result == 0);
      }
    }
  }

  @Test
  public void testBAdded2() {
    final Alu alu = new Alu();
    alu.setF0(true).setF1(true).setEnA(false).setEnB(true).setInvA(false).setInc(false);
    for (final int a : TEST_VALUES) {
      for (final int b : TEST_VALUES) {
        alu.setA(a).setB(b).calculate();
        final int result = b;
        assertThat(alu.getOut()).isEqualTo(result);
        assertThat(alu.isN()).isEqualTo(result < 0);
        assertThat(alu.isZ()).isEqualTo(result == 0);
      }
    }
  }

  @Test
  public void testAPlusOne() {
    final Alu alu = new Alu();
    alu.setF0(true).setF1(true).setEnA(true).setEnB(false).setInvA(false).setInc(true);
    for (final int a : TEST_VALUES) {
      for (final int b : TEST_VALUES) {
        alu.setA(a).setB(b).calculate();
        final int result = a + 1;
        assertThat(alu.getOut()).isEqualTo(result);
        assertThat(alu.isN()).isEqualTo(result < 0);
        assertThat(alu.isZ()).isEqualTo(result == 0);
      }
    }
  }

  @Test
  public void testB() {
    final Alu alu = new Alu();
    alu.setF0(false).setF1(true).setEnA(false).setEnB(true).setInvA(false);
    for (final boolean inc : BOOL_VALUES) {
      for (final int a : TEST_VALUES) {
        for (final int b : TEST_VALUES) {
          alu.setA(a).setB(b).setInc(inc).calculate();
          final int result = b;
          assertThat(alu.getOut()).isEqualTo(result);
          assertThat(alu.isN()).isEqualTo(result < 0);
          assertThat(alu.isZ()).isEqualTo(result == 0);
        }
      }
    }
  }

  @Test
  public void testANeg() {
    final Alu alu = new Alu();
    alu.setF0(false).setF1(true).setEnA(true).setEnB(false).setInvA(true);
    for (final boolean inc : BOOL_VALUES) {
      for (final int a : TEST_VALUES) {
        for (final int b : TEST_VALUES) {
          alu.setA(a).setB(b).setInc(inc).calculate();
          final int result = ~a;
          assertThat(alu.getOut()).isEqualTo(result);
          assertThat(alu.isN()).isEqualTo(result < 0);
          assertThat(alu.isZ()).isEqualTo(result == 0);
        }
      }
    }
  }

  @Test
  public void testBNeg() {
    final Alu alu = new Alu();
    alu.setF0(true).setF1(false).setEnB(true);
    for (final boolean inc : BOOL_VALUES) {
      for (final boolean ena : BOOL_VALUES) {
        for (final boolean inva : BOOL_VALUES) {
          for (final int a : TEST_VALUES) {
            for (final int b : TEST_VALUES) {
              alu.setA(a).setB(b).setInc(inc).setEnA(ena).setInvA(inva).calculate();
              final int result = ~b;
              assertThat(alu.getOut()).isEqualTo(result);
              assertThat(alu.isN()).isEqualTo(result < 0);
              assertThat(alu.isZ()).isEqualTo(result == 0);
            }
          }
        }
      }
    }
  }

  @Test
  public void testAddAAndB() {
    final Alu alu = new Alu();
    alu.setF0(true).setF1(true).setEnA(true).setEnB(true).setInvA(false).setInc(false);
    for (final int a : TEST_VALUES) {
      for (final int b : TEST_VALUES) {
        alu.setA(a).setB(b).calculate();
        final int result = a + b;
        assertThat(alu.getOut()).isEqualTo(result);
        assertThat(alu.isN()).isEqualTo(result < 0);
        assertThat(alu.isZ()).isEqualTo(result == 0);
      }
    }
  }

  /**
   * Tests the performance of {@link Alu#calculate()}. Assuming that adding to numbers is the most intensive job.<br />
   * Test should run on a NetBook and do at least 1000 calculations per second.
   */
  @Test(timeout = 1000)
  public void testAddAAndB_Performance() {
    final Alu alu = new Alu();
    alu.setF0(true).setF1(true).setEnA(true).setEnB(true).setInvA(false).setInc(false);

    for (int i = 0; i < 1000; ++i) {
      alu.setA(i).setB(i).calculate();
      final int result = 2 * i;
      assertThat(alu.getOut()).isEqualTo(result);
      assertThat(alu.isN()).isEqualTo(result < 0);
      assertThat(alu.isZ()).isEqualTo(result == 0);
    }
  }

  @Test
  public void testAddAAndBAndOne() {
    final Alu alu = new Alu();
    alu.setF0(true).setF1(true).setEnA(true).setEnB(true).setInvA(false).setInc(true);
    for (final int a : TEST_VALUES) {
      for (final int b : TEST_VALUES) {
        alu.setA(a).setB(b).calculate();
        final int result = a + b + 1;
        assertThat(alu.getOut()).isEqualTo(result);
        assertThat(alu.isN()).isEqualTo(result < 0);
        assertThat(alu.isZ()).isEqualTo(result == 0);
      }
    }
  }

  @Test
  public void testAddAAndOne() {
    final Alu alu = new Alu();
    alu.setF0(true).setF1(true).setEnA(true).setEnB(false).setInvA(false).setInc(true);
    for (final int a : TEST_VALUES) {
      for (final int b : TEST_VALUES) {
        alu.setA(a).setB(b).calculate();
        final int result = a + 1;
        assertThat(alu.getOut()).isEqualTo(result);
        assertThat(alu.isN()).isEqualTo(result < 0);
        assertThat(alu.isZ()).isEqualTo(result == 0);
      }
    }
  }

  @Test
  public void testAddBAndOne() {
    final Alu alu = new Alu();
    alu.setF0(true).setF1(true).setEnA(false).setEnB(true).setInvA(false).setInc(true);
    for (final int a : TEST_VALUES) {
      for (final int b : TEST_VALUES) {
        alu.setA(a).setB(b).calculate();
        final int result = b + 1;
        assertThat(alu.getOut()).isEqualTo(result);
        assertThat(alu.isN()).isEqualTo(result < 0);
        assertThat(alu.isZ()).isEqualTo(result == 0);
      }
    }
  }

  @Test
  public void testBMinusA() {
    final Alu alu = new Alu();
    alu.setF0(true).setF1(true).setEnA(true).setEnB(true).setInvA(true).setInc(true);
    for (final int a : TEST_VALUES) {
      for (final int b : TEST_VALUES) {
        alu.setA(a).setB(b).calculate();
        final int result = b - a;
        assertThat(alu.getOut()).isEqualTo(result);
        assertThat(alu.isN()).isEqualTo(result < 0);
        assertThat(alu.isZ()).isEqualTo(result == 0);
      }
    }
  }

  @Test
  public void testBMinusAMinusOne() {
    final Alu alu = new Alu();
    alu.setF0(true).setF1(true).setEnA(true).setEnB(true).setInvA(true).setInc(false);
    for (final int a : TEST_VALUES) {
      for (final int b : TEST_VALUES) {
        alu.setA(a).setB(b).calculate();
        final int result = b - a - 1;
        assertThat(alu.getOut()).isEqualTo(result);
        assertThat(alu.isN()).isEqualTo(result < 0);
        assertThat(alu.isZ()).isEqualTo(result == 0);
      }
    }
  }

  @Test
  public void testBMinusOne() {
    final Alu alu = new Alu();
    alu.setF0(true).setF1(true).setEnA(false).setEnB(true).setInvA(true).setInc(false);
    for (final int a : TEST_VALUES) {
      for (final int b : TEST_VALUES) {
        alu.setA(a).setB(b).calculate();
        final int result = b - 1;
        assertThat(alu.getOut()).isEqualTo(result);
        assertThat(alu.isN()).isEqualTo(result < 0);
        assertThat(alu.isZ()).isEqualTo(result == 0);
      }
    }
  }

  @Test
  public void testMinusA() {
    final Alu alu = new Alu();
    alu.setF0(true).setF1(true).setEnA(true).setEnB(false).setInvA(true).setInc(true);
    for (final int a : TEST_VALUES) {
      for (final int b : TEST_VALUES) {
        alu.setA(a).setB(b).calculate();
        final int result = -a;
        assertThat(alu.getOut()).isEqualTo(result);
        assertThat(alu.isN()).isEqualTo(result < 0);
        assertThat(alu.isZ()).isEqualTo(result == 0);
      }
    }
  }

  @Test
  public void testMinusAMinusOne() {
    final Alu alu = new Alu();
    alu.setF0(true).setF1(true).setEnA(true).setEnB(false).setInvA(true).setInc(false);
    for (final int a : TEST_VALUES) {
      for (final int b : TEST_VALUES) {
        alu.setA(a).setB(b).calculate();
        final int result = -a - 1;
        assertThat(alu.getOut()).isEqualTo(result);
        assertThat(alu.isN()).isEqualTo(result < 0);
        assertThat(alu.isZ()).isEqualTo(result == 0);
      }
    }
  }

  @Test
  public void testAAndB() {
    final Alu alu = new Alu();
    alu.setF0(false).setF1(false).setEnA(true).setEnB(true).setInvA(false);
    for (final boolean inc : BOOL_VALUES) {
      for (final int a : TEST_VALUES) {
        for (final int b : TEST_VALUES) {
          alu.setA(a).setB(b).setInc(inc).calculate();
          final int result = a & b;
          assertThat(alu.getOut()).isEqualTo(result);
          assertThat(alu.isN()).isEqualTo(result < 0);
          assertThat(alu.isZ()).isEqualTo(result == 0);
        }
      }
    }
  }

  @Test
  public void testAOrB() {
    final Alu alu = new Alu();
    alu.setF0(false).setF1(true).setEnA(true).setEnB(true).setInvA(false);
    for (final boolean inc : BOOL_VALUES) {
      for (final int a : TEST_VALUES) {
        for (final int b : TEST_VALUES) {
          alu.setA(a).setB(b).setInc(inc).calculate();
          final int result = a | b;
          assertThat(alu.getOut()).isEqualTo(result);
          assertThat(alu.isN()).isEqualTo(result < 0);
          assertThat(alu.isZ()).isEqualTo(result == 0);
        }
      }
    }
  }

  @Test
  public void testZero() {
    final Alu alu = new Alu();
    alu.setF0(false).setF1(false).setEnB(false);
    for (final boolean inc : BOOL_VALUES) {
      for (final boolean ena : BOOL_VALUES) {
        for (final boolean inva : BOOL_VALUES) {
          for (final int a : TEST_VALUES) {
            for (final int b : TEST_VALUES) {
              alu.setA(a).setB(b).setInc(inc).setEnA(ena).setInvA(inva).calculate();
              assertThat(alu.getOut()).isZero();
              assertThat(alu.isN()).isFalse();
              assertThat(alu.isZ()).isTrue();
            }
          }
        }
      }
    }
  }

  @Test
  public void testOne() {
    final Alu alu = new Alu();
    alu.setF0(true).setF1(true).setEnA(false).setEnB(false).setInvA(false).setInc(true);
    for (final int a : TEST_VALUES) {
      for (final int b : TEST_VALUES) {
        alu.setA(a).setB(b).calculate();
        assertThat(alu.getOut()).isEqualTo(1);
        assertThat(alu.isN()).isFalse();
        assertThat(alu.isZ()).isFalse();
      }
    }
  }

  @Test
  public void testMinusOne() {
    final Alu alu = new Alu();
    alu.setF0(false).setF1(true).setEnA(false).setEnB(false).setInvA(true);
    for (final boolean inc : BOOL_VALUES) {
      for (final int a : TEST_VALUES) {
        for (final int b : TEST_VALUES) {
          alu.setA(a).setB(b).setInc(inc).calculate();
          assertThat(alu.getOut()).isEqualTo(-1);
          assertThat(alu.isN()).isTrue();
          assertThat(alu.isZ()).isFalse();
        }
      }
    }
  }
}
