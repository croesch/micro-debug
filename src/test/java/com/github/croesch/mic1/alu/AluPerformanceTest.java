package com.github.croesch.mic1.alu;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

/**
 * Provides performance tests for {@link Alu}.
 * 
 * @author croesch
 * @since Date: Nov 19, 2011
 */
public class AluPerformanceTest {

  /**
   * Tests the performance of {@link Alu#calculate()}. Assuming that adding to numbers is the most intensive job.<br />
   * Test should run on a NetBook and do at least 1000 calculations per second.
   */
  @Test(timeout = 1000)
  public void testAddAAndB_Performance() {
    final Alu alu = new Alu();
    alu.setF0(true).setF1(true).setEnA(true).setEnB(true).setInvA(false).setInC(false);

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
