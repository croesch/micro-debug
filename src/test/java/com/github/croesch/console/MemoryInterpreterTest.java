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

import java.io.IOException;

import org.junit.Test;

import com.github.croesch.DefaultTestCase;
import com.github.croesch.commons.Settings;
import com.github.croesch.console.MemoryInterpreter;
import com.github.croesch.error.FileFormatException;
import com.github.croesch.i18n.Text;
import com.github.croesch.mic1.mem.Memory;
import com.github.croesch.mic1.register.Register;

/**
 * Provides test cases for {@link MemoryInterpreter}
 * 
 * @author croesch
 * @since Date: Feb 13, 2012
 */
public class MemoryInterpreterTest extends DefaultTestCase {

  private Memory mem;

  private MemoryInterpreter interpreter;

  @Override
  public void setUpDetails() throws FileFormatException {
    this.mem = new Memory(Settings.MIC1_MEM_MACRO_MAXSIZE.getValue(),
                          ClassLoader.getSystemResourceAsStream("mic1/add.ijvm"));
    this.interpreter = new MemoryInterpreter(this.mem);
    Register.CPP.setValue(Settings.MIC1_REGISTER_CPP_DEFVAL.getValue());
  }

  @Test
  public void testPrintCode_All() throws IOException {
    printlnMethodName();
    this.interpreter.printCode();
    final StringBuilder sb = readFile("mic1/add.ijvm.dis");

    assertThat(out.toString()).isEqualTo(sb.toString());

  }

  @Test
  public void testPrintCode_Part1() throws IOException {
    printlnMethodName();
    this.interpreter.printCode(0, 0x1D);
    final StringBuilder sb = readFile("mic1/add_part1.ijvm.dis");

    assertThat(out.toString()).isEqualTo(sb.toString());
    out.reset();

    this.interpreter.printCode(-1, 0x1D);

    assertThat(out.toString()).isEqualTo(sb.toString());
    out.reset();

    this.interpreter.printCode(Integer.MIN_VALUE, 0x1E);

    assertThat(out.toString()).isEqualTo(sb.toString());

  }

  @Test
  public void testPrintCode_Around1() throws IOException {
    printlnMethodName();
    this.interpreter.printCodeAroundLine(0, 0x1D);
    final StringBuilder sb = readFile("mic1/add_part1.ijvm.dis");

    assertThat(out.toString()).isEqualTo(sb.toString());
    out.reset();

    this.interpreter.printCodeAroundLine(1, 0x1C);

    assertThat(out.toString()).isEqualTo(sb.toString());
    out.reset();

    this.interpreter.printCodeAroundLine(14, 15);

    assertThat(out.toString()).isEqualTo(sb.toString());

  }

  @Test
  public void testPrintCode_Part2() throws IOException {
    printlnMethodName();
    this.interpreter.printCode(0x18, 0x2E);
    final StringBuilder sb = readFile("mic1/add_part2.ijvm.dis");

    assertThat(out.toString()).isEqualTo(sb.toString());
    out.reset();

    this.interpreter.printCode(0x18, 0x2F);

    assertThat(out.toString()).isEqualTo(sb.toString());
    out.reset();

    this.interpreter.printCode(0x17, 0x2E);
    assertThat(out.toString()).isNotEqualTo(sb.toString());
    out.reset();

    this.interpreter.printCode(0x18, 0x30);
    assertThat(out.toString()).isNotEqualTo(sb.toString());
    out.reset();

  }

  @Test
  public void testPrintCode_Around2() throws IOException {
    printlnMethodName();
    this.interpreter.printCodeAroundLine(35, 11);
    final StringBuilder sb = readFile("mic1/add_part2.ijvm.dis");

    assertThat(out.toString()).isEqualTo(sb.toString());
    out.reset();

  }

  @Test
  public void testPrintCode_Part3() throws IOException {
    printlnMethodName();
    final StringBuilder sb = readFile("mic1/add_part3.ijvm.dis");
    this.interpreter.printCode(0xff, 0x11d);

    assertThat(out.toString()).isEqualTo(sb.toString());
    out.reset();

    this.interpreter.printCode(0xff, 0x11E);
    assertThat(out.toString()).isEqualTo(sb.toString());
    out.reset();

    this.interpreter.printCode(0xff, Integer.MAX_VALUE);
    assertThat(out.toString()).isEqualTo(sb.toString());
    out.reset();

    this.interpreter.printCode(0x100, 0x11d);
    assertThat(out.toString()).isNotEqualTo(sb.toString());
    out.reset();

  }

  @Test
  public void testPrintCode_Around() {
    printlnMethodName();
    this.interpreter.printCodeAroundLine(2, 0);

    assertThat(out.toString()).isEqualTo("     0x2: [ 0x59] DUP" + getLineSeparator());

  }

  @Test
  public void testPrintCode_Around3() throws IOException {
    printlnMethodName();
    final StringBuilder sb = readFile("mic1/add_part3.ijvm.dis");
    this.interpreter.printCodeAroundLine(0x10E, 15);

    assertThat(out.toString()).isEqualTo(sb.toString());
    out.reset();

    this.interpreter.printCodeAroundLine(0x10F, 16);
    assertThat(out.toString()).isEqualTo(sb.toString());
    out.reset();

    this.interpreter.printCodeAroundLine(0x110, 17);
    assertThat(out.toString()).isEqualTo(sb.toString());
    out.reset();

  }

  @Test
  public void testPrintContent() throws FileFormatException {
    printlnMethodName();
    this.mem = new Memory(Byte.MAX_VALUE, ClassLoader.getSystemResourceAsStream("mic1/test.ijvm"));
    this.interpreter = new MemoryInterpreter(this.mem);

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
                                                 + Text.MEMORY_CONTENT.text("    0x7E", "0x0") + getLineSeparator());
    out.reset();

    this.interpreter.printContent(Integer.MAX_VALUE, Byte.MAX_VALUE - 3);
    assertThat(out.toString()).isEqualTo(Text.MEMORY_CONTENT.text("    0x7C", "0x0") + getLineSeparator()
                                                 + Text.MEMORY_CONTENT.text("    0x7D", "0x0") + getLineSeparator()
                                                 + Text.MEMORY_CONTENT.text("    0x7E", "0x0") + getLineSeparator());
    out.reset();
  }

  @Test
  public void testGetFormattedLine() {
    printlnMethodName();

    assertThat(this.interpreter.getFormattedLine(-10)).isNullOrEmpty();
    assertThat(this.interpreter.getFormattedLine(0)).isEqualTo("     0x0: [ 0x10] BIPUSH 0x0");
  }

  @Test
  public void testPrintStack() {
    printlnMethodName();
    Register.SP.setValue(Settings.MIC1_REGISTER_SP_DEFVAL.getValue());

    this.interpreter.printStack(1);
    assertThat(out.toString()).isEqualTo(Text.STACK_EMPTY.text() + getLineSeparator());
  }
}
