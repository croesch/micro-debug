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
import java.io.IOException;
import java.io.PrintStream;

import org.junit.Test;

import com.github.croesch.DefaultTestCase;
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
public class Mic1Test extends DefaultTestCase {

  private Mic1 processor;

  @Override
  protected void setUpDetails() throws FileFormatException {
    this.processor = new Mic1(ClassLoader.getSystemResourceAsStream("mic1/hi.mic1"),
                              ClassLoader.getSystemResourceAsStream("mic1/hi.ijvm"));
  }

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
  public void testStepN() {
    this.processor.microStep(200);
    assertThat(out.toString()).isEqualTo(Text.TICKS.text(14) + getLineSeparator());

    out.reset();
    this.processor.reset();
    this.processor.microStep(200);
    assertThat(out.toString()).isEqualTo(Text.TICKS.text(14) + getLineSeparator());
  }

  @Test
  public void testStepOne() {
    for (int i = 0; i < 14; ++i) {
      this.processor.microStep();
      assertThat(out.toString()).isEqualTo(Text.TICKS.text(1) + getLineSeparator());
      out.reset();
    }
    this.processor.microStep();
    assertThat(out.toString()).isEmpty();

    this.processor.reset();
    for (int i = 0; i < 14; ++i) {
      this.processor.microStep();
      assertThat(out.toString()).isEqualTo(Text.TICKS.text(1) + getLineSeparator());
      out.reset();
    }
    this.processor.microStep();
    assertThat(out.toString()).isEmpty();
  }

  @Test
  public void testHi() throws FileFormatException {

    testHi(this.processor);

    this.processor.reset();
    testRunHi1(this.processor);

    this.processor.reset();
    testRunHi2(this.processor);
  }

  @Test
  public void testHaltPerUndefinedAddress() throws FileFormatException {
    final Mic1 processor = new Mic1(ClassLoader.getSystemResourceAsStream("mic1/hi-halt-per-null.mic1"),
                                    ClassLoader.getSystemResourceAsStream("mic1/hi.ijvm"));
    testHi(processor);

    processor.reset();
    testRunHi1(processor);

    processor.reset();
    testRunHi2(processor);
  }

  private void testRunHi2(final Mic1 processor) {
    final ByteArrayOutputStream micOut = new ByteArrayOutputStream();
    Output.setOut(new PrintStream(micOut));

    assertThat(processor.isHaltInstruction()).isFalse();
    assertThat(processor.run()).isEqualTo(14);
    assertThat(processor.isHaltInstruction()).isTrue();
    assertThat(processor.run()).isEqualTo(0);
    assertThat(processor.isHaltInstruction()).isTrue();

    assertThat(micOut.toString()).isEqualTo("Hi!\n");
    Output.setOut(System.out);
  }

  private void testRunHi1(final Mic1 processor) {
    final ByteArrayOutputStream micOut = new ByteArrayOutputStream();
    Output.setOut(new PrintStream(micOut));
    Printer.setPrintStream(new PrintStream(out));

    processor.microStep(1);
    assertOneTickDoneAndResetPrintStream(out);
    processor.microStep(3);
    assertThat(out.toString()).isEqualTo(Text.TICKS.text(3) + getLineSeparator());
    out.reset();

    assertThat(processor.isHaltInstruction()).isFalse();
    assertThat(processor.run()).isEqualTo(10);
    assertThat(processor.isHaltInstruction()).isTrue();
    assertThat(processor.run()).isEqualTo(0);
    assertThat(processor.isHaltInstruction()).isTrue();

    assertThat(micOut.toString()).isEqualTo("Hi!\n");
    Output.setOut(System.out);
  }

