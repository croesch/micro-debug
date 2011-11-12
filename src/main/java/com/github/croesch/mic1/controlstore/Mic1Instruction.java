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

import java.util.BitSet;

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

  /** MIR[35:27] - bits that are responsible for calculation of next MPC */
  private final int nextAddress;

  /** MIR[26] - bit that is responsible for manipulation of the next MPC value */
  private final boolean jmpC;

  /** MIR[25] - bit that is responsible for manipulation of the next MPC value, if ALU result is < 0 */
  private final boolean jmpN;

  /** MIR[24] - bit that is responsible for manipulation of the next MPC value, if ALU result is zero */
  private final boolean jmpZ;

  /** MIR[23] - bit that makes the shifter shift the result of the ALU eight bits to the left */
  private final boolean sll8;

  /** MIR[22] - bit that makes the shifter shift the result of the ALU arithmetically one bit to the right */
  private final boolean sra1;

  /** MIR[21] - one of two bits that are responsible for what calculation the ALU does */
  private final boolean f0;

  /** MIR[22] - one of two bits that are responsible for what calculation the ALU does */
  private final boolean f1;

  /** MIR[19] - ENA bit that enables the value of A in the ALU, if set - basically A AND ENA is calculated */
  private final boolean enableA;

  /** MIR[18] - ENB bit that enables the value of B in the ALU, if set - basically B AND ENB is calculated */
  private final boolean enableB;

  /** MIR[17] - INVA bit that inverts the value of A in the ALU, if set - basically (A AND ENA) XOR INVA is calculated */
  private final boolean invertA;

  /** MIR[16] - INC bit that defines the first carry in in the ALU - so it increments the result of ALU by one */
  private final boolean increment;

  /** MIR[15:7]: set of bits that are responsible for the registers that are filled with the C-Bus value */
  private final Mic1CBusSignalSet cBusSet = new Mic1CBusSignalSet();

  /** MIR[6:4]: set of bits that are responsible for communication with external memory (main memory and program memory) */
  private final Mic1MemorySignalSet mem = new Mic1MemorySignalSet();

  /** responsible which register's value is written on the B-Bus */
  private final Mic1BBusRegister bBusSelect;

  /**
   * Constructs a single mic1-instruction.
   * 
   * @since Date: Nov 12, 2011
   * @param addr contains the MIR[35:27]. Only the lowest nine bits are fetched, it contains the value that is used to
   *        calculate next value of MPC.
   * @param bits contains the MIR[26:4]. Contains several signals that control the processor in some ways. For further
   *        details see the comments of the fields or the script of Karl Stroetmann.
   * @param b contains the MIR[3:0]. Only the lowest four bits are fetched, it contains the bits that are used to define
   *        which register's value is written to the B-Bus.
   */
  public Mic1Instruction(final int addr, final BitSet bits, final Mic1BBusRegister b) {
    this.nextAddress = addr & ADDRESS_MASK;
    this.bBusSelect = b;

    int i = 0;
    this.jmpC = bits.get(i++); // fetch bit number 0 from the BitSet
    this.jmpN = bits.get(i++); // fetch bit number 1 from the BitSet
    this.jmpZ = bits.get(i++); // fetch bit number 2 from the BitSet
    this.sll8 = bits.get(i++); // fetch bit number 3 from the BitSet
    this.sra1 = bits.get(i++); // fetch bit number 4 from the BitSet
    this.f0 = bits.get(i++); // fetch bit number 5 from the BitSet
    this.f1 = bits.get(i++); // fetch bit number 6 from the BitSet
    this.enableA = bits.get(i++); // fetch bit number 7 from the BitSet
    this.enableB = bits.get(i++); // fetch bit number 8 from the BitSet
    this.invertA = bits.get(i++); // fetch bit number 9 from the BitSet
    this.increment = bits.get(i++); // fetch bit number 10 from the BitSet
    this.cBusSet.setH(bits.get(i++)); // fetch bit number 11 from the BitSet
    this.cBusSet.setOpc(bits.get(i++)); // fetch bit number 12 from the BitSet
    this.cBusSet.setTos(bits.get(i++)); // fetch bit number 13 from the BitSet
    this.cBusSet.setCpp(bits.get(i++)); // fetch bit number 14 from the BitSet
    this.cBusSet.setLv(bits.get(i++)); // fetch bit number 15 from the BitSet
    this.cBusSet.setSp(bits.get(i++)); // fetch bit number 16 from the BitSet
    this.cBusSet.setPc(bits.get(i++)); // fetch bit number 17 from the BitSet
    this.cBusSet.setMdr(bits.get(i++)); // fetch bit number 18 from the BitSet
    this.cBusSet.setMar(bits.get(i++)); // fetch bit number 19 from the BitSet
    this.mem.setWrite(bits.get(i++)); // fetch bit number 20 from the BitSet
    this.mem.setRead(bits.get(i++)); // fetch bit number 21 from the BitSet
    this.mem.setFetch(bits.get(i++)); // fetch bit number 22 from the BitSet
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
    if (this.jmpN || this.jmpZ) {
      final char c;
      if (this.jmpN) {
        c = 'N';
      } else {
        c = 'Z';
      }
      s.insert(0, c + "=");
      s.append(";if (").append(c).append(") goto 0x");
      s.append(convertIntToHex(this.nextAddress | HIGHEST_BIT_OF_ADDRESS)).append("; else goto 0x");
      s.append(convertIntToHex(this.nextAddress));
    } else if (this.jmpC) {
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
    return Integer.toHexString(i).toUpperCase();
  }

  /**
   * Decodes the signals that belong to the memory: write, read and fetch. It appends a text to the given
   * {@link StringBuilder}.
   * 
   * @since Date: Nov 10, 2011
   * @param s the {@link StringBuilder} to append the text to.
   */
  void decodeMemoryBits(final StringBuilder s) {
    // decode the mem bits
    if (this.mem.isWrite()) {
      s.append(";wr");
    }
    if (this.mem.isRead()) {
      s.append(";rd");
    }
    if (this.mem.isFetch()) {
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
    if (this.sra1) {
      s.append(">>1");
    }
    if (this.sll8) {
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
    if (!this.f0 && !this.f1) { // a AND b
      decodeALUAnd(s, a, b);
    } else if (!this.f0 && this.f1) { // a OR b
      decodeALUOr(s, a, b);
    } else if (this.f0 && !this.f1) { // NOT b
      decodeALUNotB(s, b);
    } else if (this.f0 && this.f1) { // a + b
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
    if (this.enableA) {
      if (this.invertA) {
        if (this.enableB) {
          s.append(b);
        }
        s.append("-").append(a);
        if (!this.increment) {
          s.append("-1");
        }
      } else {
        s.append(a);
        if (this.enableB) {
          s.append("+").append(b);
        }
        if (this.increment) {
          s.append("+1");
        }
      }
    } else { // a not enabled
      if (this.invertA && !this.increment) {
        if (this.enableB) {
          s.append(b);
        }
        s.append("-1");
      } else if (!this.invertA && this.increment) {
        if (this.enableB) {
          s.append(b).append("+");
        }
        s.append("1");
      } else {
        if (this.enableB) {
          s.append(b);
        } else {
          s.append("0");
        }
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
    if (this.enableB) {
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
    if (this.enableA) {
      if (this.enableB) {
        if (this.invertA) {
          s.append("(" + TXT_NOT + " ").append(a).append(") OR ").append(b);
        } else {
          s.append(a).append(" OR ").append(b);
        }
      } else {
        if (this.invertA) {
          s.append(TXT_NOT + " ").append(a);
        } else {
          s.append(a);
        }
      }
    } else { // a is not enabled
      if (this.invertA) {
        s.append("-1");
      } else {
        if (this.enableB) {
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
    if (this.enableB) {
      if (this.enableA) {
        if (this.invertA) {
          s.append("(" + TXT_NOT + " ").append(a).append(")");
        } else {
          s.append(a);
        }
        s.append(" AND ").append(b);
      } else if (this.invertA) {
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
    switch (this.bBusSelect) {
      case MDR:
        return "MDR";
      case PC:
        return "PC";
      case MBR:
        return "MBR";
      case MBRU:
        return "MBRU";
      case SP:
        return "SP";
      case LV:
        return "LV";
      case CPP:
        return "CPP";
      case TOS:
        return "TOS";
      case OPC:
        return "OPC";
      default:
        return unknown;
    }
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
    if (this.cBusSet.isH()) {
      s.append("H=");
    }
    if (this.cBusSet.isOpc()) {
      s.append("OPC=");
    }
    if (this.cBusSet.isTos()) {
      s.append("TOS=");
    }
    if (this.cBusSet.isCpp()) {
      s.append("CPP=");
    }
    if (this.cBusSet.isLv()) {
      s.append("LV=");
    }
    if (this.cBusSet.isSp()) {
      s.append("SP=");
    }
    if (this.cBusSet.isPc()) {
      s.append("PC=");
    }
    if (this.cBusSet.isMdr()) {
      s.append("MDR=");
    }
    if (this.cBusSet.isMar()) {
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
    result = prime * result + Boolean.valueOf(this.enableA).hashCode();
    result = prime * result + Boolean.valueOf(this.enableB).hashCode();
    result = prime * result + Boolean.valueOf(this.f0).hashCode();
    result = prime * result + Boolean.valueOf(this.f1).hashCode();
    result = prime * result + Boolean.valueOf(this.increment).hashCode();
    result = prime * result + Boolean.valueOf(this.invertA).hashCode();
    result = prime * result + Boolean.valueOf(this.jmpC).hashCode();
    result = prime * result + Boolean.valueOf(this.jmpN).hashCode();
    result = prime * result + Boolean.valueOf(this.jmpZ).hashCode();
    result = prime * result + this.nextAddress;
    result = prime * result + Boolean.valueOf(this.sll8).hashCode();
    result = prime * result + Boolean.valueOf(this.sra1).hashCode();
    result = prime * result + this.cBusSet.hashCode();
    result = prime * result + this.mem.hashCode();
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
    if (this.enableA != other.enableA) {
      return false;
    }
    if (this.enableB != other.enableB) {
      return false;
    }
    if (this.f0 != other.f0) {
      return false;
    }
    if (this.f1 != other.f1) {
      return false;
    }
    if (this.increment != other.increment) {
      return false;
    }
    if (this.invertA != other.invertA) {
      return false;
    }
    if (this.jmpC != other.jmpC) {
      return false;
    }
    if (this.jmpN != other.jmpN) {
      return false;
    }
    if (this.jmpZ != other.jmpZ) {
      return false;
    }
    if (this.nextAddress != other.nextAddress) {
      return false;
    }
    if (this.sll8 != other.sll8) {
      return false;
    }
    if (this.sra1 != other.sra1) {
      return false;
    }
    if (!this.cBusSet.equals(other.cBusSet)) {
      return false;
    }
    return this.mem.equals(other.mem);
  }

}
