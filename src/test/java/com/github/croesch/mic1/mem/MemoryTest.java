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
package com.github.croesch.mic1.mem;

import static org.fest.assertions.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Reader;

import org.junit.Before;
import org.junit.Test;

import com.github.croesch.DefaultTestCase;
import com.github.croesch.TestUtil;
import com.github.croesch.error.FileFormatException;
import com.github.croesch.i18n.Text;
import com.github.croesch.mic1.io.Input;
import com.github.croesch.mic1.io.Output;
import com.github.croesch.mic1.register.Register;
import com.github.croesch.misc.Printer;
import com.github.croesch.misc.Settings;
import com.github.croesch.misc.Utils;

/**
 * Provides test cases for {@link Memory}
 * 
 * @author croesch
 * @since Date: Nov 23, 2011
 */
public class MemoryTest extends DefaultTestCase {

  private Memory mem;

  private final byte[] bytes = new byte[Byte.MAX_VALUE];

  @Before
  public void setUp() throws FileFormatException {
    for (byte b = 0; b < Byte.MAX_VALUE; ++b) {
      this.bytes[b] = b;
    }

    this.mem = new Memory(Byte.MAX_VALUE, ClassLoader.getSystemResourceAsStream("mic1/test.ijvm"));
  }

  @Test(expected = FileFormatException.class)
  public void testConstructor_WrongMagicNumber() throws FileFormatException {
    this.mem = new Memory(4, ClassLoader.getSystemResourceAsStream("mic1/wrong-file-format-0.ijvm"));
  }

  @Test
  public void testConstructor_EmptyData() throws FileFormatException {
    // file ends after magic number
    this.mem = new Memory(4, ClassLoader.getSystemResourceAsStream("mic1/wrong-file-format-1.ijvm"));
  }

  @Test(expected = FileFormatException.class)
  public void testConstructor_UnexpectedEOF_0() throws FileFormatException {
    // file ends after reading block size (no data in block)
    this.mem = new Memory(4, ClassLoader.getSystemResourceAsStream("mic1/wrong-file-format-2.ijvm"));
  }

  @Test(expected = FileFormatException.class)
  public void testConstructor_UnexpectedEOF_1() throws FileFormatException {
    // file ends while reading data of a block
    this.mem = new Memory(4, ClassLoader.getSystemResourceAsStream("mic1/wrong-file-format-3.ijvm"));
  }

  @Test
  public void testConstructor_UnexpectedEOF_2() throws FileFormatException {
    // file ends while reading block size
    this.mem = new Memory(4, ClassLoader.getSystemResourceAsStream("mic1/wrong-file-format-4.ijvm"));
  }

  @Test(expected = FileFormatException.class)
  public void testConstructor_UnexpectedEOF_3() throws FileFormatException {
    // file ends while reading start address for memory
    this.mem = new Memory(4, ClassLoader.getSystemResourceAsStream("mic1/wrong-file-format-5.ijvm"));
  }

  @Test
  public void testConstructor_FileWith_OverlappingByte_0() throws FileFormatException {
    this.mem = new Memory(1, ClassLoader.getSystemResourceAsStream("mic1/ff-file-0.ijvm"));

    this.mem.setRead(true);
    this.mem.setWordAddress(0);
    this.mem.poke();
    this.mem.fillRegisters(Register.H, null);
    assertThat(Register.H.getValue()).isEqualTo(0xFFFFFF);
    assertThat(this.mem.getWord(0)).isEqualTo(0xFFFFFF);
  }

  @Test
  public void testConstructor_FileWith_OverlappingByte_1() throws FileFormatException {
    this.mem = new Memory(1, ClassLoader.getSystemResourceAsStream("mic1/ff-file-1.ijvm"));

    this.mem.setRead(true);
    this.mem.setWordAddress(0);
    this.mem.poke();
    this.mem.fillRegisters(Register.H, null);
    assertThat(Register.H.getValue()).isEqualTo(0xFF00FFFF);
    assertThat(this.mem.getWord(0)).isEqualTo(0xFF00FFFF);
  }

  @Test
  public void testConstructor_FileWith_OverlappingByte_2() throws FileFormatException {
    this.mem = new Memory(1, ClassLoader.getSystemResourceAsStream("mic1/ff-file-2.ijvm"));

    this.mem.setRead(true);
    this.mem.setWordAddress(0);
    this.mem.poke();
    this.mem.fillRegisters(Register.H, null);
    assertThat(Register.H.getValue()).isEqualTo(0xFFFF00FF);
    assertThat(this.mem.getWord(0)).isEqualTo(0xFFFF00FF);
  }

