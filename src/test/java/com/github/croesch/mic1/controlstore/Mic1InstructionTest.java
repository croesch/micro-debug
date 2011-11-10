package com.github.croesch.mic1.controlstore;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.fail;

import java.util.BitSet;

import org.junit.Before;
import org.junit.Test;

/**
 * TODO Comment here ...
 * 
 * @author croesch
 * @since Date: Nov 10, 2011
 */
public class Mic1InstructionTest {

  private Mic1Instruction instruction;

  private StringBuilder stringBuilder;

  private static final boolean[] BOOLEAN_POSSIBILITIES = new boolean[] { true, false };

  @Before
  public void setUp() {
    this.instruction = new Mic1Instruction(0, new BitSet(), 0);
    this.stringBuilder = new StringBuilder();
  }

  @Test
  public void testHashCode() {
    fail("Not yet implemented");
  }

  @Test
  public void testToString() {
    fail("Not yet implemented");
  }

  @Test
  public void testDecodeJMPAndAddress() {
    fail("Not yet implemented");
  }

  @Test
  public void testConvertIntToHex() {
    fail("Not yet implemented");
  }

  @Test
  public void testDecodeMemoryBits() {
    final BitSet bits = new BitSet();

    // try all combinations of write, read and fetch
    for (final boolean write : BOOLEAN_POSSIBILITIES) {
      for (final boolean read : BOOLEAN_POSSIBILITIES) {
        for (final boolean fetch : BOOLEAN_POSSIBILITIES) {
          // reset the content of the string builder
          this.stringBuilder = new StringBuilder("[...]");
          // set the bits in the bit set
          bits.clear();
          bits.set(20, write);
          bits.set(21, read);
          bits.set(22, fetch);
          // create Mic1Instruction
          this.instruction = new Mic1Instruction(0, bits, 0);

          // ensure the method appends generated text
          final String start = this.stringBuilder.toString();
          // call decoding
          this.instruction.decodeMemoryBits(this.stringBuilder);
          // check the created text
          assertThat(this.stringBuilder.toString()).startsWith(start);
          assertThat(this.stringBuilder.toString().contains(";wr")).isEqualTo(write);
          assertThat(this.stringBuilder.toString().contains(";rd")).isEqualTo(read);
          assertThat(this.stringBuilder.toString().contains(";fetch")).isEqualTo(fetch);
        }
      }
    }
  }

  @Test
  public void testDecodeShifterOperation() {
    final BitSet bits = new BitSet();

    // try all combinations of sra1 and sll8
    for (final boolean sll8 : BOOLEAN_POSSIBILITIES) {
      for (final boolean sra1 : BOOLEAN_POSSIBILITIES) {
        // reset the content of the string builder
        this.stringBuilder = new StringBuilder("[...]");
        // set the bits in the bit set
        bits.clear();
        bits.set(3, sll8);
        bits.set(4, sra1);
        // create Mic1Instruction
        this.instruction = new Mic1Instruction(0, bits, 0);

        final String start = this.stringBuilder.toString();
        // call decoding
        this.instruction.decodeShifterOperation(this.stringBuilder);
        // check the created text
        assertThat(this.stringBuilder.toString()).startsWith(start);
        assertThat(this.stringBuilder.toString().contains(">>1")).isEqualTo(sra1);
        assertThat(this.stringBuilder.toString().contains("<<8")).isEqualTo(sll8);
      }
    }
  }

  @Test
  public void testDecodeALUOperation() {
    fail("Not yet implemented");
  }

