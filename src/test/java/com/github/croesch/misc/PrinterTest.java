/*
 * Copyright (C) 2011-2012 Christian Roesch
 * This file is part of micro-debug.
 * micro-debug is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * micro-debug is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with micro-debug. If not, see <http://www.gnu.org/licenses/>.
 */
package com.github.croesch.misc;

import static org.fest.assertions.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.StringReader;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.github.croesch.DefaultTestCase;
import com.github.croesch.TestUtil;
import com.github.croesch.i18n.Text;

/**
 * Provides test cases for {@link Printer}.
 * 
 * @author croesch
 * @since Date: Dec 2, 2011
 */
public class PrinterTest extends DefaultTestCase {

  private final ByteArrayOutputStream out = new ByteArrayOutputStream();

  @Before
  public void setUp() {
    Printer.setPrintStream(new PrintStream(this.out));
  }

  @After
  public void tearDown() {
    this.out.reset();
  }

  @Test
  public void testNullValues() {
    Printer.println(null);
    assertThat(this.out.toString()).isEmpty();

    Printer.println((String)null);
    assertThat(this.out.toString()).isEmpty();

    Printer.printErrorln(null);
    assertThat(this.out.toString()).isEmpty();
  }

  @Test
  public void testPrintln() {
    Printer.println("asd");
    assertThat(this.out.toString()).isEqualTo("asd" + TestUtil.getLineSeparator());

    Printer.println("");
    assertThat(this.out.toString()).isEqualTo("asd" + TestUtil.getLineSeparator() + "" + TestUtil.getLineSeparator());

    this.out.reset();

    Printer.println("Dies ist eine neue Zeile" + TestUtil.getLineSeparator() + "und hier noch eine");
    assertThat(this.out.toString()).isEqualTo("Dies ist eine neue Zeile" + TestUtil.getLineSeparator()
                                              + "und hier noch eine" + TestUtil.getLineSeparator());
  }

  @Test
  public void testPrintln_Object() {
    Printer.println(14);
    assertThat(this.out.toString()).isEqualTo("14" + TestUtil.getLineSeparator());

    this.out.reset();

    Printer.println(123);
    assertThat(this.out.toString()).isEqualTo("123" + TestUtil.getLineSeparator());
  }

  @Test
  public void testPrint() {
    Printer.printErrorln("asd");
    assertThat(this.out.toString()).isEqualTo(Text.ERROR.text("asd") + TestUtil.getLineSeparator());

    this.out.reset();

    Printer.printErrorln("Dies ist eine neue Zeile" + TestUtil.getLineSeparator() + "und hier noch eine");
    assertThat(this.out.toString()).isEqualTo(Text.ERROR.text("Dies ist eine neue Zeile") + TestUtil.getLineSeparator()
                                              + Text.ERROR.text("und hier noch eine") + TestUtil.getLineSeparator());
  }

  @Test
  public void testSetPrintStream() {
    final ByteArrayOutputStream out1 = new ByteArrayOutputStream();
    final ByteArrayOutputStream out2 = new ByteArrayOutputStream();
    Printer.setPrintStream(new PrintStream(out1));

    Printer.println("asd");
    assertThat(out1.toString()).isEqualTo("asd" + TestUtil.getLineSeparator());

    Printer.setPrintStream(null);

    Printer.println("asd");
    assertThat(out1.toString()).isEqualTo("asd" + TestUtil.getLineSeparator() + "asd" + TestUtil.getLineSeparator());

    Printer.setPrintStream(new PrintStream(out2));

    Printer.println("asd");
    assertThat(out1.toString()).isEqualTo("asd" + TestUtil.getLineSeparator() + "asd" + TestUtil.getLineSeparator());
    assertThat(out2.toString()).isEqualTo("asd" + TestUtil.getLineSeparator());

    Printer.setPrintStream(null);

    Printer.println("asd");
    assertThat(out1.toString()).isEqualTo("asd" + TestUtil.getLineSeparator() + "asd" + TestUtil.getLineSeparator());
    assertThat(out2.toString()).isEqualTo("asd" + TestUtil.getLineSeparator() + "asd" + TestUtil.getLineSeparator());
  }

  @Test
  public void testPrintReaderToPrinter_Null() {
    Printer.printReader(null);
    assertThat(this.out.toString()).isEmpty();
  }

  @Test(expected = IOException.class)
  public void testPrintReaderToPrinter_ClosedAfter() throws IOException {
    final StringReader r = new StringReader("xy");
    Printer.printReader(r);
    assertThat(this.out.toString()).isEqualTo("xy" + TestUtil.getLineSeparator());
    r.read(); // stream closed
  }

  @Test
  public void testPrintReaderToPrinter_ClosedBefore() throws IOException {
    final StringReader r = new StringReader("xy");
    r.close();
    Printer.printReader(r);
    assertThat(this.out.toString()).isEmpty();
  }

  @Test
  public void testPrintReaderToPrinter() {
    String text = "aaa" + TestUtil.getLineSeparator() + "bb" + TestUtil.getLineSeparator() + "ccc"
                  + TestUtil.getLineSeparator();
    Printer.printReader(new StringReader(text));
    assertThat(this.out.toString()).isEqualTo(text);
    this.out.reset();

    text = "";
    Printer.printReader(new StringReader(text));
    assertThat(this.out.toString()).isEqualTo(text);
    this.out.reset();

    text = "as0987654321" + TestUtil.getLineSeparator() + "!!§$%&/()=?" + TestUtil.getLineSeparator()
           + "@Å‚â‚¬Å§â†#â†“â†’" + TestUtil.getLineSeparator();
    Printer.printReader(new StringReader(text));
    assertThat(this.out.toString()).isEqualTo(text);
  }
}