  private void testHi(final Mic1 processor) {
    final ByteArrayOutputStream micOut = new ByteArrayOutputStream();
    Output.setBuffered(false);
    Output.setOut(new PrintStream(micOut));
    Printer.setPrintStream(new PrintStream(out));

    // 00: MAR = PC = 0; rd; goto 1;
    processor.microStep();
    assertThat(Register.MAR.getValue()).isEqualTo(0);
    assertThat(Register.PC.getValue()).isEqualTo(0);
    assertThat(micOut.toString()).isEmpty();
    assertThat(processor.isHaltInstruction()).isFalse();
    assertOneTickDoneAndResetPrintStream(out);

    // 01: LV = H = -1; goto 2;
    processor.microStep();
    assertThat(Register.MAR.getValue()).isEqualTo(0);
    assertThat(Register.PC.getValue()).isEqualTo(0);
    assertThat(Register.LV.getValue()).isEqualTo(-1);
    assertThat(Register.H.getValue()).isEqualTo(-1);
    assertThat(micOut.toString()).isEmpty();
    assertThat(processor.isHaltInstruction()).isFalse();
    assertOneTickDoneAndResetPrintStream(out);

    // 02: LV = H + LV; goto 3;
    processor.microStep();
    assertThat(Register.MDR.getValue()).isEqualTo('H');
    assertThat(Register.MAR.getValue()).isEqualTo(0);
    assertThat(Register.PC.getValue()).isEqualTo(0);
    assertThat(Register.LV.getValue()).isEqualTo(-2);
    assertThat(Register.H.getValue()).isEqualTo(-1);
    assertThat(micOut.toString()).isEmpty();
    assertThat(processor.isHaltInstruction()).isFalse();
    assertOneTickDoneAndResetPrintStream(out);

    // 03: MAR = LV - 1; wr; goto 4;
    processor.microStep();
    assertThat(Register.MDR.getValue()).isEqualTo('H');
    assertThat(Register.MAR.getValue()).isEqualTo(-3);
    assertThat(Register.PC.getValue()).isEqualTo(0);
    assertThat(Register.LV.getValue()).isEqualTo(-2);
    assertThat(Register.H.getValue()).isEqualTo(-1);
    assertThat(micOut.toString()).isEqualTo("H");
    assertThat(processor.isHaltInstruction()).isFalse();
    assertOneTickDoneAndResetPrintStream(out);

    // 04: MAR = PC = PC + 1; rd; goto 5;
    processor.microStep();
    assertThat(Register.MDR.getValue()).isEqualTo('H');
    assertThat(Register.MAR.getValue()).isEqualTo(1);
    assertThat(Register.PC.getValue()).isEqualTo(1);
    assertThat(Register.LV.getValue()).isEqualTo(-2);
    assertThat(Register.H.getValue()).isEqualTo(-1);
    assertThat(micOut.toString()).isEqualTo("H");
    assertThat(processor.isHaltInstruction()).isFalse();
    assertOneTickDoneAndResetPrintStream(out);

    // 05: MAR = LV - 1; goto 6;
    processor.microStep();
    assertThat(Register.MDR.getValue()).isEqualTo('i');
    assertThat(Register.MAR.getValue()).isEqualTo(-3);
    assertThat(Register.PC.getValue()).isEqualTo(1);
    assertThat(Register.LV.getValue()).isEqualTo(-2);
    assertThat(Register.H.getValue()).isEqualTo(-1);
    assertThat(micOut.toString()).isEqualTo("H");
    assertThat(processor.isHaltInstruction()).isFalse();
    assertOneTickDoneAndResetPrintStream(out);

    // 06: wr; goto 7;
    processor.microStep();
    assertThat(Register.MDR.getValue()).isEqualTo('i');
    assertThat(Register.MAR.getValue()).isEqualTo(-3);
    assertThat(Register.PC.getValue()).isEqualTo(1);
    assertThat(Register.LV.getValue()).isEqualTo(-2);
    assertThat(Register.H.getValue()).isEqualTo(-1);
    assertThat(micOut.toString()).isEqualTo("Hi");
    assertThat(processor.isHaltInstruction()).isFalse();
    assertOneTickDoneAndResetPrintStream(out);

    // 07: MAR = PC = PC + 1; rd; goto 8;
    processor.microStep();
    assertThat(Register.MDR.getValue()).isEqualTo('i');
    assertThat(Register.MAR.getValue()).isEqualTo(2);
    assertThat(Register.PC.getValue()).isEqualTo(2);
    assertThat(Register.LV.getValue()).isEqualTo(-2);
    assertThat(Register.H.getValue()).isEqualTo(-1);
    assertThat(micOut.toString()).isEqualTo("Hi");
    assertThat(processor.isHaltInstruction()).isFalse();
    assertOneTickDoneAndResetPrintStream(out);

    // 08: MAR = LV - 1; goto 9;
    processor.microStep();
    assertThat(Register.MDR.getValue()).isEqualTo('!');
    assertThat(Register.MAR.getValue()).isEqualTo(-3);
    assertThat(Register.PC.getValue()).isEqualTo(2);
    assertThat(Register.LV.getValue()).isEqualTo(-2);
    assertThat(Register.H.getValue()).isEqualTo(-1);
    assertThat(micOut.toString()).isEqualTo("Hi");
    assertThat(processor.isHaltInstruction()).isFalse();
    assertOneTickDoneAndResetPrintStream(out);

    // 09: wr; goto 10;
    processor.microStep();
    assertThat(Register.MDR.getValue()).isEqualTo('!');
    assertThat(Register.MAR.getValue()).isEqualTo(-3);
    assertThat(Register.PC.getValue()).isEqualTo(2);
    assertThat(Register.LV.getValue()).isEqualTo(-2);
    assertThat(Register.H.getValue()).isEqualTo(-1);
    assertThat(micOut.toString()).isEqualTo("Hi!");
    assertThat(processor.isHaltInstruction()).isFalse();
    assertOneTickDoneAndResetPrintStream(out);

    // 10: MAR = PC = PC + 1; rd; goto 11;
    processor.microStep();
    assertThat(Register.MDR.getValue()).isEqualTo('!');
    assertThat(Register.MAR.getValue()).isEqualTo(3);
    assertThat(Register.PC.getValue()).isEqualTo(3);
    assertThat(Register.LV.getValue()).isEqualTo(-2);
    assertThat(Register.H.getValue()).isEqualTo(-1);
    assertThat(micOut.toString()).isEqualTo("Hi!");
    assertThat(processor.isHaltInstruction()).isFalse();
    assertOneTickDoneAndResetPrintStream(out);

    // 11: MAR = LV - 1; goto 12;
    processor.microStep();
    assertThat(Register.MDR.getValue()).isEqualTo('\n');
    assertThat(Register.MAR.getValue()).isEqualTo(-3);
    assertThat(Register.PC.getValue()).isEqualTo(3);
    assertThat(Register.LV.getValue()).isEqualTo(-2);
    assertThat(Register.H.getValue()).isEqualTo(-1);
    assertThat(micOut.toString()).isEqualTo("Hi!");
    assertThat(processor.isHaltInstruction()).isFalse();
    assertOneTickDoneAndResetPrintStream(out);

    // 12: wr; goto 13;
    processor.microStep();
    assertThat(Register.MDR.getValue()).isEqualTo('\n');
    assertThat(Register.MAR.getValue()).isEqualTo(-3);
    assertThat(Register.PC.getValue()).isEqualTo(3);
    assertThat(Register.LV.getValue()).isEqualTo(-2);
    assertThat(Register.H.getValue()).isEqualTo(-1);
    assertThat(micOut.toString()).isEqualTo("Hi!\n");
    assertThat(processor.isHaltInstruction()).isFalse();
    assertOneTickDoneAndResetPrintStream(out);

    // 13: halt;
    processor.microStep();
    assertThat(Register.MDR.getValue()).isEqualTo('\n');
    assertThat(Register.MAR.getValue()).isEqualTo(-3);
    assertThat(Register.PC.getValue()).isEqualTo(3);
    assertThat(Register.LV.getValue()).isEqualTo(-2);
    assertThat(Register.H.getValue()).isEqualTo(-1);
    assertThat(micOut.toString()).isEqualTo("Hi!\n");
    assertThat(processor.isHaltInstruction()).isTrue();
    assertOneTickDoneAndResetPrintStream(out);

    processor.microStep();
    assertThat(out.toString()).isEmpty();

    processor.microStep(15);
    assertThat(out.toString()).isEmpty();

    Output.setOut(System.out);
  }

