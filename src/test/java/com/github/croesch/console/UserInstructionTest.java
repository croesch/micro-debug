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
package com.github.croesch.console;

import static org.fest.assertions.Assertions.assertThat;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

import org.junit.Before;
import org.junit.Test;

import com.github.croesch.TestUtil;
import com.github.croesch.error.FileFormatException;
import com.github.croesch.i18n.Text;
import com.github.croesch.mic1.Mic1;
import com.github.croesch.mic1.register.Register;
import com.github.croesch.misc.Printer;

/**
 * Provides test cases for {@link UserInstruction}.
 * 
 * @author croesch
 * @since Date: Dec 3, 2011
 */
public class UserInstructionTest {

  private Mic1 processor;

  @Before
  public void setUp() throws FileFormatException {
    this.processor = new Mic1(ClassLoader.getSystemResourceAsStream("mic1/hi.mic1"),
                              ClassLoader.getSystemResourceAsStream("mic1/hi.ijvm"));
  }

  @Test
  public final void testOf() {
    TestUtil.printMethodName();

    assertThat(UserInstruction.of("Help")).isSameAs(UserInstruction.HELP);

    for (final UserInstruction ins : UserInstruction.values()) {
      final String name = ins.name().replaceAll("_", "-");
      assertThat(UserInstruction.of(name)).isSameAs(ins);
      assertThat(UserInstruction.of(name.toLowerCase())).isSameAs(ins);
      assertThat(UserInstruction.of("--" + name)).isNull();
      assertThat(UserInstruction.of("--" + name.toLowerCase())).isNull();
      TestUtil.printStep();
    }

    assertThat(UserInstruction.of(null)).isNull();
    assertThat(UserInstruction.of("")).isNull();
    assertThat(UserInstruction.of(" ")).isNull();

    TestUtil.printEndOfMethod();
  }

  @Test
  public final void testGetSize() {
    assertThat(UserInstruction.getSize(null)).isZero();
    assertThat(UserInstruction.getSize(new String[] {})).isZero();
    assertThat(UserInstruction.getSize(new String[] { null })).isEqualTo(1);
    assertThat(UserInstruction.getSize(new String[] { "" })).isEqualTo(1);
    assertThat(UserInstruction.getSize(new String[] { " " })).isEqualTo(1);
    assertThat(UserInstruction.getSize(new String[] { " ", "" })).isEqualTo(2);
    assertThat(UserInstruction.getSize(new String[] { "H", "14" })).isEqualTo(2);
    assertThat(UserInstruction.getSize(new Object[] { "H", "14" })).isEqualTo(2);
    assertThat(UserInstruction.getSize(new Object[] { "H", 14 })).isEqualTo(2);
  }

  @Test
  public final void testExecuteExit() throws IOException {
    final ByteArrayOutputStream out = new ByteArrayOutputStream();
    Printer.setPrintStream(new PrintStream(out));

    assertThat(UserInstruction.EXIT.execute(null)).isFalse();
    assertThat(UserInstruction.EXIT.execute(null, "asd")).isFalse();
    assertThat(UserInstruction.EXIT.execute(null, "asd", "asd")).isFalse();

    assertThat(out.toString()).isEmpty();
    Printer.setPrintStream(System.out);
  }

  @Test
  public final void testExecuteHelp() throws IOException {
    final ByteArrayOutputStream out = new ByteArrayOutputStream();
    Printer.setPrintStream(new PrintStream(out));

    assertThat(UserInstruction.HELP.execute(null, "asd")).isTrue();
    assertThat(UserInstruction.HELP.execute(null, "asd", "asd")).isTrue();
    out.reset();

    assertThat(UserInstruction.HELP.execute(null)).isTrue();

    final StringBuilder sb = new StringBuilder();
    BufferedReader reader = null;
    try {
      reader = new BufferedReader(new InputStreamReader(getClass().getClassLoader()
        .getResourceAsStream("instruction-help.txt")));
      String line;
      while ((line = reader.readLine()) != null) {
        sb.append(line).append("\n");
      }
    } finally {
      if (reader != null) {
        reader.close();
      }
    }

    assertThat(out.toString()).isEqualTo(sb.toString());

    Printer.setPrintStream(System.out);
  }

