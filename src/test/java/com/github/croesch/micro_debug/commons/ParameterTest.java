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
package com.github.croesch.micro_debug.commons;

import static org.fest.assertions.Assertions.assertThat;

import java.io.ByteArrayOutputStream;

import org.junit.Test;

import com.github.croesch.micro_debug.DefaultTestCase;
import com.github.croesch.micro_debug.i18n.Text;
import com.github.croesch.micro_debug.mic1.register.Register;

/**
 * Provides test cases for {@link Parameter}.
 * 
 * @author croesch
 * @since Date: Dec 3, 2011
 */
public class ParameterTest extends DefaultTestCase {

  @Test
  public void testNumber_Valid() {
    assertThat(Parameter.NUMBER.getValue("12")).isEqualTo(12);
    assertThat(Parameter.NUMBER.getValue("0x12")).isEqualTo(0x12);
    assertThat(Parameter.NUMBER.getValue("12_16")).isEqualTo(0x12);
    assertThat(Parameter.NUMBER.getValue("1234_10")).isEqualTo(1234);
    assertThat(Parameter.NUMBER.getValue("1010_2")).isEqualTo(10);
    assertThat(Parameter.NUMBER.getValue("0b1010")).isEqualTo(10);
    assertThat(Parameter.NUMBER.getValue("11010_002")).isEqualTo(26);
    assertThat(Parameter.NUMBER.getValue("1g_17")).isEqualTo(33);
    assertThat(Parameter.NUMBER.getValue("-1g_17")).isEqualTo(-33);
    assertThat(Parameter.NUMBER.getValue("-1G_17")).isEqualTo(-33);
    assertThat(Parameter.NUMBER.getValue("-011")).isEqualTo(-11);
    assertThat(Parameter.NUMBER.getValue("-0000")).isEqualTo(0);
    assertThat(Parameter.NUMBER.getValue("0o0")).isEqualTo(0);
    assertThat(Parameter.NUMBER.getValue("0o27")).isEqualTo(23);
    assertThat(Parameter.NUMBER.getValue("27_8")).isEqualTo(23);
    assertThat(Parameter.NUMBER.getValue("0B0")).isEqualTo(0);
    assertThat(Parameter.NUMBER.getValue("0O0")).isEqualTo(0);
    assertThat(Parameter.NUMBER.getValue("0X0")).isEqualTo(0);

    assertThat(out.toString()).isEmpty();
  }

  @Test
  public void testRegister_Valid() {
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
  }

  @Test
  public void testRegister_Invalid() {
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
  }

  @Test
  public void testNumber_Invalid() {
    testInvalidNumber("", out);
    testInvalidNumber(" ", out);
    testInvalidNumber("\t", out);
    testInvalidNumber("A", out);
    testInvalidNumber("1010_a", out);
    testInvalidNumber("2010_2", out);
    testInvalidNumber("a010_2", out);
    testInvalidNumber("a010", out);
    testInvalidNumber("0 10", out);
    testInvalidNumber("010.1", out);
    testInvalidNumber("010,1", out);
    testInvalidNumber("--12", out);
    testInvalidNumber("0b12", out);
    testInvalidNumber("0o19", out);
    testInvalidNumber("0x1G", out);
    testInvalidNumber("0b1_2", out);
    testInvalidNumber("0o1_8", out);
    testInvalidNumber("0x1_16", out);
    testInvalidNumber("1_1_6", out);
    assertThat(Parameter.NUMBER.getValue(null)).isNull();
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
