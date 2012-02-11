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
package com.github.croesch.mic1.controlstore;

import static org.fest.assertions.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

import com.github.croesch.DefaultTestCase;
import com.github.croesch.mic1.register.Register;

/**
 * Provides test cases for {@link Mic1InstructionReader}. Basically tests that it produces the expected output from
 * given sample input.
 * 
 * @author croesch
 * @since Date: Nov 10, 2011
 */
public class Mic1InstructionReaderTest extends DefaultTestCase {

  @Test
  public void testRead() throws IOException {
    // MIR[36]: 1110 0111 1001 1001 1000 1111 1111 1000 1000
    // bytes  : e    7    9    9    8    f    f    8    8
    // bits   : 1c    f      2 3  6 7    11         20  8
    final byte[] buf = new byte[] { (byte) 0xe7, (byte) 0x99, (byte) 0x8f, (byte) 0xf8, (byte) 0x8f };
    final InputStream in = new ByteArrayInputStream(buf);
    final Mic1Instruction value = Mic1InstructionReader.read(in);

    final JMPSignalSet jmpSet = new JMPSignalSet();
    final ALUSignalSet aluSet = new ALUSignalSet();
    final CBusSignalSet cBusSet = new CBusSignalSet();

    jmpSet.setJmpZ(true);
    aluSet.setSLL8(true).setF1(true).setEnA(true);
    cBusSet.setH(true).setOpc(true).setTos(true).setCpp(true);
    cBusSet.setLv(true).setSp(true).setPc(true).setMdr(true).setMar(true);

    final Mic1Instruction expected = new Mic1Instruction(0x1cf,
                                                         jmpSet,
                                                         aluSet,
                                                         cBusSet,
                                                         new Mic1MemorySignalSet(),
                                                         Register.OPC);

    assertThat(value).isEqualTo(expected);
  }

  /**
   * Tests that the reader does only return registers for B-Bus-select that can be written on the B-Bus.
   */
  @Test
  public void testRead_BBus() throws IOException {
    printMethodName();

    // some bits to read
    final byte[] buf = new byte[] { (byte) 0xe7, (byte) 0x99, (byte) 0x8f, (byte) 0xf8, (byte) 0x00 };

    // test that the H and MAR cannot be read as b-bus-select
    for (int b = 0; b < 9; ++b) {
      final Register[] expected = new Register[] { Register.MDR,
                                                  Register.PC,
                                                  Register.MBR,
                                                  Register.MBRU,
                                                  Register.SP,
                                                  Register.LV,
                                                  Register.CPP,
                                                  Register.TOS,
                                                  Register.OPC };

      buf[4] = (byte) (b << 4);
      final InputStream in = new ByteArrayInputStream(buf);
      final Register bBusSelect = Mic1InstructionReader.read(in).getbBusSelect();

      assertThat(bBusSelect).isEqualTo(expected[b]);

      in.close();
      printStep();
    }

    printLoopEnd();

    // test that all values greater or equal than 0x90 for the fifth bit of MIR result in null as B-Bus-select
    for (int b = 9; b < 16; ++b) {
      buf[4] = (byte) (b << 4);
      final InputStream in = new ByteArrayInputStream(buf);

      assertThat(Mic1InstructionReader.read(in).getbBusSelect()).isNull();

      in.close();
      printStep();
    }

    printEndOfMethod();
  }

