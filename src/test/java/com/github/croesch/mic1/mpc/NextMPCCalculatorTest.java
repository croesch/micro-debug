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
package com.github.croesch.mic1.mpc;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

import com.github.croesch.DefaultTestCase;
import com.github.croesch.TestUtil;

/**
 * Provides test cases for {@link NextMPCCalculator}. It first tests all variations for control lines JMPN, JMPZ, N and
 * Z and the resulting highest bit of MPC after calculation. And then it tests the behavior of the calculator for the
 * values of JMPC.
 * 
 * @author croesch
 * @since Date: Nov 7, 2011
 */
public class NextMPCCalculatorTest extends DefaultTestCase {

  private NextMPCCalculator nMPCC;

  @Override
  protected void setUpDetails() {
    this.nMPCC = new NextMPCCalculator();
  }

  @Test
  public void testGetMpc_0000() {
    this.nMPCC.setJmpN(false);
    this.nMPCC.setJmpZ(false);
    this.nMPCC.setN(false);
    this.nMPCC.setZ(false);

    int addr = 0;
    this.nMPCC.setAddr(addr);
    this.nMPCC.calculate();
    // mpc[8] should be equal to adr[8] = false
    assertThat(this.nMPCC.getMpc()).isZero();

    addr = 0x100;
    this.nMPCC.setAddr(addr);
    this.nMPCC.calculate();
    // mpc[8] should be equal to adr[8] = true
    assertThat(this.nMPCC.getMpc()).isEqualTo(0x100);
  }

  @Test
  public void testGetMpc_0001() {
    this.nMPCC.setJmpN(false);
    this.nMPCC.setJmpZ(false);
    this.nMPCC.setN(false);
    this.nMPCC.setZ(true);

    int addr = 0;
    this.nMPCC.setAddr(addr);
    this.nMPCC.calculate();
    // mpc[8] should be equal to adr[8] = false
    assertThat(this.nMPCC.getMpc()).isZero();

    addr = 0x100;
    this.nMPCC.setAddr(addr);
    this.nMPCC.calculate();
    // mpc[8] should be equal to adr[8] = true
    assertThat(this.nMPCC.getMpc()).isEqualTo(0x100);
  }

  @Test
  public void testGetMpc_0010() {
    this.nMPCC.setJmpN(false);
    this.nMPCC.setJmpZ(false);
    this.nMPCC.setN(true);
    this.nMPCC.setZ(false);

    int addr = 0x200; // 1->0<-_0000_0000
    this.nMPCC.setAddr(addr);
    this.nMPCC.calculate();
    // mpc[8] should be equal to adr[8] = false
    assertThat(this.nMPCC.getMpc()).isZero();

    addr = 0x1FF;// ->1<-_1111_1111
    this.nMPCC.setAddr(addr);
    this.nMPCC.calculate();
    // mpc[8] should be equal to adr[8] = true
    assertThat(this.nMPCC.getMpc()).isEqualTo(0x1FF);
  }

  @Test
  public void testGetMpc_0011() {
    this.nMPCC.setJmpN(false);
    this.nMPCC.setJmpZ(false);
    this.nMPCC.setN(true);
    this.nMPCC.setZ(true);

    int addr = 0x6ab; // 11->0<-_0000_0000
    this.nMPCC.setAddr(addr);
    this.nMPCC.calculate();
    // mpc[8] should be equal to adr[8] = false
    assertThat(this.nMPCC.getMpc()).isEqualTo(0xab);

    addr = 0x7FF; // 11->1<-_1111_1111
    this.nMPCC.setAddr(addr);
    this.nMPCC.calculate();
    // mpc[8] should be equal to adr[8] = true
    assertThat(this.nMPCC.getMpc()).isEqualTo(0x1FF);
  }

  @Test
  public void testGetMpc_0100() {
    this.nMPCC.setJmpN(false);
    this.nMPCC.setJmpZ(true);
    this.nMPCC.setN(false);
    this.nMPCC.setZ(false);

    int addr = 0;
    this.nMPCC.setAddr(addr);
    this.nMPCC.calculate();
    // mpc[8] should be equal to adr[8] = false
    assertThat(this.nMPCC.getMpc()).isZero();

    addr = 0x100;
    this.nMPCC.setAddr(addr);
    this.nMPCC.calculate();
    // mpc[8] should be equal to adr[8] = true
    assertThat(this.nMPCC.getMpc()).isEqualTo(0x100);
  }

  @Test
  public void testGetMpc_0101() {
    this.nMPCC.setJmpN(false);
    this.nMPCC.setJmpZ(true);
    this.nMPCC.setN(false);
    this.nMPCC.setZ(true);

    int addr = 0;
    this.nMPCC.setAddr(addr);
    this.nMPCC.calculate();
    // mpc[8] should be true, cause JMPZ and Z
    assertThat(this.nMPCC.getMpc()).isEqualTo(0x100);

    addr = 0x100;
    this.nMPCC.setAddr(addr);
    this.nMPCC.calculate();
    // mpc[8] should be true, cause JMPZ and Z
    assertThat(this.nMPCC.getMpc()).isEqualTo(0x100);
  }

