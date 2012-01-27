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
import com.github.croesch.mic1.register.Register;

/**
 * Provides test cases for {@link TraceManager}.
 * 
 * @author croesch
 * @since Date: Jan 15, 2012
 */
public class TraceManagerTest extends DefaultTestCase {

  private TraceManager tm;

  @Override
  protected void setUpDetails() {
    this.tm = new TraceManager();
  }

  @Test
  public void testTraceRegister() {
    for (final Register r : Register.values()) {
      assertThat(this.tm.isTracing(r)).isFalse();
      this.tm.traceRegister(r);
      assertThat(this.tm.isTracing(r)).isTrue();
    }
  }

  @Test
  public void testTraceAllRegisters() {
    for (final Register r : Register.values()) {
      assertThat(this.tm.isTracing(r)).isFalse();
    }

    this.tm.traceRegister();

    for (final Register r : Register.values()) {
      assertThat(this.tm.isTracing(r)).isTrue();
    }
  }

  @Test
  public void testUntraceAllRegisters() {
    this.tm.traceRegister();

    for (final Register r : Register.values()) {
      assertThat(this.tm.isTracing(r)).isTrue();
    }

    this.tm.untraceRegister();

    for (final Register r : Register.values()) {
      assertThat(this.tm.isTracing(r)).isFalse();
    }
  }

  @Test
  public void testUntraceRegister() {
    this.tm.traceRegister();

    for (final Register r : Register.values()) {
      assertThat(this.tm.isTracing(r)).isTrue();
      this.tm.untraceRegister(r);
      assertThat(this.tm.isTracing(r)).isFalse();
    }
  }

  @Test
  public void testUpdateTracedRegisters() {
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
    this.tm.update(null);

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
    this.tm.traceMicro();
    assertThat(this.tm.isTracingMicro()).isTrue();

    this.tm.untraceMicro();
    assertThat(this.tm.isTracingMicro()).isFalse();
  }
}
