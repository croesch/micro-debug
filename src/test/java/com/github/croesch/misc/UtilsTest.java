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

import java.io.ByteArrayInputStream;

import org.junit.Test;

import com.github.croesch.DefaultTestCase;
import com.github.croesch.error.FileFormatException;

/**
 * Provides tests for {@link Utils}.
 * 
 * @author croesch
 * @since Date: Nov 23, 2011
 */
public class UtilsTest extends DefaultTestCase {

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
  public void testToHextString() {
    assertThat(Utils.toHexString(12)).isEqualTo("0xC");
    assertThat(Utils.toHexString(-12)).isEqualTo("0xFFFFFFF4");
    assertThat(Utils.toHexString(0)).isEqualTo("0x0");
    assertThat(Utils.toHexString(1)).isEqualTo("0x1");
    assertThat(Utils.toHexString(100)).isEqualTo("0x64");
    assertThat(Utils.toHexString(42)).isEqualTo("0x2A");
    assertThat(Utils.toHexString(4711)).isEqualTo("0x1267");
    assertThat(Utils.toHexString(-4711)).isEqualTo("0xFFFFED99");
    assertThat(Utils.toHexString(-1)).isEqualTo("0xFFFFFFFF");
    assertThat(Utils.toHexString(Integer.MAX_VALUE)).isEqualTo("0x7FFFFFFF");
    assertThat(Utils.toHexString(Integer.MIN_VALUE)).isEqualTo("0x80000000");
  }

  @Test
  public void testGetNextHigherValue() {
    assertThat(Utils.getNextHigherValue(-20, 0, 200, 19, 4, 15, 16)).isEqualTo(0);
    assertThat(Utils.getNextHigherValue(0, 0, 200, 19, 4, 15, 16)).isEqualTo(4);
    assertThat(Utils.getNextHigherValue(12, 0, 200, 19, 4, 15, 16)).isEqualTo(15);
    assertThat(Utils.getNextHigherValue(17, 0, 200, 19, 4, 15, 16)).isEqualTo(19);
    assertThat(Utils.getNextHigherValue(11, 0, 1, 2, 5, 10, 15, 20, 33, 23, 13, 3, -3)).isEqualTo(13);
    assertThat(Utils.getNextHigherValue(110, 0, 1, 2, 5, 10, 15, 20, 33, 23, 13, 3, -3)).isEqualTo(Integer.MAX_VALUE);
    assertThat(Utils.getNextHigherValue(110)).isEqualTo(Integer.MAX_VALUE);
  }
}
