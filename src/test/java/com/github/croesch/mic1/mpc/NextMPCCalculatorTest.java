package com.github.croesch.mic1.mpc;

import static org.fest.assertions.Assertions.assertThat;

import java.util.BitSet;

import org.junit.Before;
import org.junit.Test;

/**
 * Provides test cases for {@link NextMPCCalculator}. It first tests all variations for control lines JMPN, JMPZ, N and
 * Z and the resulting highest bit of MPC after calculation. And then it tests the behavior of the calculator for the
 * values of JMPC.
 * 
 * @author croesch
 * @since Date: Nov 7, 2011
 */
public class NextMPCCalculatorTest {

  private NextMPCCalculator nMPCC;

  @Before
  public void setUp() {
    this.nMPCC = new NextMPCCalculator();
  }

  @Test
  public void testGetMpc_0000() {
    this.nMPCC.setJmpN(false);
    this.nMPCC.setJmpZ(false);
    this.nMPCC.setN(false);
    this.nMPCC.setZ(false);

    final BitSet addr = new BitSet(9);
    this.nMPCC.setAddr(addr);
    this.nMPCC.calculate();
    // mpc[8] should be equal to adr[8] = false
    assertThat(this.nMPCC.getMpc().get(8)).isFalse();

    addr.set(8); // adr[8] = true
    assertThat(this.nMPCC.getMpc().get(8)).isFalse();
    this.nMPCC.calculate();
    // nothing should change, because we don't want to have a reference to our BitSet in the calculator
    assertThat(this.nMPCC.getMpc().get(8)).isFalse();
    this.nMPCC.setAddr(addr);
    this.nMPCC.calculate();
    // mpc[8] should be equal to adr[8] = true
    assertThat(this.nMPCC.getMpc().get(8)).isTrue();
  }

  @Test
  public void testGetMpc_0001() {
    this.nMPCC.setJmpN(false);
    this.nMPCC.setJmpZ(false);
    this.nMPCC.setN(false);
    this.nMPCC.setZ(true);

    final BitSet addr = new BitSet(9);
    this.nMPCC.setAddr(addr);
    this.nMPCC.calculate();
    // mpc[8] should be equal to adr[8] = false
    assertThat(this.nMPCC.getMpc().get(8)).isFalse();

    addr.set(8); // adr[8] = true
    assertThat(this.nMPCC.getMpc().get(8)).isFalse();
    this.nMPCC.calculate();
    // nothing should change, because we don't want to have a reference to our BitSet in the calculator
    assertThat(this.nMPCC.getMpc().get(8)).isFalse();
    this.nMPCC.setAddr(addr);
    this.nMPCC.calculate();
    // mpc[8] should be equal to adr[8] = true
    assertThat(this.nMPCC.getMpc().get(8)).isTrue();
  }

  @Test
  public void testGetMpc_0010() {
    this.nMPCC.setJmpN(false);
    this.nMPCC.setJmpZ(false);
    this.nMPCC.setN(true);
    this.nMPCC.setZ(false);

    final BitSet addr = new BitSet(9);
    this.nMPCC.setAddr(addr);
    this.nMPCC.calculate();
    // mpc[8] should be equal to adr[8] = false
    assertThat(this.nMPCC.getMpc().get(8)).isFalse();

    addr.set(8); // adr[8] = true
    assertThat(this.nMPCC.getMpc().get(8)).isFalse();
    this.nMPCC.calculate();
    // nothing should change, because we don't want to have a reference to our BitSet in the calculator
    assertThat(this.nMPCC.getMpc().get(8)).isFalse();
    this.nMPCC.setAddr(addr);
    this.nMPCC.calculate();
    // mpc[8] should be equal to adr[8] = true
    assertThat(this.nMPCC.getMpc().get(8)).isTrue();
  }

