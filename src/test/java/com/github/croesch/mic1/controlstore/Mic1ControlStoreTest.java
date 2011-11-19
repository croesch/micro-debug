package com.github.croesch.mic1.controlstore;

import java.io.ByteArrayInputStream;

import org.junit.Test;

import com.github.croesch.mic1.FileFormatException;

/**
 * Contains test cases for {@link Mic1ControlStore}.
 * 
 * @author croesch
 * @since Date: Nov 19, 2011
 */
public class Mic1ControlStoreTest {

  @Test(expected = FileFormatException.class)
  public void testConstructor_TooShortFile_Bytes0() throws FileFormatException {
    new Mic1ControlStore(new ByteArrayInputStream(new byte[] {}));
  }

  @Test(expected = FileFormatException.class)
  public void testConstructor_TooShortFile_Bytes1() throws FileFormatException {
    new Mic1ControlStore(new ByteArrayInputStream(new byte[] { 1 }));
  }

  @Test(expected = FileFormatException.class)
  public void testConstructor_TooShortFile_Bytes2() throws FileFormatException {
    new Mic1ControlStore(new ByteArrayInputStream(new byte[] { 1, 2 }));
  }

  @Test(expected = FileFormatException.class)
  public void testConstructor_TooShortFile_Bytes3() throws FileFormatException {
    new Mic1ControlStore(new ByteArrayInputStream(new byte[] { 1, 2, 3 }));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructor_Null() throws FileFormatException {
    new Mic1ControlStore(null);
  }

  @Test(expected = FileFormatException.class)
  public void testConstructor_FalseFormat() throws FileFormatException {
    new Mic1ControlStore(new ByteArrayInputStream(new byte[] { 0x12, 0x34, 0x56, 0x77 }));
  }

  @Test(expected = FileFormatException.class)
  public void testConstructor_EmptyFile() throws FileFormatException {
    new Mic1ControlStore(new ByteArrayInputStream(new byte[] { 0x12, 0x34, 0x56, 0x78 }));
  }

  @Test(expected = FileFormatException.class)
  public void testConstructor_TooBigFile() throws FileFormatException {
    // magic number + 512 * 5 byte + 5 byte to have the first instruction and the file is too big.
    final byte[] bs = new byte[4 + 513 * 5];
    bs[0] = 0x12;
    bs[1] = 0x34;
    bs[2] = 0x56;
    bs[3] = 0x78;

    new Mic1ControlStore(new ByteArrayInputStream(bs));
  }

}
