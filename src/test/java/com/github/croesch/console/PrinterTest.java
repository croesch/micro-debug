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

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.Test;

import com.github.croesch.i18n.Text;

/**
 * Provides test cases for {@link Printer}.
 * 
 * @author croesch
 * @since Date: Dec 2, 2011
 */
public class PrinterTest {

  @Test
  public void testNullValues() {
    final ByteArrayOutputStream out = new ByteArrayOutputStream();
    Printer.setPrintStream(new PrintStream(out));

    Printer.println(null);
    assertThat(out.toString()).isEmpty();

    Printer.println((String) null);
    assertThat(out.toString()).isEmpty();

    Printer.printErrorln(null);
    assertThat(out.toString()).isEmpty();
  }

  @Test
  public void testPrintln() {
    final ByteArrayOutputStream out = new ByteArrayOutputStream();
    Printer.setPrintStream(new PrintStream(out));

    Printer.println("asd");
    assertThat(out.toString()).isEqualTo("asd\n");

    Printer.println("");
    assertThat(out.toString()).isEqualTo("asd\n\n");

    out.reset();

    Printer.println("Dies ist eine neue Zeile\nund hier noch eine");
    assertThat(out.toString()).isEqualTo("Dies ist eine neue Zeile\nund hier noch eine\n");
  }

  @Test
  public void testPrintln_Object() {
    final ByteArrayOutputStream out = new ByteArrayOutputStream();
    Printer.setPrintStream(new PrintStream(out));

    Printer.println(14);
    assertThat(out.toString()).isEqualTo("14\n");

    out.reset();

    Printer.println(123);
    assertThat(out.toString()).isEqualTo("123\n");
  }

  @Test
  public void testPrint() {
    final ByteArrayOutputStream out = new ByteArrayOutputStream();
    Printer.setPrintStream(new PrintStream(out));

    Printer.printErrorln("asd");
    assertThat(out.toString()).isEqualTo(Text.ERROR.text("asd") + "\n");

    out.reset();

    Printer.printErrorln("Dies ist eine neue Zeile\nund hier noch eine");
    assertThat(out.toString()).isEqualTo(Text.ERROR.text("Dies ist eine neue Zeile") + "\n"
                                                 + Text.ERROR.text("und hier noch eine") + "\n");
  }

  @Test
  public void testSetPrintStream() {
    final ByteArrayOutputStream out1 = new ByteArrayOutputStream();
    final ByteArrayOutputStream out2 = new ByteArrayOutputStream();
    Printer.setPrintStream(new PrintStream(out1));

    Printer.println("asd");
    assertThat(out1.toString()).isEqualTo("asd\n");

    Printer.setPrintStream(null);

    Printer.println("asd");
    assertThat(out1.toString()).isEqualTo("asd\nasd\n");

    Printer.setPrintStream(new PrintStream(out2));

    Printer.println("asd");
    assertThat(out1.toString()).isEqualTo("asd\nasd\n");
    assertThat(out2.toString()).isEqualTo("asd\n");

    Printer.setPrintStream(null);

    Printer.println("asd");
    assertThat(out1.toString()).isEqualTo("asd\nasd\n");
    assertThat(out2.toString()).isEqualTo("asd\nasd\n");
  }
}
