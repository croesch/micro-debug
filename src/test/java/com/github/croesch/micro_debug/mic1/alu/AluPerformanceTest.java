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
 * Provides performance tests for {@link Alu}.
 * 
 * @author croesch
 * @since Date: Nov 19, 2011
 */
public class AluPerformanceTest extends DefaultTestCase {

  /**
   * Tests the performance of {@link Alu#calculate()}. Assuming that adding to numbers is the most intensive job.<br />
   * Test should run on a NetBook and do at least 1000 calculations per second.
   */
  @Test(timeout = 1000)
  public void testAddAAndB_Performance() {
    final Alu alu = new Alu();
    alu.setF0(true).setF1(true).setEnA(true).setEnB(true).setInvA(false).setInc(false);

    // power of netbook is good enough to calculate 1500000 per second, netbook 200000 per second
    // execution with sonar and several analysis tools is only able to execute 10000 per second
    for (int i = 0; i < 200000; ++i) {
      alu.setA(i).setB(i).calculate();
      final int result = 2 * i;
      assertThat(alu.getOut()).isEqualTo(result);
      assertThat(alu.isN()).isEqualTo(result < 0);
      assertThat(alu.isZ()).isEqualTo(result == 0);
    }
  }
}
