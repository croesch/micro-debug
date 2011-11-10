package com.github.croesch.mic1.controlstore;

import static org.fest.assertions.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.BitSet;

import org.junit.Test;

/**
 * Provides test cases for {@link Mic1InstructionReader}. Basically tests that it produces the expected output from
 * given sample input.
 * 
 * @author croesch
 * @since Date: Nov 10, 2011
 */
public class Mic1InstructionReaderTest {

  @Test
  public void testRead() throws IOException {
    // MIR[36]: 1110 0111 1001 1001 1000 1111 1111 1000 1000
    // bytes  : e    7    9    9    8    f    f    8    8
    // bits   : 1c    f      2 3  6 7    11         20  8
    final byte[] buf = new byte[] { (byte) 0xe7, (byte) 0x99, (byte) 0x8f, (byte) 0xf8, (byte) 0x8f };
    final InputStream in = new ByteArrayInputStream(buf);
    final Mic1Instruction value = Mic1InstructionReader.read(in);
    final BitSet bits = new BitSet();
    bits.set(2);
    bits.set(3);
    bits.set(6);
    bits.set(7);
    bits.set(11, 20);
    final Mic1Instruction expected = new Mic1Instruction(0x1cf, bits, 0x8);

    assertThat(value).isEqualTo(expected);
  }

  @Test
  public void testRead_All() throws IOException {
    // MIR[36]: 1111 1111 1111 1111 1111 1111 1111 1111 1111
    // bytes  : f    f    f    f    f    f    f    f    f
    // bits   : 1f    f    0                         24 f
    final byte[] buf = new byte[] { (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xf0 };
    final InputStream in = new ByteArrayInputStream(buf);
    final Mic1Instruction value = Mic1InstructionReader.read(in);
    final BitSet bits = new BitSet();
    bits.set(0, 24);
    final Mic1Instruction expected = new Mic1Instruction(0x1ff, bits, 0xf);

    assertThat(value).isEqualTo(expected);
  }

  @Test
  public void testRead_NotNull_MoreThanFiveBytes() throws IOException {
    final byte[] buf = new byte[] { (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xf0, (byte) 0xf0 };
    final InputStream in = new ByteArrayInputStream(buf);
    final Mic1Instruction value = Mic1InstructionReader.read(in);

    assertThat(value).isNotNull();
  }

  @Test
  public void testRead_NotNull_FiveBytes() throws IOException {
    final byte[] buf = new byte[] { (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xf0, (byte) 0xf0 };
    final InputStream in = new ByteArrayInputStream(buf);
    final Mic1Instruction value = Mic1InstructionReader.read(in);

    assertThat(value).isNotNull();
  }

  @Test
  public void testRead_Null_FourBytes() throws IOException {
    final byte[] buf = new byte[] { (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xf0 };
    final InputStream in = new ByteArrayInputStream(buf);
    final Mic1Instruction value = Mic1InstructionReader.read(in);

    assertThat(value).isNull();
  }

  @Test
  public void testRead_Null_ThreeBytes() throws IOException {
    final byte[] buf = new byte[] { (byte) 0xff, (byte) 0xff, (byte) 0xff };
    final InputStream in = new ByteArrayInputStream(buf);
    final Mic1Instruction value = Mic1InstructionReader.read(in);

    assertThat(value).isNull();
  }

  @Test
  public void testRead_Null_TwoBytes() throws IOException {
    final byte[] buf = new byte[] { (byte) 0xff, (byte) 0xff };
    final InputStream in = new ByteArrayInputStream(buf);
    final Mic1Instruction value = Mic1InstructionReader.read(in);

    assertThat(value).isNull();
  }

  @Test
  public void testRead_Null_OneByte() throws IOException {
    final byte[] buf = new byte[] { (byte) 0xab };
    final InputStream in = new ByteArrayInputStream(buf);
    final Mic1Instruction value = Mic1InstructionReader.read(in);

    assertThat(value).isNull();
  }

  @Test
  public void testRead_Null_ZeroBytes() throws IOException {
    final byte[] buf = new byte[] {};
    final InputStream in = new ByteArrayInputStream(buf);
    final Mic1Instruction value = Mic1InstructionReader.read(in);

    assertThat(value).isNull();
  }

  @Test
  public void testRead_NotNull_AndThenNull() throws IOException {
    // have eleven bytes, so can read two times
    byte[] buf = new byte[] { (byte) 0xff,
                             (byte) 0xff,
                             (byte) 0xff,
                             (byte) 0xff,
                             (byte) 0xf0,
                             (byte) 0xf0,
                             (byte) 0xf0,
                             (byte) 0xf0,
                             (byte) 0xf0,
                             (byte) 0xf0,
                             (byte) 0xf0 };
    InputStream in = new ByteArrayInputStream(buf);

    Mic1Instruction value = Mic1InstructionReader.read(in);
    assertThat(value).isNotNull();

    value = Mic1InstructionReader.read(in);
    assertThat(value).isNotNull();

    value = Mic1InstructionReader.read(in);
    assertThat(value).isNull();

    // have ten bytes, so can read two times
    buf = new byte[] { (byte) 0xff,
                      (byte) 0xff,
                      (byte) 0xff,
                      (byte) 0xff,
                      (byte) 0xf0,
                      (byte) 0xf0,
                      (byte) 0xf0,
                      (byte) 0xf0,
                      (byte) 0xf0,
                      (byte) 0xf0 };
    in = new ByteArrayInputStream(buf);

    value = Mic1InstructionReader.read(in);
    assertThat(value).isNotNull();

    value = Mic1InstructionReader.read(in);
    assertThat(value).isNotNull();

    value = Mic1InstructionReader.read(in);
    assertThat(value).isNull();

    // have nine bytes, so can read one time
    buf = new byte[] { (byte) 0xff,
                      (byte) 0xff,
                      (byte) 0xff,
                      (byte) 0xff,
                      (byte) 0xf0,
                      (byte) 0xf0,
                      (byte) 0xf0,
                      (byte) 0xf0,
                      (byte) 0xf0 };
    in = new ByteArrayInputStream(buf);

    value = Mic1InstructionReader.read(in);
    assertThat(value).isNotNull();

    value = Mic1InstructionReader.read(in);
    assertThat(value).isNull();

    // have eight bytes, so can read one time
    buf = new byte[] { (byte) 0xff,
                      (byte) 0xff,
                      (byte) 0xff,
                      (byte) 0xff,
                      (byte) 0xf0,
                      (byte) 0xf0,
                      (byte) 0xf0,
                      (byte) 0xf0 };
    in = new ByteArrayInputStream(buf);

    value = Mic1InstructionReader.read(in);
    assertThat(value).isNotNull();

    value = Mic1InstructionReader.read(in);
    assertThat(value).isNull();

    // have seven bytes, so can read one time
    buf = new byte[] { (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xf0, (byte) 0xf0, (byte) 0xf0 };
    in = new ByteArrayInputStream(buf);

    value = Mic1InstructionReader.read(in);
    assertThat(value).isNotNull();

    value = Mic1InstructionReader.read(in);
    assertThat(value).isNull();

    value = Mic1InstructionReader.read(in);
    assertThat(value).isNull();

    value = Mic1InstructionReader.read(in);
    assertThat(value).isNull();
  }
}
