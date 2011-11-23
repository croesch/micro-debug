package com.github.croesch;

import static org.fest.assertions.Assertions.assertThat;

import java.io.ByteArrayInputStream;

import org.junit.Test;

import com.github.croesch.error.FileFormatException;
import com.github.croesch.misc.Utils;

/**
 * TODO Comment here ...
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
}
