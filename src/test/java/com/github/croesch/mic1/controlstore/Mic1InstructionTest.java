package com.github.croesch.mic1.controlstore;

import static org.fest.assertions.Assertions.assertThat;

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
    System.err.println("not yet implemented");
  }

  @Test
  public void testToString() {
    System.err.println("not yet implemented");
  }

  @Test
  public void testDecodeJMPAndAddress() {
    System.err.println("not yet implemented");
  }

  @Test
  public void testConvertIntToHex() {
    System.err.println("not yet implemented");
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
    // reset the content of the string builder
    this.stringBuilder = new StringBuilder("[...]");

    final BitSet bits = new BitSet();
    bits.set(1);
    bits.set(3);
    bits.set(7); // enable a
    bits.set(8); // enable b
    bits.set(11);
    bits.set(13);
    bits.set(15);
    bits.set(17);

    bits.set(5);
    bits.set(6);
    this.instruction = new Mic1Instruction(0, bits, 0);
    String start = this.stringBuilder.toString();
    // call decoding
    this.instruction.decodeALUOperation(this.stringBuilder, "A", "B");
    // check the created text
    assertThat(this.stringBuilder.toString()).isEqualTo(start + "A+B");

    bits.set(5, false);
    bits.set(6);
    this.instruction = new Mic1Instruction(0, bits, 0);
    start = this.stringBuilder.toString();
    // call decoding
    this.instruction.decodeALUOperation(this.stringBuilder, "A", "B");
    // check the created text
    assertThat(this.stringBuilder.toString()).isEqualTo(start + "A OR B");

    bits.set(5);
    bits.set(6, false);
    this.instruction = new Mic1Instruction(0, bits, 0);
    start = this.stringBuilder.toString();
    // call decoding
    this.instruction.decodeALUOperation(this.stringBuilder, "A", "B");
    // check the created text
    assertThat(this.stringBuilder.toString()).isEqualTo(start + "NOT B");

    bits.set(5, false);
    bits.set(6, false);
    this.instruction = new Mic1Instruction(0, bits, 0);
    start = this.stringBuilder.toString();
    // call decoding
    this.instruction.decodeALUOperation(this.stringBuilder, "A", "B");
    // check the created text
    assertThat(this.stringBuilder.toString()).isEqualTo(start + "A AND B");
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
    final BitSet bits = new BitSet();

    for (final boolean enableA : BOOLEAN_POSSIBILITIES) {
      for (final boolean enableB : BOOLEAN_POSSIBILITIES) {
        for (final boolean invertA : BOOLEAN_POSSIBILITIES) {
          // reset the content of the string builder
          this.stringBuilder = new StringBuilder("[...]");
          // set the bits in the bit set
          bits.clear();
          bits.set(7, enableA);
          bits.set(8, enableB);
          bits.set(9, invertA);
          // create Mic1Instruction
          this.instruction = new Mic1Instruction(0, bits, 0);

          final String start = this.stringBuilder.toString();
          // call decoding
          this.instruction.decodeALUOr(this.stringBuilder, "A", "B");

          // check the created text
          if (enableA && enableB && invertA) {
            assertThat(this.stringBuilder.toString()).isEqualTo(start + "(NOT A) OR B");
          } else if (enableA && enableB && !invertA) {
            assertThat(this.stringBuilder.toString()).isEqualTo(start + "A OR B");
          } else if (enableA && !enableB && invertA) {
            assertThat(this.stringBuilder.toString()).isEqualTo(start + "NOT A");
          } else if (enableA && !enableB && !invertA) {
            assertThat(this.stringBuilder.toString()).isEqualTo(start + "A");
          } else if (!enableA && enableB && invertA) {
            assertThat(this.stringBuilder.toString()).isEqualTo(start + "-1");
          } else if (!enableA && enableB && !invertA) {
            assertThat(this.stringBuilder.toString()).isEqualTo(start + "B");
          } else if (!enableA && !enableB && invertA) {
            assertThat(this.stringBuilder.toString()).isEqualTo(start + "-1");
          } else if (!enableA && !enableB && !invertA) {
            assertThat(this.stringBuilder.toString()).isEqualTo(start + "0");
          }
        }
      }
    }
  }

  @Test
  public void testDecodeALUAnd() {
    final BitSet bits = new BitSet();

    for (final boolean enableA : BOOLEAN_POSSIBILITIES) {
      for (final boolean enableB : BOOLEAN_POSSIBILITIES) {
        for (final boolean invertA : BOOLEAN_POSSIBILITIES) {
          // reset the content of the string builder
          this.stringBuilder = new StringBuilder("[...]");
          // set the bits in the bit set
          bits.clear();
          bits.set(7, enableA);
          bits.set(8, enableB);
          bits.set(9, invertA);
          // create Mic1Instruction
          this.instruction = new Mic1Instruction(0, bits, 0);

          final String start = this.stringBuilder.toString();
          // call decoding
          this.instruction.decodeALUAnd(this.stringBuilder, "A", "B");

          // check the created text
          if (enableA && enableB && invertA) {
            assertThat(this.stringBuilder.toString()).isEqualTo(start + "(NOT A) AND B");
          } else if (enableA && enableB && !invertA) {
            assertThat(this.stringBuilder.toString()).isEqualTo(start + "A AND B");
          } else if (enableA && !enableB && invertA) {
            assertThat(this.stringBuilder.toString()).isEqualTo(start + "0");
          } else if (enableA && !enableB && !invertA) {
            assertThat(this.stringBuilder.toString()).isEqualTo(start + "0");
          } else if (!enableA && enableB && invertA) {
            assertThat(this.stringBuilder.toString()).isEqualTo(start + "B");
          } else if (!enableA && enableB && !invertA) {
            assertThat(this.stringBuilder.toString()).isEqualTo(start + "0");
          } else if (!enableA && !enableB && invertA) {
            assertThat(this.stringBuilder.toString()).isEqualTo(start + "0");
          } else if (!enableA && !enableB && !invertA) {
            assertThat(this.stringBuilder.toString()).isEqualTo(start + "0");
          }
        }
      }
    }
  }

  @Test
  public void testDecodeBBusBits() {
    checkDecodingOfBBusBits("MDR", 0);
    checkDecodingOfBBusBits("PC", 1);
    checkDecodingOfBBusBits("MBR", 2);
    checkDecodingOfBBusBits("MBRU", 3);
    checkDecodingOfBBusBits("SP", 4);
    checkDecodingOfBBusBits("LV", 5);
    checkDecodingOfBBusBits("CPP", 6);
    checkDecodingOfBBusBits("TOS", 7);
    checkDecodingOfBBusBits("OPC", 8);

    for (int i = 9; i < 16; ++i) {
      checkDecodingOfBBusBits("???", i);
    }
  }

  private void checkDecodingOfBBusBits(final String s, final int i) {
    this.instruction = new Mic1Instruction(0, new BitSet(), i);
    // check it multiple times
    assertThat(this.instruction.decodeBBusBits()).isEqualTo(s);
    assertThat(this.instruction.decodeBBusBits()).isEqualTo(s);
    assertThat(this.instruction.decodeBBusBits()).isEqualTo(s);
    assertThat(this.instruction.decodeBBusBits()).isEqualTo(s);
    assertThat(this.instruction.decodeBBusBits()).isEqualTo(s);
  }

  @Test
  public void testDecodeCBusBits() {
    final BitSet bits = new BitSet();

    for (final boolean h : BOOLEAN_POSSIBILITIES) {
      for (final boolean opc : BOOLEAN_POSSIBILITIES) {
        for (final boolean tos : BOOLEAN_POSSIBILITIES) {
          for (final boolean cpp : BOOLEAN_POSSIBILITIES) {
            for (final boolean lv : BOOLEAN_POSSIBILITIES) {
              for (final boolean mar : BOOLEAN_POSSIBILITIES) {
                for (final boolean mdr : BOOLEAN_POSSIBILITIES) {
                  for (final boolean pc : BOOLEAN_POSSIBILITIES) {
                    for (final boolean sp : BOOLEAN_POSSIBILITIES) {
                      // reset the content of the string builder
                      this.stringBuilder = new StringBuilder("[...]");
                      // set the bits in the bit set
                      bits.clear();
                      bits.set(11, h);
                      bits.set(12, opc);
                      bits.set(13, tos);
                      bits.set(14, cpp);
                      bits.set(15, lv);
                      bits.set(16, sp);
                      bits.set(17, pc);
                      bits.set(18, mdr);
                      bits.set(19, mar);
                      // create Mic1Instruction
                      this.instruction = new Mic1Instruction(0, bits, 0);

                      final String start = this.stringBuilder.toString();
                      // call decoding
                      this.instruction.decodeCBusBits(this.stringBuilder);
                      // check the created text
                      assertThat(this.stringBuilder.toString()).startsWith(start);

                      assertThat(this.stringBuilder.toString().contains("H=")).isEqualTo(h);
                      assertThat(this.stringBuilder.toString().contains("CPP=")).isEqualTo(cpp);
                      assertThat(this.stringBuilder.toString().contains("LV=")).isEqualTo(lv);
                      assertThat(this.stringBuilder.toString().contains("MAR=")).isEqualTo(mar);
                      assertThat(this.stringBuilder.toString().contains("MDR=")).isEqualTo(mdr);
                      assertThat(this.stringBuilder.toString().contains("OPC=")).isEqualTo(opc);
                      // OPC= also contains PC= ..
                      assertThat(
                                 this.stringBuilder.toString().contains("]PC=")
                                         || this.stringBuilder.toString().contains("=PC=")).isEqualTo(pc);
                      assertThat(this.stringBuilder.toString().contains("SP=")).isEqualTo(sp);
                      assertThat(this.stringBuilder.toString().contains("TOS=")).isEqualTo(tos);
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
  }

  @Test
  public void testEqualsObject() {
    System.err.println("not yet implemented");
  }

}