  @Test
  public void testGetMpc_0011() {
    this.nMPCC.setJmpN(false);
    this.nMPCC.setJmpZ(false);
    this.nMPCC.setN(true);
    this.nMPCC.setZ(true);

    final BitSet addr = new BitSet(9);
    this.nMPCC.setAddr(addr);
    this.nMPCC.calculate();
    // mpc[8] should be equal to adr[8] = false
    assertThat(this.nMPCC.getMpc().get(8)).isFalse();

    addr.set(8); // adr[8] = true
    assertThat(this.nMPCC.getMpc().get(8)).isFalse();
    this.nMPCC.calculate();
    // nothing should change, because we don't want to have a reference to our BitSet in the calculator
    assertThat(this.nMPCC.getMpc().get(8)).isFalse();
    this.nMPCC.setAddr(addr);
    this.nMPCC.calculate();
    // mpc[8] should be equal to adr[8] = true
    assertThat(this.nMPCC.getMpc().get(8)).isTrue();
  }

  @Test
  public void testGetMpc_0100() {
    this.nMPCC.setJmpN(false);
    this.nMPCC.setJmpZ(true);
    this.nMPCC.setN(false);
    this.nMPCC.setZ(false);

    final BitSet addr = new BitSet(9);
    this.nMPCC.setAddr(addr);
    this.nMPCC.calculate();
    // mpc[8] should be equal to adr[8] = false
    assertThat(this.nMPCC.getMpc().get(8)).isFalse();

    addr.set(8); // adr[8] = true
    assertThat(this.nMPCC.getMpc().get(8)).isFalse();
    this.nMPCC.calculate();
    // nothing should change, because we don't want to have a reference to our BitSet in the calculator
    assertThat(this.nMPCC.getMpc().get(8)).isFalse();
    this.nMPCC.setAddr(addr);
    this.nMPCC.calculate();
    // mpc[8] should be equal to adr[8] = true
    assertThat(this.nMPCC.getMpc().get(8)).isTrue();
  }

  @Test
  public void testGetMpc_0101() {
    this.nMPCC.setJmpN(false);
    this.nMPCC.setJmpZ(true);
    this.nMPCC.setN(false);
    this.nMPCC.setZ(true);

    final BitSet addr = new BitSet(9);
    this.nMPCC.setAddr(addr);
    this.nMPCC.calculate();
    // mpc[8] should be true, cause JMPZ and Z
    assertThat(this.nMPCC.getMpc().get(8)).isTrue();

    addr.set(8); // adr[8] = true
    assertThat(this.nMPCC.getMpc().get(8)).isTrue();
    this.nMPCC.calculate();
    // nothing should change, because we don't want to have a reference to our BitSet in the calculator
    assertThat(this.nMPCC.getMpc().get(8)).isTrue();
    this.nMPCC.setAddr(addr);
    this.nMPCC.calculate();
    // mpc[8] should be true, cause JMPZ and Z
    assertThat(this.nMPCC.getMpc().get(8)).isTrue();
  }

  @Test
  public void testGetMpc_0110() {
    this.nMPCC.setJmpN(false);
    this.nMPCC.setJmpZ(true);
    this.nMPCC.setN(true);
    this.nMPCC.setZ(false);

    final BitSet addr = new BitSet(9);
    this.nMPCC.setAddr(addr);
    this.nMPCC.calculate();
    // mpc[8] should be equal to adr[8] = false
    assertThat(this.nMPCC.getMpc().get(8)).isFalse();

    addr.set(8); // adr[8] = true
    assertThat(this.nMPCC.getMpc().get(8)).isFalse();
    this.nMPCC.calculate();
    // nothing should change, because we don't want to have a reference to our BitSet in the calculator
    assertThat(this.nMPCC.getMpc().get(8)).isFalse();
    this.nMPCC.setAddr(addr);
    this.nMPCC.calculate();
    // mpc[8] should be equal to adr[8] = true
    assertThat(this.nMPCC.getMpc().get(8)).isTrue();
  }