  @Test(expected = NullPointerException.class)
  public final void testExecuteRun_NPE() throws IOException {
    UserInstruction.RUN.execute(null, "asd");
  }

  @Test
  public final void testExecuteRun() throws IOException {
    final ByteArrayOutputStream out = new ByteArrayOutputStream();
    Printer.setPrintStream(new PrintStream(out));

    assertThat(UserInstruction.RUN.execute(this.processor, "asd")).isTrue();
    assertThat(UserInstruction.RUN.execute(this.processor, "asd", "asd")).isTrue();
    out.reset();

    setUp();

    assertThat(UserInstruction.RUN.execute(this.processor)).isTrue();
    assertThat(out.toString()).isEqualTo(Text.TICKS.text(14) + "\n");

    out.reset();
    assertThat(UserInstruction.RUN.execute(this.processor)).isTrue();
    assertThat(out.toString()).isEmpty();

    Printer.setPrintStream(System.out);
  }

  @Test
  public final void testExecuteSet_WrongNumberOfParameters() {
    final ByteArrayOutputStream out = new ByteArrayOutputStream();
    Printer.setPrintStream(new PrintStream(out));

    // 0

    Register.CPP.setValue(0xa1234);
    assertThat(UserInstruction.SET.execute(null)).isTrue();
    assertThat(Register.CPP.getValue()).isEqualTo(0xa1234);
    assertThat(out.toString()).isEqualTo(Text.ERROR.text(Text.WRONG_PARAM_NUMBER.text(2, 0)) + "\n");
    out.reset();

    assertThat(UserInstruction.SET.execute(null, (String[]) null)).isTrue();
    assertThat(Register.CPP.getValue()).isEqualTo(0xa1234);
    assertThat(out.toString()).isEqualTo(Text.ERROR.text(Text.WRONG_PARAM_NUMBER.text(2, 0)) + "\n");
    out.reset();

    assertThat(UserInstruction.SET.execute(null, new String[] {})).isTrue();
    assertThat(Register.CPP.getValue()).isEqualTo(0xa1234);
    assertThat(out.toString()).isEqualTo(Text.ERROR.text(Text.WRONG_PARAM_NUMBER.text(2, 0)) + "\n");
    out.reset();

    // 1

    assertThat(UserInstruction.SET.execute(null, new String[] { null })).isTrue();
    assertThat(Register.CPP.getValue()).isEqualTo(0xa1234);
    assertThat(out.toString()).isEqualTo(Text.ERROR.text(Text.WRONG_PARAM_NUMBER.text(2, 1)) + "\n");
    out.reset();

    assertThat(UserInstruction.SET.execute(null, new String[] { "H" })).isTrue();
    assertThat(Register.CPP.getValue()).isEqualTo(0xa1234);
    assertThat(out.toString()).isEqualTo(Text.ERROR.text(Text.WRONG_PARAM_NUMBER.text(2, 1)) + "\n");
    out.reset();

    assertThat(UserInstruction.SET.execute(null, new String[] { "2" })).isTrue();
    assertThat(Register.CPP.getValue()).isEqualTo(0xa1234);
    assertThat(out.toString()).isEqualTo(Text.ERROR.text(Text.WRONG_PARAM_NUMBER.text(2, 1)) + "\n");
    out.reset();

    // 3

    assertThat(UserInstruction.SET.execute(null, new String[] { null, "asd", "" })).isTrue();
    assertThat(Register.CPP.getValue()).isEqualTo(0xa1234);
    assertThat(out.toString()).isEqualTo(Text.ERROR.text(Text.WRONG_PARAM_NUMBER.text(2, 3)) + "\n");
    out.reset();

    assertThat(UserInstruction.SET.execute(null, new String[] { "H", null, " " })).isTrue();
    assertThat(Register.CPP.getValue()).isEqualTo(0xa1234);
    assertThat(out.toString()).isEqualTo(Text.ERROR.text(Text.WRONG_PARAM_NUMBER.text(2, 3)) + "\n");
    out.reset();

    assertThat(UserInstruction.SET.execute(null, new String[] { "2", "\t", null })).isTrue();
    assertThat(Register.CPP.getValue()).isEqualTo(0xa1234);
    assertThat(out.toString()).isEqualTo(Text.ERROR.text(Text.WRONG_PARAM_NUMBER.text(2, 3)) + "\n");
    out.reset();

    Printer.setPrintStream(System.out);
  }

