package com.github.croesch.mic1.controlstore;

import static org.fest.assertions.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

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

    final Mic1JMPSignalSet jmpSet = new Mic1JMPSignalSet();
    final Mic1ALUSignalSet aluSet = new Mic1ALUSignalSet();
    final Mic1CBusSignalSet cBusSet = new Mic1CBusSignalSet();

    jmpSet.setJmpZ(true); // 2
    aluSet.setSLL8(true); // 3
    aluSet.setF1(true); // 6
    aluSet.setEnA(true); // 7
    cBusSet.setH(true); //11
    cBusSet.setOpc(true); //12
    cBusSet.setTos(true); //13
    cBusSet.setCpp(true); //14
    cBusSet.setLv(true); //15
    cBusSet.setSp(true); //16
    cBusSet.setPc(true); //17
    cBusSet.setMdr(true); //18
    cBusSet.setMar(true); //19

    final Mic1Instruction expected = new Mic1Instruction(0x1cf,
                                                         jmpSet,
                                                         aluSet,
                                                         cBusSet,
                                                         new Mic1MemorySignalSet(),
                                                         Mic1BBusRegister.OPC);

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

    final Mic1JMPSignalSet jmpSet = new Mic1JMPSignalSet();
    final Mic1ALUSignalSet aluSet = new Mic1ALUSignalSet();
    final Mic1CBusSignalSet cBusSet = new Mic1CBusSignalSet();
    final Mic1MemorySignalSet memSet = new Mic1MemorySignalSet();

    jmpSet.setJmpC(true); // 0
    jmpSet.setJmpN(true); // 1
    jmpSet.setJmpZ(true); // 2
    aluSet.setSLL8(true); // 3
    aluSet.setSRA1(true); // 4
    aluSet.setF0(true); // 5
    aluSet.setF1(true); // 6
    aluSet.setEnA(true); // 7
    aluSet.setEnB(true); // 8
    aluSet.setInvA(true); // 9
    aluSet.setInc(true); // 10
    cBusSet.setH(true); // 11
    cBusSet.setOpc(true); // 12
    cBusSet.setTos(true); // 13
    cBusSet.setCpp(true); // 14
    cBusSet.setLv(true); // 15
    cBusSet.setSp(true); // 16
    cBusSet.setPc(true); // 17
    cBusSet.setMdr(true); // 18
    cBusSet.setMar(true); // 19
    memSet.setWrite(true); // 20
    memSet.setRead(true); // 21
    memSet.setFetch(true); // 22

    final Mic1Instruction expected = new Mic1Instruction(0x1ff, jmpSet, aluSet, cBusSet, memSet, null);

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
