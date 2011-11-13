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

/**
 * Mic1Instruction This class represents an instruction which might appear in the Mic-1 control store. To Do We should
 * probably create a separate class for Mic1Reader and move the read method from here to the Mic1Reader class.
 * Modification History Name Date Comment ---------------- ---------- ---------------------------------------- Ray Ontko
 * 1998.09.01 Created
 */
public final class Mic1Instruction {

  /** bit mask for the value of address */
  private static final int ADDRESS_MASK = 0x1ff;

  /** contains a bit mask to define the highest bit in the nine-bit-value of address - 2 to the 8th */
  private static final int HIGHEST_BIT_OF_ADDRESS = 256;

  /** the text to represent negotiation of a value */
  private static final String TXT_NOT = "NOT";

  /** MIR[35:27] - bits that used for calculation of next MPC - basic address */
  private final int nextAddress;

  /** MIR[26:24]: set of bits that are basic for calculation of next MPC */
  private final Mic1JMPSignalSet jmpSignals = new Mic1JMPSignalSet();

  /** MIR[23:16]: set of bits that are responsible for the behavior of the ALU and the shifter */
  private final Mic1ALUSignalSet aluSignals = new Mic1ALUSignalSet();

  /** MIR[15:7]: set of bits that are responsible for the registers that are filled with the C-Bus value */
  private final Mic1CBusSignalSet cBusSignals = new Mic1CBusSignalSet();

  /** MIR[6:4]: set of bits that are responsible for communication with external memory (main memory and program memory) */
  private final Mic1MemorySignalSet memorySignals = new Mic1MemorySignalSet();

  /** responsible which register's value is written on the B-Bus */
  private final Mic1BBusRegister bBusSelect;

  /**
   * Constructs a single mic1-instruction.
   * 
   * @since Date: Nov 13, 2011
   * @param addr contains the MIR[35:27]. Only the lowest nine bits are fetched, it contains the value that is used to
   *        calculate next value of MPC.
   * @param jmpSet MIR[26:24]: set of bits that are basic for calculation of next MPC
   * @param aluSet MIR[23:16]: set of bits that are responsible for the behavior of the ALU and the shifter
   * @param cBusSet MIR[15:7]: set of bits that are responsible for the registers that are filled with the C-Bus value
   * @param memSet MIR[6:4]: set of bits that are responsible for communication with external memory (main memory and
   *        program memory) details see the comments of the fields or the script of Karl Stroetmann.
   * @param b contains the MIR[3:0]. Only the lowest four bits are fetched, it contains the bits that are used to define
   *        which register's value is written to the B-Bus.
   */
  public Mic1Instruction(final int addr,
                         final Mic1JMPSignalSet jmpSet,
                         final Mic1ALUSignalSet aluSet,
                         final Mic1CBusSignalSet cBusSet,
                         final Mic1MemorySignalSet memSet,
                         final Mic1BBusRegister b) {
    this.nextAddress = addr & ADDRESS_MASK;
    this.bBusSelect = b;

    this.jmpSignals.copyOf(jmpSet);
    this.aluSignals.copyOf(aluSet);
    this.cBusSignals.copyOf(cBusSet);
    this.memorySignals.copyOf(memSet);
  }

