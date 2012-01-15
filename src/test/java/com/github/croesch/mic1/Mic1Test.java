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
package com.github.croesch.mic1;

import static org.fest.assertions.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.Test;

import com.github.croesch.error.FileFormatException;
import com.github.croesch.i18n.Text;
import com.github.croesch.mic1.io.Output;
import com.github.croesch.mic1.register.Register;
import com.github.croesch.misc.Printer;

/**
 * Provides test cases for {@link Mic1}.
 * 
 * @author croesch
 * @since Date: Dec 1, 2011
 */
public class Mic1Test {

  @Test(timeout = 1000)
  public void testPerformanceOfProcessor() throws FileFormatException {
    final Mic1 processor = new Mic1(ClassLoader.getSystemResourceAsStream("mic1/performance.mic1"),
                                    ClassLoader.getSystemResourceAsStream("mic1/empty.ijvm"));

    Register.PC.setValue(-1);
    for (int i = 0; i < 1000; ++i) {
      // MAR = PC = PC + 1; rd; goto 0;
      processor.doTick();
      assertThat(Register.PC.getValue()).isEqualTo(i);
      assertThat(Register.MAR.getValue()).isEqualTo(i);
      assertThat(Register.MDR.getValue()).isEqualTo(0);
    }
  }

  @Test(timeout = 4000)
  public void testDivide() throws FileFormatException {
    final ByteArrayOutputStream out = new ByteArrayOutputStream();
    Output.setOut(new PrintStream(out));

    final Mic1 processor = new Mic1(ClassLoader.getSystemResourceAsStream("mic1/mic1ijvm2.mic1"),
                                    ClassLoader.getSystemResourceAsStream("mic1/divtest.ijvm"));
    assertThat(processor.run()).isEqualTo(3965);

    assertThat(out.toString()).isEqualTo("11111111111111111111111110000000\n");
    Output.setOut(System.out);
  }

  @Test
  public void testIJVM() throws FileFormatException {
    final ByteArrayOutputStream out = new ByteArrayOutputStream();
    Output.setOut(new PrintStream(out));
    Output.setBuffered(false);

    final Mic1 processor = new Mic1(ClassLoader.getSystemResourceAsStream("mic1/mic1ijvm.mic1"),
                                    ClassLoader.getSystemResourceAsStream("mic1/ijvmtest.ijvm"));
    assertThat(processor.run()).isEqualTo(31816);

    assertThat(out.toString()).isEqualTo("OK");
    Output.setOut(System.out);
  }

  @Test
  public void testHi() throws FileFormatException {
    Mic1 processor = new Mic1(ClassLoader.getSystemResourceAsStream("mic1/hi.mic1"),
                              ClassLoader.getSystemResourceAsStream("mic1/hi.ijvm"));

    testHi(processor);

    processor = new Mic1(ClassLoader.getSystemResourceAsStream("mic1/hi.mic1"),
                         ClassLoader.getSystemResourceAsStream("mic1/hi.ijvm"));
    testRunHi1(processor);

    processor = new Mic1(ClassLoader.getSystemResourceAsStream("mic1/hi.mic1"),
                         ClassLoader.getSystemResourceAsStream("mic1/hi.ijvm"));
    testRunHi2(processor);
  }

  @Test
  public void testHaltPerUndefinedAddress() throws FileFormatException {
    Mic1 processor = new Mic1(ClassLoader.getSystemResourceAsStream("mic1/hi-halt-per-null.mic1"),
                              ClassLoader.getSystemResourceAsStream("mic1/hi.ijvm"));
    testHi(processor);

    processor = new Mic1(ClassLoader.getSystemResourceAsStream("mic1/hi-halt-per-null.mic1"),
                         ClassLoader.getSystemResourceAsStream("mic1/hi.ijvm"));
    testRunHi1(processor);

    processor = new Mic1(ClassLoader.getSystemResourceAsStream("mic1/hi-halt-per-null.mic1"),
                         ClassLoader.getSystemResourceAsStream("mic1/hi.ijvm"));
    testRunHi2(processor);
  }

  private void testRunHi2(final Mic1 processor) {
    final ByteArrayOutputStream out = new ByteArrayOutputStream();
    Output.setOut(new PrintStream(out));

    assertThat(processor.isHaltInstruction()).isFalse();
    assertThat(processor.run()).isEqualTo(14);
    assertThat(processor.isHaltInstruction()).isTrue();
    assertThat(processor.run()).isEqualTo(0);
    assertThat(processor.isHaltInstruction()).isTrue();

    assertThat(out.toString()).isEqualTo("Hi!\n");
    Output.setOut(System.out);
  }

