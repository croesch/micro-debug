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
package com.github.croesch.micro_debug.mic1;

import static org.fest.assertions.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import org.junit.Test;

import com.github.croesch.micro_debug.DefaultTestCase;
import com.github.croesch.micro_debug.commons.Printer;
import com.github.croesch.micro_debug.console.Mic1Interpreter;
import com.github.croesch.micro_debug.error.FileFormatException;
import com.github.croesch.micro_debug.error.MacroFileFormatException;
import com.github.croesch.micro_debug.error.MicroFileFormatException;
import com.github.croesch.micro_debug.i18n.Text;
import com.github.croesch.micro_debug.mic1.io.Input;
import com.github.croesch.micro_debug.mic1.io.Output;
import com.github.croesch.micro_debug.mic1.register.Register;
import com.github.croesch.micro_debug.settings.Settings;

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
    init("mic1/hi.mic1", "mic1/hi.ijvm");
  }

  private void init(final String micFile, final String ijvmFile) throws FileFormatException {
    this.processor = new Mic1(ClassLoader.getSystemResourceAsStream(micFile),
                              ClassLoader.getSystemResourceAsStream(ijvmFile));
    new Mic1Interpreter(this.processor);
  }

  @Test(timeout = 1000)
  public void testPerformanceOfProcessor() throws FileFormatException {
    printlnMethodName();
    final Mic1 processor = new Mic1(ClassLoader.getSystemResourceAsStream("mic1/performance.mic1"),
                                    ClassLoader.getSystemResourceAsStream("mic1/empty.ijvm"));
    new Mic1Interpreter(processor);

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
    printlnMethodName();
    final ByteArrayOutputStream out = new ByteArrayOutputStream();
    Output.setOut(new PrintStream(out));

    init("mic1/mic1ijvm2.mic1", "mic1/divtest.ijvm");
    assertThat(this.processor.run()).isEqualTo(3965);

    assertThat(out.toString()).isEqualTo("11111111111111111111111110000000\n");
    Output.setOut(System.out);
  }

  @Test
  public void testIJVM() throws FileFormatException {
    printlnMethodName();
    final ByteArrayOutputStream out = new ByteArrayOutputStream();
    Output.setOut(new PrintStream(out));
    Output.setBuffered(false);

    init("mic1/mic1ijvm.mic1", "mic1/ijvmtest.ijvm");
    assertThat(this.processor.run()).isEqualTo(31816);

    assertThat(out.toString()).isEqualTo("OK");
    Output.setOut(System.out);
  }

  @Test
  public void testMicroStepN() {
    printlnMethodName();
    this.processor.microStep(200);
    assertThat(out.toString()).isEqualTo(Text.TICKS.text(14) + getLineSeparator());

    out.reset();
    this.processor.reset();
    this.processor.microStep(200);
    assertThat(out.toString()).isEqualTo(Text.TICKS.text(14) + getLineSeparator());
  }

  @Test
  public void testMicroStepOne() {
    printMethodName();
    for (int i = 0; i < 14; ++i) {
      assertThat(this.processor.getNextMpc()).isEqualTo(i);
      this.processor.microStep();
      assertThat(this.processor.getOldMpc()).isEqualTo(i);
      assertThat(out.toString()).isEqualTo(Text.TICKS.text(1) + getLineSeparator());
      out.reset();
      printStep();
    }
    printLoopEnd();
    this.processor.microStep();
    assertThat(out.toString()).isEmpty();

    this.processor.reset();
    for (int i = 0; i < 14; ++i) {
      this.processor.microStep();
      assertThat(out.toString()).isEqualTo(Text.TICKS.text(1) + getLineSeparator());
      out.reset();
      printStep();
    }
    this.processor.microStep();
    assertThat(out.toString()).isEmpty();
    printEndOfMethod();
  }

  @Test
  public void testHi() throws FileFormatException {
    printlnMethodName();
    testHi(this.processor);

    this.processor.reset();
    testRunHi1(this.processor);

    this.processor.reset();
    testRunHi2(this.processor);
  }

  @Test
  public void testHaltPerUndefinedAddress() throws FileFormatException {
    printlnMethodName();
    init("mic1/hi-halt-per-null.mic1", "mic1/hi.ijvm");
    testHi(this.processor);

    this.processor.reset();
    testRunHi1(this.processor);

    this.processor.reset();
    testRunHi2(this.processor);
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
    assertTicksDoneAndResetPrintStream(1);
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
    assertTicksDoneAndResetPrintStream(1);

    // 01: LV = H = -1; goto 2;
    processor.microStep();
    assertThat(Register.MAR.getValue()).isEqualTo(0);
    assertThat(Register.PC.getValue()).isEqualTo(0);
    assertThat(Register.LV.getValue()).isEqualTo(-1);
    assertThat(Register.H.getValue()).isEqualTo(-1);
    assertThat(micOut.toString()).isEmpty();
    assertThat(processor.isHaltInstruction()).isFalse();
    assertTicksDoneAndResetPrintStream(1);

    // 02: LV = H + LV; goto 3;
    processor.microStep();
    assertThat(Register.MDR.getValue()).isEqualTo('H');
    assertThat(Register.MAR.getValue()).isEqualTo(0);
    assertThat(Register.PC.getValue()).isEqualTo(0);
    assertThat(Register.LV.getValue()).isEqualTo(-2);
    assertThat(Register.H.getValue()).isEqualTo(-1);
    assertThat(micOut.toString()).isEmpty();
    assertThat(processor.isHaltInstruction()).isFalse();
    assertTicksDoneAndResetPrintStream(1);

    // 03: MAR = LV - 1; wr; goto 4;
    processor.microStep();
    assertThat(Register.MDR.getValue()).isEqualTo('H');
    assertThat(Register.MAR.getValue()).isEqualTo(-3);
    assertThat(Register.PC.getValue()).isEqualTo(0);
    assertThat(Register.LV.getValue()).isEqualTo(-2);
    assertThat(Register.H.getValue()).isEqualTo(-1);
    assertThat(micOut.toString()).isEqualTo("H");
    assertThat(processor.isHaltInstruction()).isFalse();
    assertTicksDoneAndResetPrintStream(1);

    // 04: MAR = PC = PC + 1; rd; goto 5;
    processor.microStep();
    assertThat(Register.MDR.getValue()).isEqualTo('H');
    assertThat(Register.MAR.getValue()).isEqualTo(1);
    assertThat(Register.PC.getValue()).isEqualTo(1);
    assertThat(Register.LV.getValue()).isEqualTo(-2);
    assertThat(Register.H.getValue()).isEqualTo(-1);
    assertThat(micOut.toString()).isEqualTo("H");
    assertThat(processor.isHaltInstruction()).isFalse();
    assertTicksDoneAndResetPrintStream(1);

    // 05: MAR = LV - 1; goto 6;
    processor.microStep();
    assertThat(Register.MDR.getValue()).isEqualTo('i');
    assertThat(Register.MAR.getValue()).isEqualTo(-3);
    assertThat(Register.PC.getValue()).isEqualTo(1);
    assertThat(Register.LV.getValue()).isEqualTo(-2);
    assertThat(Register.H.getValue()).isEqualTo(-1);
    assertThat(micOut.toString()).isEqualTo("H");
    assertThat(processor.isHaltInstruction()).isFalse();
    assertTicksDoneAndResetPrintStream(1);

    // 06: wr; goto 7;
    processor.microStep();
    assertThat(Register.MDR.getValue()).isEqualTo('i');
    assertThat(Register.MAR.getValue()).isEqualTo(-3);
    assertThat(Register.PC.getValue()).isEqualTo(1);
    assertThat(Register.LV.getValue()).isEqualTo(-2);
    assertThat(Register.H.getValue()).isEqualTo(-1);
    assertThat(micOut.toString()).isEqualTo("Hi");
    assertThat(processor.isHaltInstruction()).isFalse();
    assertTicksDoneAndResetPrintStream(1);

    // 07: MAR = PC = PC + 1; rd; goto 8;
    processor.microStep();
    assertThat(Register.MDR.getValue()).isEqualTo('i');
    assertThat(Register.MAR.getValue()).isEqualTo(2);
    assertThat(Register.PC.getValue()).isEqualTo(2);
    assertThat(Register.LV.getValue()).isEqualTo(-2);
    assertThat(Register.H.getValue()).isEqualTo(-1);
    assertThat(micOut.toString()).isEqualTo("Hi");
    assertThat(processor.isHaltInstruction()).isFalse();
    assertTicksDoneAndResetPrintStream(1);

    // 08: MAR = LV - 1; goto 9;
    processor.microStep();
    assertThat(Register.MDR.getValue()).isEqualTo('!');
    assertThat(Register.MAR.getValue()).isEqualTo(-3);
    assertThat(Register.PC.getValue()).isEqualTo(2);
    assertThat(Register.LV.getValue()).isEqualTo(-2);
    assertThat(Register.H.getValue()).isEqualTo(-1);
    assertThat(micOut.toString()).isEqualTo("Hi");
    assertThat(processor.isHaltInstruction()).isFalse();
    assertTicksDoneAndResetPrintStream(1);

    // 09: wr; goto 10;
    processor.microStep();
    assertThat(Register.MDR.getValue()).isEqualTo('!');
    assertThat(Register.MAR.getValue()).isEqualTo(-3);
    assertThat(Register.PC.getValue()).isEqualTo(2);
    assertThat(Register.LV.getValue()).isEqualTo(-2);
    assertThat(Register.H.getValue()).isEqualTo(-1);
    assertThat(micOut.toString()).isEqualTo("Hi!");
    assertThat(processor.isHaltInstruction()).isFalse();
    assertTicksDoneAndResetPrintStream(1);

    // 10: MAR = PC = PC + 1; rd; goto 11;
    processor.microStep();
    assertThat(Register.MDR.getValue()).isEqualTo('!');
    assertThat(Register.MAR.getValue()).isEqualTo(3);
    assertThat(Register.PC.getValue()).isEqualTo(3);
    assertThat(Register.LV.getValue()).isEqualTo(-2);
    assertThat(Register.H.getValue()).isEqualTo(-1);
    assertThat(micOut.toString()).isEqualTo("Hi!");
    assertThat(processor.isHaltInstruction()).isFalse();
    assertTicksDoneAndResetPrintStream(1);

    // 11: MAR = LV - 1; goto 12;
    processor.microStep();
    assertThat(Register.MDR.getValue()).isEqualTo('\n');
    assertThat(Register.MAR.getValue()).isEqualTo(-3);
    assertThat(Register.PC.getValue()).isEqualTo(3);
    assertThat(Register.LV.getValue()).isEqualTo(-2);
    assertThat(Register.H.getValue()).isEqualTo(-1);
    assertThat(micOut.toString()).isEqualTo("Hi!");
    assertThat(processor.isHaltInstruction()).isFalse();
    assertTicksDoneAndResetPrintStream(1);

    // 12: wr; goto 13;
    processor.microStep();
    assertThat(Register.MDR.getValue()).isEqualTo('\n');
    assertThat(Register.MAR.getValue()).isEqualTo(-3);
    assertThat(Register.PC.getValue()).isEqualTo(3);
    assertThat(Register.LV.getValue()).isEqualTo(-2);
    assertThat(Register.H.getValue()).isEqualTo(-1);
    assertThat(micOut.toString()).isEqualTo("Hi!\n");
    assertThat(processor.isHaltInstruction()).isFalse();
    assertTicksDoneAndResetPrintStream(1);

    // 13: halt;
    processor.microStep();
    assertThat(Register.MDR.getValue()).isEqualTo('\n');
    assertThat(Register.MAR.getValue()).isEqualTo(-3);
    assertThat(Register.PC.getValue()).isEqualTo(3);
    assertThat(Register.LV.getValue()).isEqualTo(-2);
    assertThat(Register.H.getValue()).isEqualTo(-1);
    assertThat(micOut.toString()).isEqualTo("Hi!\n");
    assertThat(processor.isHaltInstruction()).isTrue();
    assertTicksDoneAndResetPrintStream(1);

    processor.microStep();
    assertThat(out.toString()).isEmpty();

    processor.microStep(15);
    assertThat(out.toString()).isEmpty();

    Output.setOut(System.out);
  }

  @Test
  public void testErrorTexts() {
    printlnMethodName();
    // less than four bytes
    try {
      new Mic1(new ByteArrayInputStream(new byte[] {}), new ByteArrayInputStream(new byte[] {}));
      throw new AssertionError("should throw exception");
    } catch (final MicroFileFormatException e) {
      // expected
    } catch (final MacroFileFormatException e) {
      throw new AssertionError("should throw other exception");
    }
    assertThat(out.toString()).isEmpty();

    // wrong magic number
    try {
      new Mic1(new ByteArrayInputStream(new byte[] { 0x1d, (byte) 0xea, (byte) 0xdf, (byte) 0xad }),
               new ByteArrayInputStream(new byte[] { 0x12, 0x34, 0x56, 0x78 }));
      throw new AssertionError("should throw exception");
    } catch (final MicroFileFormatException e) {
      // expected
    } catch (final MacroFileFormatException e) {
      throw new AssertionError("should throw other exception");
    }
    assertThat(out.toString()).isEmpty();

    // empty files
    try {
      new Mic1(new ByteArrayInputStream(new byte[] { 0x12, 0x34, 0x56, 0x78 }),
               new ByteArrayInputStream(new byte[] { 0x1d, (byte) 0xea, (byte) 0xdf, (byte) 0xad }));
      throw new AssertionError("should throw exception");
    } catch (final MicroFileFormatException e) {
      // expected
    } catch (final MacroFileFormatException e) {
      throw new AssertionError("should throw other exception");
    }
    assertThat(out.toString()).isEmpty();

    // unexpected eof
    try {
      new Mic1(new ByteArrayInputStream(new byte[] { 0x12, 0x34, 0x56, 0x78, 0, 0, 0, 0, 0 }),
               new ByteArrayInputStream(new byte[] { 0x1d, (byte) 0xea, (byte) 0xdf, (byte) 0xad, 0 }));
      throw new AssertionError("should throw exception");
    } catch (final MicroFileFormatException e) {
      throw new AssertionError("should throw other exception");
    } catch (final MacroFileFormatException e) {
      // expected
    }
    assertThat(out.toString()).isEmpty();

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
    } catch (final MicroFileFormatException e) {
      throw new AssertionError("should throw other exception");
    } catch (final MacroFileFormatException e) {
      // expected
    }
    assertThat(out.toString()).isEmpty();

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
    } catch (final MicroFileFormatException e) {
      // expected
    } catch (final MacroFileFormatException e) {
      throw new AssertionError("should throw other exception");
    }
    assertThat(out.toString()).isEmpty();
  }

  @Test
  public void testStep() throws FileFormatException {
    printlnMethodName();
    init("mic1/mic1ijvm.mic1", "mic1/add.ijvm");
    assertThat(Register.PC.getValue()).isEqualTo(Settings.MIC1_REGISTER_PC_DEFVAL.getValue());

    this.processor.step();
    assertThat(Register.PC.getValue()).isEqualTo(0);
    assertTicksDoneAndResetPrintStream(3);

    this.processor.step();
    assertThat(Register.PC.getValue()).isEqualTo(2);
    assertTicksDoneAndResetPrintStream(4);

    this.processor.step();
    assertThat(Register.PC.getValue()).isEqualTo(3);
    assertTicksDoneAndResetPrintStream(3);

    this.processor.step();
    assertThat(Register.PC.getValue()).isEqualTo(5);
    assertTicksDoneAndResetPrintStream(7);

    this.processor.step();
    assertThat(Register.PC.getValue()).isEqualTo(7);
    assertTicksDoneAndResetPrintStream(7);

    this.processor.step();
    assertThat(Register.PC.getValue()).isEqualTo(9);
    assertTicksDoneAndResetPrintStream(4);

    this.processor.step();
    assertThat(Register.PC.getValue()).isEqualTo(10);
    assertTicksDoneAndResetPrintStream(9);

    this.processor.step();
    assertThat(Register.PC.getValue()).isEqualTo(13);
    assertTicksDoneAndResetPrintStream(8);

    this.processor.step();
    assertThat(Register.PC.getValue()).isEqualTo(71);
    assertTicksDoneAndResetPrintStream(23);
  }

  @Test
  public void testStepN() throws FileFormatException {
    printlnMethodName();
    init("mic1/mic1ijvm.mic1", "mic1/add.ijvm");
    Input.setIn(new ByteArrayInputStream("2\n2\n".getBytes()));
    Output.setBuffered(true);
    Output.setOut(new PrintStream(out));
    assertThat(Register.PC.getValue()).isEqualTo(Settings.MIC1_REGISTER_PC_DEFVAL.getValue());

    this.processor.step(0);
    assertThat(Register.PC.getValue()).isEqualTo(Settings.MIC1_REGISTER_PC_DEFVAL.getValue());
    assertThat(out.toString()).isEmpty();

    this.processor.step(1);
    assertThat(Register.PC.getValue()).isEqualTo(0);
    assertTicksDoneAndResetPrintStream(3);

    this.processor.step(2);
    assertThat(Register.PC.getValue()).isEqualTo(3);
    assertTicksDoneAndResetPrintStream(7);

    this.processor.step(3);
    assertThat(Register.PC.getValue()).isEqualTo(9);
    assertTicksDoneAndResetPrintStream(18);

    this.processor.step(-1);
    assertThat(Register.PC.getValue()).isEqualTo(9);
    assertThat(out.toString()).isEmpty();

    this.processor.step(0);
    assertThat(Register.PC.getValue()).isEqualTo(9);
    assertThat(out.toString()).isEmpty();

    this.processor.step(1);
    assertThat(Register.PC.getValue()).isEqualTo(10);
    assertTicksDoneAndResetPrintStream(9);

    this.processor.step(2);
    assertThat(Register.PC.getValue()).isEqualTo(71);
    assertTicksDoneAndResetPrintStream(31);

    this.processor.step(560);
    assertThat(Register.PC.getValue()).isEqualTo(0x11D);
    assertThat(out.toString()).isEqualTo(Text.INPUT_MIC1.text() + " 2\n" + Text.INPUT_MIC1.text()
                                                 + "+2\n========\n00000004\n" + Text.TICKS.text(3213)
                                                 + getLineSeparator());
    out.reset();

    this.processor.step(560);
    assertThat(Register.PC.getValue()).isEqualTo(0x41);
    assertTicksDoneAndResetPrintStream(11);

    this.processor.step(560000);
    assertThat(Register.PC.getValue()).isEqualTo(0x41);
    assertThat(out.toString()).isEmpty();

    Output.setOut(System.out);
  }

  @Test
  public final void testReset_Input() throws IOException {
    printlnMethodName();
    Input.setIn(new ByteArrayInputStream("2\n2\n2\n2\n".getBytes()));
    init("mic1/mic1ijvm.mic1", "mic1/add.ijvm");

    this.processor.step(38);
    assertThat(out.toString()).isEqualTo(Text.INPUT_MIC1.text() + Text.TICKS.text(208) + getLineSeparator());
    out.reset();
    this.processor.reset();
    this.processor.step(38);
    assertThat(out.toString()).isEqualTo(Text.INPUT_MIC1.text() + Text.TICKS.text(208) + getLineSeparator());
    out.reset();
  }

  @Test
  public final void testReset_Output() throws IOException {
    printlnMethodName();
    Input.setIn(new ByteArrayInputStream("2\n2\n2\n2\n".getBytes()));
    init("mic1/mic1ijvm.mic1", "mic1/add.ijvm");

    this.processor.step(38);
    assertThat(out.toString()).isEqualTo(Text.INPUT_MIC1.text() + Text.TICKS.text(208) + getLineSeparator());
    assertThat(micOut.toString()).isEmpty();
    out.reset();
    this.processor.reset();
    this.processor.run();
    assertThat(out.toString()).isEqualTo(Text.INPUT_MIC1.text() + Text.INPUT_MIC1.text() + Text.TICKS.text(3292)
                                                 + getLineSeparator());
    assertThat(micOut.toString()).isEqualTo(" 2\n+2\n========\n00000004\n");
    out.reset();
  }

  @Test
  public void testEquals() throws IOException {
    printlnMethodName();
    InputStream micFile = getClass().getClassLoader().getResourceAsStream("mic1/mic1ijvm.mic1");
    InputStream macFile = getClass().getClassLoader().getResourceAsStream("mic1/add.ijvm");
    final Mic1 one = new Mic1(micFile, macFile);
    new Mic1Interpreter(one);
    micFile.close();
    macFile.close();

    micFile = getClass().getClassLoader().getResourceAsStream("mic1/mic1ijvm.mic1");
    macFile = getClass().getClassLoader().getResourceAsStream("mic1/add.ijvm");
    final Mic1 two = new Mic1(micFile, macFile);
    new Mic1Interpreter(two);
    micFile.close();
    macFile.close();

    assertThat(one).isEqualTo(one);
    assertThat(two).isEqualTo(two);
    assertThat(one).isEqualTo(two);
    assertThat(one).isNotEqualTo(null);
    assertThat(one).isNotEqualTo(one.toString());
    assertThat(one).isNotEqualTo(this.processor);
    assertThat(two).isNotEqualTo(this.processor);

    one.step();

    assertThat(one).isNotEqualTo(two);
    assertThat(two).isNotEqualTo(one);

    two.step(2);

    assertThat(one).isNotEqualTo(two);
    assertThat(two).isNotEqualTo(one);

    one.step(1);

    assertThat(one).isEqualTo(one);
    assertThat(two).isEqualTo(two);

    one.setMemoryValue(12, 3);

    assertThat(one).isNotEqualTo(two);
    assertThat(two).isNotEqualTo(one);

    two.setMemoryValue(12, 3);

    assertThat(one).isEqualTo(one);
    assertThat(two).isEqualTo(two);
  }

  @Test
  public void testMpc() throws FileFormatException {
    printlnMethodName();
    init("mic1/mic1ijvm.mic1", "mic1/add.ijvm");

    assertThat(this.processor.getNextMpc()).isEqualTo(0);
    assertThat(this.processor.getOldMpc()).isEqualTo(-1);
    this.processor.microStep();

    assertThat(this.processor.getNextMpc()).isEqualTo(2);
    assertThat(this.processor.getOldMpc()).isEqualTo(0);
    this.processor.microStep();

    assertThat(this.processor.getNextMpc()).isEqualTo(0);
    assertThat(this.processor.getOldMpc()).isEqualTo(2);
    this.processor.microStep();

    assertThat(this.processor.getNextMpc()).isEqualTo(2);
    assertThat(this.processor.getOldMpc()).isEqualTo(0);
    this.processor.microStep();

    assertThat(this.processor.getNextMpc()).isEqualTo(16);
    assertThat(this.processor.getOldMpc()).isEqualTo(2);
    this.processor.microStep();

    assertThat(this.processor.getNextMpc()).isEqualTo(22);
    assertThat(this.processor.getOldMpc()).isEqualTo(16);
    this.processor.microStep();

    assertThat(this.processor.getNextMpc()).isEqualTo(23);
    assertThat(this.processor.getOldMpc()).isEqualTo(22);
    this.processor.microStep();

    assertThat(this.processor.getNextMpc()).isEqualTo(2);
    assertThat(this.processor.getOldMpc()).isEqualTo(23);
    this.processor.microStep();

    assertThat(this.processor.getNextMpc()).isEqualTo(89);
    assertThat(this.processor.getOldMpc()).isEqualTo(2);
    this.processor.microStep();
  }

  @Test
  public void testMacroAddress() throws FileFormatException {
    printlnMethodName();
    init("mic1/mic1ijvm.mic1", "mic1/add.ijvm");

    assertThat(this.processor.getNextMacroAddress()).isEqualTo(-1);
    assertThat(this.processor.getLastMacroAddress()).isEqualTo(-1);
    this.processor.microStep();

    assertThat(this.processor.getNextMacroAddress()).isEqualTo(-1);
    assertThat(this.processor.getLastMacroAddress()).isEqualTo(-1);
    this.processor.microStep();

    assertThat(this.processor.getNextMacroAddress()).isEqualTo(0);
    assertThat(this.processor.getLastMacroAddress()).isEqualTo(-1);
    this.processor.microStep();

    assertThat(this.processor.getNextMacroAddress()).isEqualTo(0);
    assertThat(this.processor.getLastMacroAddress()).isEqualTo(-1);
    this.processor.microStep();

    assertThat(this.processor.getNextMacroAddress()).isEqualTo(1);
    assertThat(this.processor.getLastMacroAddress()).isEqualTo(0);
    this.processor.microStep();

    assertThat(this.processor.getNextMacroAddress()).isEqualTo(1);
    assertThat(this.processor.getLastMacroAddress()).isEqualTo(0);
    this.processor.microStep();

    assertThat(this.processor.getNextMacroAddress()).isEqualTo(2);
    assertThat(this.processor.getLastMacroAddress()).isEqualTo(0);
    this.processor.microStep();

    assertThat(this.processor.getNextMacroAddress()).isEqualTo(2);
    assertThat(this.processor.getLastMacroAddress()).isEqualTo(0);
    this.processor.microStep();

    assertThat(this.processor.getNextMacroAddress()).isEqualTo(3);
    assertThat(this.processor.getLastMacroAddress()).isEqualTo(2);
    this.processor.microStep();

    assertThat(this.processor.getNextMacroAddress()).isEqualTo(3);
    assertThat(this.processor.getLastMacroAddress()).isEqualTo(2);
    this.processor.step();

    assertThat(this.processor.getNextMacroAddress()).isEqualTo(3);
    assertThat(this.processor.getLastMacroAddress()).isEqualTo(2);
    this.processor.step();

    assertThat(this.processor.getNextMacroAddress()).isEqualTo(5);
    assertThat(this.processor.getLastMacroAddress()).isEqualTo(3);
    this.processor.step();

    assertThat(this.processor.getNextMacroAddress()).isEqualTo(7);
    assertThat(this.processor.getLastMacroAddress()).isEqualTo(5);
    this.processor.step();

    assertThat(this.processor.getNextMacroAddress()).isEqualTo(9);
    assertThat(this.processor.getLastMacroAddress()).isEqualTo(7);
    this.processor.microStep();
    assertThat(this.processor.getNextMacroAddress()).isEqualTo(10);
    assertThat(this.processor.getLastMacroAddress()).isEqualTo(9);
  }

  @Test
  public void testInterrupted() throws FileFormatException, InterruptedException {
    printlnMethodName();
    init("mic1/mic1ijvm.mic1", "mic1/add.ijvm");

    assertThat(this.processor.isInterrupted()).isFalse();
    this.processor.interrupt();
    assertThat(this.processor.isInterrupted()).isTrue();

    this.processor.microStep();
    assertThat(this.processor.isInterrupted()).isFalse();
    this.processor.interrupt();
    assertThat(this.processor.isInterrupted()).isTrue();

    this.processor.microStep(2);
    assertThat(this.processor.isInterrupted()).isFalse();
    this.processor.interrupt();
    assertThat(this.processor.isInterrupted()).isTrue();

    this.processor.step();
    assertThat(this.processor.isInterrupted()).isFalse();
    this.processor.interrupt();
    assertThat(this.processor.isInterrupted()).isTrue();

    this.processor.step(2);
    assertThat(this.processor.isInterrupted()).isFalse();
    this.processor.interrupt();
    assertThat(this.processor.isInterrupted()).isTrue();

    new Thread(new Runnable() {
      public void run() {
        Mic1Test.this.processor.run();
      }
    }).start();
    Thread.sleep(10);
    assertThat(this.processor.isInterrupted()).isFalse();
    this.processor.interrupt();
    assertThat(this.processor.isInterrupted()).isTrue();
  }

  int ticks = Integer.MAX_VALUE;

  @Test
  public void testInterrupt() throws InterruptedException, FileFormatException {
    printlnMethodName();

    init("mic1/mic1ijvm2.mic1", "mic1/divtest.ijvm");

    final ReentrantLock lock = new ReentrantLock();
    final Condition condition = lock.newCondition();
    lock.lock();

    new Thread(new Runnable() {
      public void run() {
        lock.lock();
        try {
          condition.signal();
        } finally {
          lock.unlock();
        }
        Mic1Test.this.ticks = Mic1Test.this.processor.run();
      }
    }).start();

    try {
      condition.await();
    } finally {
      lock.unlock();
    }

    this.processor.interrupt();
    Thread.sleep(10);
    assertThat(this.ticks).isLessThan(3965);
  }
}