  @Test
  public void testGetMpc_0110() {
    this.nMPCC.setJmpN(false);
    this.nMPCC.setJmpZ(true);
    this.nMPCC.setN(true);
    this.nMPCC.setZ(false);

    int addr = 0;
    this.nMPCC.setAddr(addr);
    this.nMPCC.calculate();
    // mpc[8] should be equal to adr[8] = false
    assertThat(this.nMPCC.getMpc()).isZero();

    addr = 0x100;
    this.nMPCC.setAddr(addr);
    this.nMPCC.calculate();
    // mpc[8] should be equal to adr[8] = true
    assertThat(this.nMPCC.getMpc()).isEqualTo(0x100);
  }

  @Test
  public void testGetMpc_0111() {
    this.nMPCC.setJmpN(false);
    this.nMPCC.setJmpZ(true);
    this.nMPCC.setN(true);
    this.nMPCC.setZ(true);

    int addr = 0;
    this.nMPCC.setAddr(addr);
    this.nMPCC.calculate();
    // mpc[8] should be true, cause JMPZ and Z
    assertThat(this.nMPCC.getMpc()).isEqualTo(0x100);

    addr = 0x100;
    this.nMPCC.setAddr(addr);
    this.nMPCC.calculate();
    // mpc[8] should be true, cause JMPZ and Z
    assertThat(this.nMPCC.getMpc()).isEqualTo(0x100);
  }

  @Test
  public void testGetMpc_1000() {
    this.nMPCC.setJmpN(true);
    this.nMPCC.setJmpZ(false);
    this.nMPCC.setN(false);
    this.nMPCC.setZ(false);

    int addr = 0;
    this.nMPCC.setAddr(addr);
    this.nMPCC.calculate();
    // mpc[8] should be equal to adr[8] = false
    assertThat(this.nMPCC.getMpc()).isZero();

    addr = 0x100;
    this.nMPCC.setAddr(addr);
    this.nMPCC.calculate();
    // mpc[8] should be equal to adr[8] = true
    assertThat(this.nMPCC.getMpc()).isEqualTo(0x100);
  }

  @Test
  public void testGetMpc_1001() {
    this.nMPCC.setJmpN(true);
    this.nMPCC.setJmpZ(false);
    this.nMPCC.setN(false);
    this.nMPCC.setZ(true);

    int addr = 0;
    this.nMPCC.setAddr(addr);
    this.nMPCC.calculate();
    // mpc[8] should be equal to adr[8] = false
    assertThat(this.nMPCC.getMpc()).isZero();

    addr = 0x100;
    this.nMPCC.setAddr(addr);
    this.nMPCC.calculate();
    // mpc[8] should be equal to adr[8] = true
    assertThat(this.nMPCC.getMpc()).isEqualTo(0x100);
  }

  @Test
  public void testGetMpc_1010() {
    this.nMPCC.setJmpN(true);
    this.nMPCC.setJmpZ(false);
    this.nMPCC.setN(true);
    this.nMPCC.setZ(false);

    int addr = 0;
    this.nMPCC.setAddr(addr);
    this.nMPCC.calculate();
    // mpc[8] should be true, cause JMPN and N
    assertThat(this.nMPCC.getMpc()).isEqualTo(0x100);

    addr = 0x100;
    this.nMPCC.setAddr(addr);
    this.nMPCC.calculate();
    // mpc[8] should be true, cause JMPN and N
    assertThat(this.nMPCC.getMpc()).isEqualTo(0x100);
  }

  @Test
  public void testGetMpc_1011() {
    this.nMPCC.setJmpN(true);
    this.nMPCC.setJmpZ(false);
    this.nMPCC.setN(true);
    this.nMPCC.setZ(true);

    int addr = 0;
    this.nMPCC.setAddr(addr);
    this.nMPCC.calculate();
    // mpc[8] should be true, cause JMPN and N
    assertThat(this.nMPCC.getMpc()).isEqualTo(0x100);

    addr = 0x100;
    this.nMPCC.setAddr(addr);
    this.nMPCC.calculate();
    // mpc[8] should be true, cause JMPN and N
    assertThat(this.nMPCC.getMpc()).isEqualTo(0x100);
  }

  @Test
  public void testGetMpc_1100() {
    this.nMPCC.setJmpN(true);
    this.nMPCC.setJmpZ(true);
    this.nMPCC.setN(false);
    this.nMPCC.setZ(false);

    int addr = 0;
    this.nMPCC.setAddr(addr);
    this.nMPCC.calculate();
    // mpc[8] should be equal to adr[8] = false
    assertThat(this.nMPCC.getMpc()).isZero();

    addr = 0x100;
    this.nMPCC.setAddr(addr);
    this.nMPCC.calculate();
    // mpc[8] should be equal to adr[8] = true
    assertThat(this.nMPCC.getMpc()).isEqualTo(0x100);
  }

