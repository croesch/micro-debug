/*
 * Copyright (C) 1999, Prentice-Hall, Inc.
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

import com.github.croesch.commons.Utils;
import com.github.croesch.mic1.register.Register;

/**
 * Based on the implementation of <em>Ray Ontko</em>. <br>
 * Decoder for {@link MicroInstruction}. Is able to construct a {@link String} representing a given instruction.
 * 
 * @author croesch
 * @since Date: Nov 13, 2011
 */
public final class Mic1InstructionDecoder {

  /** contains a bit mask to define the highest bit in the nine-bit-value of address - 2 to the 8th */
  private static final int HIGHEST_BIT_OF_ADDRESS = 256;

  /** the text to represent 'not' operation of a value */
  private static final String TXT_NOT = "NOT";

  /** the text to represent 'or' operation of two values */
  private static final String TXT_OR = "OR";

  /** the text to represent 'and' operation of two values */
  private static final String TXT_AND = "AND";

  /**
   * Hides constructor from being invoked.
   * 
   * @since Date: Nov 13, 2011
   */
  private Mic1InstructionDecoder() {
    throw new AssertionError("called hidden constructor of utility class.");
  }

  /**
   * Decodes the given instruction and returns the {@link String} representing that instruction.
   * 
   * @since Date: Nov 13, 2011
   * @param instruction the instruction to decode and represent as {@link String}
   * @return the {@link String} representing the function of the given instruction
   */
  public static String decode(final MicroInstruction instruction) {
    StringBuilder decodedInstruction = new StringBuilder();
    final String aBusValue = Register.H.name();
    final String bBusValue = decodeBBusBits(instruction.getbBusSelect());

    decodeCBusBits(instruction.getCBusSignals(), decodedInstruction);
    decodeALUOperation(instruction.getAluSignals(), decodedInstruction, aBusValue, bBusValue);
    decodeShifterOperation(instruction.getAluSignals(), decodedInstruction);
    decodeMemoryBits(instruction.getMemorySignals(), decodedInstruction);
    decodeJMPAndAddress(instruction.getJmpSignals(), instruction.getNextAddress(), decodedInstruction);

    // TODO decide when to write 'nop'
    //    if (s.toString().equals("0")) {
    //      s = new StringBuilder("nop");
    //    } else 
    if (decodedInstruction.toString().startsWith("0;")) {
      decodedInstruction = new StringBuilder(decodedInstruction.toString().substring(2));
    }

    return decodedInstruction.toString();
  }

  /**
   * Decodes the given signals that belong to calculation of next MPC: JMPN, JMPC, JMPZ (and address). It genereates the
   * text and appends it to the given {@link StringBuilder}.
   * 
   * @since Date: Nov 12, 2011
   * @param jmpSignals the signals to decode
   * @param nextAddress the bytes of MIC that define the address
   * @param s the {@link StringBuilder} to append the text to.
   */
  static void decodeJMPAndAddress(final JMPSignalSet jmpSignals, final int nextAddress, final StringBuilder s) {
    // decode the JMP bits/addr
    if (jmpSignals.isJmpN() || jmpSignals.isJmpZ()) {
      final char c;
      if (jmpSignals.isJmpN()) {
        c = 'N';
      } else {
        c = 'Z';
      }
      s.insert(0, c + "=");
      s.append(";if (").append(c).append(") goto ");
      s.append(Utils.toHexString(nextAddress | HIGHEST_BIT_OF_ADDRESS)).append("; else goto ");
      s.append(Utils.toHexString(nextAddress));
    } else if (jmpSignals.isJmpC()) {
      s.append(";goto (").append(Register.MBR.name());
      if (nextAddress != 0) {
        s.append(" ").append(TXT_OR).append(" ").append(Utils.toHexString(nextAddress));
      }
      s.append(")");
    } else {
      s.append(";goto ").append(Utils.toHexString(nextAddress));
    }
  }

  /**
   * Decodes the given signals: write, read and fetch. It appends a text to the given {@link StringBuilder}.
   * 
   * @since Date: Nov 10, 2011
   * @param memorySignals the signals to decode
   * @param s the {@link StringBuilder} to append the text to.
   */
  static void decodeMemoryBits(final MemorySignalSet memorySignals, final StringBuilder s) {
    // decode the memorySignals bits
    if (memorySignals.isWrite()) {
      s.append(";wr");
    }
    if (memorySignals.isRead()) {
      s.append(";rd");
    }
    if (memorySignals.isFetch()) {
      s.append(";fetch");
    }
  }

