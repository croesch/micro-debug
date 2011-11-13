package com.github.croesch.mic1.controlstore;

import static org.fest.assertions.Assertions.assertThat;

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
    this.instruction = new Mic1Instruction(0,
                                           new Mic1JMPSignalSet(),
                                           new Mic1ALUSignalSet(),
                                           new Mic1CBusSignalSet(),
                                           new Mic1MemorySignalSet(),
                                           null);
    this.stringBuilder = new StringBuilder();
  }

  @Test
  public void testToString() {
    printMethodName();

    this.instruction = new Mic1Instruction(0,
                                           new Mic1JMPSignalSet(),
                                           new Mic1ALUSignalSet(),
                                           new Mic1CBusSignalSet(),
                                           new Mic1MemorySignalSet(),
                                           Mic1BBusRegister.MDR);
    assertThat(this.instruction.toString()).isEqualTo("goto 0x0");

    final Mic1JMPSignalSet jmpSet = new Mic1JMPSignalSet();
    final Mic1ALUSignalSet aluSet = new Mic1ALUSignalSet();
    final Mic1CBusSignalSet cBusSet = new Mic1CBusSignalSet();
    final Mic1MemorySignalSet memSet = new Mic1MemorySignalSet();
    jmpSet.setJmpZ(true);
    aluSet.setSRA1(true);
    aluSet.setF1(true);
    aluSet.setEnB(true);
    aluSet.setInc(true);
    cBusSet.setOpc(true);
    cBusSet.setCpp(true);
    cBusSet.setSp(true);
    this.instruction = new Mic1Instruction(47, jmpSet, aluSet, cBusSet, memSet, Mic1BBusRegister.LV);
    assertThat(this.instruction.toString()).isEqualTo("Z=OPC=CPP=SP=LV>>1;if (Z) goto 0x12F; else goto 0x2F");

    printEndOfMethod();
  }

  @Test
  public void testDecodeJMPAndAddress() {
    printMethodName();
    // reset the content of the string builder
    this.stringBuilder = new StringBuilder("[...]");

    final Mic1JMPSignalSet jmpSet = new Mic1JMPSignalSet();
    jmpSet.setJmpC(false);
    jmpSet.setJmpN(false);
    jmpSet.setJmpZ(false);
    this.instruction = new Mic1Instruction(47,
                                           jmpSet,
                                           new Mic1ALUSignalSet(),
                                           new Mic1CBusSignalSet(),
                                           new Mic1MemorySignalSet(),
                                           Mic1BBusRegister.MDR);
    String start = this.stringBuilder.toString();
    // call decoding
    this.instruction.decodeJMPAndAddress(this.stringBuilder);
    // check the created text
    assertThat(this.stringBuilder.toString()).isEqualTo(start + ";goto 0x2F");

    jmpSet.setJmpC(true);
    jmpSet.setJmpN(false);
    jmpSet.setJmpZ(false);
    this.instruction = new Mic1Instruction(47,
                                           jmpSet,
                                           new Mic1ALUSignalSet(),
                                           new Mic1CBusSignalSet(),
                                           new Mic1MemorySignalSet(),
                                           Mic1BBusRegister.MDR);
    start = this.stringBuilder.toString();
    // call decoding
    this.instruction.decodeJMPAndAddress(this.stringBuilder);
    // check the created text
    assertThat(this.stringBuilder.toString()).isEqualTo(start + ";goto (MBR OR 0x2F)");

    jmpSet.setJmpC(true);
    jmpSet.setJmpN(false);
    jmpSet.setJmpZ(false);
    this.instruction = new Mic1Instruction(0,
                                           jmpSet,
                                           new Mic1ALUSignalSet(),
                                           new Mic1CBusSignalSet(),
                                           new Mic1MemorySignalSet(),
                                           Mic1BBusRegister.MDR);
    start = this.stringBuilder.toString();
    // call decoding
    this.instruction.decodeJMPAndAddress(this.stringBuilder);
    // check the created text
    assertThat(this.stringBuilder.toString()).isEqualTo(start + ";goto (MBR)");

    jmpSet.setJmpC(false);
    jmpSet.setJmpN(true);
    jmpSet.setJmpZ(false);
    this.instruction = new Mic1Instruction(47,
                                           jmpSet,
                                           new Mic1ALUSignalSet(),
                                           new Mic1CBusSignalSet(),
                                           new Mic1MemorySignalSet(),
                                           Mic1BBusRegister.MDR);
    start = this.stringBuilder.toString();
    // call decoding
    this.instruction.decodeJMPAndAddress(this.stringBuilder);
    // check the created text
    assertThat(this.stringBuilder.toString()).isEqualTo("N=" + start + ";if (N) goto 0x12F; else goto 0x2F");

    jmpSet.setJmpC(false);
    jmpSet.setJmpN(false);
    jmpSet.setJmpZ(true);
    this.instruction = new Mic1Instruction(47,
                                           jmpSet,
                                           new Mic1ALUSignalSet(),
                                           new Mic1CBusSignalSet(),
                                           new Mic1MemorySignalSet(),
                                           Mic1BBusRegister.MDR);
    start = this.stringBuilder.toString();
    // call decoding
    this.instruction.decodeJMPAndAddress(this.stringBuilder);
    // check the created text
    assertThat(this.stringBuilder.toString()).isEqualTo("Z=" + start + ";if (Z) goto 0x12F; else goto 0x2F");

    jmpSet.setJmpC(false);
    jmpSet.setJmpN(true);
    jmpSet.setJmpZ(false);
    this.instruction = new Mic1Instruction(447,
                                           jmpSet,
                                           new Mic1ALUSignalSet(),
                                           new Mic1CBusSignalSet(),
                                           new Mic1MemorySignalSet(),
                                           Mic1BBusRegister.MDR);
    start = this.stringBuilder.toString();
    // call decoding
    this.instruction.decodeJMPAndAddress(this.stringBuilder);
    // check the created text
    assertThat(this.stringBuilder.toString()).isEqualTo("N=" + start + ";if (N) goto 0x1BF; else goto 0x1BF");

    jmpSet.setJmpC(false);
    jmpSet.setJmpN(false);
    jmpSet.setJmpZ(true);
    this.instruction = new Mic1Instruction(447,
                                           jmpSet,
                                           new Mic1ALUSignalSet(),
                                           new Mic1CBusSignalSet(),
                                           new Mic1MemorySignalSet(),
                                           Mic1BBusRegister.MDR);
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

    final Mic1MemorySignalSet memSet = new Mic1MemorySignalSet();

    // try all combinations of write, read and fetch
    for (final boolean write : BOOLEAN_POSSIBILITIES) {
      for (final boolean read : BOOLEAN_POSSIBILITIES) {
        for (final boolean fetch : BOOLEAN_POSSIBILITIES) {
          // reset the content of the string builder
          this.stringBuilder = new StringBuilder("[...]");

          memSet.setRead(read);
          memSet.setWrite(write);
          memSet.setFetch(fetch);
          this.instruction = new Mic1Instruction(0,
                                                 new Mic1JMPSignalSet(),
                                                 new Mic1ALUSignalSet(),
                                                 new Mic1CBusSignalSet(),
                                                 memSet,
                                                 Mic1BBusRegister.MDR);

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
    final Mic1ALUSignalSet aluSet = new Mic1ALUSignalSet();

    // try all combinations of sra1 and sll8
    for (final boolean sll8 : BOOLEAN_POSSIBILITIES) {
      for (final boolean sra1 : BOOLEAN_POSSIBILITIES) {
        // reset the content of the string builder
        this.stringBuilder = new StringBuilder("[...]");

        aluSet.setSLL8(sll8);
        aluSet.setSRA1(sra1);
        this.instruction = new Mic1Instruction(0,
                                               new Mic1JMPSignalSet(),
                                               aluSet,
                                               new Mic1CBusSignalSet(),
                                               new Mic1MemorySignalSet(),
                                               Mic1BBusRegister.MDR);

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

    final Mic1JMPSignalSet jmpSet = new Mic1JMPSignalSet();
    final Mic1ALUSignalSet aluSet = new Mic1ALUSignalSet();
    final Mic1CBusSignalSet cBusSet = new Mic1CBusSignalSet();
    final Mic1MemorySignalSet memSet = new Mic1MemorySignalSet();
    jmpSet.setJmpN(true);
    aluSet.setSLL8(true);
    aluSet.setEnA(true);
    aluSet.setEnB(true);
    cBusSet.setH(true);
    cBusSet.setTos(true);
    cBusSet.setLv(true);
    cBusSet.setPc(true);

    aluSet.setF0(true);
    aluSet.setF1(true);
    testSingleDecodeALUOperation(jmpSet, aluSet, cBusSet, memSet, "A+B");

    aluSet.setF0(false);
    aluSet.setF1(true);
    testSingleDecodeALUOperation(jmpSet, aluSet, cBusSet, memSet, "A OR B");

    aluSet.setF0(true);
    aluSet.setF1(false);
    testSingleDecodeALUOperation(jmpSet, aluSet, cBusSet, memSet, "NOT B");

    aluSet.setF0(false);
    aluSet.setF1(false);
    testSingleDecodeALUOperation(jmpSet, aluSet, cBusSet, memSet, "A AND B");

    printEndOfMethod();
  }

  private void testSingleDecodeALUOperation(final Mic1JMPSignalSet jmpSet,
                                            final Mic1ALUSignalSet aluSet,
                                            final Mic1CBusSignalSet cBusSet,
                                            final Mic1MemorySignalSet memSet,
                                            final String expected) {
    this.instruction = new Mic1Instruction(0, jmpSet, aluSet, cBusSet, memSet, Mic1BBusRegister.MDR);
    final String start = this.stringBuilder.toString();
    // call decoding
    this.instruction.decodeALUOperation(this.stringBuilder, "A", "B");
    // check the created text
    assertThat(this.stringBuilder.toString()).isEqualTo(start + expected);
  }

  @Test
  public void testDecodeALUPlus() {
    printMethodName();
    final Mic1ALUSignalSet aluSet = new Mic1ALUSignalSet();

    for (final boolean enableA : BOOLEAN_POSSIBILITIES) {
      for (final boolean enableB : BOOLEAN_POSSIBILITIES) {
        for (final boolean invertA : BOOLEAN_POSSIBILITIES) {
          for (final boolean increment : BOOLEAN_POSSIBILITIES) {
            // reset the content of the string builder
            this.stringBuilder = new StringBuilder("[...]");

            aluSet.setEnA(enableA);
            aluSet.setEnB(enableB);
            aluSet.setInvA(invertA);
            aluSet.setInc(increment);
            this.instruction = new Mic1Instruction(0,
                                                   new Mic1JMPSignalSet(),
                                                   aluSet,
                                                   new Mic1CBusSignalSet(),
                                                   new Mic1MemorySignalSet(),
                                                   Mic1BBusRegister.MDR);

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
    final Mic1ALUSignalSet aluSet = new Mic1ALUSignalSet();

    // test ENB and !ENB
    for (final boolean enableB : BOOLEAN_POSSIBILITIES) {
      // reset the content of the string builder
      this.stringBuilder = new StringBuilder("[...]");

      aluSet.setEnB(enableB);
      this.instruction = new Mic1Instruction(0,
                                             new Mic1JMPSignalSet(),
                                             aluSet,
                                             new Mic1CBusSignalSet(),
                                             new Mic1MemorySignalSet(),
                                             Mic1BBusRegister.MDR);

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
    final Mic1ALUSignalSet aluSet = new Mic1ALUSignalSet();

    for (final boolean enableA : BOOLEAN_POSSIBILITIES) {
      for (final boolean enableB : BOOLEAN_POSSIBILITIES) {
        for (final boolean invertA : BOOLEAN_POSSIBILITIES) {
          // reset the content of the string builder
          this.stringBuilder = new StringBuilder("[...]");

          aluSet.setEnA(enableA);
          aluSet.setEnB(enableB);
          aluSet.setInvA(invertA);
          this.instruction = new Mic1Instruction(0,
                                                 new Mic1JMPSignalSet(),
                                                 aluSet,
                                                 new Mic1CBusSignalSet(),
                                                 new Mic1MemorySignalSet(),
                                                 Mic1BBusRegister.MDR);

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
    final Mic1ALUSignalSet aluSet = new Mic1ALUSignalSet();

    for (final boolean enableA : BOOLEAN_POSSIBILITIES) {
      for (final boolean enableB : BOOLEAN_POSSIBILITIES) {
        for (final boolean invertA : BOOLEAN_POSSIBILITIES) {
          // reset the content of the string builder
          this.stringBuilder = new StringBuilder("[...]");

          aluSet.setEnA(enableA);
          aluSet.setEnB(enableB);
          aluSet.setInvA(invertA);
          this.instruction = new Mic1Instruction(0,
                                                 new Mic1JMPSignalSet(),
                                                 aluSet,
                                                 new Mic1CBusSignalSet(),
                                                 new Mic1MemorySignalSet(),
                                                 Mic1BBusRegister.MDR);

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

  private void checkDecodingOfBBusBits(final String s, final Mic1BBusRegister reg) {
    this.instruction = new Mic1Instruction(0,
                                           new Mic1JMPSignalSet(),
                                           new Mic1ALUSignalSet(),
                                           new Mic1CBusSignalSet(),
                                           new Mic1MemorySignalSet(),
                                           reg);
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
    final Mic1CBusSignalSet cBusSet = new Mic1CBusSignalSet();

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
                      cBusSet.setH(h);
                      cBusSet.setOpc(opc);
                      cBusSet.setTos(tos);
                      cBusSet.setCpp(cpp);
                      cBusSet.setLv(lv);
                      cBusSet.setSp(sp);
                      cBusSet.setPc(pc);
                      cBusSet.setMdr(mdr);
                      cBusSet.setMar(mar);
                      this.instruction = new Mic1Instruction(0,
                                                             new Mic1JMPSignalSet(),
                                                             new Mic1ALUSignalSet(),
                                                             cBusSet,
                                                             new Mic1MemorySignalSet(),
                                                             Mic1BBusRegister.MDR);

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

    final Mic1MemorySignalSet memSet = new Mic1MemorySignalSet();
    final Mic1CBusSignalSet cBusSet = new Mic1CBusSignalSet();
    final Mic1ALUSignalSet aluSet = new Mic1ALUSignalSet();
    final Mic1JMPSignalSet jmpSet = new Mic1JMPSignalSet();
    Mic1Instruction other = new Mic1Instruction(addr, jmpSet, aluSet, cBusSet, memSet, b);
    assertThat(this.instruction).isEqualTo(other);
    assertThat(this.instruction.hashCode()).isEqualTo(this.instruction.hashCode());

    b = Mic1BBusRegister.OPC;
    other = compareInstructionToOther(addr, b, memSet, cBusSet, aluSet, jmpSet, other);

    addr = 17;
    other = compareInstructionToOther(addr, b, memSet, cBusSet, aluSet, jmpSet, other);
  }

  @Test
  public void testHashCodeAndEqualsObject_Memory() {
    final int addr = 0;
    final Mic1BBusRegister b = null;
    final Mic1MemorySignalSet memSet = new Mic1MemorySignalSet();
    final Mic1CBusSignalSet cBusSet = new Mic1CBusSignalSet();
    final Mic1ALUSignalSet aluSet = new Mic1ALUSignalSet();
    final Mic1JMPSignalSet jmpSet = new Mic1JMPSignalSet();
    Mic1Instruction other = new Mic1Instruction(addr, jmpSet, aluSet, cBusSet, memSet, b);

    memSet.setFetch(true);
    other = compareInstructionToOther(addr, b, memSet, cBusSet, aluSet, jmpSet, other);
    memSet.setRead(true);
    other = compareInstructionToOther(addr, b, memSet, cBusSet, aluSet, jmpSet, other);
    memSet.setWrite(true);
    other = compareInstructionToOther(addr, b, memSet, cBusSet, aluSet, jmpSet, other);
    memSet.setFetch(false);
    other = compareInstructionToOther(addr, b, memSet, cBusSet, aluSet, jmpSet, other);
    memSet.setRead(false);
    other = compareInstructionToOther(addr, b, memSet, cBusSet, aluSet, jmpSet, other);
    memSet.setWrite(false);
    other = compareInstructionToOther(addr, b, memSet, cBusSet, aluSet, jmpSet, other);
  }

  @Test
  public void testHashCodeAndEqualsObject_JMP() {
    final int addr = 0;
    final Mic1BBusRegister b = null;
    final Mic1MemorySignalSet memSet = new Mic1MemorySignalSet();
    final Mic1CBusSignalSet cBusSet = new Mic1CBusSignalSet();
    final Mic1ALUSignalSet aluSet = new Mic1ALUSignalSet();
    final Mic1JMPSignalSet jmpSet = new Mic1JMPSignalSet();
    Mic1Instruction other = new Mic1Instruction(addr, jmpSet, aluSet, cBusSet, memSet, b);

    jmpSet.setJmpC(true);
    other = compareInstructionToOther(addr, b, memSet, cBusSet, aluSet, jmpSet, other);
    jmpSet.setJmpN(true);
    other = compareInstructionToOther(addr, b, memSet, cBusSet, aluSet, jmpSet, other);
    jmpSet.setJmpZ(true);
    other = compareInstructionToOther(addr, b, memSet, cBusSet, aluSet, jmpSet, other);

    jmpSet.setJmpC(false);
    other = compareInstructionToOther(addr, b, memSet, cBusSet, aluSet, jmpSet, other);
    jmpSet.setJmpN(false);
    other = compareInstructionToOther(addr, b, memSet, cBusSet, aluSet, jmpSet, other);
    jmpSet.setJmpZ(false);
    other = compareInstructionToOther(addr, b, memSet, cBusSet, aluSet, jmpSet, other);
  }

  @Test
  public void testHashCodeAndEqualsObject_ALU() {
    final int addr = 0;
    final Mic1BBusRegister b = null;
    final Mic1MemorySignalSet memSet = new Mic1MemorySignalSet();
    final Mic1CBusSignalSet cBusSet = new Mic1CBusSignalSet();
    final Mic1ALUSignalSet aluSet = new Mic1ALUSignalSet();
    final Mic1JMPSignalSet jmpSet = new Mic1JMPSignalSet();
    Mic1Instruction other = new Mic1Instruction(addr, jmpSet, aluSet, cBusSet, memSet, b);

    aluSet.setEnA(true);
    other = compareInstructionToOther(addr, b, memSet, cBusSet, aluSet, jmpSet, other);
    aluSet.setEnB(true);
    other = compareInstructionToOther(addr, b, memSet, cBusSet, aluSet, jmpSet, other);
    aluSet.setF0(true);
    other = compareInstructionToOther(addr, b, memSet, cBusSet, aluSet, jmpSet, other);
    aluSet.setF1(true);
    other = compareInstructionToOther(addr, b, memSet, cBusSet, aluSet, jmpSet, other);
    aluSet.setInc(true);
    other = compareInstructionToOther(addr, b, memSet, cBusSet, aluSet, jmpSet, other);
    aluSet.setInvA(true);
    other = compareInstructionToOther(addr, b, memSet, cBusSet, aluSet, jmpSet, other);
    aluSet.setSLL8(true);
    other = compareInstructionToOther(addr, b, memSet, cBusSet, aluSet, jmpSet, other);
    aluSet.setSRA1(true);
    other = compareInstructionToOther(addr, b, memSet, cBusSet, aluSet, jmpSet, other);

    aluSet.setEnA(false);
    other = compareInstructionToOther(addr, b, memSet, cBusSet, aluSet, jmpSet, other);
    aluSet.setEnB(false);
    other = compareInstructionToOther(addr, b, memSet, cBusSet, aluSet, jmpSet, other);
    aluSet.setF0(false);
    other = compareInstructionToOther(addr, b, memSet, cBusSet, aluSet, jmpSet, other);
    aluSet.setF1(false);
    other = compareInstructionToOther(addr, b, memSet, cBusSet, aluSet, jmpSet, other);
    aluSet.setInc(false);
    other = compareInstructionToOther(addr, b, memSet, cBusSet, aluSet, jmpSet, other);
    aluSet.setInvA(false);
    other = compareInstructionToOther(addr, b, memSet, cBusSet, aluSet, jmpSet, other);
    aluSet.setSLL8(false);
    other = compareInstructionToOther(addr, b, memSet, cBusSet, aluSet, jmpSet, other);
    aluSet.setSRA1(false);
    other = compareInstructionToOther(addr, b, memSet, cBusSet, aluSet, jmpSet, other);
  }

  @Test
  public void testHashCodeAndEqualsObject_CBus() {
    final int addr = 0;
    final Mic1BBusRegister b = null;
    final Mic1MemorySignalSet memSet = new Mic1MemorySignalSet();
    final Mic1CBusSignalSet cBusSet = new Mic1CBusSignalSet();
    final Mic1ALUSignalSet aluSet = new Mic1ALUSignalSet();
    final Mic1JMPSignalSet jmpSet = new Mic1JMPSignalSet();
    Mic1Instruction other = new Mic1Instruction(addr, jmpSet, aluSet, cBusSet, memSet, b);

    cBusSet.setCpp(true);
    other = compareInstructionToOther(addr, b, memSet, cBusSet, aluSet, jmpSet, other);
    cBusSet.setH(true);
    other = compareInstructionToOther(addr, b, memSet, cBusSet, aluSet, jmpSet, other);
    cBusSet.setLv(true);
    other = compareInstructionToOther(addr, b, memSet, cBusSet, aluSet, jmpSet, other);
    cBusSet.setMar(true);
    other = compareInstructionToOther(addr, b, memSet, cBusSet, aluSet, jmpSet, other);
    cBusSet.setMdr(true);
    other = compareInstructionToOther(addr, b, memSet, cBusSet, aluSet, jmpSet, other);
    cBusSet.setOpc(true);
    other = compareInstructionToOther(addr, b, memSet, cBusSet, aluSet, jmpSet, other);
    cBusSet.setPc(true);
    other = compareInstructionToOther(addr, b, memSet, cBusSet, aluSet, jmpSet, other);
    cBusSet.setSp(true);
    other = compareInstructionToOther(addr, b, memSet, cBusSet, aluSet, jmpSet, other);
    cBusSet.setTos(true);
    other = compareInstructionToOther(addr, b, memSet, cBusSet, aluSet, jmpSet, other);

    cBusSet.setCpp(false);
    other = compareInstructionToOther(addr, b, memSet, cBusSet, aluSet, jmpSet, other);
    cBusSet.setH(false);
    other = compareInstructionToOther(addr, b, memSet, cBusSet, aluSet, jmpSet, other);
    cBusSet.setLv(false);
    other = compareInstructionToOther(addr, b, memSet, cBusSet, aluSet, jmpSet, other);
    cBusSet.setMar(false);
    other = compareInstructionToOther(addr, b, memSet, cBusSet, aluSet, jmpSet, other);
    cBusSet.setMdr(false);
    other = compareInstructionToOther(addr, b, memSet, cBusSet, aluSet, jmpSet, other);
    cBusSet.setOpc(false);
    other = compareInstructionToOther(addr, b, memSet, cBusSet, aluSet, jmpSet, other);
    cBusSet.setPc(false);
    other = compareInstructionToOther(addr, b, memSet, cBusSet, aluSet, jmpSet, other);
    cBusSet.setSp(false);
    other = compareInstructionToOther(addr, b, memSet, cBusSet, aluSet, jmpSet, other);
    cBusSet.setTos(false);
    other = compareInstructionToOther(addr, b, memSet, cBusSet, aluSet, jmpSet, other);

    printEndOfMethod();
  }

  private Mic1Instruction compareInstructionToOther(final int addr,
                                                    final Mic1BBusRegister b,
                                                    final Mic1MemorySignalSet memSet,
                                                    final Mic1CBusSignalSet cBusSet,
                                                    final Mic1ALUSignalSet aluSet,
                                                    final Mic1JMPSignalSet jmpSet,
                                                    Mic1Instruction other) {
    this.instruction = new Mic1Instruction(addr, jmpSet, aluSet, cBusSet, memSet, b);
    assertThat(this.instruction).isNotEqualTo(other);
    assertThat(this.instruction.hashCode()).isNotEqualTo(other.hashCode());
    other = new Mic1Instruction(addr, jmpSet, aluSet, cBusSet, memSet, b); // make object equal to instruction
    return other;
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
