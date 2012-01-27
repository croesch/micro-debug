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
package com.github.croesch.mic1.controlstore;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

import com.github.croesch.DefaultTestCase;
import com.github.croesch.TestUtil;
import com.github.croesch.mic1.register.Register;

/**
 * Provides test cases for {@link Mic1Instruction}.
 * 
 * @author croesch
 * @since Date: Nov 10, 2011
 */
public class Mic1InstructionDecoderTest extends DefaultTestCase {

  private Mic1Instruction instruction;

  private StringBuilder stringBuilder;

  private static final boolean[] BOOLEAN_POSSIBILITIES = new boolean[] { true, false };

  @Override
  protected void setUpDetails() {
    this.instruction = new Mic1Instruction(0,
                                           new Mic1JMPSignalSet(),
                                           new Mic1ALUSignalSet(),
                                           new Mic1CBusSignalSet(),
                                           new Mic1MemorySignalSet(),
                                           null);
    this.stringBuilder = new StringBuilder("[...]");
  }

  @Test
  public void testToString() {
    printMethodName();

    this.instruction = new Mic1Instruction(0,
                                           new Mic1JMPSignalSet(),
                                           new Mic1ALUSignalSet(),
                                           new Mic1CBusSignalSet(),
                                           new Mic1MemorySignalSet(),
                                           Register.MDR);
    assertThat(Mic1InstructionDecoder.decode(this.instruction)).isEqualTo("goto 0x0");

    this.instruction = new Mic1Instruction(144,
                                           new Mic1JMPSignalSet(),
                                           new Mic1ALUSignalSet(),
                                           new Mic1CBusSignalSet(),
                                           new Mic1MemorySignalSet(),
                                           Register.SP);
    assertThat(Mic1InstructionDecoder.decode(this.instruction)).isEqualTo("goto 0x90");

    final Mic1JMPSignalSet jmpSet = new Mic1JMPSignalSet();
    final Mic1ALUSignalSet aluSet = new Mic1ALUSignalSet();
    final Mic1CBusSignalSet cBusSet = new Mic1CBusSignalSet();
    final Mic1MemorySignalSet memSet = new Mic1MemorySignalSet();
    jmpSet.setJmpZ(true);
    aluSet.setSRA1(true).setF1(true).setEnB(true).setInc(true);
    cBusSet.setOpc(true).setCpp(true).setSp(true);
    this.instruction = new Mic1Instruction(47, jmpSet, aluSet, cBusSet, memSet, Register.LV);
    assertThat(Mic1InstructionDecoder.decode(this.instruction))
      .isEqualTo("Z=OPC=CPP=SP=LV>>1;if (Z) goto 0x12F; else goto 0x2F");

    printEndOfMethod();
  }