  @Test
  public void testGetMpc_1101() {
    this.nMPCC.setJmpN(true);
    this.nMPCC.setJmpZ(true);
    this.nMPCC.setN(false);
    this.nMPCC.setZ(true);

    int addr = 0;
    this.nMPCC.setAddr(addr);
    this.nMPCC.calculate();
    // mpc[8] should be true, cause JMPZ and Z
    assertThat(this.nMPCC.getMpc()).isEqualTo(0x100);

    addr = 0x100;
    this.nMPCC.setAddr(addr);
    this.nMPCC.calculate();
    // mpc[8] should be true, cause JMPZ and Z
    assertThat(this.nMPCC.getMpc()).isEqualTo(0x100);
  }

  @Test
  public void testGetMpc_1110() {
    this.nMPCC.setJmpN(true);
    this.nMPCC.setJmpZ(true);
    this.nMPCC.setN(true);
    this.nMPCC.setZ(false);

    int addr = 0;
    this.nMPCC.setAddr(addr);
    this.nMPCC.calculate();
    // mpc[8] should be true, cause JMPN and N
    assertThat(this.nMPCC.getMpc()).isEqualTo(0x100);

    addr = 0x100;
    this.nMPCC.setAddr(addr);
    this.nMPCC.calculate();
    // mpc[8] should be true, cause JMPN and N
    assertThat(this.nMPCC.getMpc()).isEqualTo(0x100);
  }

  @Test
  public void testGetMpc_1111() {
    this.nMPCC.setJmpN(true);
    this.nMPCC.setJmpZ(true);
    this.nMPCC.setN(true);
    this.nMPCC.setZ(true);

    int addr = 0;
    this.nMPCC.setAddr(addr);
    this.nMPCC.calculate();
    // mpc[8] should be true, cause JMPZ and Z (and JMPN and N)
    assertThat(this.nMPCC.getMpc()).isEqualTo(0x100);

    addr = 0x100;
    this.nMPCC.setAddr(addr);
    this.nMPCC.calculate();
    // mpc[8] should be true, cause JMPZ and Z (and JMPN and N)
    assertThat(this.nMPCC.getMpc()).isEqualTo(0x100);
  }

  @Test
  public void testGetMpc_Bits0To7_JMPC() {
    TestUtil.printMethodName();

    final int addr = 0xAA; // [0]1010_1010
    final byte mbr = 0x55; // 0101_0101

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

            if (i == 0 && k == 0 || j == 0 && l == 0) {
              // N && jmpN || Z && jmpZ
              assertThat(this.nMPCC.getMpc()).isEqualTo(0x1FF);
            } else {
              assertThat(this.nMPCC.getMpc()).isEqualTo(0xFF);
            }
            TestUtil.printStep();
          }
          TestUtil.printLoopEnd();
        }
        TestUtil.printLoopEnd();
      }
      TestUtil.printLoopEnd();
    }
    TestUtil.printEndOfMethod();
  }

  @Test
  public void testGetMpc_Bits0To7_NotJMPC() {
    TestUtil.printMethodName();

    final int addr = 0xAA; // [0]_1010_1010
    final byte mbr = 0x55; // 0101_0101

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

            if (i == 0 && k == 0 || j == 0 && l == 0) {
              // N && jmpN || Z && jmpZ
              assertThat(this.nMPCC.getMpc()).isEqualTo(0x1AA);
            } else {
              assertThat(this.nMPCC.getMpc()).isEqualTo(0xAA);
            }
            TestUtil.printStep();
          }
          TestUtil.printLoopEnd();
        }
        TestUtil.printLoopEnd();
      }
      TestUtil.printLoopEnd();
    }
    TestUtil.printEndOfMethod();
  }

  @Test
  public void testGetMpc_MaximumNineBits() {
    TestUtil.printMethodName();

    final int addr = 0xFFFFFFFF; // [0]_1010_1010
    final byte mbr = (byte) 0xFF; // 0101_0101

    this.nMPCC.setAddr(addr);
    this.nMPCC.setMbr(mbr);

    for (int h = 0; h < 2; ++h) {
      for (int i = 0; i < 2; ++i) {
        for (int j = 0; j < 2; ++j) {
          for (int k = 0; k < 2; ++k) {
            for (int l = 0; l < 2; ++l) {
              this.nMPCC.setJmpC(h == 0);
              this.nMPCC.setJmpN(i == 0);
              this.nMPCC.setJmpZ(j == 0);
              this.nMPCC.setN(k == 0);
              this.nMPCC.setZ(l == 0);

              this.nMPCC.calculate();

              assertThat(this.nMPCC.getMpc() & 0xFFFFFE00).isZero();
              TestUtil.printStep();
            }
            TestUtil.printLoopEnd();
          }
          TestUtil.printLoopEnd();
        }
        TestUtil.printLoopEnd();
      }
      TestUtil.printLoopEnd();
    }
    TestUtil.printEndOfMethod();
  }
}
