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
package com.github.croesch.ui;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

import com.github.croesch.DefaultTestCase;
import com.github.croesch.error.FileFormatException;
import com.github.croesch.i18n.Text;
import com.github.croesch.mic1.mem.Memory;
import com.github.croesch.mic1.register.Register;

/**
 * Provides test cases for {@link TraceManager}.
 * 
 * @author croesch
 * @since Date: Jan 15, 2012
 */
public class TraceManagerTest extends DefaultTestCase {

  private TraceManager tm;

  private Memory mem;

  @Override
  protected void setUpDetails() throws FileFormatException {
    // create empty memory
    this.mem = new Memory(Byte.MAX_VALUE, ClassLoader.getSystemResourceAsStream("mic1/wrong-file-format-1.ijvm"));
    this.tm = new TraceManager(this.mem);
  }

  @Test
  public void testTraceRegister() {
    printMethodName();
    for (final Register r : Register.values()) {
      assertThat(this.tm.isTracing(r)).isFalse();
      this.tm.traceRegister(r);
      assertThat(this.tm.isTracing(r)).isTrue();
      printStep();
    }
    printEndOfMethod();
  }

  @Test
  public void testTraceAllRegisters() {
    printMethodName();
    for (final Register r : Register.values()) {
      assertThat(this.tm.isTracing(r)).isFalse();
      printStep();
    }

    printLoopEnd();
    this.tm.traceRegister();

    for (final Register r : Register.values()) {
      assertThat(this.tm.isTracing(r)).isTrue();
      printStep();
    }
    printEndOfMethod();
  }

  @Test
  public void testUntraceAllRegisters() {
    printMethodName();
    this.tm.traceRegister();

    for (final Register r : Register.values()) {
      assertThat(this.tm.isTracing(r)).isTrue();
      printStep();
    }

    printLoopEnd();
    this.tm.untraceRegister();

    for (final Register r : Register.values()) {
      assertThat(this.tm.isTracing(r)).isFalse();
      printStep();
    }
    printEndOfMethod();
  }

  @Test
  public void testUntraceRegister() {
    printMethodName();
    this.tm.traceRegister();

    for (final Register r : Register.values()) {
      assertThat(this.tm.isTracing(r)).isTrue();
      this.tm.untraceRegister(r);
      assertThat(this.tm.isTracing(r)).isFalse();
      printStep();
    }
    printEndOfMethod();
  }