  @Test
  public void testExecuteSet_Valid() {
    final ByteArrayOutputStream out = new ByteArrayOutputStream();
    Printer.setPrintStream(new PrintStream(out));

    Register.CPP.setValue(0xa1234);

    assertThat(UserInstruction.SET.execute(null, Register.CPP.name(), "14")).isTrue();
    assertThat(Register.CPP.getValue()).isEqualTo(0xE);
    assertThat(out.toString()).isEmpty();

    assertThat(UserInstruction.SET.execute(null, Register.H.name(), "0xF1")).isTrue();
    assertThat(Register.CPP.getValue()).isEqualTo(0xE);
    assertThat(Register.H.getValue()).isEqualTo(0xF1);
    assertThat(out.toString()).isEmpty();

    assertThat(UserInstruction.SET.execute(null, Register.CPP.name(), "2_1010")).isTrue();
    assertThat(Register.CPP.getValue()).isEqualTo(0xa);
    assertThat(out.toString()).isEmpty();

    Printer.setPrintStream(System.out);
  }

  @Test
  public void testExecuteSet_Invalid() {
    final ByteArrayOutputStream out = new ByteArrayOutputStream();
    Printer.setPrintStream(new PrintStream(out));

    Register.CPP.setValue(0xa1234);

    assertThat(UserInstruction.SET.execute(null, "abc", "14")).isTrue();
    assertThat(Register.CPP.getValue()).isEqualTo(0xa1234);
    assertThat(out.toString()).isEqualTo(Text.ERROR.text(Text.INVALID_REGISTER.text("abc")) + "\n");
    out.reset();

    assertThat(UserInstruction.SET.execute(null, "", "14")).isTrue();
    assertThat(Register.CPP.getValue()).isEqualTo(0xa1234);
    assertThat(out.toString()).isEqualTo(Text.ERROR.text(Text.INVALID_REGISTER.text("")) + "\n");
    out.reset();

    assertThat(UserInstruction.SET.execute(null, " h ", "14")).isTrue();
    assertThat(Register.CPP.getValue()).isEqualTo(0xa1234);
    assertThat(out.toString()).isEqualTo(Text.ERROR.text(Text.INVALID_REGISTER.text(" h ")) + "\n");
    out.reset();

    assertThat(UserInstruction.SET.execute(null, Register.CPP.name(), "2_14")).isTrue();
    assertThat(Register.CPP.getValue()).isEqualTo(0xa1234);
    assertThat(out.toString()).isEqualTo(Text.ERROR.text(Text.INVALID_NUMBER.text("2_14")) + "\n");
    out.reset();

    assertThat(UserInstruction.SET.execute(null, Register.CPP.name(), "0xXY")).isTrue();
    assertThat(Register.CPP.getValue()).isEqualTo(0xa1234);
    assertThat(out.toString()).isEqualTo(Text.ERROR.text(Text.INVALID_NUMBER.text("0xXY")) + "\n");
    out.reset();

    assertThat(UserInstruction.SET.execute(null, Register.CPP.name(), "H")).isTrue();
    assertThat(Register.CPP.getValue()).isEqualTo(0xa1234);
    assertThat(out.toString()).isEqualTo(Text.ERROR.text(Text.INVALID_NUMBER.text("H")) + "\n");
    out.reset();

    assertThat(UserInstruction.SET.execute(null, "abc", "2_14")).isTrue();
    assertThat(Register.CPP.getValue()).isEqualTo(0xa1234);
    assertThat(out.toString()).isEqualTo(Text.ERROR.text(Text.INVALID_REGISTER.text("abc")) + "\n"
                                                 + Text.ERROR.text(Text.INVALID_NUMBER.text("2_14")) + "\n");
    out.reset();

    assertThat(UserInstruction.SET.execute(null, "REG", "0xXY")).isTrue();
    assertThat(Register.CPP.getValue()).isEqualTo(0xa1234);
    assertThat(out.toString()).isEqualTo(Text.ERROR.text(Text.INVALID_REGISTER.text("REG")) + "\n"
                                                 + Text.ERROR.text(Text.INVALID_NUMBER.text("0xXY")) + "\n");
    out.reset();

    assertThat(UserInstruction.SET.execute(null, "12", "H")).isTrue();
    assertThat(Register.CPP.getValue()).isEqualTo(0xa1234);
    assertThat(out.toString()).isEqualTo(Text.ERROR.text(Text.INVALID_REGISTER.text("12")) + "\n"
                                                 + Text.ERROR.text(Text.INVALID_NUMBER.text("H")) + "\n");
    out.reset();

    Printer.setPrintStream(System.out);
  }