  @Test
  public void testGetMpc_0111() {
    this.nMPCC.setJmpN(false);
    this.nMPCC.setJmpZ(true);
    this.nMPCC.setN(true);
    this.nMPCC.setZ(true);

    final BitSet addr = new BitSet(9);
    this.nMPCC.setAddr(addr);
    this.nMPCC.calculate();
    // mpc[8] should be true, cause JMPZ and Z
    assertThat(this.nMPCC.getMpc().get(8)).isTrue();

    addr.set(8); // adr[8] = true
    assertThat(this.nMPCC.getMpc().get(8)).isTrue();
    this.nMPCC.calculate();
    // nothing should change, because we don't want to have a reference to our BitSet in the calculator
    assertThat(this.nMPCC.getMpc().get(8)).isTrue();
    this.nMPCC.setAddr(addr);
    this.nMPCC.calculate();
    // mpc[8] should be true, cause JMPZ and Z
    assertThat(this.nMPCC.getMpc().get(8)).isTrue();
  }

  @Test
  public void testGetMpc_1000() {
    this.nMPCC.setJmpN(true);
    this.nMPCC.setJmpZ(false);
    this.nMPCC.setN(false);
    this.nMPCC.setZ(false);

    final BitSet addr = new BitSet(9);
    this.nMPCC.setAddr(addr);
    this.nMPCC.calculate();
    // mpc[8] should be equal to adr[8] = false
    assertThat(this.nMPCC.getMpc().get(8)).isFalse();

    addr.set(8); // adr[8] = true
    assertThat(this.nMPCC.getMpc().get(8)).isFalse();
    this.nMPCC.calculate();
    // nothing should change, because we don't want to have a reference to our BitSet in the calculator
    assertThat(this.nMPCC.getMpc().get(8)).isFalse();
    this.nMPCC.setAddr(addr);
    this.nMPCC.calculate();
    // mpc[8] should be equal to adr[8] = true
    assertThat(this.nMPCC.getMpc().get(8)).isTrue();
  }

  @Test
  public void testGetMpc_1001() {
    this.nMPCC.setJmpN(true);
    this.nMPCC.setJmpZ(false);
    this.nMPCC.setN(false);
    this.nMPCC.setZ(true);

    final BitSet addr = new BitSet(9);
    this.nMPCC.setAddr(addr);
    this.nMPCC.calculate();
    // mpc[8] should be equal to adr[8] = false
    assertThat(this.nMPCC.getMpc().get(8)).isFalse();

    addr.set(8); // adr[8] = true
    assertThat(this.nMPCC.getMpc().get(8)).isFalse();
    this.nMPCC.calculate();
    // nothing should change, because we don't want to have a reference to our BitSet in the calculator
    assertThat(this.nMPCC.getMpc().get(8)).isFalse();
    this.nMPCC.setAddr(addr);
    this.nMPCC.calculate();
    // mpc[8] should be equal to adr[8] = true
    assertThat(this.nMPCC.getMpc().get(8)).isTrue();
  }

  @Test
  public void testGetMpc_1010() {
    this.nMPCC.setJmpN(true);
    this.nMPCC.setJmpZ(false);
    this.nMPCC.setN(true);
    this.nMPCC.setZ(false);

    final BitSet addr = new BitSet(9);
    this.nMPCC.setAddr(addr);
    this.nMPCC.calculate();
    // mpc[8] should be true, cause JMPN and N
    assertThat(this.nMPCC.getMpc().get(8)).isTrue();

    addr.set(8); // adr[8] = true
    assertThat(this.nMPCC.getMpc().get(8)).isTrue();
    this.nMPCC.calculate();
    // nothing should change, because we don't want to have a reference to our BitSet in the calculator
    assertThat(this.nMPCC.getMpc().get(8)).isTrue();
    this.nMPCC.setAddr(addr);
    this.nMPCC.calculate();
    // mpc[8] should be true, cause JMPN and N
    assertThat(this.nMPCC.getMpc().get(8)).isTrue();
  }

