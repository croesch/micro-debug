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
import java.io.StringReader;

import org.junit.Test;

import com.github.croesch.DefaultTestCase;
import com.github.croesch.commons.Reader;
import com.github.croesch.i18n.Text;

/**
 * Provides test cases for {@link Debugger}.
 * 
 * @author croesch
 * @since Date: Dec 3, 2011
 */
public class DebuggerTest extends DefaultTestCase {

  @Test
  public void testRun_Exit() {
    printlnMethodName();
    final Debugger debugger = new Debugger(null);

    Reader.setReader(new StringReader("exit"));
    debugger.run();

    Reader.setReader(new StringReader("EXIT"));
    debugger.run();

    Reader.setReader(new StringReader("exit now or never!!!"));
    debugger.run();

    assertThat(out.toString()).isEqualTo(Text.INPUT_DEBUGGER.text() + Text.INPUT_DEBUGGER.text()
                                                 + Text.INPUT_DEBUGGER.text());
  }

  @Test
  public void testRun_WrongCommand() {
    printlnMethodName();
    final Debugger debugger = new Debugger(null);

    Reader.setReader(new StringReader("excel\nexit"));
    debugger.run();
    assertThat(out.toString()).isEqualTo(Text.INPUT_DEBUGGER.text()
                                                 + Text.ERROR.text(Text.UNKNOWN_INSTRUCTION.text("excel"))
                                                 + getLineSeparator() + Text.INPUT_DEBUGGER.text());
    out.reset();

    Reader.setReader(new StringReader("schließe dich!\nEXIT"));
    debugger.run();
    assertThat(out.toString()).isEqualTo(Text.INPUT_DEBUGGER.text()
                                                 + Text.ERROR.text(Text.UNKNOWN_INSTRUCTION.text("schließe"))
                                                 + getLineSeparator() + Text.INPUT_DEBUGGER.text());
    out.reset();
  }

  @Test
  public void testRun_Help() throws IOException {
    printlnMethodName();
    final Debugger debugger = new Debugger(null);

    final StringBuilder sb = readFile("instruction-help.txt");

    Reader.setReader(new StringReader("help\nexit"));
    debugger.run();
    assertThat(out.toString()).isEqualTo(Text.INPUT_DEBUGGER.text() + sb.toString() + Text.INPUT_DEBUGGER.text());
    out.reset();

    Reader.setReader(new StringReader("HELP\nexit"));
    debugger.run();
    assertThat(out.toString()).isEqualTo(Text.INPUT_DEBUGGER.text() + sb.toString() + Text.INPUT_DEBUGGER.text());
    out.reset();

    Reader.setReader(new StringReader("heLp me!!\nexit"));
    debugger.run();
    assertThat(out.toString()).isEqualTo(Text.INPUT_DEBUGGER.text() + sb.toString() + Text.INPUT_DEBUGGER.text());
    out.reset();
  }
}
