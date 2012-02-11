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

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.junit.Test;

import com.github.croesch.DefaultTestCase;
import com.github.croesch.error.FileFormatException;
import com.github.croesch.mic1.register.Register;

/**
 * Contains test cases for {@link MicroControlStore}.
 * 
 * @author croesch
 * @since Date: Nov 19, 2011
 */
public class Mic1ControlStoreTest extends DefaultTestCase {

  private MicroControlStore store;

  @Override
  protected void setUpDetails() throws Exception {
    this.store = new MicroControlStore(ClassLoader.getSystemResourceAsStream("mic1/mic1ijvm.mic1"));
  }

  @Test(expected = FileFormatException.class)
  public void testConstructor_TooShortFile_Bytes0() throws FileFormatException {
    new MicroControlStore(new ByteArrayInputStream(new byte[] {}));
  }

  @Test(expected = FileFormatException.class)
  public void testConstructor_TooShortFile_Bytes1() throws FileFormatException {
    new MicroControlStore(new ByteArrayInputStream(new byte[] { 1 }));
  }

  @Test(expected = FileFormatException.class)
  public void testConstructor_TooShortFile_Bytes2() throws FileFormatException {
    new MicroControlStore(new ByteArrayInputStream(new byte[] { 1, 2 }));
  }

  @Test(expected = FileFormatException.class)
  public void testConstructor_TooShortFile_Bytes3() throws FileFormatException {
    new MicroControlStore(new ByteArrayInputStream(new byte[] { 1, 2, 3 }));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructor_Null() throws FileFormatException {
    new MicroControlStore(null);
  }

  @Test(expected = FileFormatException.class)
  public void testConstructor_FalseFormat() throws FileFormatException {
    new MicroControlStore(new ByteArrayInputStream(new byte[] { 0x12, 0x34, 0x56, 0x77 }));
  }

  @Test(expected = FileFormatException.class)
  public void testConstructor_EmptyFile() throws FileFormatException {
    new MicroControlStore(new ByteArrayInputStream(new byte[] { 0x12, 0x34, 0x56, 0x78 }));
  }

  @Test(expected = FileFormatException.class)
  public void testConstructor_IOException() throws FileFormatException {
    new MicroControlStore(new InputStream() {
      private final byte[] bytes = new byte[] { 0x12, 0x34, 0x56, 0x78 };

      private int counter = 0;

      @Override
      public int read() throws IOException {
        if (this.counter < this.bytes.length) {
          return this.bytes[this.counter++];
        }
        throw new IOException();
      }
    });
  }

  @Test(expected = FileFormatException.class)
  public void testConstructor_TooBigFile() throws FileFormatException {
    // magic number + 512 * 5 byte + 5 byte to have the first instruction and the file is too big.
    final byte[] bs = new byte[4 + 513 * 5];
    bs[0] = 0x12;
    bs[1] = 0x34;
    bs[2] = 0x56;
    bs[3] = 0x78;

    new MicroControlStore(new ByteArrayInputStream(bs));
  }

  @Test
  public void testDecodingOfBinaryFile() throws IOException {
    printMethodName();

    final BufferedReader expectedFile = new BufferedReader(new InputStreamReader(ClassLoader.getSystemResourceAsStream("mic1/mic1ijvm.txt")));

    for (int i = 0; i < 512; ++i) {
      assertThat(Mic1InstructionDecoder.decode(this.store.getInstruction(i))).isEqualTo(expectedFile.readLine());
      printStep();
    }

    printEndOfMethod();
  }

  @Test
  public void testGetSingleInstruction() throws IOException {
    printlnMethodName();

    Mic1Instruction expected = new Mic1Instruction(2,
                                                   new JMPSignalSet(),
                                                   new ALUSignalSet(),
                                                   new CBusSignalSet(),
                                                   new MemorySignalSet(),
                                                   Register.MDR);
    assertThat(this.store.getInstruction(0)).isEqualTo(expected);

    expected = new Mic1Instruction(0xFE,
                                   new JMPSignalSet(),
                                   new ALUSignalSet(),
                                   new CBusSignalSet(),
                                   new MemorySignalSet(),
                                   Register.MDR);
    assertThat(this.store.getInstruction(511)).isEqualTo(expected);

    final ALUSignalSet aluSet = new ALUSignalSet();
    aluSet.setF1(true);
    aluSet.setInvA(true);
    final CBusSignalSet cBusSet = new CBusSignalSet();
    cBusSet.setH(true);
    cBusSet.setOpc(true);
    expected = new Mic1Instruction(0x62,
                                   new JMPSignalSet(),
                                   aluSet,
                                   cBusSet,
                                   new MemorySignalSet(),
                                   Register.MDR);
    assertThat(this.store.getInstruction(0xFE)).isEqualTo(expected);
  }

  @Test
  public void testPrintCode_All() throws IOException {
    printlnMethodName();
    this.store.printCode();
    assertThat(out.toString()).isEqualTo(readFile("mic1/mic1ijvm.mic1.dis").toString());
  }

  @Test
  public void testPrintCode_Part1() throws IOException {
    printlnMethodName();
    final String expected = readFile("mic1/mic1ijvm_part1.mic1.dis").toString();
    final int end = 0x20;

    this.store.printCode(0, end);
    assertThat(out.toString()).isEqualTo(expected);
    out.reset();

    this.store.printCode(end, 0);
    assertThat(out.toString()).isEqualTo(expected);
    out.reset();

    this.store.printCode(end, -42);
    assertThat(out.toString()).isEqualTo(expected);
    out.reset();

    this.store.printCode(-42, end);
    assertThat(out.toString()).isEqualTo(expected);
    out.reset();
  }

  @Test
  public void testPrintCode_Part2() throws IOException {
    printlnMethodName();
    final String expected = readFile("mic1/mic1ijvm_part2.mic1.dis").toString();

    this.store.printCode(0x35, 0x63);
    assertThat(out.toString()).isEqualTo(expected);
    out.reset();

    this.store.printCode(0x63, 0x35);
    assertThat(out.toString()).isEqualTo(expected);
    out.reset();
  }

  @Test
  public void testPrintCode_Part3() throws IOException {
    printlnMethodName();
    final String expected = readFile("mic1/mic1ijvm_part3.mic1.dis").toString();
    final int start = 0x87;

    this.store.printCode(start, 0x1FF);
    assertThat(out.toString()).isEqualTo(expected);
    out.reset();

    this.store.printCode(0x1FF, start);
    assertThat(out.toString()).isEqualTo(expected);
    out.reset();

    this.store.printCode(start, 4711);
    assertThat(out.toString()).isEqualTo(expected);
    out.reset();

    this.store.printCode(4711, start);
    assertThat(out.toString()).isEqualTo(expected);
    out.reset();
  }

  @Test
  public void testPrintCode_Around_Part1() throws IOException {
    printlnMethodName();
    final String expected = readFile("mic1/mic1ijvm_part1.mic1.dis").toString();

    this.store.printCodeAroundLine(0x10, 0x10);
    assertThat(out.toString()).isEqualTo(expected);
    out.reset();

    this.store.printCodeAroundLine(0xF, 0x11);
    assertThat(out.toString()).isEqualTo(expected);
    out.reset();

    this.store.printCodeAroundLine(0, 0x20);
    assertThat(out.toString()).isEqualTo(expected);
    out.reset();

    this.store.printCodeAroundLine(0x9, 0x17);
    assertThat(out.toString()).isEqualTo(expected);
    out.reset();
  }

  @Test
  public void testPrintCode_Around_Part2() throws IOException {
    printlnMethodName();
    final String expected = readFile("mic1/mic1ijvm_part2.mic1.dis").toString();

    this.store.printCodeAroundLine(0x4C, 0x17);
    assertThat(out.toString()).isEqualTo(expected);
    out.reset();

    this.store.printCodeAroundLine(0x4C, 0x16);
    assertThat(out.toString()).isNotEqualTo(expected);
    out.reset();

    this.store.printCodeAroundLine(0x4C, 0x18);
    assertThat(out.toString()).isNotEqualTo(expected);
    out.reset();

    this.store.printCodeAroundLine(0x17, 0x4C);
    assertThat(out.toString()).isNotEqualTo(expected);
    out.reset();
  }

  @Test
  public void testPrintCode_Around_Part3() throws IOException {
    printlnMethodName();
    final String expected = readFile("mic1/mic1ijvm_part3.mic1.dis").toString();

    this.store.printCodeAroundLine(0x143, 0xBC);
    assertThat(out.toString()).isEqualTo(expected);
    out.reset();

    this.store.printCodeAroundLine(0x144, 0xBD);
    assertThat(out.toString()).isEqualTo(expected);
    out.reset();

    this.store.printCodeAroundLine(0x145, 0xBE);
    assertThat(out.toString()).isEqualTo(expected);
    out.reset();

    this.store.printCodeAroundLine(0x1FF, 0x178);
    assertThat(out.toString()).isEqualTo(expected);
    out.reset();
  }

  @Test
  public void testPrintCode_Hi() throws IOException {
    printlnMethodName();
    this.store = new MicroControlStore(ClassLoader.getSystemResourceAsStream("mic1/hi.mic1"));

    this.store.printCode();
    assertThat(out.toString()).isEqualTo(readFile("mic1/hi.mic1.dis").toString());
    out.reset();

    this.store.printCode(0x10, 0x20);
    assertThat(out.toString()).isEmpty();

    this.store.printCodeAroundLine(0x20, 0x5);
    assertThat(out.toString()).isEmpty();
  }

  @Test
  public void testPrintCode_WithNullValues() throws IOException {
    printlnMethodName();
    this.store = new MicroControlStore(ClassLoader.getSystemResourceAsStream("mic1/hi-with-null.mic1"));

    this.store.printCode();
    assertThat(out.toString()).isEqualTo(readFile("mic1/hi-with-null.mic1.dis").toString());
  }
}