  @Test
  public void testGetMpc_1011() {
    this.nMPCC.setJmpN(true);
    this.nMPCC.setJmpZ(false);
    this.nMPCC.setN(true);
    this.nMPCC.setZ(true);

    final BitSet addr = new BitSet(9);
    this.nMPCC.setAddr(addr);
    this.nMPCC.calculate();
    // mpc[8] should be true, cause JMPN and N
    assertThat(this.nMPCC.getMpc().get(8)).isTrue();

    addr.set(8); // adr[8] = true
    assertThat(this.nMPCC.getMpc().get(8)).isTrue();
    this.nMPCC.calculate();
    // nothing should change, because we don't want to have a reference to our BitSet in the calculator
    assertThat(this.nMPCC.getMpc().get(8)).isTrue();
    this.nMPCC.setAddr(addr);
    this.nMPCC.calculate();
    // mpc[8] should be true, cause JMPN and N
    assertThat(this.nMPCC.getMpc().get(8)).isTrue();
  }

  @Test
  public void testGetMpc_1100() {
    this.nMPCC.setJmpN(true);
    this.nMPCC.setJmpZ(true);
    this.nMPCC.setN(false);
    this.nMPCC.setZ(false);

    final BitSet addr = new BitSet(9);
    this.nMPCC.setAddr(addr);
    this.nMPCC.calculate();
    // mpc[8] should be equal to adr[8] = false
    assertThat(this.nMPCC.getMpc().get(8)).isFalse();

    addr.set(8); // adr[8] = true
    assertThat(this.nMPCC.getMpc().get(8)).isFalse();
    this.nMPCC.calculate();
    // nothing should change, because we don't want to have a reference to our BitSet in the calculator
    assertThat(this.nMPCC.getMpc().get(8)).isFalse();
    this.nMPCC.setAddr(addr);
    this.nMPCC.calculate();
    // mpc[8] should be equal to adr[8] = true
    assertThat(this.nMPCC.getMpc().get(8)).isTrue();
  }

  @Test
  public void testGetMpc_1101() {
    this.nMPCC.setJmpN(true);
    this.nMPCC.setJmpZ(true);
    this.nMPCC.setN(false);
    this.nMPCC.setZ(true);

    final BitSet addr = new BitSet(9);
    this.nMPCC.setAddr(addr);
    this.nMPCC.calculate();
    // mpc[8] should be true, cause JMPZ and Z
    assertThat(this.nMPCC.getMpc().get(8)).isTrue();

    addr.set(8); // adr[8] = true
    assertThat(this.nMPCC.getMpc().get(8)).isTrue();
    this.nMPCC.calculate();
    // nothing should change, because we don't want to have a reference to our BitSet in the calculator
    assertThat(this.nMPCC.getMpc().get(8)).isTrue();
    this.nMPCC.setAddr(addr);
    this.nMPCC.calculate();
    // mpc[8] should be true, cause JMPZ and Z
    assertThat(this.nMPCC.getMpc().get(8)).isTrue();
  }

  @Test
  public void testGetMpc_1110() {
    this.nMPCC.setJmpN(true);
    this.nMPCC.setJmpZ(true);
    this.nMPCC.setN(true);
    this.nMPCC.setZ(false);

    final BitSet addr = new BitSet(9);
    this.nMPCC.setAddr(addr);
    this.nMPCC.calculate();
    // mpc[8] should be true, cause JMPN and N
    assertThat(this.nMPCC.getMpc().get(8)).isTrue();

    addr.set(8); // adr[8] = true
    assertThat(this.nMPCC.getMpc().get(8)).isTrue();
    this.nMPCC.calculate();
    // nothing should change, because we don't want to have a reference to our BitSet in the calculator
    assertThat(this.nMPCC.getMpc().get(8)).isTrue();
    this.nMPCC.setAddr(addr);
    this.nMPCC.calculate();
    // mpc[8] should be true, cause JMPN and N
    assertThat(this.nMPCC.getMpc().get(8)).isTrue();
  }