  /**
   * Decodes the given signals: SRA1 and SLL8. It appends the generated text to the given {@link StringBuilder}.
   * 
   * @since Date: Nov 10, 2011
   * @param aluSignals the signals to decode
   * @param s the {@link StringBuilder} to append the text to.
   */
  static void decodeShifterOperation(final ALUSignalSet aluSignals, final StringBuilder s) {
    // decode the shifter operation
    if (aluSignals.isSRA1()) {
      s.append(">>1");
    }
    if (aluSignals.isSLL8()) {
      s.append("<<8");
    }
  }

  /**
   * Decodes the given signals. Will generate text that explains what the ALU is calculating, based on the signals
   * values. It appends the generated text to the given {@link StringBuilder}.
   * 
   * @since Date: Nov 11, 2011
   * @param aluSignals the signals to decode
   * @param s the {@link StringBuilder} to append the text to.
   * @param a the decoded text that describes the value written in the input A of the ALU
   * @param b the decoded text that describes the value written in the input B of the ALU
   */
  static void decodeALUOperation(final ALUSignalSet aluSignals,
                                 final StringBuilder s,
                                 final String a,
                                 final String b) {
    // decode the ALU operation
    if (!aluSignals.isF0() && !aluSignals.isF1()) { // a AND b
      decodeALUAnd(aluSignals, s, a, b);
    } else if (!aluSignals.isF0() && aluSignals.isF1()) { // a OR b
      decodeALUOr(aluSignals, s, a, b);
    } else if (aluSignals.isF0() && !aluSignals.isF1()) { // NOT b
      decodeALUNotB(aluSignals, s, b);
    } else if (aluSignals.isF0() && aluSignals.isF1()) { // a + b
      decodeALUPlus(aluSignals, s, a, b);
    }
  }

  /**
   * Decodes the given signals pretending to add two values. The signals are: ENA, ENB, INVA and INC. It appends the
   * generated text to the given {@link StringBuilder}.
   * 
   * @since Date: Nov 10, 2011
   * @param aluSignals the signals to decode
   * @param s the {@link StringBuilder} to append the text to.
   * @param a the decoded text that describes the value written in the input A of the ALU
   * @param b the decoded text that describes the value written in the input B of the ALU
   */
  static void decodeALUPlus(final ALUSignalSet aluSignals, final StringBuilder s, final String a, final String b) {
    if (aluSignals.isEnA()) {
      decodeALUPlusAEnabled(aluSignals, s, a, b);
    } else {
      decodeALUPlusADisabled(aluSignals, s, b);
    }
  }

  /**
   * Decodes the given signals pretending to add two values. The signals are: ENB, INVA and INC. It appends the
   * generated text to the given {@link StringBuilder}. The signal ENA mustn't be set if this method is called.
   * 
   * @since Date: Nov 13, 2011
   * @param aluSignals the signals to decode
   * @param s the {@link StringBuilder} to append the text to.
   * @param b the decoded text that describes the value written in the input B of the ALU
   */
  private static void decodeALUPlusADisabled(final ALUSignalSet aluSignals, final StringBuilder s, final String b) {
    if (aluSignals.isInvA() && !aluSignals.isInc()) {
      if (aluSignals.isEnB()) {
        s.append(b);
      }
      s.append("-1");
    } else if (!aluSignals.isInvA() && aluSignals.isInc()) {
      if (aluSignals.isEnB()) {
        s.append(b).append("+");
      }
      s.append("1");
    } else {
      if (aluSignals.isEnB()) {
        s.append(b);
      } else {
        s.append("0");
      }
    }
  }

  /**
   * Decodes the given signals pretending to add two values. The signals are: ENB, INVA and INC. It appends the
   * generated text to the given {@link StringBuilder}. The signal ENA must be set if this method is called.
   * 
   * @since Date: Nov 13, 2011
   * @param aluSignals the signals to decode
   * @param s the {@link StringBuilder} to append the text to.
   * @param a the decoded text that describes the value written in the input A of the ALU
   * @param b the decoded text that describes the value written in the input B of the ALU
   */
  private static void decodeALUPlusAEnabled(final ALUSignalSet aluSignals,
                                            final StringBuilder s,
                                            final String a,
                                            final String b) {
    if (aluSignals.isInvA()) {
      if (aluSignals.isEnB()) {
        s.append(b);
      }
      s.append("-").append(a);
      if (!aluSignals.isInc()) {
        s.append("-1");
      }
    } else {
      s.append(a);
      if (aluSignals.isEnB()) {
        s.append("+").append(b);
      }
      if (aluSignals.isInc()) {
        s.append("+1");
      }
    }
  }