  @Test
  public final void testExecuteStep() throws IOException {
    final ByteArrayOutputStream out = new ByteArrayOutputStream();
    Printer.setPrintStream(new PrintStream(out));

    assertThat(UserInstruction.STEP.execute(this.processor)).isTrue();
    assertThat(Register.MAR.getValue()).isZero();
    assertThat(Register.PC.getValue()).isZero();
    assertThat(this.processor.isHaltInstruction()).isFalse();
    assertThat(out.toString()).isEqualTo(Text.TICKS.text(1) + "\n");
    out.reset();

    setUp();

    assertThat(UserInstruction.STEP.execute(this.processor, (String[]) null)).isTrue();
    assertThat(Register.MAR.getValue()).isZero();
    assertThat(Register.PC.getValue()).isZero();
    assertThat(this.processor.isHaltInstruction()).isFalse();
    assertThat(out.toString()).isEqualTo(Text.TICKS.text(1) + "\n");
    out.reset();

    assertThat(UserInstruction.STEP.execute(this.processor, "zwei")).isTrue();
    assertThat(Register.MAR.getValue()).isZero();
    assertThat(Register.PC.getValue()).isZero();
    assertThat(Register.LV.getValue()).isEqualTo(-1);
    assertThat(Register.H.getValue()).isEqualTo(-1);
    assertThat(this.processor.isHaltInstruction()).isFalse();
    assertThat(out.toString()).isEqualTo(Text.ERROR.text(Text.INVALID_NUMBER.text("zwei")) + "\n" + Text.TICKS.text(1)
                                                 + "\n");
    out.reset();

    setUp();

    assertThat(UserInstruction.STEP.execute(this.processor, "20")).isTrue();
    assertThat(Register.MDR.getValue()).isEqualTo('\n');
    assertThat(Register.MAR.getValue()).isEqualTo(-3);
    assertThat(Register.PC.getValue()).isEqualTo(3);
    assertThat(Register.LV.getValue()).isEqualTo(-2);
    assertThat(Register.H.getValue()).isEqualTo(-1);
    assertThat(this.processor.isHaltInstruction()).isTrue();
    assertThat(out.toString()).isEqualTo(Text.TICKS.text(14) + "\n");
    out.reset();

    assertThat(UserInstruction.STEP.execute(this.processor, "1")).isTrue();
    assertThat(Register.MDR.getValue()).isEqualTo('\n');
    assertThat(Register.MAR.getValue()).isEqualTo(-3);
    assertThat(Register.PC.getValue()).isEqualTo(3);
    assertThat(Register.LV.getValue()).isEqualTo(-2);
    assertThat(Register.H.getValue()).isEqualTo(-1);
    assertThat(this.processor.isHaltInstruction()).isTrue();
    assertThat(out.toString()).isEmpty();
    out.reset();

    setUp();

    assertThat(UserInstruction.STEP.execute(this.processor, "2_10")).isTrue();
    assertThat(Register.MAR.getValue()).isZero();
    assertThat(Register.PC.getValue()).isZero();
    assertThat(Register.LV.getValue()).isEqualTo(-1);
    assertThat(Register.H.getValue()).isEqualTo(-1);
    assertThat(this.processor.isHaltInstruction()).isFalse();
    assertThat(out.toString()).isEqualTo(Text.TICKS.text(2) + "\n");

    Printer.setPrintStream(System.out);
  }

