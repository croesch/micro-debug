package com.github.croesch.mic1.shifter;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

import com.github.croesch.TestUtil;

/**
 * Provides test cases for {@link Shifter}. Basically tests output with variations of the input control lines:<br>
 * <table>
 * <tr>
 * <th>SLL8</th>
 * <th>SRA1</th>
 * <th>action</th>
 * </tr>
 * <tr>
 * <td>0</td>
 * <td>0</td>
 * <td>do nothing</td>
 * </tr>
 * <tr>
 * <td>0</td>
 * <td>1</td>
 * <td>shift one bit arithmetically to the right</td>
 * </tr>
 * <tr>
 * <td>1</td>
 * <td>0</td>
 * <td>shift one bit logical to the left</td>
 * </tr>
 * <tr>
 * <td>1</td>
 * <td>1</td>
 * <td><i>not defined</i></td>
 * </tr>
 * </table>
 * 
 * @author croesch
 * @since Date: Nov 7, 2011
 */
public class ShifterTest {

  @Test
  public void testGetOutput() {
    TestUtil.printMethodName();

    final int[] in = new int[] { Integer.MIN_VALUE, -17, -1274839, 0, Integer.MAX_VALUE };
    final int[] out = new int[] { Integer.MIN_VALUE, -17, -1274839, 0, Integer.MAX_VALUE };

    final Shifter s = new Shifter();
    for (int i = 0; i < in.length; ++i) {
      s.setInput(in[i]);

      // be sure that one iteration has been done..
      if (i > 0) {
        // output is still old result
        assertThat(s.getOutput()).isEqualTo(out[i - 1]);
      }

      s.calculate();
      // System.out.println("shifttest: " + in[i] + " = " + out[i] + "; result=" + s.getOutput());
      assertThat(s.getOutput()).isEqualTo(out[i]);

      TestUtil.printStep();
    }

    TestUtil.printEndOfMethod();
  }

  @Test
  public void testGetOutput_NoShift() {
    TestUtil.printMethodName();

    final Shifter s = new Shifter();
    s.setSLL8(false);
    s.setSRA1(false);
    final int[] in = new int[] { Integer.MIN_VALUE, -17, -1274839, 0, Integer.MAX_VALUE };
    final int[] out = new int[] { Integer.MIN_VALUE, -17, -1274839, 0, Integer.MAX_VALUE };

    for (int i = 0; i < in.length; ++i) {
      s.setInput(in[i]);

      // be sure that one iteration has been done..
      if (i > 0) {
        // output is still old result
        assertThat(s.getOutput()).isEqualTo(out[i - 1]);
      }

      s.calculate();
      // System.out.println("shifttest: " + in[i] + " = " + out[i] + "; result=" + s.getOutput());
      assertThat(s.getOutput()).isEqualTo(out[i]);

      TestUtil.printStep();
    }
    TestUtil.printEndOfMethod();
  }

  @Test
  public void testGetOutput_ShiftLeft() {
    TestUtil.printMethodName();

    final Shifter s = new Shifter();
    s.setSLL8(true);
    s.setSRA1(false);
    final int[] in = new int[] { Integer.MIN_VALUE, 0x12345678, 0x900abcde, 0, 0x7fffffff, 1 };
    final int[] out = new int[] { 0x00000000, 0x34567800, 0x0abcde00, 0, 0xffffff00, 0x00000100 };

    for (int i = 0; i < in.length; ++i) {
      s.setInput(in[i]);

      // be sure that one iteration has been done..
      if (i > 0) {
        // output is still old result
        assertThat(s.getOutput()).isEqualTo(out[i - 1]);
      }

      s.calculate();
      // System.out.println("shifttest: " + in[i] + " << 8 = " + out[i] + "; result=" + s.getOutput());
      assertThat(s.getOutput()).isEqualTo(out[i]);

      TestUtil.printStep();
    }
    TestUtil.printEndOfMethod();
  }

  @Test
  public void testGetOutput_ShiftRight() {
    TestUtil.printMethodName();

    final Shifter s = new Shifter();
    s.setSLL8(false);
    s.setSRA1(true);
    final int[] in = new int[] { Integer.MIN_VALUE, 0x12345678, 0x900abcde, 2, 0x7fffffff, 1 };
    final int[] out = new int[] { 0xc0000000, 0x091a2b3c, 0xc8055e6f, 1, 0x3fffffff, 0x00000000 };

    for (int i = 0; i < in.length; ++i) {
      s.setInput(in[i]);

      // be sure that one iteration has been done..
      if (i > 0) {
        // output is still old result
        assertThat(s.getOutput()).isEqualTo(out[i - 1]);
      }

      s.calculate();
      // System.out.println("shifttest: " + in[i] + " >> 1 = " + out[i] + "; result=" + s.getOutput());
      assertThat(s.getOutput()).isEqualTo(out[i]);

      TestUtil.printStep();
    }
    TestUtil.printEndOfMethod();
  }

  @Test(expected = IllegalStateException.class)
  public void testGetOutput_ShiftBoth() {
    TestUtil.printlnMethodName();

    final Shifter s = new Shifter();
    // not allowed - shift in both directions!
    s.setSLL8(true);
    s.setSRA1(true);
    s.setInput(4711);
    // throws exception
    s.calculate();
  }
}