  @Test
  public void testConstructor_FileWith_OverlappingByte_3() throws FileFormatException {
    this.mem = new Memory(1, ClassLoader.getSystemResourceAsStream("mic1/ff-file-3.ijvm"));

    this.mem.setRead(true);
    this.mem.setWordAddress(0);
    this.mem.poke();
    this.mem.fillRegisters(Register.H, null);
    assertThat(Register.H.getValue()).isEqualTo(0xFFFFFF00);
    assertThat(this.mem.getWord(0)).isEqualTo(0xFFFFFF00);
  }

  @Test
  public void testConstructor_FileWithFFValues() throws FileFormatException {
    TestUtil.printMethodName();
    this.mem = new Memory(2, ClassLoader.getSystemResourceAsStream("mic1/ff-file.ijvm"));

    this.mem.setFetch(true);
    this.mem.setRead(false);
    this.mem.setWrite(false);
    final int old = Register.H.getValue();
    for (byte b = 0; b < 7; ++b) {
      this.mem.setByteAddress(b);
      this.mem.poke();
      this.mem.fillRegisters(Register.H, Register.LV);
      assertThat(Register.H.getValue()).isEqualTo(old);
      assertThat(Register.LV.getValue()).isEqualTo(0xff);

      TestUtil.printStep();
    }

    TestUtil.printEndOfMethod();
  }

  @Test
  public void testWriteRead() {
    TestUtil.printMethodName();

    this.mem.setFetch(false);
    this.mem.setRead(true);
    this.mem.setWrite(false);
    final int old = Register.LV.getValue();
    for (byte b = 0; b < Byte.MAX_VALUE - 3; b += 4) {
      this.mem.setWordAddress(b / 4);
      this.mem.poke();
      this.mem.fillRegisters(Register.H, Register.LV);
      assertThat(Register.LV.getValue()).isEqualTo(old);
      int val = this.bytes[b] << 8;
      val |= this.bytes[b + 1];
      val <<= 8;
      val |= this.bytes[b + 2];
      val <<= 8;
      val |= this.bytes[b + 3];
      assertThat(Register.H.getValue()).isEqualTo(val);

      TestUtil.printStep();
    }
    TestUtil.printEndOfMethod();
  }

  @Test
  public void testWriteFetch() {
    TestUtil.printMethodName();

    this.mem.setFetch(true);
    this.mem.setRead(false);
    this.mem.setWrite(false);
    final int old = Register.H.getValue();
    for (byte b = 0; b < Byte.MAX_VALUE - 3; ++b) {
      this.mem.setByteAddress(b);
      this.mem.poke();
      this.mem.fillRegisters(Register.H, Register.LV);
      assertThat(Register.H.getValue()).isEqualTo(old);
      assertThat(Register.LV.getValue()).isEqualTo(this.bytes[b]);

      TestUtil.printStep();
    }

    TestUtil.printEndOfMethod();
  }

  @Test(expected = ArrayIndexOutOfBoundsException.class)
  public void testPoke_WriteToNegative() {
    this.mem.setWrite(true);
    this.mem.setWordAddress(-1);
    this.mem.poke();
  }

  @Test(expected = ArrayIndexOutOfBoundsException.class)
  public void testPoke_ReadFromNegative() {
    this.mem.setRead(true);
    this.mem.setWordAddress(-1);
    this.mem.poke();
  }

  @Test(expected = ArrayIndexOutOfBoundsException.class)
  public void testPoke_FetchFromNegative() {
    this.mem.setFetch(true);
    this.mem.setByteAddress(-4);
    this.mem.poke();
  }

  @Test(expected = ArrayIndexOutOfBoundsException.class)
  public void testPoke_WriteToOOM() throws FileFormatException {
    final ByteArrayInputStream programStream = new ByteArrayInputStream(new byte[] { 0x1d,
                                                                                    (byte) 0xea,
                                                                                    (byte) 0xdf,
                                                                                    (byte) 0xad });
    this.mem = new Memory(1, programStream);
    this.mem.setWrite(true);
    this.mem.setWordAddress(1);
    this.mem.poke();
  }

  @Test(expected = ArrayIndexOutOfBoundsException.class)
  public void testPoke_ReadFromOOM() throws FileFormatException {
    final ByteArrayInputStream programStream = new ByteArrayInputStream(new byte[] { 0x1d,
                                                                                    (byte) 0xea,
                                                                                    (byte) 0xdf,
                                                                                    (byte) 0xad });
    this.mem = new Memory(1, programStream);
    this.mem.setRead(true);
    this.mem.setWordAddress(1);
    this.mem.poke();
  }