  @Test
  public final void testExecuteLsReg_NoArg() throws IOException {
    final ByteArrayOutputStream out = new ByteArrayOutputStream();
    Printer.setPrintStream(new PrintStream(out));

    Register.MAR.setValue(1);
    Register.MDR.setValue(2);
    Register.PC.setValue(3);
    Register.MBR.setValue(4);
    Register.MBRU.setValue(5);
    Register.SP.setValue(6);
    Register.LV.setValue(7);
    Register.CPP.setValue(8);
    Register.TOS.setValue(9);
    Register.OPC.setValue(10);
    Register.H.setValue(11);

    assertThat(out.toString()).isEmpty();
    assertThat(UserInstruction.LS_REG.execute(this.processor, (String[]) null)).isTrue();

    assertThat(out.toString()).isEqualTo(Text.REGISTER_VALUE.text("MAR ", "0x1") + "\n"
                                                 + Text.REGISTER_VALUE.text("MDR ", "0x2") + "\n"
                                                 + Text.REGISTER_VALUE.text("PC  ", "0x3") + "\n"
                                                 + Text.REGISTER_VALUE.text("MBR ", "0x4") + "\n"
                                                 + Text.REGISTER_VALUE.text("MBRU", "0x5") + "\n"
                                                 + Text.REGISTER_VALUE.text("SP  ", "0x6") + "\n"
                                                 + Text.REGISTER_VALUE.text("LV  ", "0x7") + "\n"
                                                 + Text.REGISTER_VALUE.text("CPP ", "0x8") + "\n"
                                                 + Text.REGISTER_VALUE.text("TOS ", "0x9") + "\n"
                                                 + Text.REGISTER_VALUE.text("OPC ", "0xA") + "\n"
                                                 + Text.REGISTER_VALUE.text("H   ", "0xB") + "\n");

    Printer.setPrintStream(System.out);
  }

  @Test
  public final void testExecuteLsReg_NullArg() throws IOException {
    final ByteArrayOutputStream out = new ByteArrayOutputStream();
    Printer.setPrintStream(new PrintStream(out));

    Register.MAR.setValue(1);
    Register.MDR.setValue(2);
    Register.PC.setValue(3);
    Register.MBR.setValue(0x1283);
    Register.SP.setValue(6);
    Register.LV.setValue(7);
    Register.CPP.setValue(8);
    Register.TOS.setValue(9);
    Register.OPC.setValue(10);
    Register.H.setValue(11);

    assertThat(out.toString()).isEmpty();
    assertThat(UserInstruction.LS_REG.execute(this.processor, new String[] { null })).isTrue();

    assertThat(out.toString()).isEqualTo(Text.REGISTER_VALUE.text("MAR ", "0x1") + "\n"
                                                 + Text.REGISTER_VALUE.text("MDR ", "0x2") + "\n"
                                                 + Text.REGISTER_VALUE.text("PC  ", "0x3") + "\n"
                                                 + Text.REGISTER_VALUE.text("MBR ", "0xFFFFFF83") + "\n"
                                                 + Text.REGISTER_VALUE.text("MBRU", "0x83") + "\n"
                                                 + Text.REGISTER_VALUE.text("SP  ", "0x6") + "\n"
                                                 + Text.REGISTER_VALUE.text("LV  ", "0x7") + "\n"
                                                 + Text.REGISTER_VALUE.text("CPP ", "0x8") + "\n"
                                                 + Text.REGISTER_VALUE.text("TOS ", "0x9") + "\n"
                                                 + Text.REGISTER_VALUE.text("OPC ", "0xA") + "\n"
                                                 + Text.REGISTER_VALUE.text("H   ", "0xB") + "\n");

    Printer.setPrintStream(System.out);
  }