  private void testRunHi1(final Mic1 processor) {
    final ByteArrayOutputStream out = new ByteArrayOutputStream();
    Output.setOut(new PrintStream(out));

    processor.doTick();
    processor.doTick();
    processor.doTick();
    processor.doTick();
    assertThat(processor.isHaltInstruction()).isFalse();
    assertThat(processor.run()).isEqualTo(10);
    assertThat(processor.isHaltInstruction()).isTrue();
    assertThat(processor.run()).isEqualTo(0);
    assertThat(processor.isHaltInstruction()).isTrue();

    assertThat(out.toString()).isEqualTo("Hi!\n");
    Output.setOut(System.out);
  }

  private void testHi(final Mic1 processor) {
    final ByteArrayOutputStream out = new ByteArrayOutputStream();
    Output.setBuffered(false);
    Output.setOut(new PrintStream(out));

    // 00: MAR = PC = 0; rd; goto 1;
    processor.doTick();
    assertThat(Register.MAR.getValue()).isEqualTo(0);
    assertThat(Register.PC.getValue()).isEqualTo(0);
    assertThat(out.toString()).isEmpty();
    assertThat(processor.isHaltInstruction()).isFalse();

    // 01: LV = H = -1; goto 2;
    processor.doTick();
    assertThat(Register.MAR.getValue()).isEqualTo(0);
    assertThat(Register.PC.getValue()).isEqualTo(0);
    assertThat(Register.LV.getValue()).isEqualTo(-1);
    assertThat(Register.H.getValue()).isEqualTo(-1);
    assertThat(out.toString()).isEmpty();
    assertThat(processor.isHaltInstruction()).isFalse();

    // 02: LV = H + LV; goto 3;
    processor.doTick();
    assertThat(Register.MDR.getValue()).isEqualTo('H');
    assertThat(Register.MAR.getValue()).isEqualTo(0);
    assertThat(Register.PC.getValue()).isEqualTo(0);
    assertThat(Register.LV.getValue()).isEqualTo(-2);
    assertThat(Register.H.getValue()).isEqualTo(-1);
    assertThat(out.toString()).isEmpty();
    assertThat(processor.isHaltInstruction()).isFalse();

    // 03: MAR = LV - 1; wr; goto 4;
    processor.doTick();
    assertThat(Register.MDR.getValue()).isEqualTo('H');
    assertThat(Register.MAR.getValue()).isEqualTo(-3);
    assertThat(Register.PC.getValue()).isEqualTo(0);
    assertThat(Register.LV.getValue()).isEqualTo(-2);
    assertThat(Register.H.getValue()).isEqualTo(-1);
    assertThat(out.toString()).isEqualTo("H");
    assertThat(processor.isHaltInstruction()).isFalse();

    // 04: MAR = PC = PC + 1; rd; goto 5;
    processor.doTick();
    assertThat(Register.MDR.getValue()).isEqualTo('H');
    assertThat(Register.MAR.getValue()).isEqualTo(1);
    assertThat(Register.PC.getValue()).isEqualTo(1);
    assertThat(Register.LV.getValue()).isEqualTo(-2);
    assertThat(Register.H.getValue()).isEqualTo(-1);
    assertThat(out.toString()).isEqualTo("H");
    assertThat(processor.isHaltInstruction()).isFalse();

    // 05: MAR = LV - 1; goto 6;
    processor.doTick();
    assertThat(Register.MDR.getValue()).isEqualTo('i');
    assertThat(Register.MAR.getValue()).isEqualTo(-3);
    assertThat(Register.PC.getValue()).isEqualTo(1);
    assertThat(Register.LV.getValue()).isEqualTo(-2);
    assertThat(Register.H.getValue()).isEqualTo(-1);
    assertThat(out.toString()).isEqualTo("H");
    assertThat(processor.isHaltInstruction()).isFalse();

    // 06: wr; goto 7;
    processor.doTick();
    assertThat(Register.MDR.getValue()).isEqualTo('i');
    assertThat(Register.MAR.getValue()).isEqualTo(-3);
    assertThat(Register.PC.getValue()).isEqualTo(1);
    assertThat(Register.LV.getValue()).isEqualTo(-2);
    assertThat(Register.H.getValue()).isEqualTo(-1);
    assertThat(out.toString()).isEqualTo("Hi");
    assertThat(processor.isHaltInstruction()).isFalse();

    // 07: MAR = PC = PC + 1; rd; goto 8;
    processor.doTick();
    assertThat(Register.MDR.getValue()).isEqualTo('i');
    assertThat(Register.MAR.getValue()).isEqualTo(2);
    assertThat(Register.PC.getValue()).isEqualTo(2);
    assertThat(Register.LV.getValue()).isEqualTo(-2);
    assertThat(Register.H.getValue()).isEqualTo(-1);
    assertThat(out.toString()).isEqualTo("Hi");
    assertThat(processor.isHaltInstruction()).isFalse();

    // 08: MAR = LV - 1; goto 9;
    processor.doTick();
    assertThat(Register.MDR.getValue()).isEqualTo('!');
    assertThat(Register.MAR.getValue()).isEqualTo(-3);
    assertThat(Register.PC.getValue()).isEqualTo(2);
    assertThat(Register.LV.getValue()).isEqualTo(-2);
    assertThat(Register.H.getValue()).isEqualTo(-1);
    assertThat(out.toString()).isEqualTo("Hi");
    assertThat(processor.isHaltInstruction()).isFalse();

    // 09: wr; goto 10;
    processor.doTick();
    assertThat(Register.MDR.getValue()).isEqualTo('!');
    assertThat(Register.MAR.getValue()).isEqualTo(-3);
    assertThat(Register.PC.getValue()).isEqualTo(2);
    assertThat(Register.LV.getValue()).isEqualTo(-2);
    assertThat(Register.H.getValue()).isEqualTo(-1);
    assertThat(out.toString()).isEqualTo("Hi!");
    assertThat(processor.isHaltInstruction()).isFalse();

    // 10: MAR = PC = PC + 1; rd; goto 11;
    processor.doTick();
    assertThat(Register.MDR.getValue()).isEqualTo('!');
    assertThat(Register.MAR.getValue()).isEqualTo(3);
    assertThat(Register.PC.getValue()).isEqualTo(3);
    assertThat(Register.LV.getValue()).isEqualTo(-2);
    assertThat(Register.H.getValue()).isEqualTo(-1);
    assertThat(out.toString()).isEqualTo("Hi!");
    assertThat(processor.isHaltInstruction()).isFalse();

    // 11: MAR = LV - 1; goto 12;
    processor.doTick();
    assertThat(Register.MDR.getValue()).isEqualTo('\n');
    assertThat(Register.MAR.getValue()).isEqualTo(-3);
    assertThat(Register.PC.getValue()).isEqualTo(3);
    assertThat(Register.LV.getValue()).isEqualTo(-2);
    assertThat(Register.H.getValue()).isEqualTo(-1);
    assertThat(out.toString()).isEqualTo("Hi!");
    assertThat(processor.isHaltInstruction()).isFalse();

    // 12: wr; goto 13;
    processor.doTick();
    assertThat(Register.MDR.getValue()).isEqualTo('\n');
    assertThat(Register.MAR.getValue()).isEqualTo(-3);
    assertThat(Register.PC.getValue()).isEqualTo(3);
    assertThat(Register.LV.getValue()).isEqualTo(-2);
    assertThat(Register.H.getValue()).isEqualTo(-1);
    assertThat(out.toString()).isEqualTo("Hi!\n");
    assertThat(processor.isHaltInstruction()).isFalse();

    // 13: halt;
    processor.doTick();
    assertThat(Register.MDR.getValue()).isEqualTo('\n');
    assertThat(Register.MAR.getValue()).isEqualTo(-3);
    assertThat(Register.PC.getValue()).isEqualTo(3);
    assertThat(Register.LV.getValue()).isEqualTo(-2);
    assertThat(Register.H.getValue()).isEqualTo(-1);
    assertThat(out.toString()).isEqualTo("Hi!\n");
    assertThat(processor.isHaltInstruction()).isTrue();

    Output.setOut(System.out);
  }

