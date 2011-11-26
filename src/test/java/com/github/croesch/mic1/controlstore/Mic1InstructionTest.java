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

import org.junit.Before;
import org.junit.Test;

import com.github.croesch.TestUtil;
import com.github.croesch.mic1.register.Register;

/**
 * Provides test cases for {@link Mic1Instruction}.
 * 
 * @author croesch
 * @since Date: Nov 10, 2011
 */
public class Mic1InstructionTest {

  private Mic1Instruction instruction;

  @Before
  public void setUp() {
    this.instruction = new Mic1Instruction(0,
                                           new Mic1JMPSignalSet(),
                                           new Mic1ALUSignalSet(),
                                           new Mic1CBusSignalSet(),
                                           new Mic1MemorySignalSet(),
                                           null);
  }

  @Test
  public void testHashCodeAndEqualsObject() {
    assertThat(this.instruction).isNotEqualTo(null);
    assertThat(this.instruction).isNotEqualTo("...");
    assertThat(this.instruction).isEqualTo(this.instruction);
    assertThat(this.instruction.hashCode()).isEqualTo(this.instruction.hashCode());

    int addr = 0;
    Register b = null;

    final Mic1MemorySignalSet memSet = new Mic1MemorySignalSet();
    final Mic1CBusSignalSet cBusSet = new Mic1CBusSignalSet();
    final Mic1ALUSignalSet aluSet = new Mic1ALUSignalSet();
    final Mic1JMPSignalSet jmpSet = new Mic1JMPSignalSet();
    Mic1Instruction other = new Mic1Instruction(addr, jmpSet, aluSet, cBusSet, memSet, b);
    assertThat(this.instruction).isEqualTo(other);
    assertThat(this.instruction.hashCode()).isEqualTo(this.instruction.hashCode());

    b = Register.OPC;
    other = compareInstructionToOther(addr, b, memSet, cBusSet, aluSet, jmpSet, other);

    addr = 17;
    other = compareInstructionToOther(addr, b, memSet, cBusSet, aluSet, jmpSet, other);
  }

  @Test
  public void testHashCodeAndEqualsObject_BBus() {
    TestUtil.printMethodName();

    final int addr = 0;
    final Mic1MemorySignalSet memSet = new Mic1MemorySignalSet();
    final Mic1CBusSignalSet cBusSet = new Mic1CBusSignalSet();
    final Mic1ALUSignalSet aluSet = new Mic1ALUSignalSet();
    final Mic1JMPSignalSet jmpSet = new Mic1JMPSignalSet();
    Mic1Instruction other = new Mic1Instruction(addr, jmpSet, aluSet, cBusSet, memSet, null);

    for (final Register b : Register.values()) {
      other = compareInstructionToOther(addr, b, memSet, cBusSet, aluSet, jmpSet, other);
      TestUtil.printStep();
    }

    TestUtil.printEndOfMethod();
  }

  @Test
  public void testHashCodeAndEqualsObject_Memory() {
    final int addr = 0;
    final Register b = null;
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
    final Register b = null;
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
    final Register b = null;
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
    final Register b = null;
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
  }

  @Test
  public void testGetJmpSignals() {
    final Mic1JMPSignalSet jmpSignals = this.instruction.getJmpSignals();
    assertThat(this.instruction.getJmpSignals().isJmpN()).isFalse();

    jmpSignals.setJmpN(true);
    assertThat(this.instruction.getJmpSignals().isJmpN()).isFalse();
  }

  @Test
  public void testGetAluSignals() {
    final Mic1ALUSignalSet aluSignals = this.instruction.getAluSignals();
    assertThat(this.instruction.getAluSignals().isEnA()).isFalse();

    aluSignals.setEnA(true);
    assertThat(this.instruction.getAluSignals().isEnA()).isFalse();
  }

  @Test
  public void testGetCBusSignals() {
    final Mic1CBusSignalSet cBusSignals = this.instruction.getCBusSignals();
    assertThat(this.instruction.getCBusSignals().isCpp()).isFalse();

    cBusSignals.setCpp(true);
    assertThat(this.instruction.getCBusSignals().isCpp()).isFalse();
  }

  @Test
  public void testGetMemorySignals() {
    final Mic1MemorySignalSet memSignals = this.instruction.getMemorySignals();
    assertThat(this.instruction.getMemorySignals().isFetch()).isFalse();

    memSignals.setFetch(true);
    assertThat(this.instruction.getMemorySignals().isFetch()).isFalse();
  }

  private Mic1Instruction compareInstructionToOther(final int addr,
                                                    final Register b,
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

  @Test
  public void testToString() {
    assertThat(this.instruction.toString()).isEqualTo("0_000_00000000_000000000_000_null");

    // set all bits..
    final Mic1MemorySignalSet memSet = new Mic1MemorySignalSet();
    memSet.setFetch(true).setRead(true).setWrite(true);
    final Mic1CBusSignalSet cBusSet = new Mic1CBusSignalSet();
    cBusSet.setCpp(true).setH(true).setLv(true).setMar(true).setMdr(true);
    cBusSet.setOpc(true).setPc(true).setSp(true).setTos(true);
    final Mic1ALUSignalSet aluSet = new Mic1ALUSignalSet();
    aluSet.setEnA(true).setEnB(true).setF0(true).setF1(true);
    aluSet.setInc(true).setInvA(true).setSLL8(true).setSRA1(true);
    final Mic1JMPSignalSet jmpSet = new Mic1JMPSignalSet();
    jmpSet.setJmpC(true).setJmpN(true).setJmpZ(true);

    this.instruction = new Mic1Instruction(42, jmpSet, aluSet, cBusSet, memSet, Register.MBR);

    assertThat(this.instruction.toString()).isEqualTo("101010_111_11111111_111111111_111_MBR");
  }
}