  @Test
  public final void testExecuteLsReg_FalseArg() throws IOException {
    final ByteArrayOutputStream out = new ByteArrayOutputStream();
    Printer.setPrintStream(new PrintStream(out));

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
    assertThat(UserInstruction.LS_REG.execute(this.processor, new String[] { "Bernd" })).isTrue();

    assertThat(out.toString()).isEqualTo(Text.ERROR.text(Text.INVALID_REGISTER.text("Bernd")) + "\n"
                                                 + Text.REGISTER_VALUE.text("MAR ", "0xFFFFFFFF") + "\n"
                                                 + Text.REGISTER_VALUE.text("MDR ", "0x0") + "\n"
                                                 + Text.REGISTER_VALUE.text("PC  ", "0x1") + "\n"
                                                 + Text.REGISTER_VALUE.text("MBR ", "0x73") + "\n"
                                                 + Text.REGISTER_VALUE.text("MBRU", "0x73") + "\n"
                                                 + Text.REGISTER_VALUE.text("SP  ", "0x8BC") + "\n"
                                                 + Text.REGISTER_VALUE.text("LV  ", "0x8BD") + "\n"
                                                 + Text.REGISTER_VALUE.text("CPP ", "0x8BE") + "\n"
                                                 + Text.REGISTER_VALUE.text("TOS ", "0x8BF") + "\n"
                                                 + Text.REGISTER_VALUE.text("OPC ", "0x8C0") + "\n"
                                                 + Text.REGISTER_VALUE.text("H   ", "0x8C1") + "\n");

    Printer.setPrintStream(System.out);
  }

  @Test
  public final void testExecuteLsReg_OneArg() throws IOException {
    final ByteArrayOutputStream out = new ByteArrayOutputStream();
    Printer.setPrintStream(new PrintStream(out));

    Register.MAR.setValue(0x4711);
    assertThat(UserInstruction.LS_REG.execute(this.processor, new String[] { "MAR" })).isTrue();
    assertThat(out.toString()).isEqualTo(Text.REGISTER_VALUE.text("MAR ", "0x4711") + "\n");
    out.reset();

    Register.SP.setValue(-2);
    assertThat(UserInstruction.LS_REG.execute(this.processor, new String[] { "SP" })).isTrue();
    assertThat(out.toString()).isEqualTo(Text.REGISTER_VALUE.text("SP  ", "0xFFFFFFFE") + "\n");
    out.reset();

    Printer.setPrintStream(System.out);
  }

  @Test
  public void testTraceRegister() {
    for (final Register r : Register.values()) {
      assertThat(this.processor.isTracing(r)).isFalse();
      assertThat(UserInstruction.TRACE_REG.execute(this.processor, r.name())).isTrue();
      assertThat(this.processor.isTracing(r)).isTrue();
    }
  }

  @Test
  public void testTraceAllRegisters() {
    for (final Register r : Register.values()) {
      assertThat(this.processor.isTracing(r)).isFalse();
    }

    assertThat(UserInstruction.TRACE_REG.execute(this.processor, (String[]) null)).isTrue();

    for (final Register r : Register.values()) {
      assertThat(this.processor.isTracing(r)).isTrue();
    }
  }

  @Test
  public void testUntraceAllRegisters() {
    assertThat(UserInstruction.TRACE_REG.execute(this.processor, (String[]) null)).isTrue();

    for (final Register r : Register.values()) {
      assertThat(this.processor.isTracing(r)).isTrue();
    }

    assertThat(UserInstruction.UNTRACE_REG.execute(this.processor, (String[]) null)).isTrue();

    for (final Register r : Register.values()) {
      assertThat(this.processor.isTracing(r)).isFalse();
    }
  }

  @Test
  public void testUntraceRegister() {
    assertThat(UserInstruction.TRACE_REG.execute(this.processor, (String[]) null)).isTrue();

    for (final Register r : Register.values()) {
      assertThat(this.processor.isTracing(r)).isTrue();
      assertThat(UserInstruction.UNTRACE_REG.execute(this.processor, r.name())).isTrue();
      assertThat(this.processor.isTracing(r)).isFalse();
    }
  }

