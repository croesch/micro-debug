package com.github.croesch.mic1.controlstore;

import static org.fest.assertions.Assertions.assertThat;

import java.util.BitSet;

import org.junit.Before;
import org.junit.Test;

import com.github.croesch.TestUtil;

/**
 * Provides test cases for {@link Mic1Instruction}.
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
    this.instruction = new Mic1Instruction(0, new BitSet(), null);
    this.stringBuilder = new StringBuilder();
  }

  @Test
  public void testToString() {
    printMethodName();

    final BitSet bits = new BitSet();

    this.instruction = new Mic1Instruction(0, bits, Mic1BBusRegister.MDR);
    assertThat(this.instruction.toString()).isEqualTo("goto 0x0");

    bits.set(2);
    bits.set(4);
    bits.set(6);
    bits.set(8);
    bits.set(10);
    bits.set(12);
    bits.set(14);
    bits.set(16);
    this.instruction = new Mic1Instruction(47, bits, Mic1BBusRegister.LV);
    assertThat(this.instruction.toString()).isEqualTo("Z=OPC=CPP=SP=LV>>1;if (Z) goto 0x12F; else goto 0x2F");

    printEndOfMethod();
  }

  @Test
  public void testDecodeJMPAndAddress() {
    printMethodName();
    // reset the content of the string builder
    this.stringBuilder = new StringBuilder("[...]");
    final BitSet bits = new BitSet();

    bits.set(0, false); //JMPC
    bits.set(1, false); //JMPN
    bits.set(2, false); // JMPZ
    this.instruction = new Mic1Instruction(47, bits, Mic1BBusRegister.MDR);
    String start = this.stringBuilder.toString();
    // call decoding
    this.instruction.decodeJMPAndAddress(this.stringBuilder);
    // check the created text
    assertThat(this.stringBuilder.toString()).isEqualTo(start + ";goto 0x2F");

    bits.set(0, true); //JMPC
    bits.set(1, false); //JMPN
    bits.set(2, false); // JMPZ
    this.instruction = new Mic1Instruction(47, bits, Mic1BBusRegister.MDR);
    start = this.stringBuilder.toString();
    // call decoding
    this.instruction.decodeJMPAndAddress(this.stringBuilder);
    // check the created text
    assertThat(this.stringBuilder.toString()).isEqualTo(start + ";goto (MBR OR 0x2F)");

    bits.set(0, true); //JMPC
    bits.set(1, false); //JMPN
    bits.set(2, false); // JMPZ
    this.instruction = new Mic1Instruction(0, bits, Mic1BBusRegister.MDR);
    start = this.stringBuilder.toString();
    // call decoding
    this.instruction.decodeJMPAndAddress(this.stringBuilder);
    // check the created text
    assertThat(this.stringBuilder.toString()).isEqualTo(start + ";goto (MBR)");

    bits.set(0, false); //JMPC
    bits.set(1, true); //JMPN
    bits.set(2, false); // JMPZ
    this.instruction = new Mic1Instruction(47, bits, Mic1BBusRegister.MDR);
    start = this.stringBuilder.toString();
    // call decoding
    this.instruction.decodeJMPAndAddress(this.stringBuilder);
    // check the created text
    assertThat(this.stringBuilder.toString()).isEqualTo("N=" + start + ";if (N) goto 0x12F; else goto 0x2F");

    bits.set(0, false); //JMPC
    bits.set(1, false); //JMPN
    bits.set(2, true); // JMPZ
    this.instruction = new Mic1Instruction(47, bits, Mic1BBusRegister.MDR);
    start = this.stringBuilder.toString();
    // call decoding
    this.instruction.decodeJMPAndAddress(this.stringBuilder);
    // check the created text
    assertThat(this.stringBuilder.toString()).isEqualTo("Z=" + start + ";if (Z) goto 0x12F; else goto 0x2F");

    bits.set(0, false); //JMPC
    bits.set(1, true); //JMPN
    bits.set(2, false); // JMPZ
    this.instruction = new Mic1Instruction(447, bits, Mic1BBusRegister.MDR);
    start = this.stringBuilder.toString();
    // call decoding
    this.instruction.decodeJMPAndAddress(this.stringBuilder);
    // check the created text
    assertThat(this.stringBuilder.toString()).isEqualTo("N=" + start + ";if (N) goto 0x1BF; else goto 0x1BF");

    bits.set(0, false); //JMPC
    bits.set(1, false); //JMPN
    bits.set(2, true); // JMPZ
    this.instruction = new Mic1Instruction(447, bits, Mic1BBusRegister.MDR);
    start = this.stringBuilder.toString();
    // call decoding
    this.instruction.decodeJMPAndAddress(this.stringBuilder);
    // check the created text
    assertThat(this.stringBuilder.toString()).isEqualTo("Z=" + start + ";if (Z) goto 0x1BF; else goto 0x1BF");

    printEndOfMethod();
  }

  @Test
  public void testConvertIntToHex() {
    printMethodName();

    int i = -42;
    assertThat(Mic1Instruction.convertIntToHex(i)).isEqualTo("FFFFFFD6");

    i = 42;
    assertThat(Mic1Instruction.convertIntToHex(i)).isEqualTo("2A");

    i = 4711;
    assertThat(Mic1Instruction.convertIntToHex(i)).isEqualTo("1267");

    i = -4711;
    assertThat(Mic1Instruction.convertIntToHex(i)).isEqualTo("FFFFED99");

    i = 0;
    assertThat(Mic1Instruction.convertIntToHex(i)).isEqualTo("0");

    i = -1;
    assertThat(Mic1Instruction.convertIntToHex(i)).isEqualTo("FFFFFFFF");

    i = 1;
    assertThat(Mic1Instruction.convertIntToHex(i)).isEqualTo("1");

    i = Integer.MAX_VALUE;
    assertThat(Mic1Instruction.convertIntToHex(i)).isEqualTo("7FFFFFFF");

    i = Integer.MIN_VALUE;
    assertThat(Mic1Instruction.convertIntToHex(i)).isEqualTo("80000000");

    printEndOfMethod();
  }

  @Test
  public void testDecodeMemoryBits() {
    printMethodName();
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
          this.instruction = new Mic1Instruction(0, bits, Mic1BBusRegister.MDR);

          // ensure the method appends generated text
          final String start = this.stringBuilder.toString();
          // call decoding
          this.instruction.decodeMemoryBits(this.stringBuilder);
          // check the created text
          assertThat(this.stringBuilder.toString()).startsWith(start);
          assertThat(this.stringBuilder.toString().contains(";wr")).isEqualTo(write);
          assertThat(this.stringBuilder.toString().contains(";rd")).isEqualTo(read);
          assertThat(this.stringBuilder.toString().contains(";fetch")).isEqualTo(fetch);
          printStep();
        }
        printLoopEnd();
      }
      printLoopEnd();
    }
    printEndOfMethod();
  }

  @Test
  public void testDecodeShifterOperation() {
    printMethodName();
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
        this.instruction = new Mic1Instruction(0, bits, Mic1BBusRegister.MDR);

        final String start = this.stringBuilder.toString();
        // call decoding
        this.instruction.decodeShifterOperation(this.stringBuilder);
        // check the created text
        assertThat(this.stringBuilder.toString()).startsWith(start);
        assertThat(this.stringBuilder.toString().contains(">>1")).isEqualTo(sra1);
        assertThat(this.stringBuilder.toString().contains("<<8")).isEqualTo(sll8);
        printStep();
      }
      printLoopEnd();
    }
    printEndOfMethod();
  }

  @Test
  public void testDecodeALUOperation() {
    printMethodName();
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
    this.instruction = new Mic1Instruction(0, bits, Mic1BBusRegister.MDR);
    String start = this.stringBuilder.toString();
    // call decoding
    this.instruction.decodeALUOperation(this.stringBuilder, "A", "B");
    // check the created text
    assertThat(this.stringBuilder.toString()).isEqualTo(start + "A+B");

    bits.set(5, false);
    bits.set(6);
    this.instruction = new Mic1Instruction(0, bits, Mic1BBusRegister.MDR);
    start = this.stringBuilder.toString();
    // call decoding
    this.instruction.decodeALUOperation(this.stringBuilder, "A", "B");
    // check the created text
    assertThat(this.stringBuilder.toString()).isEqualTo(start + "A OR B");

    bits.set(5);
    bits.set(6, false);
    this.instruction = new Mic1Instruction(0, bits, Mic1BBusRegister.MDR);
    start = this.stringBuilder.toString();
    // call decoding
    this.instruction.decodeALUOperation(this.stringBuilder, "A", "B");
    // check the created text
    assertThat(this.stringBuilder.toString()).isEqualTo(start + "NOT B");

    bits.set(5, false);
    bits.set(6, false);
    this.instruction = new Mic1Instruction(0, bits, Mic1BBusRegister.MDR);
    start = this.stringBuilder.toString();
    // call decoding
    this.instruction.decodeALUOperation(this.stringBuilder, "A", "B");
    // check the created text
    assertThat(this.stringBuilder.toString()).isEqualTo(start + "A AND B");

    printEndOfMethod();
  }

  @Test
  public void testDecodeALUPlus() {
    printMethodName();
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
            this.instruction = new Mic1Instruction(0, bits, Mic1BBusRegister.MDR);

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
            printStep();
          }
          printLoopEnd();
        }
        printLoopEnd();
      }
      printLoopEnd();
    }
    printEndOfMethod();
  }

  @Test
  public void testDecodeALUNotB() {
    printMethodName();
    final BitSet bits = new BitSet();

    // test ENB and !ENB
    for (final boolean enableB : BOOLEAN_POSSIBILITIES) {
      // reset the content of the string builder
      this.stringBuilder = new StringBuilder("[...]");
      // set the bits in the bit set
      bits.clear();
      bits.set(8, enableB);
      // create Mic1Instruction
      this.instruction = new Mic1Instruction(0, bits, Mic1BBusRegister.MDR);

      final String start = this.stringBuilder.toString();
      // call decoding
      this.instruction.decodeALUNotB(this.stringBuilder, "B");
      // check the created text
      if (enableB) {
        assertThat(this.stringBuilder.toString()).isEqualTo(start + "NOT B");
      } else {
        assertThat(this.stringBuilder.toString()).isEqualTo(start + "0");
      }
      printStep();
    }
    printEndOfMethod();
  }

  @Test
  public void testDecodeALUOr() {
    printMethodName();
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
          this.instruction = new Mic1Instruction(0, bits, Mic1BBusRegister.MDR);

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
          printStep();
        }
        printLoopEnd();
      }
      printLoopEnd();
    }
    printEndOfMethod();
  }

  @Test
  public void testDecodeALUAnd() {
    printMethodName();
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
          this.instruction = new Mic1Instruction(0, bits, Mic1BBusRegister.MDR);

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
          printStep();
        }
        printLoopEnd();
      }
      printLoopEnd();
    }
    printEndOfMethod();
  }

  @Test
  public void testDecodeBBusBits() {
    printMethodName();
    checkDecodingOfBBusBits("MDR", Mic1BBusRegister.MDR);
    checkDecodingOfBBusBits("PC", Mic1BBusRegister.PC);
    checkDecodingOfBBusBits("MBR", Mic1BBusRegister.MBR);
    checkDecodingOfBBusBits("MBRU", Mic1BBusRegister.MBRU);
    checkDecodingOfBBusBits("SP", Mic1BBusRegister.SP);
    checkDecodingOfBBusBits("LV", Mic1BBusRegister.LV);
    checkDecodingOfBBusBits("CPP", Mic1BBusRegister.CPP);
    checkDecodingOfBBusBits("TOS", Mic1BBusRegister.TOS);
    checkDecodingOfBBusBits("OPC", Mic1BBusRegister.OPC);

    for (int i = 9; i < 16; ++i) {
      checkDecodingOfBBusBits("???", null);
      printStep();
    }
    printEndOfMethod();
  }

  private void checkDecodingOfBBusBits(final String s, final Mic1BBusRegister i) {
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
    printMethodName();
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
                      this.instruction = new Mic1Instruction(0, bits, Mic1BBusRegister.MDR);

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
                      printStep();
                    }
                    printLoopEnd();
                  }
                  printLoopEnd();
                }
                printLoopEnd();
              }
              printLoopEnd();
            }
            printLoopEnd();
          }
          printLoopEnd();
        }
        printLoopEnd();
      }
      printLoopEnd();
    }
    printEndOfMethod();
  }

  @Test
  public void testHashCodeAndEqualsObject() {
    printMethodName();

    assertThat(this.instruction).isNotEqualTo(null);
    assertThat(this.instruction).isNotEqualTo("...");
    assertThat(this.instruction).isEqualTo(this.instruction);
    assertThat(this.instruction.hashCode()).isEqualTo(this.instruction.hashCode());

    int addr = 0;
    Mic1BBusRegister b = null;
    final BitSet bs = new BitSet();

    Mic1Instruction other = new Mic1Instruction(addr, bs, b);
    assertThat(this.instruction).isEqualTo(other);
    assertThat(this.instruction.hashCode()).isEqualTo(this.instruction.hashCode());

    b = Mic1BBusRegister.OPC;
    this.instruction = new Mic1Instruction(addr, bs, b);
    assertThat(this.instruction).isNotEqualTo(other);
    assertThat(this.instruction.hashCode()).isNotEqualTo(other.hashCode());
    other = new Mic1Instruction(addr, bs, b); // make object equal to instruction

    addr = 17;
    this.instruction = new Mic1Instruction(addr, bs, b);
    assertThat(this.instruction).isNotEqualTo(other);
    assertThat(this.instruction.hashCode()).isNotEqualTo(other.hashCode());
    other = new Mic1Instruction(addr, bs, b); // make object equal to instruction

    for (int i = 0; i < 23; ++i) {
      bs.set(i);
      this.instruction = new Mic1Instruction(addr, bs, b);
      assertThat(this.instruction).isNotEqualTo(other);
      assertThat(this.instruction.hashCode()).isNotEqualTo(other.hashCode());
      other = new Mic1Instruction(addr, bs, b); // make object equal to instruction
      printStep();
    }

    printEndOfMethod();
  }

  private void printEndOfMethod() {
    System.out.println();
  }

  private void printLoopEnd() {
    System.out.print(" ");
  }

  private void printStep() {
    System.out.print(".");
  }

  private void printMethodName() {
    TestUtil.printMethodName(1);
  }
}