  /**
   * Decodes ENB signal of the ALU pretending to calculate 'negate B'. It appends the generated text to the given
   * {@link StringBuilder}.
   * 
   * @since Date: Nov 10, 2011
   * @param aluSignals the signals to decode
   * @param s the {@link StringBuilder} to append the text to.
   * @param b the decoded text that describes the value written in the input B of the ALU
   */
  static void decodeALUNotB(final ALUSignalSet aluSignals, final StringBuilder s, final String b) {
    if (aluSignals.isEnB()) {
      s.append(TXT_NOT).append(' ').append(b);
    } else {
      s.append("0");
    }
  }

  /**
   * Decodes the given signals of the ALU pretending to calculate 'A or B'. The signals are: ENA, ENB and INVA. It
   * appends the generated text to the given {@link StringBuilder}.
   * 
   * @since Date: Nov 11, 2011
   * @param aluSignals the signals to decode
   * @param s the {@link StringBuilder} to append the text to.
   * @param a the decoded text that describes the value written in the input A of the ALU
   * @param b the decoded text that describes the value written in the input B of the ALU
   */
  static void decodeALUOr(final ALUSignalSet aluSignals, final StringBuilder s, final String a, final String b) {
    if (aluSignals.isEnA()) {
      if (aluSignals.isEnB()) {
        if (aluSignals.isInvA()) {
          s.append("(").append(TXT_NOT).append(" ").append(a).append(")");
        } else {
          s.append(a);
        }
        s.append(" ").append(TXT_OR).append(" ").append(b);
      } else {
        if (aluSignals.isInvA()) {
          s.append(TXT_NOT).append(" ");
        }
        s.append(a);
      }
    } else { // a is not enabled
      if (aluSignals.isInvA()) {
        s.append("-1");
      } else {
        if (aluSignals.isEnB()) {
          s.append(b);
        } else {
          s.append("0");
        }
      }
    }
  }

  /**
   * Decodes the given signals of the ALU pretending to calculate 'A and B'. The signals are: ENA, ENB and INVA. It
   * appends the generated text to the given {@link StringBuilder}.
   * 
   * @since Date: Nov 11, 2011
   * @param aluSignals the signals to decode
   * @param s the {@link StringBuilder} to append the text to.
   * @param a the decoded text that describes the value written in the input A of the ALU
   * @param b the decoded text that describes the value written in the input B of the ALU
   */
  static void decodeALUAnd(final ALUSignalSet aluSignals, final StringBuilder s, final String a, final String b) {
    if (aluSignals.isEnB()) {
      if (aluSignals.isEnA()) {
        if (aluSignals.isInvA()) {
          s.append("(").append(TXT_NOT).append(" ").append(a).append(")");
        } else {
          s.append(a);
        }
        s.append(" ").append(TXT_AND).append(" ").append(b);
      } else if (aluSignals.isInvA()) {
        s.append(b);
      } else {
        s.append("0");
      }
    } else {
      s.append("0");
    }
  }

  /**
   * Decodes the signal that determines which register should be written to the BBus. It returns the generated text.
   * 
   * @since Date: Nov 11, 2011
   * @param bBusSelect the signal to decode
   * @return the generated text
   */
  static String decodeBBusBits(final Register bBusSelect) {
    final String unknown = "???";
    if (bBusSelect == null) {
      return unknown;
    }
    return bBusSelect.toString();
  }

  /**
   * Decodes the given signals that determine which register should be filled with the value of the CBus. It appends the
   * generated text to the given {@link StringBuilder}.
   * 
   * @since Date: Nov 11, 2011
   * @param s the {@link StringBuilder} to append the text to.
   * @param cBusSignals the signals to decode
   */
  static void decodeCBusBits(final CBusSignalSet cBusSignals, final StringBuilder s) {
    // decode the C-bus bits
    if (cBusSignals.isH()) {
      s.append(Register.H.name()).append("=");
    }
    if (cBusSignals.isOpc()) {
      s.append(Register.OPC.name()).append("=");
    }
    if (cBusSignals.isTos()) {
      s.append(Register.TOS.name()).append("=");
    }
    if (cBusSignals.isCpp()) {
      s.append(Register.CPP.name()).append("=");
    }
    if (cBusSignals.isLv()) {
      s.append(Register.LV.name()).append("=");
    }
    if (cBusSignals.isSp()) {
      s.append(Register.SP.name()).append("=");
    }
    if (cBusSignals.isPc()) {
      s.append(Register.PC.name()).append("=");
    }
    if (cBusSignals.isMdr()) {
      s.append(Register.MDR.name()).append("=");
    }
    if (cBusSignals.isMar()) {
      s.append(Register.MAR.name()).append("=");
    }
  }

}