  @Test(expected = ArrayIndexOutOfBoundsException.class)
  public void testPoke_FetchFromOOM() throws FileFormatException {
    final ByteArrayInputStream programStream = new ByteArrayInputStream(new byte[] { 0x1d,
                                                                                    (byte) 0xea,
                                                                                    (byte) 0xdf,
                                                                                    (byte) 0xad });
    this.mem = new Memory(1, programStream);
    this.mem.setFetch(true);
    this.mem.setByteAddress(4);
    this.mem.poke();
  }

  @Test
  public void testReadNegativeValue() {
    this.mem.setWrite(true);
    this.mem.setWordAddress(1);
    this.mem.setWordValue(0xFFFFFFFF);
    this.mem.poke();
    this.mem.setWrite(false);

    this.mem.setRead(true);
    this.mem.poke();
    this.mem.fillRegisters(Register.H, null);
    assertThat(Register.H.getValue()).isEqualTo(0xFFFFFFFF);
  }

  @Test
  public void testFetchNegativeValue() {
    this.mem.setWrite(true);
    this.mem.setWordAddress(1);
    this.mem.setWordValue(0xFFFFFFFF);
    this.mem.poke();
    this.mem.setWrite(false);

    this.mem.setFetch(true);
    this.mem.setByteAddress(5);
    this.mem.poke();
    this.mem.fillRegisters(null, Register.H);
    assertThat(Register.H.getValue()).isEqualTo(0xFF);
  }

  @Test
  public void testReadWriteAndFetch() {
    this.mem.setWrite(true);
    this.mem.setRead(true);
    this.mem.setFetch(true);
    this.mem.setWordAddress(1);
    this.mem.setByteAddress(6);
    this.mem.setWordValue(0x12345678);
    this.mem.poke();
    this.mem.fillRegisters(Register.TOS, Register.H);
    assertThat(Register.TOS.getValue()).isEqualTo(0x12345678);
    assertThat(Register.H.getValue()).isEqualTo(0x56);
  }

  @Test
  public void testDoNothing() {
    this.mem.setWordAddress(1);
    this.mem.setByteAddress(6);
    this.mem.setWordValue(0x12345678);
    this.mem.poke();
    int old = Register.H.getValue();
    this.mem.fillRegisters(Register.H, null);
    assertThat(Register.H.getValue()).isEqualTo(old);

    old = Register.H.getValue();
    this.mem.fillRegisters(null, Register.H);
    assertThat(Register.H.getValue()).isEqualTo(old);
  }

  @Test
  public void testWriteOut() {
    final ByteArrayOutputStream out = new ByteArrayOutputStream();
    Output.setOut(new PrintStream(out));

    this.mem.setWordAddress(Memory.MEMORY_MAPPED_IO_ADDRESS);
    // 0x78 = 120 -> x
    this.mem.setWordValue(0x12345678);
    this.mem.setWrite(true);

    assertThat(out.toString()).isEmpty();
    this.mem.poke();
    assertThat(out.toString()).isEmpty();
    Output.flush();
    assertThat(out.toString()).isEqualTo("x");

    int old = Register.H.getValue();
    this.mem.fillRegisters(Register.H, null);
    assertThat(Register.H.getValue()).isEqualTo(old);

    old = Register.H.getValue();
    this.mem.fillRegisters(null, Register.H);
    assertThat(Register.H.getValue()).isEqualTo(old);

    Output.setOut(System.out);
  }

  @Test
  public void testReadIn() {
    // H -> 72 = 0x48
    final ByteArrayInputStream in = new ByteArrayInputStream("Hi!".getBytes());
    Input.setIn(in);

    this.mem.setWordAddress(Memory.MEMORY_MAPPED_IO_ADDRESS);
    // write something to wordValue, that'll be overridden
    this.mem.setWordValue(0x12345678);
    this.mem.setRead(true);

    this.mem.fillRegisters(Register.H, null);
    assertThat(Register.H.getValue()).isEqualTo(0x12345678);
    this.mem.poke();
    this.mem.fillRegisters(Register.H, null);
    assertThat(Register.H.getValue()).isEqualTo(0x48);

    Input.setIn(System.in);
  }

