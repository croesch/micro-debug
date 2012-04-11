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
package com.github.croesch.micro_debug.console;

import static org.fest.assertions.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

import com.github.croesch.micro_debug.DefaultTestCase;
import com.github.croesch.micro_debug.error.FileFormatException;
import com.github.croesch.micro_debug.i18n.Text;
import com.github.croesch.micro_debug.mic1.Mic1;
import com.github.croesch.micro_debug.mic1.io.Input;
import com.github.croesch.micro_debug.mic1.register.Register;

/**
 * Provides test cases for {@link Mic1Interpreter}.
 * 
 * @author croesch
 * @since Date: Dec 1, 2011
 */
public class Mic1InterpreterTest extends DefaultTestCase {

  private Mic1 processor;

  private Mic1Interpreter interpreter;

  @Override
  protected void setUpDetails() throws FileFormatException {
    init("mic1/hi.mic1", "mic1/hi.ijvm");
  }

  private void init(final String micFile, final String ijvmFile) throws FileFormatException {
    this.processor = new Mic1(ClassLoader.getSystemResourceAsStream(micFile),
                              ClassLoader.getSystemResourceAsStream(ijvmFile));
    this.interpreter = new Mic1Interpreter(this.processor);
  }

  @Test
  public void testAddRegisterBreakPoint() {
    printlnMethodName();
    this.interpreter.addRegisterBreakpoint(Register.H, Integer.valueOf(-1));
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

  @Test
  public void testAddRegisterWriteBreakPoint_MAR() {
    printlnMethodName();
    this.interpreter.addRegisterBreakpoint(Register.MAR);
    this.processor.run();
    assertThat(out.toString()).isEqualTo(Text.TICKS.text(3) + getLineSeparator());

    out.reset();
    this.processor.run();
    assertThat(out.toString()).isEqualTo(Text.TICKS.text(1) + getLineSeparator());

    out.reset();
    this.processor.run();
    assertThat(out.toString()).isEqualTo(Text.TICKS.text(1) + getLineSeparator());

    out.reset();
    this.processor.microStep(10);
    assertThat(out.toString()).isEqualTo(Text.TICKS.text(2) + getLineSeparator());

    out.reset();
    this.processor.run();
    assertThat(out.toString()).isEqualTo(Text.TICKS.text(1) + getLineSeparator());

    out.reset();
    this.processor.run();
    assertThat(out.toString()).isEqualTo(Text.TICKS.text(2) + getLineSeparator());

    out.reset();
    this.processor.run();
    assertThat(out.toString()).isEqualTo(Text.TICKS.text(1) + getLineSeparator());

    out.reset();
    this.processor.run();
    assertThat(out.toString()).isEqualTo(Text.TICKS.text(3) + getLineSeparator());

    // reset and check if by step per step stops also

    out.reset();
    this.processor.reset();
    this.processor.microStep(2);
    assertThat(out.toString()).isEqualTo(Text.TICKS.text(2) + getLineSeparator());

    out.reset();
    this.processor.microStep();
    assertThat(out.toString()).isEqualTo(Text.TICKS.text(1) + getLineSeparator());

    out.reset();
    this.processor.microStep();
    assertThat(out.toString()).isEqualTo(Text.TICKS.text(1) + getLineSeparator());

    out.reset();
    this.processor.microStep();
    assertThat(out.toString()).isEqualTo(Text.TICKS.text(1) + getLineSeparator());

    out.reset();
    this.processor.microStep();
    assertThat(out.toString()).isEqualTo(Text.TICKS.text(1) + getLineSeparator());
  }

  @Test
  public void testAddRegisterWriteBreakPoint_MDR() {
    printlnMethodName();
    this.interpreter.addRegisterBreakpoint(Register.MDR);
    this.processor.run();
    assertThat(out.toString()).isEqualTo(Text.TICKS.text(1) + getLineSeparator());

    out.reset();
    this.processor.run();
    assertThat(out.toString()).isEqualTo(Text.TICKS.text(4) + getLineSeparator());

    out.reset();
    this.processor.run();
    assertThat(out.toString()).isEqualTo(Text.TICKS.text(3) + getLineSeparator());

    out.reset();
    this.processor.microStep(10);
    assertThat(out.toString()).isEqualTo(Text.TICKS.text(3) + getLineSeparator());

    out.reset();
    this.processor.run();
    assertThat(out.toString()).isEqualTo(Text.TICKS.text(3) + getLineSeparator());

    // reset and check if by step per step stops also

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

  @Test
  public void testAddMicroBreakPoint() {
    printlnMethodName();
    this.interpreter.addMicroBreakpoint(Integer.valueOf(2));
    this.interpreter.addMicroBreakpoint(Integer.valueOf(3));
    this.processor.run();
    assertThat(out.toString()).isEqualTo(Text.TICKS.text(2) + getLineSeparator());

    out.reset();
    this.processor.run();
    assertThat(out.toString()).isEqualTo(Text.TICKS.text(1) + getLineSeparator());

    out.reset();
    this.processor.reset();
    this.processor.microStep(2);
    assertThat(out.toString()).isEqualTo(Text.TICKS.text(2) + getLineSeparator());

    out.reset();
    this.processor.microStep(2);
    assertThat(out.toString()).isEqualTo(Text.TICKS.text(1) + getLineSeparator());

    out.reset();
    this.processor.run();
    assertThat(out.toString()).isEqualTo(Text.TICKS.text(11) + getLineSeparator());
  }

  @Test
  public void testAddMacroBreakPoint() throws FileFormatException {
    printlnMethodName();
    init("mic1/mic1ijvm.mic1", "mic1/add.ijvm");

    Input.setIn(new ByteArrayInputStream("2\n2\n".getBytes()));

    this.interpreter.addMacroBreakpoint(Integer.valueOf(2));
    this.interpreter.addMacroBreakpoint(Integer.valueOf(3));
    this.processor.run();
    assertThat(out.toString()).isEqualTo(Text.TICKS.text(7) + getLineSeparator());

    out.reset();
    this.processor.run();
    assertThat(out.toString()).isEqualTo(Text.TICKS.text(3) + getLineSeparator());

    out.reset();
    this.processor.reset();
    this.processor.microStep(2);
    assertThat(out.toString()).isEqualTo(Text.TICKS.text(2) + getLineSeparator());

    out.reset();
    this.processor.microStep(12);
    assertThat(out.toString()).isEqualTo(Text.TICKS.text(5) + getLineSeparator());

    out.reset();
    this.processor.run();
    assertThat(out.toString()).isEqualTo(Text.TICKS.text(3) + getLineSeparator());

    out.reset();
    this.processor.run();
    assertThat(out.toString()).isEqualTo(Text.INPUT_MIC1.text() + Text.INPUT_MIC1.text() + Text.TICKS.text(3282)
                                                 + getLineSeparator());
  }

  @Test
  public void testRemoveRegisterBreakPoint() {
    printlnMethodName();
    this.interpreter.addRegisterBreakpoint(Register.H, Integer.valueOf(-1));
    this.processor.run();
    assertThat(out.toString()).isEqualTo(Text.TICKS.text(2) + getLineSeparator());
    out.reset();

    this.processor.reset();
    this.processor.run();
    assertThat(out.toString()).isEqualTo(Text.TICKS.text(2) + getLineSeparator());
    out.reset();

    this.interpreter.listBreakpoints();
    final Matcher m = Pattern.compile(".*#([0-9]+).*" + getLineSeparator()).matcher(out.toString());

    assertThat(m.matches()).isTrue();
    this.interpreter.removeBreakpoint(Integer.parseInt(m.group(1)));
    out.reset();

    this.processor.reset();
    this.processor.run();
    assertThat(out.toString()).isEqualTo(Text.TICKS.text(14) + getLineSeparator());
    out.reset();
  }

  @Test
  public void testRemoveRegisterWriteBreakPoint() {
    printlnMethodName();
    this.interpreter.addRegisterBreakpoint(Register.PC);
    this.processor.run();
    assertThat(out.toString()).isEqualTo(Text.TICKS.text(4) + getLineSeparator());
    out.reset();

    this.processor.reset();
    this.processor.run();
    assertThat(out.toString()).isEqualTo(Text.TICKS.text(4) + getLineSeparator());
    out.reset();

    this.interpreter.listBreakpoints();
    final Matcher m = Pattern.compile(".*#([0-9]+).*" + getLineSeparator()).matcher(out.toString());

    assertThat(m.matches()).isTrue();
    this.interpreter.removeBreakpoint(Integer.parseInt(m.group(1)));
    out.reset();

    this.processor.reset();
    this.processor.run();
    assertThat(out.toString()).isEqualTo(Text.TICKS.text(14) + getLineSeparator());
    out.reset();
  }

  @Test
  public void testListBreakpoints() {
    printMethodName();

    this.interpreter.addRegisterBreakpoint(Register.MBRU, Integer.valueOf(16));
    this.interpreter.addRegisterBreakpoint(Register.MBRU, Integer.valueOf(-48));

    this.interpreter.addRegisterBreakpoint(Register.CPP, Integer.valueOf(-1));
    this.interpreter.addRegisterBreakpoint(Register.CPP, Integer.valueOf(Integer.MAX_VALUE));
    this.interpreter.addRegisterBreakpoint(Register.CPP, Integer.valueOf(Integer.MIN_VALUE));

    this.interpreter.addRegisterBreakpoint(Register.CPP);
    this.interpreter.addRegisterBreakpoint(Register.OPC);
    this.interpreter.addRegisterBreakpoint(Register.MBR);

    this.interpreter.addRegisterBreakpoint(Register.H, Integer.valueOf(2));
    this.interpreter.addRegisterBreakpoint(Register.H, Integer.valueOf(2));
    this.interpreter.addRegisterBreakpoint(Register.H, Integer.valueOf(3));
    this.interpreter.addRegisterBreakpoint(Register.H, Integer.valueOf(1));

    assertThat(out.toString()).isEmpty();
    this.interpreter.listBreakpoints();
    assertThat(out.toString()).matches(Text.BREAKPOINT_REGISTER.text("[0-9]+", Register.MBRU, "0x10")
                                               + getLineSeparator()
                                               + Text.BREAKPOINT_REGISTER.text("[0-9]+", Register.MBRU, "0xFFFFFFD0")
                                               + getLineSeparator()
                                               + Text.BREAKPOINT_REGISTER.text("[0-9]+", Register.CPP, "0xFFFFFFFF")
                                               + getLineSeparator()
                                               + Text.BREAKPOINT_REGISTER.text("[0-9]+", Register.CPP, "0x7FFFFFFF")
                                               + getLineSeparator()
                                               + Text.BREAKPOINT_REGISTER.text("[0-9]+", Register.CPP, "0x80000000")
                                               + getLineSeparator()
                                               + Text.BREAKPOINT_WRITE_REGISTER.text("[0-9]+", Register.CPP)
                                               + getLineSeparator()
                                               + Text.BREAKPOINT_WRITE_REGISTER.text("[0-9]+", Register.OPC)
                                               + getLineSeparator()
                                               + Text.BREAKPOINT_WRITE_REGISTER.text("[0-9]+", Register.MBR)
                                               + getLineSeparator()
                                               + Text.BREAKPOINT_REGISTER.text("[0-9]+", Register.H, "0x2")
                                               + getLineSeparator()
                                               + Text.BREAKPOINT_REGISTER.text("[0-9]+", Register.H, "0x3")
                                               + getLineSeparator()
                                               + Text.BREAKPOINT_REGISTER.text("[0-9]+", Register.H, "0x1")
                                               + getLineSeparator());

    printEndOfMethod();
  }

  @Test
  public final void testTracingLocalVariables() throws IOException {
    printlnMethodName();
    Input.setIn(new ByteArrayInputStream("2\n2\n2\n2\n".getBytes()));
    init("mic1/mic1ijvm.mic1", "mic1/add.ijvm");

    this.interpreter.traceLocalVariable(0);
    this.processor.step(11);
    out.reset();
    this.interpreter.traceLocalVariable(1);

    this.processor.step(27);
    assertThat(out.toString()).isEqualTo(Text.INPUT_MIC1.text() + Text.LOCAL_VARIABLE_VALUE.text(1, 2)
                                                 + getLineSeparator() + Text.TICKS.text(129) + getLineSeparator());
    out.reset();
    this.processor.step(9);
    assertThat(out.toString()).isEqualTo(Text.LOCAL_VARIABLE_VALUE.text(0, 2) + getLineSeparator()
                                                 + Text.TICKS.text(64) + getLineSeparator());

    out.reset();
    this.processor.step(33);
    assertThat(out.toString()).isEqualTo(Text.LOCAL_VARIABLE_VALUE.text(1, 0) + getLineSeparator()
                                                 + Text.INPUT_MIC1.text() + Text.LOCAL_VARIABLE_VALUE.text(1, 2)
                                                 + getLineSeparator() + Text.TICKS.text(184) + getLineSeparator());

    out.reset();
    this.processor.step(9);
    assertThat(out.toString()).isEqualTo(Text.TICKS.text(64) + getLineSeparator());
  }

  @Test
  public final void testUntracingLocalVariables() throws IOException {
    printlnMethodName();
    Input.setIn(new ByteArrayInputStream("2\n2\n2\n2\n".getBytes()));
    init("mic1/mic1ijvm.mic1", "mic1/add.ijvm");

    this.interpreter.traceLocalVariable(0);
    this.processor.step(11);
    out.reset();
    this.interpreter.traceLocalVariable(1);
    this.interpreter.untraceLocalVariable(0);

    this.processor.step(27);
    this.interpreter.untraceLocalVariable(1);
    assertThat(out.toString()).isEqualTo(Text.INPUT_MIC1.text() + Text.LOCAL_VARIABLE_VALUE.text(1, 2)
                                                 + getLineSeparator() + Text.TICKS.text(129) + getLineSeparator());
    out.reset();
    this.processor.step(9);
    assertThat(out.toString()).isEqualTo(Text.LOCAL_VARIABLE_VALUE.text(0, 2) + getLineSeparator()
                                                 + Text.TICKS.text(64) + getLineSeparator());

    out.reset();
    this.processor.step(33);
    assertThat(out.toString()).isEqualTo(Text.INPUT_MIC1.text() + Text.TICKS.text(184) + getLineSeparator());

    out.reset();
    this.processor.step(9);
    assertThat(out.toString()).isEqualTo(Text.TICKS.text(64) + getLineSeparator());
  }

  @Test
  public final void testTraceMacro() throws IOException {
    printlnMethodName();
    Input.setIn(new ByteArrayInputStream("2\n2\n2\n2\n".getBytes()));
    init("mic1/mic1ijvm.mic1", "mic1/add.ijvm");

    final String firstLine = Text.EXECUTED_CODE.text("     0x0: [ 0x10] BIPUSH 0x0") + getLineSeparator();
    final String expected = firstLine + Text.EXECUTED_CODE.text("     0x2: [ 0x59] DUP") + getLineSeparator()
                            + Text.EXECUTED_CODE.text("     0x3: [ 0x36] ISTORE 0") + getLineSeparator()
                            + Text.EXECUTED_CODE.text("     0x5: [ 0x36] ISTORE 1") + getLineSeparator()
                            + Text.TICKS.text(24) + getLineSeparator();

    this.processor.run();
    assertThat(out.toString()).isEqualTo(Text.INPUT_MIC1.text() + Text.INPUT_MIC1.text() + Text.TICKS.text(3292)
                                                 + getLineSeparator());
    out.reset();

    this.processor.reset();

    this.interpreter.traceMacro();
    this.processor.step(5);
    assertThat(out.toString()).isEqualTo(expected);

    this.processor.reset();
    out.reset();

    this.processor.step(2);
    assertThat(out.toString()).isEqualTo(firstLine + Text.TICKS.text(7) + getLineSeparator());
    out.reset();

    this.interpreter.untraceMacro();
    this.processor.run();
    assertThat(out.toString()).isEqualTo(Text.INPUT_MIC1.text() + Text.INPUT_MIC1.text() + Text.TICKS.text(3285)
                                                 + getLineSeparator());
  }

  @Test
  public final void testListAllRegisters() throws IOException {
    printlnMethodName();
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
    this.interpreter.listAllRegisters();

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
    printlnMethodName();
    Register.MAR.setValue(0x4711);
    this.interpreter.listSingleRegister(Register.MAR);
    assertThat(out.toString()).isEqualTo(Text.REGISTER_VALUE.text("MAR ", "0x4711") + getLineSeparator());
    out.reset();

    Register.SP.setValue(-2);
    this.interpreter.listSingleRegister(Register.SP);
    assertThat(out.toString()).isEqualTo(Text.REGISTER_VALUE.text("SP  ", "0xFFFFFFFE") + getLineSeparator());
    out.reset();
  }

  @Test
  public final void testTraceMicro() throws IOException {
    printlnMethodName();
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

    this.interpreter.traceMicro();
    this.processor.run();
    assertThat(out.toString()).isEqualTo(expected);

    this.processor.reset();
    out.reset();

    this.processor.microStep();
    assertThat(out.toString()).isEqualTo(firstLine + Text.TICKS.text(1) + getLineSeparator());
    out.reset();

    this.interpreter.untraceMicro();
    this.processor.run();
    assertThat(out.toString()).isEqualTo(Text.TICKS.text(13) + getLineSeparator());
  }

  @Test
  public void testPrintContent() throws FileFormatException {
    printlnMethodName();
    init("mic1/mic1ijvm.mic1", "mic1/test.ijvm");

    this.interpreter.printContent(0, 1);
    assertThat(out.toString()).isEqualTo(Text.MEMORY_CONTENT.text("     0x0", "0x10203") + getLineSeparator()
                                                 + Text.MEMORY_CONTENT.text("     0x1", "0x4050607")
                                                 + getLineSeparator());
    out.reset();

    this.interpreter.printContent(1, 0);
    assertThat(out.toString()).isEqualTo(Text.MEMORY_CONTENT.text("     0x0", "0x10203") + getLineSeparator()
                                                 + Text.MEMORY_CONTENT.text("     0x1", "0x4050607")
                                                 + getLineSeparator());
    out.reset();

    this.interpreter.printContent(0, 0);
    assertThat(out.toString()).isEqualTo(Text.MEMORY_CONTENT.text("     0x0", "0x10203") + getLineSeparator());
    out.reset();

    this.interpreter.printContent(2, -13);
    assertThat(out.toString()).isEqualTo(Text.MEMORY_CONTENT.text("     0x0", "0x10203") + getLineSeparator()
                                                 + Text.MEMORY_CONTENT.text("     0x1", "0x4050607")
                                                 + getLineSeparator()
                                                 + Text.MEMORY_CONTENT.text("     0x2", "0x8090A0B")
                                                 + getLineSeparator());
    out.reset();

    this.interpreter.printContent(3, 1);
    assertThat(out.toString()).isEqualTo(Text.MEMORY_CONTENT.text("     0x1", "0x4050607") + getLineSeparator()
                                                 + Text.MEMORY_CONTENT.text("     0x2", "0x8090A0B")
                                                 + getLineSeparator()
                                                 + Text.MEMORY_CONTENT.text("     0x3", "0xC0D0E0F")
                                                 + getLineSeparator());
    out.reset();

    this.interpreter.printContent(Byte.MAX_VALUE, Byte.MAX_VALUE - 3);
    assertThat(out.toString()).isEqualTo(Text.MEMORY_CONTENT.text("    0x7C", "0x0") + getLineSeparator()
                                                 + Text.MEMORY_CONTENT.text("    0x7D", "0x0") + getLineSeparator()
                                                 + Text.MEMORY_CONTENT.text("    0x7E", "0x0") + getLineSeparator()
                                                 + Text.MEMORY_CONTENT.text("    0x7F", "0x0") + getLineSeparator());
    out.reset();
  }

  @Test
  public final void testPrintStack() throws IOException {
    printlnMethodName();
    Input.setIn(new ByteArrayInputStream("2\n2\n2\n2\n".getBytes()));

    init("mic1/mic1ijvm.mic1", "mic1/add.ijvm");

    this.interpreter.printStack(1);
    assertThat(out.toString()).isEqualTo(Text.STACK_EMPTY.text() + getLineSeparator());

    this.processor.step(15);
    out.reset();
    this.interpreter.printStack(1);
    assertThat(out.toString()).isEqualTo(Text.STACK_CONTENT.text(1, "  0xC001", "0xC003") + getLineSeparator()
                                                 + Text.STACK_CONTENT.text(2, "  0xC002", "0x0") + getLineSeparator()
                                                 + Text.STACK_CONTENT.text(3, "  0xC003", "0x10") + getLineSeparator()
                                                 + Text.STACK_CONTENT.text(4, "  0xC004", "0x8000")
                                                 + getLineSeparator() + Text.STACK_CONTENT.text(5, "  0xC005", "0x32")
                                                 + getLineSeparator());

    this.processor.step();
    out.reset();
    this.interpreter.printStack(0);
    assertThat(out.toString()).isEqualTo(Text.STACK_CONTENT.text(0, "  0xC000", "0x0") + getLineSeparator()
                                                 + Text.STACK_CONTENT.text(1, "  0xC001", "0xC003")
                                                 + getLineSeparator() + Text.STACK_CONTENT.text(2, "  0xC002", "0x0")
                                                 + getLineSeparator() + Text.STACK_CONTENT.text(3, "  0xC003", "0x10")
                                                 + getLineSeparator()
                                                 + Text.STACK_CONTENT.text(4, "  0xC004", "0x8000")
                                                 + getLineSeparator() + Text.STACK_CONTENT.text(5, "  0xC005", "0x32")
                                                 + getLineSeparator() + Text.STACK_CONTENT.text(6, "  0xC006", "0x32")
                                                 + getLineSeparator());

    this.processor.step();
    out.reset();
    this.interpreter.printStack(0);
    assertThat(out.toString()).isEqualTo(Text.STACK_CONTENT.text(0, "  0xC000", "0x0") + getLineSeparator()
                                                 + Text.STACK_CONTENT.text(1, "  0xC001", "0xC003")
                                                 + getLineSeparator() + Text.STACK_CONTENT.text(2, "  0xC002", "0x0")
                                                 + getLineSeparator() + Text.STACK_CONTENT.text(3, "  0xC003", "0x10")
                                                 + getLineSeparator()
                                                 + Text.STACK_CONTENT.text(4, "  0xC004", "0x8000")
                                                 + getLineSeparator() + Text.STACK_CONTENT.text(5, "  0xC005", "0x32")
                                                 + getLineSeparator() + Text.STACK_CONTENT.text(6, "  0xC006", "0x32")
                                                 + getLineSeparator() + Text.STACK_CONTENT.text(7, "  0xC007", "0x30")
                                                 + getLineSeparator());

    this.processor.step();
    out.reset();
    this.interpreter.printStack(1);
    assertThat(out.toString()).isEqualTo(Text.STACK_CONTENT.text(1, "  0xC001", "0xC003") + getLineSeparator()
                                                 + Text.STACK_CONTENT.text(2, "  0xC002", "0x0") + getLineSeparator()
                                                 + Text.STACK_CONTENT.text(3, "  0xC003", "0x10") + getLineSeparator()
                                                 + Text.STACK_CONTENT.text(4, "  0xC004", "0x8000")
                                                 + getLineSeparator() + Text.STACK_CONTENT.text(5, "  0xC005", "0x32")
                                                 + getLineSeparator() + Text.STACK_CONTENT.text(6, "  0xC006", "0x2")
                                                 + getLineSeparator());

    this.processor.step();
    out.reset();
    this.interpreter.printStack(2);
    assertThat(out.toString()).isEqualTo(Text.STACK_CONTENT.text(2, "  0xC002", "0x0") + getLineSeparator()
                                                 + Text.STACK_CONTENT.text(3, "  0xC003", "0x10") + getLineSeparator()
                                                 + Text.STACK_CONTENT.text(4, "  0xC004", "0x8000")
                                                 + getLineSeparator() + Text.STACK_CONTENT.text(5, "  0xC005", "0x32")
                                                 + getLineSeparator());

    this.processor.step();
    out.reset();
    this.interpreter.printStack(1);
    assertThat(out.toString()).isEqualTo(Text.STACK_CONTENT.text(1, "  0xC001", "0xC003") + getLineSeparator()
                                                 + Text.STACK_CONTENT.text(2, "  0xC002", "0x0") + getLineSeparator()
                                                 + Text.STACK_CONTENT.text(3, "  0xC003", "0x10") + getLineSeparator()
                                                 + Text.STACK_CONTENT.text(4, "  0xC004", "0x8000")
                                                 + getLineSeparator() + Text.STACK_CONTENT.text(5, "  0xC005", "0x32")
                                                 + getLineSeparator() + Text.STACK_CONTENT.text(6, "  0xC006", "0x32")
                                                 + getLineSeparator());
  }
}