  @Test
  public void testUpdateTracedRegisters() {
    final ByteArrayOutputStream out = new ByteArrayOutputStream();
    Printer.setPrintStream(new PrintStream(out));

    assertThat(UserInstruction.TRACE_REG.execute(this.processor, Register.PC.name())).isTrue();
    assertThat(out.toString()).isEmpty();
    assertThat(UserInstruction.RUN.execute(this.processor, (String[]) null)).isTrue();

    assertThat(out.toString()).isEqualTo(Text.REGISTER_VALUE.text("PC  ", "0x0") + "\n"
                                                 + Text.REGISTER_VALUE.text("PC  ", "0x0") + "\n"
                                                 + Text.REGISTER_VALUE.text("PC  ", "0x0") + "\n"
                                                 + Text.REGISTER_VALUE.text("PC  ", "0x0") + "\n"
                                                 + Text.REGISTER_VALUE.text("PC  ", "0x1") + "\n"
                                                 + Text.REGISTER_VALUE.text("PC  ", "0x1") + "\n"
                                                 + Text.REGISTER_VALUE.text("PC  ", "0x1") + "\n"
                                                 + Text.REGISTER_VALUE.text("PC  ", "0x2") + "\n"
                                                 + Text.REGISTER_VALUE.text("PC  ", "0x2") + "\n"
                                                 + Text.REGISTER_VALUE.text("PC  ", "0x2") + "\n"
                                                 + Text.REGISTER_VALUE.text("PC  ", "0x3") + "\n"
                                                 + Text.REGISTER_VALUE.text("PC  ", "0x3") + "\n"
                                                 + Text.REGISTER_VALUE.text("PC  ", "0x3") + "\n"
                                                 + Text.REGISTER_VALUE.text("PC  ", "0x3") + "\n" + Text.TICKS.text(14)
                                                 + "\n");

    Printer.setPrintStream(System.out);
  }

  @Test
  public final void testTraceMicro() throws IOException {
    final String firstLine = Text.EXECUTED_CODE.text("PC=MAR=0;rd;goto 0x1") + "\n";
    final String expected = firstLine + Text.EXECUTED_CODE.text("H=LV=-1;goto 0x2") + "\n"
                            + Text.EXECUTED_CODE.text("LV=H+LV;wr;goto 0x3") + "\n"
                            + Text.EXECUTED_CODE.text("MAR=LV-1;wr;goto 0x4") + "\n"
                            + Text.EXECUTED_CODE.text("PC=MAR=PC+1;rd;goto 0x5") + "\n"
                            + Text.EXECUTED_CODE.text("MAR=LV-1;goto 0x6") + "\n"
                            + Text.EXECUTED_CODE.text("wr;goto 0x7") + "\n"
                            + Text.EXECUTED_CODE.text("PC=MAR=PC+1;rd;goto 0x8") + "\n"
                            + Text.EXECUTED_CODE.text("MAR=LV-1;goto 0x9") + "\n"
                            + Text.EXECUTED_CODE.text("wr;goto 0xA") + "\n"
                            + Text.EXECUTED_CODE.text("PC=MAR=PC+1;rd;goto 0xB") + "\n"
                            + Text.EXECUTED_CODE.text("MAR=LV-1;goto 0xC") + "\n"
                            + Text.EXECUTED_CODE.text("wr;goto 0xD") + "\n" + Text.EXECUTED_CODE.text("goto 0xD")
                            + "\n" + Text.TICKS.text(14) + "\n";
    final ByteArrayOutputStream out = new ByteArrayOutputStream();
    Printer.setPrintStream(new PrintStream(out));

    assertThat(UserInstruction.RUN.execute(this.processor)).isTrue();
    assertThat(out.toString()).isEqualTo(Text.TICKS.text(14) + "\n");
    out.reset();

    setUp();

    assertThat(UserInstruction.TRACE_MIC.execute(this.processor)).isTrue();
    assertThat(UserInstruction.RUN.execute(this.processor)).isTrue();
    assertThat(out.toString()).isEqualTo(expected);

    setUp();
    out.reset();

    assertThat(UserInstruction.TRACE_MIC.execute(this.processor)).isTrue();
    assertThat(UserInstruction.STEP.execute(this.processor)).isTrue();
    assertThat(out.toString()).isEqualTo(firstLine + Text.TICKS.text(1) + "\n");
    out.reset();

    assertThat(UserInstruction.UNTRACE_MIC.execute(this.processor)).isTrue();
    assertThat(UserInstruction.RUN.execute(this.processor)).isTrue();
    assertThat(out.toString()).isEqualTo(Text.TICKS.text(13) + "\n");

    Printer.setPrintStream(System.out);

  }
}
