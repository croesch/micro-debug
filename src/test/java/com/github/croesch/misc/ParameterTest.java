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
package com.github.croesch.misc;

import static org.fest.assertions.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.Test;

import com.github.croesch.DefaultTestCase;
import com.github.croesch.i18n.Text;
import com.github.croesch.mic1.register.Register;

/**
 * Provides test cases for {@link Parameter}.
 * 
 * @author croesch
 * @since Date: Dec 3, 2011
 */
public class ParameterTest extends DefaultTestCase {

  @Test
  public void testNumber_Valid() {
    final ByteArrayOutputStream out = new ByteArrayOutputStream();
    Printer.setPrintStream(new PrintStream(out));

    assertThat(Parameter.NUMBER.getValue("12")).isEqualTo(12);
    assertThat(Parameter.NUMBER.getValue("0x12")).isEqualTo(0x12);
    assertThat(Parameter.NUMBER.getValue("16_12")).isEqualTo(0x12);
    assertThat(Parameter.NUMBER.getValue("10_1234")).isEqualTo(1234);
    assertThat(Parameter.NUMBER.getValue("2_1010")).isEqualTo(10);
    assertThat(Parameter.NUMBER.getValue("02_11010")).isEqualTo(26);
    assertThat(Parameter.NUMBER.getValue("17_1g")).isEqualTo(33);
    assertThat(Parameter.NUMBER.getValue("17_-1g")).isEqualTo(-33);
    assertThat(Parameter.NUMBER.getValue("17_-1G")).isEqualTo(-33);
    assertThat(Parameter.NUMBER.getValue("-011")).isEqualTo(-11);
    assertThat(Parameter.NUMBER.getValue("-0000")).isEqualTo(0);

    assertThat(out.toString()).isEmpty();
    Printer.setPrintStream(System.out);
  }

  @Test
  public void testRegister_Valid() {
    final ByteArrayOutputStream out = new ByteArrayOutputStream();
    Printer.setPrintStream(new PrintStream(out));

    assertThat(Parameter.REGISTER.getValue("CPP")).isSameAs(Register.CPP);
    assertThat(Parameter.REGISTER.getValue("H")).isSameAs(Register.H);
    assertThat(Parameter.REGISTER.getValue("LV")).isSameAs(Register.LV);
    assertThat(Parameter.REGISTER.getValue("MAR")).isSameAs(Register.MAR);
    assertThat(Parameter.REGISTER.getValue("MBR")).isSameAs(Register.MBR);
    assertThat(Parameter.REGISTER.getValue("MBRU")).isSameAs(Register.MBRU);
    assertThat(Parameter.REGISTER.getValue("MDR")).isSameAs(Register.MDR);
    assertThat(Parameter.REGISTER.getValue("OPC")).isSameAs(Register.OPC);
    assertThat(Parameter.REGISTER.getValue("PC")).isSameAs(Register.PC);
    assertThat(Parameter.REGISTER.getValue("SP")).isSameAs(Register.SP);
    assertThat(Parameter.REGISTER.getValue("TOS")).isSameAs(Register.TOS);
    assertThat(Parameter.REGISTER.getValue("CPp")).isSameAs(Register.CPP);
    assertThat(Parameter.REGISTER.getValue("h")).isSameAs(Register.H);
    assertThat(Parameter.REGISTER.getValue("Lv")).isSameAs(Register.LV);
    assertThat(Parameter.REGISTER.getValue("MaR")).isSameAs(Register.MAR);
    assertThat(Parameter.REGISTER.getValue("MbR")).isSameAs(Register.MBR);
    assertThat(Parameter.REGISTER.getValue("MBrU")).isSameAs(Register.MBRU);
    assertThat(Parameter.REGISTER.getValue("MdR")).isSameAs(Register.MDR);
    assertThat(Parameter.REGISTER.getValue("opC")).isSameAs(Register.OPC);
    assertThat(Parameter.REGISTER.getValue("Pc")).isSameAs(Register.PC);
    assertThat(Parameter.REGISTER.getValue("Sp")).isSameAs(Register.SP);
    assertThat(Parameter.REGISTER.getValue("tOS")).isSameAs(Register.TOS);

    assertThat(out.toString()).isEmpty();
    Printer.setPrintStream(System.out);
  }

  @Test
  public void testRegister_Invalid() {
    final ByteArrayOutputStream out = new ByteArrayOutputStream();
    Printer.setPrintStream(new PrintStream(out));

    testInvalidRegister("a", out);
    testInvalidRegister("hH", out);
    testInvalidRegister("o", out);
    testInvalidRegister("2_a010", out);
    testInvalidRegister("xy", out);
    testInvalidRegister("", out);
    testInvalidRegister(" ", out);
    testInvalidRegister("\t", out);
    testInvalidRegister(".", out);
    assertThat(Parameter.REGISTER.getValue(null)).isNull();

    Printer.setPrintStream(System.out);
  }

  @Test
  public void testNumber_Invalid() {
    final ByteArrayOutputStream out = new ByteArrayOutputStream();
    Printer.setPrintStream(new PrintStream(out));

    testInvalidNumber("a_1010", out);
    testInvalidNumber("a_1010", out);
    testInvalidNumber("2_2010", out);
    testInvalidNumber("2_a010", out);
    testInvalidNumber("a010", out);
    testInvalidNumber("0 10", out);
    testInvalidNumber("010.1", out);
    testInvalidNumber("010,1", out);
    testInvalidNumber("--12", out);
    assertThat(Parameter.NUMBER.getValue(null)).isNull();

    Printer.setPrintStream(System.out);
  }

  private void testInvalidRegister(final String str, final ByteArrayOutputStream out) {
    assertThat(Parameter.REGISTER.getValue(str)).isNull();
    assertThat(out.toString()).isEqualTo(Text.ERROR.text(Text.INVALID_REGISTER.text(str)) + getLineSeparator());
    out.reset();
  }

  private void testInvalidNumber(final String str, final ByteArrayOutputStream out) {
    assertThat(Parameter.NUMBER.getValue(str)).isNull();
    assertThat(out.toString()).isEqualTo(Text.ERROR.text(Text.INVALID_NUMBER.text(str)) + getLineSeparator());
    out.reset();
  }

}