  private void assertOneTickDoneAndResetPrintStream(final ByteArrayOutputStream stdOut) {
    assertThat(stdOut.toString()).isEqualTo(Text.TICKS.text(1) + getLineSeparator());
    stdOut.reset();
  }

  @Test
  public void testErrorTexts() {
    // less than four bytes
    try {
      new Mic1(new ByteArrayInputStream(new byte[] {}), new ByteArrayInputStream(new byte[] {}));
      throw new AssertionError("should throw exception");
    } catch (final FileFormatException e) {
      // expected
    }
    assertThat(out.toString()).isEqualTo(Text.ERROR.text(Text.WRONG_FORMAT_MIC1.text(Text.WRONG_FORMAT_TOO_SMALL))
                                                 + getLineSeparator()
                                                 + Text.ERROR.text(Text.WRONG_FORMAT_IJVM
                                                   .text(Text.WRONG_FORMAT_TOO_SMALL)) + getLineSeparator());
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
                                                 + getLineSeparator()
                                                 + Text.ERROR.text(Text.WRONG_FORMAT_IJVM
                                                   .text(Text.WRONG_FORMAT_MAGIC_NUMBER + getLineSeparator())));
    out.reset();

    // empty files
    try {
      new Mic1(new ByteArrayInputStream(new byte[] { 0x12, 0x34, 0x56, 0x78 }),
               new ByteArrayInputStream(new byte[] { 0x1d, (byte) 0xea, (byte) 0xdf, (byte) 0xad }));
      throw new AssertionError("should throw exception");
    } catch (final FileFormatException e) {
      // expected
    }
    assertThat(out.toString()).isEqualTo(Text.ERROR.text(Text.WRONG_FORMAT_MIC1.text(Text.WRONG_FORMAT_EMPTY))
                                                 + getLineSeparator());
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
                                                 + getLineSeparator());
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
                                           .text(Text.WRONG_FORMAT_UNEXPECTED_END_OF_BLOCK)) + getLineSeparator());
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
    assertThat(out.toString()).isEqualTo(Text.ERROR.text(Text.WRONG_FORMAT_MIC1.text(Text.WRONG_FORMAT_TOO_BIG))
                                                 + getLineSeparator());
    out.reset();
  }

  @Test
  public final void testListAllRegisters() throws IOException {
    Register.MAR.setValue(-1);
    Register.MDR.setValue(0);
    Register.PC.setValue(1);
    Register.MBR.setValue(0x1273);
    Register.SP.setValue(0x8bc);
    Register.LV.setValue(0x8bd);
    Register.CPP.setValue(0x8be);
    Register.TOS.setValue(0x8bf);
    Register.OPC.setValue(0x8c0);
    Register.H.setValue(0x8c1);

    assertThat(out.toString()).isEmpty();
    this.processor.listAllRegisters();

    assertThat(out.toString()).isEqualTo(Text.REGISTER_VALUE.text("MAR ", "0xFFFFFFFF") + getLineSeparator()
                                                 + Text.REGISTER_VALUE.text("MDR ", "0x0") + getLineSeparator()
                                                 + Text.REGISTER_VALUE.text("PC  ", "0x1") + getLineSeparator()
                                                 + Text.REGISTER_VALUE.text("MBR ", "0x73") + getLineSeparator()
                                                 + Text.REGISTER_VALUE.text("MBRU", "0x73") + getLineSeparator()
                                                 + Text.REGISTER_VALUE.text("SP  ", "0x8BC") + getLineSeparator()
                                                 + Text.REGISTER_VALUE.text("LV  ", "0x8BD") + getLineSeparator()
                                                 + Text.REGISTER_VALUE.text("CPP ", "0x8BE") + getLineSeparator()
                                                 + Text.REGISTER_VALUE.text("TOS ", "0x8BF") + getLineSeparator()
                                                 + Text.REGISTER_VALUE.text("OPC ", "0x8C0") + getLineSeparator()
                                                 + Text.REGISTER_VALUE.text("H   ", "0x8C1") + getLineSeparator());
  }

  @Test
  public final void testListSingleRegister() throws IOException {
    Register.MAR.setValue(0x4711);
    this.processor.listSingleRegister(Register.MAR);
    assertThat(out.toString()).isEqualTo(Text.REGISTER_VALUE.text("MAR ", "0x4711") + getLineSeparator());
    out.reset();

    Register.SP.setValue(-2);
    this.processor.listSingleRegister(Register.SP);
    assertThat(out.toString()).isEqualTo(Text.REGISTER_VALUE.text("SP  ", "0xFFFFFFFE") + getLineSeparator());
    out.reset();
  }

  @Test
  public final void testTraceMicro() throws IOException {
    final String firstLine = Text.EXECUTED_CODE.text("PC=MAR=0;rd;goto 0x1") + getLineSeparator();
    final String expected = firstLine + Text.EXECUTED_CODE.text("H=LV=-1;goto 0x2") + getLineSeparator()
                            + Text.EXECUTED_CODE.text("LV=H+LV;wr;goto 0x3") + getLineSeparator()
                            + Text.EXECUTED_CODE.text("MAR=LV-1;wr;goto 0x4") + getLineSeparator()
                            + Text.EXECUTED_CODE.text("PC=MAR=PC+1;rd;goto 0x5") + getLineSeparator()
                            + Text.EXECUTED_CODE.text("MAR=LV-1;goto 0x6") + getLineSeparator()
                            + Text.EXECUTED_CODE.text("wr;goto 0x7") + getLineSeparator()
                            + Text.EXECUTED_CODE.text("PC=MAR=PC+1;rd;goto 0x8") + getLineSeparator()
                            + Text.EXECUTED_CODE.text("MAR=LV-1;goto 0x9") + getLineSeparator()
                            + Text.EXECUTED_CODE.text("wr;goto 0xA") + getLineSeparator()
                            + Text.EXECUTED_CODE.text("PC=MAR=PC+1;rd;goto 0xB") + getLineSeparator()
                            + Text.EXECUTED_CODE.text("MAR=LV-1;goto 0xC") + getLineSeparator()
                            + Text.EXECUTED_CODE.text("wr;goto 0xD") + getLineSeparator()
                            + Text.EXECUTED_CODE.text("goto 0xD") + getLineSeparator() + Text.TICKS.text(14)
                            + getLineSeparator();

    this.processor.run();
    assertThat(out.toString()).isEqualTo(Text.TICKS.text(14) + getLineSeparator());
    out.reset();

    this.processor.reset();

    this.processor.traceMicro();
    this.processor.run();
    assertThat(out.toString()).isEqualTo(expected);

    this.processor.reset();
    out.reset();

    this.processor.traceMicro();
    this.processor.microStep();
    assertThat(out.toString()).isEqualTo(firstLine + Text.TICKS.text(1) + getLineSeparator());
    out.reset();

    this.processor.untraceMicro();
    this.processor.run();
    assertThat(out.toString()).isEqualTo(Text.TICKS.text(13) + getLineSeparator());
  }

  @Test
  public void testRegisterBreakPoint() {
    this.processor.addBreakpoint(Register.H, Integer.valueOf(-1));
    this.processor.run();
    assertThat(out.toString()).isEqualTo(Text.TICKS.text(2) + getLineSeparator());

    out.reset();
    this.processor.run();
    assertThat(out.toString()).isEqualTo(Text.TICKS.text(1) + getLineSeparator());

    out.reset();
    this.processor.reset();
    this.processor.microStep();
    assertThat(out.toString()).isEqualTo(Text.TICKS.text(1) + getLineSeparator());

    out.reset();
    this.processor.microStep();
    assertThat(out.toString()).isEqualTo(Text.TICKS.text(1) + getLineSeparator());

    out.reset();
    this.processor.microStep();
    assertThat(out.toString()).isEqualTo(Text.TICKS.text(1) + getLineSeparator());
  }
}
