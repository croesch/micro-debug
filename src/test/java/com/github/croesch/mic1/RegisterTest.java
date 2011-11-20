package com.github.croesch.mic1;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

import com.github.croesch.TestUtil;
import com.github.croesch.mic1.register.Register;

/**
 * Provides test cases for {@link Register}.
 * 
 * @author croesch
 * @since Date: Nov 19, 2011
 */
public class RegisterTest {

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

  @Before
  public void setUp() {
    for (final Register r : Register.values()) {
      r.setValue(0);
    }
  }

  @Test
  public void testSetValue() {
    TestUtil.printMethodName();

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
          TestUtil.printStep();
        }
        old = r;
        TestUtil.printLoopEnd();
      }
    }

    TestUtil.printEndOfMethod();
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