  @Test
  public void testGetMpc_1111() {
    this.nMPCC.setJmpN(true);
    this.nMPCC.setJmpZ(true);
    this.nMPCC.setN(true);
    this.nMPCC.setZ(true);

    final BitSet addr = new BitSet(9);
    this.nMPCC.setAddr(addr);
    this.nMPCC.calculate();
    // mpc[8] should be true, cause JMPZ and Z (and JMPN and N)
    assertThat(this.nMPCC.getMpc().get(8)).isTrue();

    addr.set(8); // adr[8] = true
    assertThat(this.nMPCC.getMpc().get(8)).isTrue();
    this.nMPCC.calculate();
    // nothing should change, because we don't want to have a reference to our BitSet in the calculator
    assertThat(this.nMPCC.getMpc().get(8)).isTrue();
    this.nMPCC.setAddr(addr);
    this.nMPCC.calculate();
    // mpc[8] should be true, cause JMPZ and Z (and JMPN and N)
    assertThat(this.nMPCC.getMpc().get(8)).isTrue();
  }

  @Test
  public void testGetMpc_Bits0To7_JMPC() {
    final BitSet addr = new BitSet(9); // [0]10101010
    addr.set(1);
    addr.set(3);
    addr.set(5);
    addr.set(7);
    final byte mbr = 0x55; // 01010101

    this.nMPCC.setJmpC(true);
    this.nMPCC.setAddr(addr);
    this.nMPCC.setMbr(mbr);

    for (int i = 0; i < 2; ++i) {
      for (int j = 0; j < 2; ++j) {
        for (int k = 0; k < 2; ++k) {
          for (int l = 0; l < 2; ++l) {
            this.nMPCC.setJmpN(i == 0);
            this.nMPCC.setJmpZ(j == 0);
            this.nMPCC.setN(k == 0);
            this.nMPCC.setZ(l == 0);

            this.nMPCC.calculate();

            for (int m = 0; m < 8; ++m) {
              assertThat(this.nMPCC.getMpc().get(m)).isTrue();
            }
          }
        }
      }
    }
  }

  @Test
  public void testGetMpc_Bits0To7_NotJMPC() {
    final BitSet addr = new BitSet(9); // [0]10101010
    addr.set(1);
    addr.set(3);
    addr.set(5);
    addr.set(7);
    final byte mbr = 0x55; // 01010101

    this.nMPCC.setJmpC(false);
    this.nMPCC.setAddr(addr);
    this.nMPCC.setMbr(mbr);

    for (int i = 0; i < 2; ++i) {
      for (int j = 0; j < 2; ++j) {
        for (int k = 0; k < 2; ++k) {
          for (int l = 0; l < 2; ++l) {
            this.nMPCC.setJmpN(i == 0);
            this.nMPCC.setJmpZ(j == 0);
            this.nMPCC.setN(k == 0);
            this.nMPCC.setZ(l == 0);

            this.nMPCC.calculate();

            for (int m = 0; m < 8; ++m) {
              assertThat(this.nMPCC.getMpc().get(m)).isEqualTo(m % 2 != 0);
            }
          }
        }
      }
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSetAddr_IAE1() {
    final BitSet newAddr = new BitSet();
    newAddr.set(9);
    this.nMPCC.setAddr(newAddr);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSetAddr_IAE2() {
    final BitSet newAddr = new BitSet();
    newAddr.set(10);
    this.nMPCC.setAddr(newAddr);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSetAddr_IAE3() {
    final BitSet newAddr = new BitSet();
    newAddr.set(100);
    this.nMPCC.setAddr(newAddr);
  }
}