  @Override
  public String toString() {
    StringBuilder s = new StringBuilder();
    final String a = "H";
    final String b = decodeBBusBits();

    decodeCBusBits(s);
    decodeALUOperation(s, a, b);
    decodeShifterOperation(s);
    decodeMemoryBits(s);
    decodeJMPAndAddress(s);

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
   * Decodes signals of the instruction that belong to calculation of next MPC: JMPN, JMPC, JMPZ (and address). It
   * genereates the text and appends it to the given {@link StringBuilder}.
   * 
   * @since Date: Nov 12, 2011
   * @param s the {@link StringBuilder} to append the text to.
   */
  void decodeJMPAndAddress(final StringBuilder s) {
    // decode the JMP bits/addr
    if (this.jmpSignals.isJmpN() || this.jmpSignals.isJmpZ()) {
      final char c;
      if (this.jmpSignals.isJmpN()) {
        c = 'N';
      } else {
        c = 'Z';
      }
      s.insert(0, c + "=");
      s.append(";if (").append(c).append(") goto 0x");
      s.append(convertIntToHex(this.nextAddress | HIGHEST_BIT_OF_ADDRESS)).append("; else goto 0x");
      s.append(convertIntToHex(this.nextAddress));
    } else if (this.jmpSignals.isJmpC()) {
      if (this.nextAddress == 0) {
        s.append(";goto (MBR)");
      } else {
        s.append(";goto (MBR OR 0x").append(convertIntToHex(this.nextAddress)).append(")");
      }
    } else {
      s.append(";goto 0x").append(convertIntToHex(this.nextAddress));
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
   * Decodes the signals that belong to the memory: write, read and fetch. It appends a text to the given
   * {@link StringBuilder}.
   * 
   * @since Date: Nov 10, 2011
   * @param s the {@link StringBuilder} to append the text to.
   */
  void decodeMemoryBits(final StringBuilder s) {
    // decode the memorySignals bits
    if (this.memorySignals.isWrite()) {
      s.append(";wr");
    }
    if (this.memorySignals.isRead()) {
      s.append(";rd");
    }
    if (this.memorySignals.isFetch()) {
      s.append(";fetch");
    }
  }

  /**
   * Decodes the signals that belong to the shifter: SRA1 and SLL8. It appends the generated text to the given
   * {@link StringBuilder}.
   * 
   * @since Date: Nov 10, 2011
   * @param s the {@link StringBuilder} to append the text to.
   */
  void decodeShifterOperation(final StringBuilder s) {
    // decode the shifter operation
    if (this.aluSignals.isSRA1()) {
      s.append(">>1");
    }
    if (this.aluSignals.isSLL8()) {
      s.append("<<8");
    }
  }

  /**
   * Decodes the signals of the ALU. Will generate text that explains what the ALU is calculating, based on the signals
   * values. It appends the generated text to the given {@link StringBuilder}.
   * 
   * @since Date: Nov 11, 2011
   * @param s the {@link StringBuilder} to append the text to.
   * @param a the decoded text that describes the value written in the input A of the ALU
   * @param b the decoded text that describes the value written in the input B of the ALU
   */
  void decodeALUOperation(final StringBuilder s, final String a, final String b) {
    // decode the ALU operation
    if (!this.aluSignals.isF0() && !this.aluSignals.isF1()) { // a AND b
      decodeALUAnd(s, a, b);
    } else if (!this.aluSignals.isF0() && this.aluSignals.isF1()) { // a OR b
      decodeALUOr(s, a, b);
    } else if (this.aluSignals.isF0() && !this.aluSignals.isF1()) { // NOT b
      decodeALUNotB(s, b);
    } else if (this.aluSignals.isF0() && this.aluSignals.isF1()) { // a + b
      decodeALUPlus(s, a, b);
    }
  }

  /**
   * Decodes the signals of the ALU if other signals say it should add two values. The signals are: ENA, ENB, INVA and
   * INC. It appends the generated text to the given {@link StringBuilder}.
   * 
   * @since Date: Nov 10, 2011
   * @param s the {@link StringBuilder} to append the text to.
   * @param a the decoded text that describes the value written in the input A of the ALU
   * @param b the decoded text that describes the value written in the input B of the ALU
   */
  void decodeALUPlus(final StringBuilder s, final String a, final String b) {
    if (this.aluSignals.isEnA()) {
      decodeALUPlusAEnabled(s, a, b);
    } else {
      decodeALUPlusADisabled(s, b);
    }
  }

  /**
   * Decodes the signals of the ALU if other signals say it should add two values. The signals are: ENB, INVA and INC.
   * It appends the generated text to the given {@link StringBuilder}. The signal ENA mustn't be set if this method is
   * called.
   * 
   * @since Date: Nov 13, 2011
   * @param s the {@link StringBuilder} to append the text to.
   * @param b the decoded text that describes the value written in the input B of the ALU
   */
  private void decodeALUPlusADisabled(final StringBuilder s, final String b) {
    if (this.aluSignals.isInvA() && !this.aluSignals.isInc()) {
      if (this.aluSignals.isEnB()) {
        s.append(b);
      }
      s.append("-1");
    } else if (!this.aluSignals.isInvA() && this.aluSignals.isInc()) {
      if (this.aluSignals.isEnB()) {
        s.append(b).append("+");
      }
      s.append("1");
    } else {
      if (this.aluSignals.isEnB()) {
        s.append(b);
      } else {
        s.append("0");
      }
    }
  }

  /**
   * Decodes the signals of the ALU if other signals say it should add two values. The signals are: ENB, INVA and INC.
   * It appends the generated text to the given {@link StringBuilder}. The signal ENA must be set if this method is
   * called.
   * 
   * @since Date: Nov 13, 2011
   * @param s the {@link StringBuilder} to append the text to.
   * @param a the decoded text that describes the value written in the input A of the ALU
   * @param b the decoded text that describes the value written in the input B of the ALU
   */
  private void decodeALUPlusAEnabled(final StringBuilder s, final String a, final String b) {
    if (this.aluSignals.isInvA()) {
      if (this.aluSignals.isEnB()) {
        s.append(b);
      }
      s.append("-").append(a);
      if (!this.aluSignals.isInc()) {
        s.append("-1");
      }
    } else {
      s.append(a);
      if (this.aluSignals.isEnB()) {
        s.append("+").append(b);
      }
      if (this.aluSignals.isInc()) {
        s.append("+1");
      }
    }
  }

  /**
   * Decodes ENB signal of the ALU if other signals say it should negate B. It appends the generated text to the given
   * {@link StringBuilder}.
   * 
   * @since Date: Nov 10, 2011
   * @param s the {@link StringBuilder} to append the text to.
   * @param b the decoded text that describes the value written in the input B of the ALU
   */
  void decodeALUNotB(final StringBuilder s, final String b) {
    if (this.aluSignals.isEnB()) {
      s.append(TXT_NOT).append(' ').append(b);
    } else {
      s.append("0");
    }
  }

  /**
   * Decodes the signals of the ALU if other signals say it should calculate A or B. The signals are: ENA, ENB and INVA.
   * It appends the generated text to the given {@link StringBuilder}.
   * 
   * @since Date: Nov 11, 2011
   * @param s the {@link StringBuilder} to append the text to.
   * @param a the decoded text that describes the value written in the input A of the ALU
   * @param b the decoded text that describes the value written in the input B of the ALU
   */
  void decodeALUOr(final StringBuilder s, final String a, final String b) {
    if (this.aluSignals.isEnA()) {
      if (this.aluSignals.isEnB()) {
        if (this.aluSignals.isInvA()) {
          s.append("(" + TXT_NOT + " ").append(a).append(") OR ").append(b);
        } else {
          s.append(a).append(" OR ").append(b);
        }
      } else {
        if (this.aluSignals.isInvA()) {
          s.append(TXT_NOT + " ").append(a);
        } else {
          s.append(a);
        }
      }
    } else { // a is not enabled
      if (this.aluSignals.isInvA()) {
        s.append("-1");
      } else {
        if (this.aluSignals.isEnB()) {
          s.append(b);
        } else {
          s.append("0");
        }
      }
    }
  }

  /**
   * Decodes the signals of the ALU if other signals say it should calculate A and B. The signals are: ENA, ENB and
   * INVA. It appends the generated text to the given {@link StringBuilder}.
   * 
   * @since Date: Nov 11, 2011
   * @param s the {@link StringBuilder} to append the text to.
   * @param a the decoded text that describes the value written in the input A of the ALU
   * @param b the decoded text that describes the value written in the input B of the ALU
   */
  void decodeALUAnd(final StringBuilder s, final String a, final String b) {
    if (this.aluSignals.isEnB()) {
      if (this.aluSignals.isEnA()) {
        if (this.aluSignals.isInvA()) {
          s.append("(" + TXT_NOT + " ").append(a).append(")");
        } else {
          s.append(a);
        }
        s.append(" AND ").append(b);
      } else if (this.aluSignals.isInvA()) {
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
   * @return the generated text
   */
  String decodeBBusBits() {
    final String unknown = "???";
    if (this.bBusSelect == null) {
      return unknown;
    }
    return this.bBusSelect.toString();
  }

  /**
   * Decodes the signals that determines which register should be written with the value of the CBus. It appends the
   * generated text to the given {@link StringBuilder}.
   * 
   * @since Date: Nov 11, 2011
   * @param s the {@link StringBuilder} to append the text to.
   */
  void decodeCBusBits(final StringBuilder s) {
    // decode the C-bus bits
    if (this.cBusSignals.isH()) {
      s.append("H=");
    }
    if (this.cBusSignals.isOpc()) {
      s.append("OPC=");
    }
    if (this.cBusSignals.isTos()) {
      s.append("TOS=");
    }
    if (this.cBusSignals.isCpp()) {
      s.append("CPP=");
    }
    if (this.cBusSignals.isLv()) {
      s.append("LV=");
    }
    if (this.cBusSignals.isSp()) {
      s.append("SP=");
    }
    if (this.cBusSignals.isPc()) {
      s.append("PC=");
    }
    if (this.cBusSignals.isMdr()) {
      s.append("MDR=");
    }
    if (this.cBusSignals.isMar()) {
      s.append("MAR=");
    }
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result *= prime;
    if (this.bBusSelect != null) {
      result += this.bBusSelect.hashCode();
    }
    result = prime * result + this.nextAddress;
    result = prime * result + this.aluSignals.hashCode();
    result = prime * result + this.jmpSignals.hashCode();
    result = prime * result + this.cBusSignals.hashCode();
    result = prime * result + this.memorySignals.hashCode();
    return result;
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final Mic1Instruction other = (Mic1Instruction) obj;
    if (this.bBusSelect != other.bBusSelect) {
      return false;
    }
    if (this.nextAddress != other.nextAddress) {
      return false;
    }
    if (!this.jmpSignals.equals(other.jmpSignals)) {
      return false;
    }
    if (!this.aluSignals.equals(other.aluSignals)) {
      return false;
    }
    if (!this.cBusSignals.equals(other.cBusSignals)) {
      return false;
    }
    return this.memorySignals.equals(other.memorySignals);
  }

}