  @Test
  public void testUpdateTracedRegisters() {
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

    this.tm.traceRegister();
    assertThat(out.toString()).isEmpty();
    this.tm.update(null, 0);

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
  public void testTraceMicro() throws FileFormatException {
    printlnMethodName();
    this.tm.traceMicro();
    assertThat(this.tm.isTracingMicro()).isTrue();

    this.tm.untraceMicro();
    assertThat(this.tm.isTracingMicro()).isFalse();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIAEConstructor() {
    new TraceManager(null);
  }

  @Test
  public void testTraceLocalVariable_LocalVariableExists() {
    printlnMethodName();
    Register.LV.setValue(0);
    this.mem.setWord(0, 0);

    this.tm.traceLocalVariable(2);
    assertThat(this.tm.isTracingLocalVariable(2)).isFalse();

    this.mem.setWord(0, 1);
    this.tm.traceLocalVariable(2);
    assertThat(this.tm.isTracingLocalVariable(2)).isFalse();

    this.mem.setWord(0, 2);
    this.tm.traceLocalVariable(2);
    assertThat(this.tm.isTracingLocalVariable(2)).isFalse();

    this.mem.setWord(0, 3);
    this.tm.traceLocalVariable(2);
    assertThat(this.tm.isTracingLocalVariable(2)).isTrue();

    this.mem.setWord(0, 2);
    assertThat(this.tm.isTracingLocalVariable(2)).isFalse();

    this.mem.setWord(0, 3);
    assertThat(this.tm.isTracingLocalVariable(2)).isTrue();

    this.mem.setWord(2, 12);
    assertThat(this.tm.isTracingLocalVariable(2)).isTrue();
  }

  @Test
  public void testTraceLocalVariable_NegativeNumber() {
    printlnMethodName();
    Register.LV.setValue(100);
    this.mem.setWord(100, 150);

    this.tm.traceLocalVariable(0);
    this.tm.traceLocalVariable(-1);
    this.tm.traceLocalVariable(-10);
    this.tm.traceLocalVariable(-100);
    this.tm.traceLocalVariable(-1000);
    assertThat(this.tm.isTracingLocalVariable(42)).isFalse();
    assertThat(this.tm.isTracingLocalVariable(0)).isFalse();
    assertThat(this.tm.isTracingLocalVariable(-1)).isFalse();
    assertThat(this.tm.isTracingLocalVariable(-10)).isFalse();
    assertThat(this.tm.isTracingLocalVariable(-100)).isFalse();
    assertThat(this.tm.isTracingLocalVariable(-1000)).isFalse();
  }

  @Test
  public void testTraceLocalVariable() {
    printlnMethodName();
    Register.LV.setValue(100);
    this.mem.setWord(100, 151);
    this.mem.setWord(101, 151);

    this.tm.traceLocalVariable(1);
    this.tm.traceLocalVariable(5);
    this.tm.traceLocalVariable(10);
    this.tm.traceLocalVariable(50);
    this.tm.traceLocalVariable(51);
    this.tm.traceLocalVariable(100);
    assertThat(this.tm.isTracingLocalVariable(1)).isTrue();
    assertThat(this.tm.isTracingLocalVariable(5)).isTrue();
    assertThat(this.tm.isTracingLocalVariable(10)).isTrue();
    assertThat(this.tm.isTracingLocalVariable(50)).isTrue();
    assertThat(this.tm.isTracingLocalVariable(51)).isFalse();
    assertThat(this.tm.isTracingLocalVariable(100)).isFalse();

    Register.LV.setValue(101);
    this.tm.traceLocalVariable(5);
    assertThat(this.tm.isTracingLocalVariable(1)).isFalse();
    assertThat(this.tm.isTracingLocalVariable(4)).isFalse();
    assertThat(this.tm.isTracingLocalVariable(5)).isTrue();
    assertThat(this.tm.isTracingLocalVariable(9)).isFalse();
    assertThat(this.tm.isTracingLocalVariable(10)).isFalse();
    assertThat(this.tm.isTracingLocalVariable(49)).isFalse();
    assertThat(this.tm.isTracingLocalVariable(50)).isFalse();
    assertThat(this.tm.isTracingLocalVariable(51)).isFalse();
    assertThat(this.tm.isTracingLocalVariable(99)).isFalse();
    assertThat(this.tm.isTracingLocalVariable(100)).isFalse();

    Register.LV.setValue(100);
    assertThat(this.tm.isTracingLocalVariable(1)).isTrue();
    assertThat(this.tm.isTracingLocalVariable(5)).isTrue();
    assertThat(this.tm.isTracingLocalVariable(6)).isFalse();
    assertThat(this.tm.isTracingLocalVariable(10)).isTrue();
    assertThat(this.tm.isTracingLocalVariable(50)).isTrue();
    assertThat(this.tm.isTracingLocalVariable(51)).isFalse();
    assertThat(this.tm.isTracingLocalVariable(100)).isFalse();
  }

  @Test
  public void testUntraceLocalVariable() {
    printlnMethodName();
    Register.LV.setValue(100);
    this.mem.setWord(100, 151);

    this.tm.traceLocalVariable(1);
    this.tm.traceLocalVariable(5);
    this.tm.traceLocalVariable(10);
    this.tm.traceLocalVariable(25);
    this.tm.traceLocalVariable(30);
    this.tm.traceLocalVariable(50);
    this.tm.traceLocalVariable(50);
    this.tm.traceLocalVariable(50);

    assertThat(this.tm.isTracingLocalVariable(1)).isTrue();
    assertThat(this.tm.isTracingLocalVariable(5)).isTrue();
    assertThat(this.tm.isTracingLocalVariable(10)).isTrue();
    assertThat(this.tm.isTracingLocalVariable(25)).isTrue();
    assertThat(this.tm.isTracingLocalVariable(30)).isTrue();
    assertThat(this.tm.isTracingLocalVariable(50)).isTrue();

    this.tm.untraceLocalVariable(1);
    this.tm.untraceLocalVariable(50);

    assertThat(this.tm.isTracingLocalVariable(1)).isFalse();
    assertThat(this.tm.isTracingLocalVariable(5)).isTrue();
    assertThat(this.tm.isTracingLocalVariable(10)).isTrue();
    assertThat(this.tm.isTracingLocalVariable(25)).isTrue();
    assertThat(this.tm.isTracingLocalVariable(30)).isTrue();
    assertThat(this.tm.isTracingLocalVariable(50)).isFalse();

    this.tm.traceLocalVariable(50);
    this.tm.untraceLocalVariable(10);
    this.tm.untraceLocalVariable(30);

    assertThat(this.tm.isTracingLocalVariable(1)).isFalse();
    assertThat(this.tm.isTracingLocalVariable(5)).isTrue();
    assertThat(this.tm.isTracingLocalVariable(10)).isFalse();
    assertThat(this.tm.isTracingLocalVariable(25)).isTrue();
    assertThat(this.tm.isTracingLocalVariable(30)).isFalse();
    assertThat(this.tm.isTracingLocalVariable(50)).isTrue();
  }
}
