package com.github.croesch.mic1.mem;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

import com.github.croesch.TestUtil;
import com.github.croesch.mic1.register.Register;

/**
 * Provides test cases for {@link Memory}
 * 
 * @author croesch
 * @since Date: Nov 23, 2011
 */
public class MemoryTest {

  private Memory mem;

  private final byte[] bytes = new byte[Byte.MAX_VALUE];

  @Before
  public void setUp() {
    this.mem = new Memory(Byte.MAX_VALUE);
    for (byte b = 0; b < Byte.MAX_VALUE; ++b) {
      this.bytes[b] = b;
    }

    this.mem.setFetch(false);
    this.mem.setRead(false);
    this.mem.setWrite(true);
    for (byte b = 0; b < Byte.MAX_VALUE - 3; b += 4) {
      int val = this.bytes[b] << 8;
      val |= this.bytes[b + 1];
      val <<= 8;
      val |= this.bytes[b + 2];
      val <<= 8;
      val |= this.bytes[b + 3];
      this.mem.setWordAddress(b / 4);
      this.mem.setWordValue(val);
      this.mem.poke();
    }
    this.mem.setWrite(false);
  }

  @Test
  public void testWriteRead() {
    TestUtil.printMethodName();

    this.mem.setFetch(false);
    this.mem.setRead(true);
    this.mem.setWrite(false);
    final int old = Register.LV.getValue();
    for (byte b = 0; b < Byte.MAX_VALUE - 3; b += 4) {
      this.mem.setWordAddress(b / 4);
      this.mem.poke();
      this.mem.fillRegisters(Register.H, Register.LV);
      assertThat(Register.LV.getValue()).isEqualTo(old);
      int val = this.bytes[b] << 8;
      val |= this.bytes[b + 1];
      val <<= 8;
      val |= this.bytes[b + 2];
      val <<= 8;
      val |= this.bytes[b + 3];
      assertThat(Register.H.getValue()).isEqualTo(val);

      TestUtil.printStep();
    }
    TestUtil.printEndOfMethod();
  }

  @Test
  public void testWriteFetch() {
    TestUtil.printMethodName();

    this.mem.setFetch(true);
    this.mem.setRead(false);
    this.mem.setWrite(false);
    final int old = Register.H.getValue();
    for (byte b = 0; b < Byte.MAX_VALUE - 3; ++b) {
      this.mem.setByteAddress(b);
      this.mem.poke();
      this.mem.fillRegisters(Register.H, Register.LV);
      assertThat(Register.H.getValue()).isEqualTo(old);
      assertThat(Register.LV.getValue()).isEqualTo(this.bytes[b]);

      TestUtil.printStep();
    }

    TestUtil.printEndOfMethod();
  }

  @Test(expected = ArrayIndexOutOfBoundsException.class)
  public void testPoke_WriteToNegative() {
    this.mem.setWrite(true);
    this.mem.setWordAddress(-1);
    this.mem.poke();
  }

  @Test(expected = ArrayIndexOutOfBoundsException.class)
  public void testPoke_ReadFromNegative() {
    this.mem.setRead(true);
    this.mem.setWordAddress(-1);
    this.mem.poke();
  }

  @Test(expected = ArrayIndexOutOfBoundsException.class)
  public void testPoke_FetchFromNegative() {
    this.mem.setFetch(true);
    this.mem.setByteAddress(-4);
    this.mem.poke();
  }

  @Test(expected = ArrayIndexOutOfBoundsException.class)
  public void testPoke_WriteToOOM() {
    this.mem = new Memory(1);
    this.mem.setWrite(true);
    this.mem.setWordAddress(1);
    this.mem.poke();
  }

  @Test(expected = ArrayIndexOutOfBoundsException.class)
  public void testPoke_ReadFromOOM() {
    this.mem = new Memory(1);
    this.mem.setRead(true);
    this.mem.setWordAddress(1);
    this.mem.poke();
  }

  @Test(expected = ArrayIndexOutOfBoundsException.class)
  public void testPoke_FetchFromOOM() {
    this.mem = new Memory(1);
    this.mem.setFetch(true);
    this.mem.setByteAddress(4);
    this.mem.poke();
  }

  @Test
  public void testReadNegativeValue() {
    this.mem.setWrite(true);
    this.mem.setWordAddress(1);
    this.mem.setWordValue(0xFFFFFFFF);
    this.mem.poke();
    this.mem.setWrite(false);

    this.mem.setRead(true);
    this.mem.poke();
    this.mem.fillRegisters(Register.H, null);
    assertThat(Register.H.getValue()).isEqualTo(0xFFFFFFFF);
  }

  @Test
  public void testFetchNegativeValue() {
    this.mem.setWrite(true);
    this.mem.setWordAddress(1);
    this.mem.setWordValue(0xFFFFFFFF);
    this.mem.poke();
    this.mem.setWrite(false);

    this.mem.setFetch(true);
    this.mem.setByteAddress(5);
    this.mem.poke();
    this.mem.fillRegisters(null, Register.H);
    assertThat(Register.H.getValue()).isEqualTo(0xFF);
  }

  @Test
  public void testReadWriteAndFetch() {
    this.mem.setWrite(true);
    this.mem.setRead(true);
    this.mem.setFetch(true);
    this.mem.setWordAddress(1);
    this.mem.setByteAddress(6);
    this.mem.setWordValue(0x12345678);
    this.mem.poke();
    this.mem.fillRegisters(Register.TOS, Register.H);
    assertThat(Register.TOS.getValue()).isEqualTo(0x12345678);
    assertThat(Register.H.getValue()).isEqualTo(0x56);
  }

  @Test
  public void testDoNothing() {
    this.mem.setWordAddress(1);
    this.mem.setByteAddress(6);
    this.mem.setWordValue(0x12345678);
    this.mem.poke();
    int old = Register.H.getValue();
    this.mem.fillRegisters(Register.H, null);
    assertThat(Register.H.getValue()).isEqualTo(old);

    old = Register.H.getValue();
    this.mem.fillRegisters(null, Register.H);
    assertThat(Register.H.getValue()).isEqualTo(old);
  }
}
