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
 * Contains test cases for {@link Mic1ControlStore}.
 * 
 * @author croesch
 * @since Date: Nov 19, 2011
 */
public class Mic1ControlStoreTest extends DefaultTestCase {

  @Test(expected = FileFormatException.class)
  public void testConstructor_TooShortFile_Bytes0() throws FileFormatException {
    new Mic1ControlStore(new ByteArrayInputStream(new byte[] {}));
  }

  @Test(expected = FileFormatException.class)
  public void testConstructor_TooShortFile_Bytes1() throws FileFormatException {
    new Mic1ControlStore(new ByteArrayInputStream(new byte[] { 1 }));
  }

  @Test(expected = FileFormatException.class)
  public void testConstructor_TooShortFile_Bytes2() throws FileFormatException {
    new Mic1ControlStore(new ByteArrayInputStream(new byte[] { 1, 2 }));
  }

  @Test(expected = FileFormatException.class)
  public void testConstructor_TooShortFile_Bytes3() throws FileFormatException {
    new Mic1ControlStore(new ByteArrayInputStream(new byte[] { 1, 2, 3 }));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructor_Null() throws FileFormatException {
    new Mic1ControlStore(null);
  }

  @Test(expected = FileFormatException.class)
  public void testConstructor_FalseFormat() throws FileFormatException {
    new Mic1ControlStore(new ByteArrayInputStream(new byte[] { 0x12, 0x34, 0x56, 0x77 }));
  }

  @Test(expected = FileFormatException.class)
  public void testConstructor_EmptyFile() throws FileFormatException {
    new Mic1ControlStore(new ByteArrayInputStream(new byte[] { 0x12, 0x34, 0x56, 0x78 }));
  }

  @Test(expected = FileFormatException.class)
  public void testConstructor_IOException() throws FileFormatException {
    new Mic1ControlStore(new InputStream() {
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

    new Mic1ControlStore(new ByteArrayInputStream(bs));
  }

  @Test
  public void testDecodingOfBinaryFile() throws IOException {
    printMethodName();

    final Mic1ControlStore store = new Mic1ControlStore(ClassLoader.getSystemResourceAsStream("mic1/mic1ijvm.mic1"));
    final BufferedReader expectedFile = new BufferedReader(new InputStreamReader(ClassLoader.getSystemResourceAsStream("mic1/mic1ijvm.txt")));

    for (int i = 0; i < 512; ++i) {
      assertThat(Mic1InstructionDecoder.decode(store.getInstruction(i))).isEqualTo(expectedFile.readLine());
      printStep();
    }

    printEndOfMethod();
  }

  @Test
  public void testGetSingleInstruction() throws IOException {
    final Mic1ControlStore store = new Mic1ControlStore(ClassLoader.getSystemResourceAsStream("mic1/mic1ijvm.mic1"));

    Mic1Instruction expected = new Mic1Instruction(2,
                                                   new Mic1JMPSignalSet(),
                                                   new Mic1ALUSignalSet(),
                                                   new Mic1CBusSignalSet(),
                                                   new Mic1MemorySignalSet(),
                                                   Register.MDR);
    assertThat(store.getInstruction(0)).isEqualTo(expected);

    expected = new Mic1Instruction(0xFE,
                                   new Mic1JMPSignalSet(),
                                   new Mic1ALUSignalSet(),
                                   new Mic1CBusSignalSet(),
                                   new Mic1MemorySignalSet(),
                                   Register.MDR);
    assertThat(store.getInstruction(511)).isEqualTo(expected);

    final Mic1ALUSignalSet aluSet = new Mic1ALUSignalSet();
    aluSet.setF1(true);
    aluSet.setInvA(true);
    final Mic1CBusSignalSet cBusSet = new Mic1CBusSignalSet();
    cBusSet.setH(true);
    cBusSet.setOpc(true);
    expected = new Mic1Instruction(0x62,
                                   new Mic1JMPSignalSet(),
                                   aluSet,
                                   cBusSet,
                                   new Mic1MemorySignalSet(),
                                   Register.MDR);
    assertThat(store.getInstruction(0xFE)).isEqualTo(expected);
  }

}