  @Test
  public void testDecodeALUPlus() {
    final BitSet bits = new BitSet();

    for (final boolean enableA : BOOLEAN_POSSIBILITIES) {
      for (final boolean enableB : BOOLEAN_POSSIBILITIES) {
        for (final boolean invertA : BOOLEAN_POSSIBILITIES) {
          for (final boolean increment : BOOLEAN_POSSIBILITIES) {
            // reset the content of the string builder
            this.stringBuilder = new StringBuilder("[...]");
            // set the bits in the bit set
            bits.clear();
            bits.set(7, enableA);
            bits.set(8, enableB);
            bits.set(9, invertA);
            bits.set(10, increment);
            // create Mic1Instruction
            this.instruction = new Mic1Instruction(0, bits, 0);

            final String start = this.stringBuilder.toString();
            // call decoding
            this.instruction.decodeALUPlus(this.stringBuilder, "A", "B");

            // check the created text
            if (enableA && enableB && invertA && increment) {
              assertThat(this.stringBuilder.toString()).isEqualTo(start + "B-A");
            } else if (enableA && enableB && invertA && !increment) {
              assertThat(this.stringBuilder.toString()).isEqualTo(start + "B-A-1");
            } else if (enableA && enableB && !invertA && increment) {
              assertThat(this.stringBuilder.toString()).isEqualTo(start + "A+B+1");
            } else if (enableA && enableB && !invertA && !increment) {
              assertThat(this.stringBuilder.toString()).isEqualTo(start + "A+B");
            } else if (enableA && !enableB && invertA && increment) {
              assertThat(this.stringBuilder.toString()).isEqualTo(start + "-A");
            } else if (enableA && !enableB && invertA && !increment) {
              assertThat(this.stringBuilder.toString()).isEqualTo(start + "-A-1");
            } else if (enableA && !enableB && !invertA && increment) {
              assertThat(this.stringBuilder.toString()).isEqualTo(start + "A+1");
            } else if (enableA && !enableB && !invertA && !increment) {
              assertThat(this.stringBuilder.toString()).isEqualTo(start + "A");
            } else if (!enableA && enableB && invertA && increment) {
              assertThat(this.stringBuilder.toString()).isEqualTo(start + "B");
            } else if (!enableA && enableB && invertA && !increment) {
              assertThat(this.stringBuilder.toString()).isEqualTo(start + "B-1");
            } else if (!enableA && enableB && !invertA && increment) {
              assertThat(this.stringBuilder.toString()).isEqualTo(start + "B+1");
            } else if (!enableA && enableB && !invertA && !increment) {
              assertThat(this.stringBuilder.toString()).isEqualTo(start + "B");
            } else if (!enableA && !enableB && invertA && increment) {
              assertThat(this.stringBuilder.toString()).isEqualTo(start + "0");
            } else if (!enableA && !enableB && invertA && !increment) {
              assertThat(this.stringBuilder.toString()).isEqualTo(start + "-1");
            } else if (!enableA && !enableB && !invertA && increment) {
              assertThat(this.stringBuilder.toString()).isEqualTo(start + "1");
            } else if (!enableA && !enableB && !invertA && !increment) {
              assertThat(this.stringBuilder.toString()).isEqualTo(start + "0");
            }
          }
        }
      }
    }
  }

  @Test
  public void testDecodeALUNotB() {
    final BitSet bits = new BitSet();

    // test ENB and !ENB
    for (final boolean enableB : BOOLEAN_POSSIBILITIES) {
      // reset the content of the string builder
      this.stringBuilder = new StringBuilder("[...]");
      // set the bits in the bit set
      bits.clear();
      bits.set(8, enableB);
      // create Mic1Instruction
      this.instruction = new Mic1Instruction(0, bits, 0);

      final String start = this.stringBuilder.toString();
      // call decoding
      this.instruction.decodeALUNotB(this.stringBuilder, "B");
      // check the created text
      if (enableB) {
        assertThat(this.stringBuilder.toString()).isEqualTo(start + "NOT B");
      } else {
        assertThat(this.stringBuilder.toString()).isEqualTo(start + "0");
      }
    }
  }

  @Test
  public void testDecodeALUOr() {
    fail("Not yet implemented");
  }

  @Test
  public void testDecodeALUAnd() {
    fail("Not yet implemented");
  }

  @Test
  public void testDecodeBBusBits() {
    fail("Not yet implemented");
  }

  @Test
  public void testDecodeCBusBits() {
    fail("Not yet implemented");
  }

  @Test
  public void testEqualsObject() {
    fail("Not yet implemented");
  }

}