  @Test
  public void testReadEmptyStream() {
    final ByteArrayInputStream in = new ByteArrayInputStream("".getBytes());
    Input.setIn(in);

    this.mem.setWordAddress(Memory.MEMORY_MAPPED_IO_ADDRESS);
    // write something to wordValue, that'll be overridden
    this.mem.setWordValue(0x12345678);
    this.mem.setRead(true);

    this.mem.fillRegisters(Register.H, null);
    assertThat(Register.H.getValue()).isEqualTo(0x12345678);
    this.mem.poke();
    this.mem.fillRegisters(Register.H, null);
    // read -1 but as byte value -> 0xFF as integer
    assertThat(Register.H.getValue()).isEqualTo(0xFF);

    Input.setIn(System.in);
  }

  @Test
  public void testGetSetWord() {
    assertThat(this.mem.getWord(0)).isEqualTo(0x00010203);
    assertThat(this.mem.getWord(1)).isEqualTo(0x04050607);
    assertThat(this.mem.getWord(2)).isEqualTo(0x08090A0B);
    assertThat(this.mem.getWord(3)).isEqualTo(0x0C0D0E0F);
    assertThat(this.mem.getWord(4)).isEqualTo(0x10111213);
    assertThat(this.mem.getWord(5)).isEqualTo(0x14151617);
    assertThat(this.mem.getWord(6)).isEqualTo(0x18191A1B);
    assertThat(this.mem.getWord(7)).isEqualTo(0x1C1D1E1F);

    this.mem.setWord(0, 0x98979695);

    assertThat(this.mem.getWord(0)).isEqualTo(0x98979695);
    assertThat(this.mem.getWord(1)).isEqualTo(0x04050607);
    assertThat(this.mem.getWord(2)).isEqualTo(0x08090A0B);
    assertThat(this.mem.getWord(3)).isEqualTo(0x0C0D0E0F);
    assertThat(this.mem.getWord(4)).isEqualTo(0x10111213);
    assertThat(this.mem.getWord(5)).isEqualTo(0x14151617);
    assertThat(this.mem.getWord(6)).isEqualTo(0x18191A1B);
    assertThat(this.mem.getWord(7)).isEqualTo(0x1C1D1E1F);

    this.mem.setByteAddress(2);
    this.mem.setFetch(true);
    this.mem.poke();
    this.mem.fillRegisters(Register.H, Register.LV);
    assertThat(Register.LV.getValue()).isEqualTo(0x96);

    this.mem.setFetch(false);
  }

  @Test
  public void testSetInvalidAddresses() {
    final ByteArrayOutputStream out = new ByteArrayOutputStream();
    Printer.setPrintStream(new PrintStream(out));

    this.mem.setWord(-1, 0);
    assertThat(out.toString()).isEqualTo(Text.ERROR.text(Text.INVALID_MEM_ADDR.text(Utils.toHexString(-1))) + "\n");
    out.reset();

    this.mem.setWord(0, 0);
    assertThat(out.toString()).isEmpty();

    this.mem.setWord(Byte.MAX_VALUE, 0);
    assertThat(out.toString()).isEqualTo(Text.ERROR.text(Text.INVALID_MEM_ADDR.text(Utils.toHexString(Byte.MAX_VALUE)))
                                                 + "\n");

    Printer.setPrintStream(System.out);
  }

  @Test
  public void testGetInvalidAddresses() {
    final ByteArrayOutputStream out = new ByteArrayOutputStream();
    Printer.setPrintStream(new PrintStream(out));

    this.mem.getWord(-1);
    assertThat(out.toString()).isEqualTo(Text.ERROR.text(Text.INVALID_MEM_ADDR.text(Utils.toHexString(-1))) + "\n");
    out.reset();

    this.mem.getWord(0);
    assertThat(out.toString()).isEmpty();

    this.mem.getWord(Byte.MAX_VALUE);
    assertThat(out.toString()).isEqualTo(Text.ERROR.text(Text.INVALID_MEM_ADDR.text(Utils.toHexString(Byte.MAX_VALUE)))
                                                 + "\n");

    Printer.setPrintStream(System.out);
  }

  @Test
  public void testPrintCode_All() throws IOException {
    final ByteArrayOutputStream out = new ByteArrayOutputStream();
    Printer.setPrintStream(new PrintStream(out));

    this.mem = new Memory(Settings.MIC1_MEMORY_MAXSIZE.getValue(),
                          ClassLoader.getSystemResourceAsStream("mic1/add.ijvm"));
    this.mem.printCode();
    final StringBuilder sb = new StringBuilder();
    final Reader r = new InputStreamReader(ClassLoader.getSystemResourceAsStream("mic1/add.ijvm.dis"));
    int c;
    while ((c = r.read()) != -1) {
      sb.append((char) c);
    }

    assertThat(out.toString()).isEqualTo(sb.toString());

    Printer.setPrintStream(System.out);
  }
}