  @Test
  public void testDecodeJMPAndAddress() {
    printMethodName();
    // reset the content of the string builder
    this.stringBuilder = new StringBuilder("[...]");

    final Mic1JMPSignalSet jmpSet = new Mic1JMPSignalSet();
    jmpSet.setJmpC(false).setJmpN(false).setJmpZ(false);
    String start = this.stringBuilder.toString();
    // call decoding
    Mic1InstructionDecoder.decodeJMPAndAddress(jmpSet, 47, this.stringBuilder);
    // check the created text
    assertThat(this.stringBuilder.toString()).isEqualTo(start + ";goto 0x2F");

    jmpSet.setJmpC(true).setJmpN(false).setJmpZ(false);
    start = this.stringBuilder.toString();
    // call decoding
    Mic1InstructionDecoder.decodeJMPAndAddress(jmpSet, 47, this.stringBuilder);
    // check the created text
    assertThat(this.stringBuilder.toString()).isEqualTo(start + ";goto (MBR OR 0x2F)");

    jmpSet.setJmpC(true).setJmpN(false).setJmpZ(false);
    start = this.stringBuilder.toString();
    // call decoding
    Mic1InstructionDecoder.decodeJMPAndAddress(jmpSet, 0, this.stringBuilder);
    // check the created text
    assertThat(this.stringBuilder.toString()).isEqualTo(start + ";goto (MBR)");

    jmpSet.setJmpC(false).setJmpN(true).setJmpZ(false);
    start = this.stringBuilder.toString();
    // call decoding
    Mic1InstructionDecoder.decodeJMPAndAddress(jmpSet, 47, this.stringBuilder);
    // check the created text
    assertThat(this.stringBuilder.toString()).isEqualTo("N=" + start + ";if (N) goto 0x12F; else goto 0x2F");

    jmpSet.setJmpC(false).setJmpN(false).setJmpZ(true);
    start = this.stringBuilder.toString();
    // call decoding
    Mic1InstructionDecoder.decodeJMPAndAddress(jmpSet, 47, this.stringBuilder);
    // check the created text
    assertThat(this.stringBuilder.toString()).isEqualTo("Z=" + start + ";if (Z) goto 0x12F; else goto 0x2F");

    jmpSet.setJmpC(false).setJmpN(true).setJmpZ(false);
    start = this.stringBuilder.toString();
    // call decoding
    Mic1InstructionDecoder.decodeJMPAndAddress(jmpSet, 447, this.stringBuilder);
    // check the created text
    assertThat(this.stringBuilder.toString()).isEqualTo("N=" + start + ";if (N) goto 0x1BF; else goto 0x1BF");

    jmpSet.setJmpC(false).setJmpN(false).setJmpZ(true);
    start = this.stringBuilder.toString();
    // call decoding
    Mic1InstructionDecoder.decodeJMPAndAddress(jmpSet, 447, this.stringBuilder);
    // check the created text
    assertThat(this.stringBuilder.toString()).isEqualTo("Z=" + start + ";if (Z) goto 0x1BF; else goto 0x1BF");

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

          memSet.setRead(read).setWrite(write).setFetch(fetch);

          // ensure the method appends generated text
          final String start = this.stringBuilder.toString();
          // call decoding
          Mic1InstructionDecoder.decodeMemoryBits(memSet, this.stringBuilder);
          // check the created text
          assertThat(this.stringBuilder.toString()).startsWith(start);
          assertThat(this.stringBuilder.toString().contains(";wr")).isEqualTo(write);
          assertThat(this.stringBuilder.toString().contains(";rd")).isEqualTo(read);
          assertThat(this.stringBuilder.toString().contains(";fetch")).isEqualTo(fetch);
          TestUtil.printStep();
        }
        TestUtil.printLoopEnd();
      }
      TestUtil.printLoopEnd();
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

        aluSet.setSLL8(sll8).setSRA1(sra1);

        final String start = this.stringBuilder.toString();
        // call decoding
        Mic1InstructionDecoder.decodeShifterOperation(aluSet, this.stringBuilder);
        // check the created text
        assertThat(this.stringBuilder.toString()).startsWith(start);
        assertThat(this.stringBuilder.toString().contains(">>1")).isEqualTo(sra1);
        assertThat(this.stringBuilder.toString().contains("<<8")).isEqualTo(sll8);
        TestUtil.printStep();
      }
      TestUtil.printLoopEnd();
    }
    printEndOfMethod();
  }

  @Test
  public void testDecodeALUOperation() {
    printMethodName();
    // reset the content of the string builder
    this.stringBuilder = new StringBuilder("[...]");

    final Mic1ALUSignalSet aluSet = new Mic1ALUSignalSet();
    aluSet.setSLL8(true).setEnA(true).setEnB(true);

    aluSet.setF0(true).setF1(true);
    testSingleDecodeALUOperation(aluSet, "A+B");

    aluSet.setF0(false).setF1(true);
    testSingleDecodeALUOperation(aluSet, "A OR B");

    aluSet.setF0(true).setF1(false);
    testSingleDecodeALUOperation(aluSet, "NOT B");

    aluSet.setF0(false).setF1(false);
    testSingleDecodeALUOperation(aluSet, "A AND B");

    printEndOfMethod();
  }

  private void testSingleDecodeALUOperation(final Mic1ALUSignalSet aluSet, final String expected) {
    final String start = this.stringBuilder.toString();
    // call decoding
    Mic1InstructionDecoder.decodeALUOperation(aluSet, this.stringBuilder, "A", "B");
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

            aluSet.setEnA(enableA).setEnB(enableB).setInvA(invertA).setInc(increment);

            final String start = this.stringBuilder.toString();
            // call decoding
            Mic1InstructionDecoder.decodeALUPlus(aluSet, this.stringBuilder, "A", "B");

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
            TestUtil.printStep();
          }
          TestUtil.printLoopEnd();
        }
        TestUtil.printLoopEnd();
      }
      TestUtil.printLoopEnd();
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

      final String start = this.stringBuilder.toString();
      // call decoding
      Mic1InstructionDecoder.decodeALUNotB(aluSet, this.stringBuilder, "B");
      // check the created text
      if (enableB) {
        assertThat(this.stringBuilder.toString()).isEqualTo(start + "NOT B");
      } else {
        assertThat(this.stringBuilder.toString()).isEqualTo(start + "0");
      }
      TestUtil.printStep();
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

          aluSet.setEnA(enableA).setEnB(enableB).setInvA(invertA);

          final String start = this.stringBuilder.toString();
          // call decoding
          Mic1InstructionDecoder.decodeALUOr(aluSet, this.stringBuilder, "A", "B");

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
          TestUtil.printStep();
        }
        TestUtil.printLoopEnd();
      }
      TestUtil.printLoopEnd();
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

          aluSet.setEnA(enableA).setEnB(enableB).setInvA(invertA);

          final String start = this.stringBuilder.toString();
          // call decoding
          Mic1InstructionDecoder.decodeALUAnd(aluSet, this.stringBuilder, "A", "B");

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
          TestUtil.printStep();
        }
        TestUtil.printLoopEnd();
      }
      TestUtil.printLoopEnd();
    }
    printEndOfMethod();
  }

  @Test
  public void testDecodeBBusBits() {
    printMethodName();
    checkDecodingOfBBusBits("MAR", Register.MAR);
    checkDecodingOfBBusBits("MDR", Register.MDR);
    checkDecodingOfBBusBits("PC", Register.PC);
    checkDecodingOfBBusBits("MBR", Register.MBR);
    checkDecodingOfBBusBits("MBRU", Register.MBRU);
    checkDecodingOfBBusBits("SP", Register.SP);
    checkDecodingOfBBusBits("LV", Register.LV);
    checkDecodingOfBBusBits("CPP", Register.CPP);
    checkDecodingOfBBusBits("TOS", Register.TOS);
    checkDecodingOfBBusBits("OPC", Register.OPC);
    checkDecodingOfBBusBits("H", Register.H);
    checkDecodingOfBBusBits("???", null);

    printEndOfMethod();
  }

  private void checkDecodingOfBBusBits(final String s, final Register reg) {
    // check it multiple times
    assertThat(Mic1InstructionDecoder.decodeBBusBits(reg)).isEqualTo(s);
    assertThat(Mic1InstructionDecoder.decodeBBusBits(reg)).isEqualTo(s);
    assertThat(Mic1InstructionDecoder.decodeBBusBits(reg)).isEqualTo(s);
    assertThat(Mic1InstructionDecoder.decodeBBusBits(reg)).isEqualTo(s);
    assertThat(Mic1InstructionDecoder.decodeBBusBits(reg)).isEqualTo(s);
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
                      cBusSet.setH(h).setOpc(opc).setTos(tos).setCpp(cpp).setLv(lv);
                      cBusSet.setSp(sp).setPc(pc).setMdr(mdr).setMar(mar);

                      final String start = this.stringBuilder.toString();
                      // call decoding
                      Mic1InstructionDecoder.decodeCBusBits(cBusSet, this.stringBuilder);
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
                      TestUtil.printStep();
                    }
                    TestUtil.printLoopEnd();
                  }
                  TestUtil.printLoopEnd();
                }
                TestUtil.printLoopEnd();
              }
              TestUtil.printLoopEnd();
            }
            TestUtil.printLoopEnd();
          }
          TestUtil.printLoopEnd();
        }
        TestUtil.printLoopEnd();
      }
      TestUtil.printLoopEnd();
    }
    printEndOfMethod();
  }

  private void printEndOfMethod() {
    TestUtil.printEndOfMethod();
  }

  private void printMethodName() {
    TestUtil.printMethodName(1);
  }
}
