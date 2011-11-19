/*
 *
 *  Mic1Instruction.java
 *
 *  mic1 microarchitecture simulator 
 *  Copyright (C) 1999, Prentice-Hall, Inc. 
 * 
 *  This program is free software; you can redistribute it and/or modify 
 *  it under the terms of the GNU General Public License as published by 
 *  the Free Software Foundation; either version 2 of the License, or 
 *  (at your option) any later version. 
 * 
 *  This program is distributed in the hope that it will be useful, but 
 *  WITHOUT ANY WARRANTY; without even the implied warranty of 
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General 
 *  Public License for more details. 
 * 
 *  You should have received a copy of the GNU General Public License along with 
 *  this program; if not, write to: 
 * 
 *    Free Software Foundation, Inc. 
 *    59 Temple Place - Suite 330 
 *    Boston, MA 02111-1307, USA. 
 * 
 *  A copy of the GPL is available online the GNU web site: 
 * 
 *    http://www.gnu.org/copyleft/gpl.html
 * 
 */
package com.github.croesch.mic1.controlstore;

import java.util.Locale;

import com.github.croesch.mic1.Register;

/**
 * Decoder for {@link Mic1Instruction}. Is able to construct a {@link String} representing a given instruction.
 * 
 * @author croesch
 * @since Date: Nov 13, 2011
 */
public final class Mic1InstructionDecoder {

  /** contains a bit mask to define the highest bit in the nine-bit-value of address - 2 to the 8th */
  private static final int HIGHEST_BIT_OF_ADDRESS = 256;

  /** the text to represent negotiation of a value */
  private static final String TXT_NOT = "NOT";

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
  public static String decode(final Mic1Instruction instruction) {
    StringBuilder s = new StringBuilder();
    final String a = "H";
    final String b = decodeBBusBits(instruction.getbBusSelect());

    decodeCBusBits(instruction.getCBusSignals(), s);
    decodeALUOperation(instruction.getAluSignals(), s, a, b);
    decodeShifterOperation(instruction.getAluSignals(), s);
    decodeMemoryBits(instruction.getMemorySignals(), s);
    decodeJMPAndAddress(instruction.getJmpSignals(), instruction.getNextAddress(), s);

    // TODO decide when to write 'nop'
    //    if (s.toString().equals("0")) {
    //      s = new StringBuilder("nop");
    //    } else 
    if (s.toString().startsWith("0;")) {
      s = new StringBuilder(s.toString().substring(2));
    }

    return s.toString();
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
  static void decodeJMPAndAddress(final Mic1JMPSignalSet jmpSignals, final int nextAddress, final StringBuilder s) {
    // decode the JMP bits/addr
    if (jmpSignals.isJmpN() || jmpSignals.isJmpZ()) {
      final char c;
      if (jmpSignals.isJmpN()) {
        c = 'N';
      } else {
        c = 'Z';
      }
      s.insert(0, c + "=");
      s.append(";if (").append(c).append(") goto 0x");
      s.append(convertIntToHex(nextAddress | HIGHEST_BIT_OF_ADDRESS)).append("; else goto 0x");
      s.append(convertIntToHex(nextAddress));
    } else if (jmpSignals.isJmpC()) {
      if (nextAddress == 0) {
        s.append(";goto (MBR)");
      } else {
        s.append(";goto (MBR OR 0x").append(convertIntToHex(nextAddress)).append(")");
      }
    } else {
      s.append(";goto 0x").append(convertIntToHex(nextAddress));
    }
  }

  /**
   * Converts the given number to a hexadecimal string and then converts the result to upper case letters.
   * 
   * @since Date: Nov 10, 2011
   * @param i the number to convert to hexadecimal
   * @return a {@link String} representing the value of i in hexadecimal
   * @see Integer#toHexString(int)
   */
  static String convertIntToHex(final int i) {
    return Integer.toHexString(i).toUpperCase(Locale.getDefault());
  }

  /**
   * Decodes the given signals: write, read and fetch. It appends a text to the given {@link StringBuilder}.
   * 
   * @since Date: Nov 10, 2011
   * @param memorySignals the signals to decode
   * @param s the {@link StringBuilder} to append the text to.
   */
  static void decodeMemoryBits(final Mic1MemorySignalSet memorySignals, final StringBuilder s) {
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
  static void decodeShifterOperation(final Mic1ALUSignalSet aluSignals, final StringBuilder s) {
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
  static void decodeALUOperation(final Mic1ALUSignalSet aluSignals,
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
  static void decodeALUPlus(final Mic1ALUSignalSet aluSignals, final StringBuilder s, final String a, final String b) {
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
  private static void decodeALUPlusADisabled(final Mic1ALUSignalSet aluSignals, final StringBuilder s, final String b) {
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
  private static void decodeALUPlusAEnabled(final Mic1ALUSignalSet aluSignals,
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
  static void decodeALUNotB(final Mic1ALUSignalSet aluSignals, final StringBuilder s, final String b) {
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
  static void decodeALUOr(final Mic1ALUSignalSet aluSignals, final StringBuilder s, final String a, final String b) {
    if (aluSignals.isEnA()) {
      if (aluSignals.isEnB()) {
        if (aluSignals.isInvA()) {
          s.append("(" + TXT_NOT + " ").append(a).append(") OR ").append(b);
        } else {
          s.append(a).append(" OR ").append(b);
        }
      } else {
        if (aluSignals.isInvA()) {
          s.append(TXT_NOT + " ").append(a);
        } else {
          s.append(a);
        }
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
  static void decodeALUAnd(final Mic1ALUSignalSet aluSignals, final StringBuilder s, final String a, final String b) {
    if (aluSignals.isEnB()) {
      if (aluSignals.isEnA()) {
        if (aluSignals.isInvA()) {
          s.append("(" + TXT_NOT + " ").append(a).append(")");
        } else {
          s.append(a);
        }
        s.append(" AND ").append(b);
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
  static void decodeCBusBits(final Mic1CBusSignalSet cBusSignals, final StringBuilder s) {
    // decode the C-bus bits
    if (cBusSignals.isH()) {
      s.append("H=");
    }
    if (cBusSignals.isOpc()) {
      s.append("OPC=");
    }
    if (cBusSignals.isTos()) {
      s.append("TOS=");
    }
    if (cBusSignals.isCpp()) {
      s.append("CPP=");
    }
    if (cBusSignals.isLv()) {
      s.append("LV=");
    }
    if (cBusSignals.isSp()) {
      s.append("SP=");
    }
    if (cBusSignals.isPc()) {
      s.append("PC=");
    }
    if (cBusSignals.isMdr()) {
      s.append("MDR=");
    }
    if (cBusSignals.isMar()) {
      s.append("MAR=");
    }
  }

}
