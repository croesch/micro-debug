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
package com.github.croesch;

import static org.fest.assertions.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.StringReader;

import org.junit.Test;

import com.github.croesch.console.io.Printer;
import com.github.croesch.error.FileFormatException;
import com.github.croesch.misc.Utils;

/**
 * Provides tests for {@link Utils}.
 * 
 * @author croesch
 * @since Date: Nov 23, 2011
 */
public class UtilsTest {

  @Test
  public void testIsOneValueMinusOne() {
    assertThat(Utils.isOneValueMinusOne(null)).isFalse();
    assertThat(Utils.isOneValueMinusOne(new int[] {})).isFalse();
    assertThat(Utils.isOneValueMinusOne(new int[] { 1 })).isFalse();
    assertThat(Utils.isOneValueMinusOne(new int[] { -1 })).isTrue();
    assertThat(Utils.isOneValueMinusOne(new int[] { -1, 1 })).isTrue();
    assertThat(Utils.isOneValueMinusOne(new int[] { 1, -1 })).isTrue();
    assertThat(Utils.isOneValueMinusOne(new int[] { 1, 1, 1, 17, -2, 0, -3 })).isFalse();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCheckMagicNumber_Null() throws FileFormatException {
    Utils.checkMagicNumber(null, 12);
  }

  @Test(expected = FileFormatException.class)
  public void testCheckMagicNumber_ZeroBytes() throws FileFormatException {
    Utils.checkMagicNumber(new ByteArrayInputStream(new byte[] {}), 12);
  }

  @Test(expected = FileFormatException.class)
  public void testCheckMagicNumber_OneBytes() throws FileFormatException {
    Utils.checkMagicNumber(new ByteArrayInputStream(new byte[] { 1 }), 12);
  }

  @Test(expected = FileFormatException.class)
  public void testCheckMagicNumber_TwoBytes() throws FileFormatException {
    Utils.checkMagicNumber(new ByteArrayInputStream(new byte[] { 1, 2 }), 12);
  }

  @Test(expected = FileFormatException.class)
  public void testCheckMagicNumber_ThreeBytes() throws FileFormatException {
    Utils.checkMagicNumber(new ByteArrayInputStream(new byte[] { 1, 2, 3 }), 12);
  }

  @Test(expected = FileFormatException.class)
  public void testCheckMagicNumber_Wrong_First() throws FileFormatException {
    Utils.checkMagicNumber(new ByteArrayInputStream(new byte[] { 0x11, 0x34, 0x56, 0x78 }), 0x12345678);
  }

  @Test(expected = FileFormatException.class)
  public void testCheckMagicNumber_Wrong_Second() throws FileFormatException {
    Utils.checkMagicNumber(new ByteArrayInputStream(new byte[] { 0x12, 0x33, 0x56, 0x78 }), 0x12345678);
  }

  @Test(expected = FileFormatException.class)
  public void testCheckMagicNumber_Wrong_Third() throws FileFormatException {
    Utils.checkMagicNumber(new ByteArrayInputStream(new byte[] { 0x12, 0x34, 0x66, 0x78 }), 0x12345678);
  }

  @Test(expected = FileFormatException.class)
  public void testCheckMagicNumber_Wrong_Fourth() throws FileFormatException {
    Utils.checkMagicNumber(new ByteArrayInputStream(new byte[] { 0x12, 0x34, 0x56, 0 }), 0x12345678);
  }

  @Test
  public void testCheckMagicNumber_Correct() throws FileFormatException {
    Utils.checkMagicNumber(new ByteArrayInputStream(new byte[] { 0x12, 0x34, 0x56, 0x78 }), 0x12345678);
    Utils.checkMagicNumber(new ByteArrayInputStream(new byte[] { 0x12, 0x34, 0x56, 0x78, 0 }), 0x12345678);
    Utils.checkMagicNumber(new ByteArrayInputStream(new byte[] { 0x12, 0x34, 0x56, 0x78, -1 }), 0x12345678);
    Utils.checkMagicNumber(new ByteArrayInputStream(new byte[] { 0x12, 0x34, 0x56, 0x78, 0x0, 0x1 }), 0x12345678);
    Utils.checkMagicNumber(new ByteArrayInputStream(new byte[] { 0, 0, 0, 0 }), 0);
    Utils.checkMagicNumber(new ByteArrayInputStream(new byte[] { -1, -1, -1, -1 }), -1);
    Utils.checkMagicNumber(new ByteArrayInputStream(new byte[] { 4, 4, 4, 4 }), 0x04040404);
  }

  @Test
  public void testBytesToInt() {
    assertThat(Utils.bytesToInt((byte) 0, (byte) 0, (byte) 0, (byte) 0)).isEqualTo(0);
    assertThat(Utils.bytesToInt((byte) -1, (byte) -1, (byte) -1, (byte) -1)).isEqualTo(-1);
    assertThat(Utils.bytesToInt((byte) 0x12, (byte) 0x34, (byte) 0x56, (byte) 0x78)).isEqualTo(0x12345678);
    assertThat(Utils.bytesToInt((byte) 0x87, (byte) 0x65, (byte) 0x43, (byte) 0x21)).isEqualTo(0x87654321);
    assertThat(Utils.bytesToInt((byte) 0xff, (byte) 0, (byte) 0xff, (byte) 0)).isEqualTo(0xff00ff00);
    assertThat(Utils.bytesToInt((byte) 0, (byte) 0xff, (byte) 0, (byte) 0xaa)).isEqualTo(0xff00aa);
    assertThat(Utils.bytesToInt((byte) 0, (byte) 0, (byte) 0xff, (byte) 0)).isEqualTo(0xff00);
    assertThat(Utils.bytesToInt((byte) 0, (byte) 0, (byte) 0, (byte) 0xff)).isEqualTo(0xff);
    assertThat(Utils.bytesToInt((byte) 0, (byte) 0, (byte) 0, (byte) -1)).isEqualTo(255);
  }

  @Test
  public void testPrintReaderToPrinter_Null() {
    final ByteArrayOutputStream out = new ByteArrayOutputStream();
    Printer.setPrintStream(new PrintStream(out));

    Utils.printReaderToPrinter(null);
    assertThat(out.toString()).isEmpty();
  }

  @Test(expected = IOException.class)
  public void testPrintReaderToPrinter_ClosedAfter() throws IOException {
    final ByteArrayOutputStream out = new ByteArrayOutputStream();
    Printer.setPrintStream(new PrintStream(out));

    final StringReader r = new StringReader("xy");
    Utils.printReaderToPrinter(r);
    assertThat(out.toString()).isEqualTo("xy\n");
    r.read(); // stream closed
  }

  @Test
  public void testPrintReaderToPrinter_ClosedBefore() throws IOException {
    final ByteArrayOutputStream out = new ByteArrayOutputStream();
    Printer.setPrintStream(new PrintStream(out));

    final StringReader r = new StringReader("xy");
    r.close();
    Utils.printReaderToPrinter(r);
    assertThat(out.toString()).isEmpty();
  }

  @Test
  public void testPrintReaderToPrinter() {
    final ByteArrayOutputStream out = new ByteArrayOutputStream();
    Printer.setPrintStream(new PrintStream(out));

    String text = "aaa\nbb\nccc\n";
    Utils.printReaderToPrinter(new StringReader(text));
    assertThat(out.toString()).isEqualTo(text);
    out.reset();

    text = "";
    Utils.printReaderToPrinter(new StringReader(text));
    assertThat(out.toString()).isEqualTo(text);
    out.reset();

    text = "ß0987654321\n!§$%&/()=?\n@ł€ŧ←↓→\n";
    Utils.printReaderToPrinter(new StringReader(text));
    assertThat(out.toString()).isEqualTo(text);
    out.reset();
  }
}
