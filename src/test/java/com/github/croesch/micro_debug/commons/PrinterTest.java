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
import java.io.IOException;
import java.io.PrintStream;
import java.io.StringReader;

import org.junit.Test;

import com.github.croesch.micro_debug.DefaultTestCase;
import com.github.croesch.micro_debug.i18n.Text;

/**
 * Provides test cases for {@link Printer}.
 * 
 * @author croesch
 * @since Date: Dec 2, 2011
 */
public class PrinterTest extends DefaultTestCase {

  @Test
  public void testNullValues() {
    printlnMethodName();
    Printer.print(null);
    assertThat(out.toString()).isEmpty();

    Printer.println(null);
    assertThat(out.toString()).isEmpty();

    Printer.println((String) null);
    assertThat(out.toString()).isEmpty();

    Printer.printErrorln(null);
    assertThat(out.toString()).isEmpty();
  }

  @Test
  public void testPrintln() {
    printlnMethodName();
    Printer.println("asd");
    assertThat(out.toString()).isEqualTo("asd" + getLineSeparator());

    Printer.println("");
    assertThat(out.toString()).isEqualTo("asd" + getLineSeparator() + "" + getLineSeparator());

    out.reset();

    Printer.println("Dies ist eine neue Zeile" + getLineSeparator() + "und hier noch eine");
    assertThat(out.toString()).isEqualTo("Dies ist eine neue Zeile" + getLineSeparator() + "und hier noch eine"
                                                 + getLineSeparator());

    out.reset();

    Printer.println(null);
    assertThat(out.toString()).isEmpty();
  }

  @Test
  public void testPrint() {
    printlnMethodName();
    Printer.print("asd");
    assertThat(out.toString()).isEqualTo("asd");

    Printer.print("");
    assertThat(out.toString()).isEqualTo("asd");

    out.reset();

    Printer.print("Dies ist eine neue Zeile" + getLineSeparator() + "und hier noch eine");
    assertThat(out.toString()).isEqualTo("Dies ist eine neue Zeile" + getLineSeparator() + "und hier noch eine");
  }

  @Test
  public void testPrintln_Object() {
    printlnMethodName();
    Printer.println(14);
    assertThat(out.toString()).isEqualTo("14" + getLineSeparator());

    out.reset();

    Printer.println(123);
    assertThat(out.toString()).isEqualTo("123" + getLineSeparator());

    out.reset();

    Printer.println((Object) null);
    assertThat(out.toString()).isEmpty();
  }

  @Test
  public void testPrint_Object() {
    printlnMethodName();
    Printer.print(14);
    assertThat(out.toString()).isEqualTo("14");

    out.reset();

    Printer.print(123);
    assertThat(out.toString()).isEqualTo("123");

    out.reset();

    Printer.print(this);
    assertThat(out.toString()).isEqualTo(this.toString());

    out.reset();

    Printer.print(null);
    assertThat(out.toString()).isEmpty();
  }

  @Test
  public void testPrintErrorln() {
    printlnMethodName();
    Printer.printErrorln("asd");
    assertThat(out.toString()).isEqualTo(Text.ERROR.text("asd") + getLineSeparator());

    out.reset();

    Printer.printErrorln("Dies ist eine neue Zeile" + getLineSeparator() + "und hier noch eine");
    assertThat(out.toString()).isEqualTo(Text.ERROR.text("Dies ist eine neue Zeile") + getLineSeparator()
                                                 + Text.ERROR.text("und hier noch eine") + getLineSeparator());
  }

  @Test
  public void testSetPrintStream() {
    printlnMethodName();
    final ByteArrayOutputStream out1 = new ByteArrayOutputStream();
    final ByteArrayOutputStream out2 = new ByteArrayOutputStream();
    Printer.setPrintStream(new PrintStream(out1));

    Printer.println("asd");
    assertThat(out1.toString()).isEqualTo("asd" + getLineSeparator());

    Printer.setPrintStream(null);

    Printer.println("asd");
    assertThat(out1.toString()).isEqualTo("asd" + getLineSeparator() + "asd" + getLineSeparator());

    Printer.setPrintStream(new PrintStream(out2));

    Printer.println("asd");
    assertThat(out1.toString()).isEqualTo("asd" + getLineSeparator() + "asd" + getLineSeparator());
    assertThat(out2.toString()).isEqualTo("asd" + getLineSeparator());

    Printer.setPrintStream(null);

    Printer.println("asd");
    assertThat(out1.toString()).isEqualTo("asd" + getLineSeparator() + "asd" + getLineSeparator());
    assertThat(out2.toString()).isEqualTo("asd" + getLineSeparator() + "asd" + getLineSeparator());
  }

  @Test
  public void testPrintReaderToPrinter_Null() {
    printlnMethodName();
    Printer.printReader(null);
    assertThat(out.toString()).isEmpty();
  }

  @Test(expected = IOException.class)
  public void testPrintReaderToPrinter_ClosedAfter() throws IOException {
    final StringReader r = new StringReader("xy");
    Printer.printReader(r);
    assertThat(out.toString()).isEqualTo("xy" + getLineSeparator());
    r.read(); // stream closed
  }

  @Test
  public void testPrintReaderToPrinter_ClosedBefore() throws IOException {
    printlnMethodName();
    final StringReader r = new StringReader("xy");
    r.close();
    Printer.printReader(r);
    assertThat(out.toString()).isEmpty();
  }

  @Test
  public void testPrintReaderToPrinter() {
    printlnMethodName();
    String text = "aaa" + getLineSeparator() + "bb" + getLineSeparator() + "ccc" + getLineSeparator();
    Printer.printReader(new StringReader(text));
    assertThat(out.toString()).isEqualTo(text);
    out.reset();

    text = "";
    Printer.printReader(new StringReader(text));
    assertThat(out.toString()).isEqualTo(text);
    out.reset();

    text = "as0987654321" + getLineSeparator() + "!!§$%&/()=?" + getLineSeparator() + "@Å‚â‚¬Å§â†#â†“â†’"
           + getLineSeparator();
    Printer.printReader(new StringReader(text));
    assertThat(out.toString()).isEqualTo(text);
  }
}
