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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

import com.github.croesch.DefaultTestCase;
import com.github.croesch.error.FileFormatException;
import com.github.croesch.i18n.Text;
import com.github.croesch.mic1.io.Input;
import com.github.croesch.mic1.register.Register;

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
    this.processor = new Mic1(ClassLoader.getSystemResourceAsStream("mic1/hi.mic1"),
                              ClassLoader.getSystemResourceAsStream("mic1/hi.ijvm"));
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
    this.processor = new Mic1(ClassLoader.getSystemResourceAsStream("mic1/mic1ijvm.mic1"),
                              ClassLoader.getSystemResourceAsStream("mic1/add.ijvm"));
    this.interpreter = new Mic1Interpreter(this.processor);

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
  public void testListBreakpoints() {
    printMethodName();

    this.interpreter.addRegisterBreakpoint(Register.MBRU, Integer.valueOf(16));
    this.interpreter.addRegisterBreakpoint(Register.MBRU, Integer.valueOf(-48));

    this.interpreter.addRegisterBreakpoint(Register.CPP, Integer.valueOf(-1));
    this.interpreter.addRegisterBreakpoint(Register.CPP, Integer.valueOf(Integer.MAX_VALUE));
    this.interpreter.addRegisterBreakpoint(Register.CPP, Integer.valueOf(Integer.MIN_VALUE));

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
                                               + Text.BREAKPOINT_REGISTER.text("[0-9]+", Register.H, "0x2")
                                               + getLineSeparator()
                                               + Text.BREAKPOINT_REGISTER.text("[0-9]+", Register.H, "0x3")
                                               + getLineSeparator()
                                               + Text.BREAKPOINT_REGISTER.text("[0-9]+", Register.H, "0x1")
                                               + getLineSeparator());

    printEndOfMethod();
  }
}