  @Test
  public void testRead_All() throws IOException {
    // MIR[36]: 1111 1111 1111 1111 1111 1111 1111 1111 1111
    // bytes  : f    f    f    f    f    f    f    f    f
    // bits   : 1f    f    0                         24 f
    final byte[] buf = new byte[] { (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xf0 };
    final InputStream in = new ByteArrayInputStream(buf);
    final Mic1Instruction value = Mic1InstructionReader.read(in);

    final JMPSignalSet jmpSet = new JMPSignalSet();
    final ALUSignalSet aluSet = new ALUSignalSet();
    final CBusSignalSet cBusSet = new CBusSignalSet();
    final Mic1MemorySignalSet memSet = new Mic1MemorySignalSet();

    jmpSet.setJmpC(true).setJmpN(true).setJmpZ(true);
    aluSet.setSLL8(true).setSRA1(true).setF0(true).setF1(true);
    aluSet.setEnA(true).setEnB(true).setInvA(true).setInc(true);
    cBusSet.setH(true).setOpc(true).setTos(true).setCpp(true).setLv(true);
    cBusSet.setSp(true).setPc(true).setMdr(true).setMar(true);
    memSet.setWrite(true).setRead(true).setFetch(true);

    final Mic1Instruction expected = new Mic1Instruction(0x1ff, jmpSet, aluSet, cBusSet, memSet, null);

    assertThat(value).isEqualTo(expected);
  }

  @Test
  public void testRead_NotNull_MoreThanFiveBytes() throws IOException {
    final byte[] buf = new byte[] { (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xf0, (byte) 0xf0 };
    final InputStream in = new ByteArrayInputStream(buf);
    final Mic1Instruction value = Mic1InstructionReader.read(in);

    assertThat(value).isNotNull();
  }

  @Test
  public void testRead_NotNull_FiveBytes() throws IOException {
    final byte[] buf = new byte[] { (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xf0, (byte) 0xf0 };
    final InputStream in = new ByteArrayInputStream(buf);
    final Mic1Instruction value = Mic1InstructionReader.read(in);

    assertThat(value).isNotNull();
  }

  @Test
  public void testRead_Null_FourBytes() throws IOException {
    final byte[] buf = new byte[] { (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xf0 };
    final InputStream in = new ByteArrayInputStream(buf);
    final Mic1Instruction value = Mic1InstructionReader.read(in);

    assertThat(value).isNull();
  }

  @Test
  public void testRead_Null_ThreeBytes() throws IOException {
    final byte[] buf = new byte[] { (byte) 0xff, (byte) 0xff, (byte) 0xff };
    final InputStream in = new ByteArrayInputStream(buf);
    final Mic1Instruction value = Mic1InstructionReader.read(in);

    assertThat(value).isNull();
  }

  @Test
  public void testRead_Null_TwoBytes() throws IOException {
    final byte[] buf = new byte[] { (byte) 0xff, (byte) 0xff };
    final InputStream in = new ByteArrayInputStream(buf);
    final Mic1Instruction value = Mic1InstructionReader.read(in);

    assertThat(value).isNull();
  }

  @Test
  public void testRead_Null_OneByte() throws IOException {
    final byte[] buf = new byte[] { (byte) 0xab };
    final InputStream in = new ByteArrayInputStream(buf);
    final Mic1Instruction value = Mic1InstructionReader.read(in);

    assertThat(value).isNull();
  }

  @Test
  public void testRead_Null_ZeroBytes() throws IOException {
    final byte[] buf = new byte[] {};
    final InputStream in = new ByteArrayInputStream(buf);
    final Mic1Instruction value = Mic1InstructionReader.read(in);

    assertThat(value).isNull();
  }

  @Test
  public void testRead_NotNull_AndThenNull() throws IOException {
    // have eleven bytes, so can read two times
    byte[] buf = new byte[] { (byte) 0xff,
                             (byte) 0xff,
                             (byte) 0xff,
                             (byte) 0xff,
                             (byte) 0xf0,
                             (byte) 0xf0,
                             (byte) 0xf0,
                             (byte) 0xf0,
                             (byte) 0xf0,
                             (byte) 0xf0,
                             (byte) 0xf0 };
    InputStream in = new ByteArrayInputStream(buf);

    Mic1Instruction value = Mic1InstructionReader.read(in);
    assertThat(value).isNotNull();

    value = Mic1InstructionReader.read(in);
    assertThat(value).isNotNull();

    value = Mic1InstructionReader.read(in);
    assertThat(value).isNull();

    // have ten bytes, so can read two times
    buf = new byte[] { (byte) 0xff,
                      (byte) 0xff,
                      (byte) 0xff,
                      (byte) 0xff,
                      (byte) 0xf0,
                      (byte) 0xf0,
                      (byte) 0xf0,
                      (byte) 0xf0,
                      (byte) 0xf0,
                      (byte) 0xf0 };
    in = new ByteArrayInputStream(buf);

    value = Mic1InstructionReader.read(in);
    assertThat(value).isNotNull();

    value = Mic1InstructionReader.read(in);
    assertThat(value).isNotNull();

    value = Mic1InstructionReader.read(in);
    assertThat(value).isNull();

    // have nine bytes, so can read one time
    buf = new byte[] { (byte) 0xff,
                      (byte) 0xff,
                      (byte) 0xff,
                      (byte) 0xff,
                      (byte) 0xf0,
                      (byte) 0xf0,
                      (byte) 0xf0,
                      (byte) 0xf0,
                      (byte) 0xf0 };
    in = new ByteArrayInputStream(buf);

    value = Mic1InstructionReader.read(in);
    assertThat(value).isNotNull();

    value = Mic1InstructionReader.read(in);
    assertThat(value).isNull();

    // have eight bytes, so can read one time
    buf = new byte[] { (byte) 0xff,
                      (byte) 0xff,
                      (byte) 0xff,
                      (byte) 0xff,
                      (byte) 0xf0,
                      (byte) 0xf0,
                      (byte) 0xf0,
                      (byte) 0xf0 };
    in = new ByteArrayInputStream(buf);

    value = Mic1InstructionReader.read(in);
    assertThat(value).isNotNull();

    value = Mic1InstructionReader.read(in);
    assertThat(value).isNull();

    // have seven bytes, so can read one time
    buf = new byte[] { (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xf0, (byte) 0xf0, (byte) 0xf0 };
    in = new ByteArrayInputStream(buf);

    value = Mic1InstructionReader.read(in);
    assertThat(value).isNotNull();

    value = Mic1InstructionReader.read(in);
    assertThat(value).isNull();

    value = Mic1InstructionReader.read(in);
    assertThat(value).isNull();

    value = Mic1InstructionReader.read(in);
    assertThat(value).isNull();
  }
}