  @Test
  public void testErrorTexts() {
    final ByteArrayOutputStream out = new ByteArrayOutputStream();
    Printer.setPrintStream(new PrintStream(out));

    // less than four bytes
    try {
      new Mic1(new ByteArrayInputStream(new byte[] {}), new ByteArrayInputStream(new byte[] {}));
      throw new AssertionError("should throw exception");
    } catch (final FileFormatException e) {
      // expected
    }
    assertThat(out.toString()).isEqualTo(Text.ERROR.text(Text.WRONG_FORMAT_MIC1.text(Text.WRONG_FORMAT_TOO_SMALL))
                                                 + "\n"
                                                 + Text.ERROR.text(Text.WRONG_FORMAT_IJVM
                                                   .text(Text.WRONG_FORMAT_TOO_SMALL)) + "\n");
    out.reset();

    // wrong magic number
    try {
      new Mic1(new ByteArrayInputStream(new byte[] { 0x1d, (byte) 0xea, (byte) 0xdf, (byte) 0xad }),
               new ByteArrayInputStream(new byte[] { 0x12, 0x34, 0x56, 0x78 }));
      throw new AssertionError("should throw exception");
    } catch (final FileFormatException e) {
      // expected
    }
    assertThat(out.toString()).isEqualTo(Text.ERROR.text(Text.WRONG_FORMAT_MIC1.text(Text.WRONG_FORMAT_MAGIC_NUMBER))
                                                 + "\n"
                                                 + Text.ERROR.text(Text.WRONG_FORMAT_IJVM
                                                   .text(Text.WRONG_FORMAT_MAGIC_NUMBER + "\n")));
    out.reset();

    // empty files
    try {
      new Mic1(new ByteArrayInputStream(new byte[] { 0x12, 0x34, 0x56, 0x78 }),
               new ByteArrayInputStream(new byte[] { 0x1d, (byte) 0xea, (byte) 0xdf, (byte) 0xad }));
      throw new AssertionError("should throw exception");
    } catch (final FileFormatException e) {
      // expected
    }
    assertThat(out.toString()).isEqualTo(Text.ERROR.text(Text.WRONG_FORMAT_MIC1.text(Text.WRONG_FORMAT_EMPTY)) + "\n");
    out.reset();

    // unexpected eof
    try {
      new Mic1(new ByteArrayInputStream(new byte[] { 0x12, 0x34, 0x56, 0x78, 0, 0, 0, 0, 0 }),
               new ByteArrayInputStream(new byte[] { 0x1d, (byte) 0xea, (byte) 0xdf, (byte) 0xad, 0 }));
      throw new AssertionError("should throw exception");
    } catch (final FileFormatException e) {
      // expected
    }
    assertThat(out.toString()).isEqualTo(Text.ERROR.text(Text.WRONG_FORMAT_IJVM.text(Text.WRONG_FORMAT_UNEXPECTED_END))
                                                 + "\n");
    out.reset();

    // unexpected end of block
    try {
      new Mic1(new ByteArrayInputStream(new byte[] { 0x12, 0x34, 0x56, 0x78, 0, 0, 0, 0, 0 }),
               new ByteArrayInputStream(new byte[] { 0x1d,
                                                    (byte) 0xea,
                                                    (byte) 0xdf,
                                                    (byte) 0xad,
                                                    0,
                                                    0,
                                                    0,
                                                    0,
                                                    0,
                                                    0,
                                                    0,
                                                    12 }));
      throw new AssertionError("should throw exception");
    } catch (final FileFormatException e) {
      // expected
    }
    assertThat(out.toString()).isEqualTo(Text.ERROR.text(Text.WRONG_FORMAT_IJVM
                                           .text(Text.WRONG_FORMAT_UNEXPECTED_END_OF_BLOCK)) + "\n");
    out.reset();

    // file too big
    final byte[] mic1File = new byte[5000];
    mic1File[0] = 0x12;
    mic1File[1] = 0x34;
    mic1File[2] = 0x56;
    mic1File[3] = 0x78;
    try {
      new Mic1(new ByteArrayInputStream(mic1File), new ByteArrayInputStream(new byte[] { 0x1d,
                                                                                        (byte) 0xea,
                                                                                        (byte) 0xdf,
                                                                                        (byte) 0xad }));
      throw new AssertionError("should throw exception");
    } catch (final FileFormatException e) {
      // expected
    }
    assertThat(out.toString())
      .isEqualTo(Text.ERROR.text(Text.WRONG_FORMAT_MIC1.text(Text.WRONG_FORMAT_TOO_BIG)) + "\n");
    out.reset();

    Printer.setPrintStream(System.out);
  }
}
