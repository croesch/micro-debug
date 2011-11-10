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
  private static final String TXT_NOT = "NOT";

  private static final int B_MDR = 0;

  private static final int B_PC = 1;

  private static final int B_MBR = 2;

  private static final int B_MBRU = 3;

  private static final int B_SP = 4;

  private static final int B_LV = 5;

  private static final int B_CPP = 6;

  private static final int B_TOS = 7;

  private static final int B_OPC = 8;

  private final int nextAddress;

  private final boolean jmpC;

  private final boolean jmpN;

  private final boolean jmpZ;

  private final boolean sll8;

  private final boolean sra1;

  private final boolean f0;

  private final boolean f1;

  private final boolean enableA;

  private final boolean enableB;

  private final boolean invertA;

  private final boolean increment;

  private final boolean h;

  private final boolean opc;

  private final boolean tos;

  private final boolean cpp;

  private final boolean lv;

  private final boolean sp;

  private final boolean pc;

  private final boolean mdr;

  private final boolean mar;

  private final boolean write;

  private final boolean read;

  private final boolean fetch;

  private final int bBusSelect;

  public Mic1Instruction(final int addr, final BitSet bits, final int b) {
    this.nextAddress = addr & 0x1ff;
    this.bBusSelect = b & 0xf;

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
    this.h = bits.get(i++); // fetch bit number 11 from the BitSet
    this.opc = bits.get(i++); // fetch bit number 12 from the BitSet
    this.tos = bits.get(i++); // fetch bit number 13 from the BitSet
    this.cpp = bits.get(i++); // fetch bit number 14 from the BitSet
    this.lv = bits.get(i++); // fetch bit number 15 from the BitSet
    this.sp = bits.get(i++); // fetch bit number 16 from the BitSet
    this.pc = bits.get(i++); // fetch bit number 17 from the BitSet
    this.mdr = bits.get(i++); // fetch bit number 18 from the BitSet
    this.mar = bits.get(i++); // fetch bit number 19 from the BitSet
    this.write = bits.get(i++); // fetch bit number 20 from the BitSet
    this.read = bits.get(i++); // fetch bit number 21 from the BitSet
    this.fetch = bits.get(i++); // fetch bit number 22 from the BitSet
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

    if (s.toString().equals("0")) {
      s = new StringBuilder("nop");
    } else if (s.toString().startsWith("0;")) {
      s = new StringBuilder(s.toString().substring(2));
    }

    return s.toString();
  }

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
      s.append(convertIntToHex(this.nextAddress | 256)).append("; else goto 0x");
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
    if (this.write) {
      s.append(";wr");
    }
    if (this.read) {
      s.append(";rd");
    }
    if (this.fetch) {
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

  String decodeBBusBits() {
    // decode the b-bus bits
    switch (this.bBusSelect) {
      case B_MDR:
        return "MDR";
      case B_PC:
        return "PC";
      case B_MBR:
        return "MBR";
      case B_MBRU:
        return "MBRU";
      case B_SP:
        return "SP";
      case B_LV:
        return "LV";
      case B_CPP:
        return "CPP";
      case B_TOS:
        return "TOS";
      case B_OPC:
        return "OPC";
        // are the rest of these really no-ops?
      default:
        return "???";
    }
  }

  void decodeCBusBits(final StringBuilder s) {
    // decode the C-bus bits
    if (this.h) {
      s.append("H=");
    }
    if (this.opc) {
      s.append("OPC=");
    }
    if (this.tos) {
      s.append("TOS=");
    }
    if (this.cpp) {
      s.append("CPP=");
    }
    if (this.lv) {
      s.append("LV=");
    }
    if (this.sp) {
      s.append("SP=");
    }
    if (this.pc) {
      s.append("PC=");
    }
    if (this.mdr) {
      s.append("MDR=");
    }
    if (this.mar) {
      s.append("MAR=");
    }
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + this.bBusSelect;
    result = prime * result + Boolean.valueOf(this.cpp).hashCode();
    result = prime * result + Boolean.valueOf(this.enableA).hashCode();
    result = prime * result + Boolean.valueOf(this.enableB).hashCode();
    result = prime * result + Boolean.valueOf(this.f0).hashCode();
    result = prime * result + Boolean.valueOf(this.f1).hashCode();
    result = prime * result + Boolean.valueOf(this.fetch).hashCode();
    result = prime * result + Boolean.valueOf(this.h).hashCode();
    result = prime * result + Boolean.valueOf(this.increment).hashCode();
    result = prime * result + Boolean.valueOf(this.invertA).hashCode();
    result = prime * result + Boolean.valueOf(this.jmpC).hashCode();
    result = prime * result + Boolean.valueOf(this.jmpN).hashCode();
    result = prime * result + Boolean.valueOf(this.jmpZ).hashCode();
    result = prime * result + Boolean.valueOf(this.lv).hashCode();
    result = prime * result + Boolean.valueOf(this.mar).hashCode();
    result = prime * result + Boolean.valueOf(this.mdr).hashCode();
    result = prime * result + this.nextAddress;
    result = prime * result + Boolean.valueOf(this.opc).hashCode();
    result = prime * result + Boolean.valueOf(this.pc).hashCode();
    result = prime * result + Boolean.valueOf(this.read).hashCode();
    result = prime * result + Boolean.valueOf(this.sll8).hashCode();
    result = prime * result + Boolean.valueOf(this.sp).hashCode();
    result = prime * result + Boolean.valueOf(this.sra1).hashCode();
    result = prime * result + Boolean.valueOf(this.tos).hashCode();
    result = prime * result + Boolean.valueOf(this.write).hashCode();
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
    if (this.cpp != other.cpp) {
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
    if (this.fetch != other.fetch) {
      return false;
    }
    if (this.h != other.h) {
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
    if (this.lv != other.lv) {
      return false;
    }
    if (this.mar != other.mar) {
      return false;
    }
    if (this.mdr != other.mdr) {
      return false;
    }
    if (this.nextAddress != other.nextAddress) {
      return false;
    }
    if (this.opc != other.opc) {
      return false;
    }
    if (this.pc != other.pc) {
      return false;
    }
    if (this.read != other.read) {
      return false;
    }
    if (this.sll8 != other.sll8) {
      return false;
    }
    if (this.sp != other.sp) {
      return false;
    }
    if (this.sra1 != other.sra1) {
      return false;
    }
    if (this.tos != other.tos) {
      return false;
    }
    if (this.write != other.write) {
      return false;
    }
    return true;
  }

}