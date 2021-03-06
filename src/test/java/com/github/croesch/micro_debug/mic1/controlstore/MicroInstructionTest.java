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
package com.github.croesch.micro_debug.mic1.controlstore;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

import com.github.croesch.micro_debug.DefaultTestCase;
import com.github.croesch.micro_debug.mic1.register.Register;

/**
 * Provides test cases for {@link MicroInstruction}.
 * 
 * @author croesch
 * @since Date: Nov 10, 2011
 */
public class MicroInstructionTest extends DefaultTestCase {

  private MicroInstruction instruction;

  @Override
  protected void setUpDetails() {
    this.instruction = new MicroInstruction(0,
                                            new JMPSignalSet(),
                                            new ALUSignalSet(),
                                            new CBusSignalSet(),
                                            new MemorySignalSet(),
                                            null);
  }

  @Test
  public void testHashCodeAndEqualsObject() {
    printlnMethodName();

    assertThat(this.instruction).isNotEqualTo(null);
    assertThat(this.instruction).isNotEqualTo("...");
    assertThat(this.instruction).isEqualTo(this.instruction);
    assertThat(this.instruction.hashCode()).isEqualTo(this.instruction.hashCode());

    int addr = 0;
    Register b = null;

    final MemorySignalSet memSet = new MemorySignalSet();
    final CBusSignalSet cBusSet = new CBusSignalSet();
    final ALUSignalSet aluSet = new ALUSignalSet();
    final JMPSignalSet jmpSet = new JMPSignalSet();
    MicroInstruction other = new MicroInstruction(addr, jmpSet, aluSet, cBusSet, memSet, b);
    assertThat(this.instruction).isEqualTo(other);
    assertThat(this.instruction.hashCode()).isEqualTo(this.instruction.hashCode());

    b = Register.OPC;
    other = compareInstructionToOther(addr, b, memSet, cBusSet, aluSet, jmpSet, other);

    addr = 17;
    other = compareInstructionToOther(addr, b, memSet, cBusSet, aluSet, jmpSet, other);
  }

  @Test
  public void testHashCodeAndEqualsObject_BBus() {
    printMethodName();

    final int addr = 0;
    final MemorySignalSet memSet = new MemorySignalSet();
    final CBusSignalSet cBusSet = new CBusSignalSet();
    final ALUSignalSet aluSet = new ALUSignalSet();
    final JMPSignalSet jmpSet = new JMPSignalSet();
    MicroInstruction other = new MicroInstruction(addr, jmpSet, aluSet, cBusSet, memSet, null);

    for (final Register b : Register.values()) {
      other = compareInstructionToOther(addr, b, memSet, cBusSet, aluSet, jmpSet, other);
      printStep();
    }

    printEndOfMethod();
  }

  @Test
  public void testHashCodeAndEqualsObject_Memory() {
    printlnMethodName();

    final int addr = 0;
    final Register b = null;
    final MemorySignalSet memSet = new MemorySignalSet();
    final CBusSignalSet cBusSet = new CBusSignalSet();
    final ALUSignalSet aluSet = new ALUSignalSet();
    final JMPSignalSet jmpSet = new JMPSignalSet();
    MicroInstruction other = new MicroInstruction(addr, jmpSet, aluSet, cBusSet, memSet, b);

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
    printlnMethodName();

    final int addr = 0;
    final Register b = null;
    final MemorySignalSet memSet = new MemorySignalSet();
    final CBusSignalSet cBusSet = new CBusSignalSet();
    final ALUSignalSet aluSet = new ALUSignalSet();
    final JMPSignalSet jmpSet = new JMPSignalSet();
    MicroInstruction other = new MicroInstruction(addr, jmpSet, aluSet, cBusSet, memSet, b);

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
    printlnMethodName();

    final int addr = 0;
    final Register b = null;
    final MemorySignalSet memSet = new MemorySignalSet();
    final CBusSignalSet cBusSet = new CBusSignalSet();
    final ALUSignalSet aluSet = new ALUSignalSet();
    final JMPSignalSet jmpSet = new JMPSignalSet();
    MicroInstruction other = new MicroInstruction(addr, jmpSet, aluSet, cBusSet, memSet, b);

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
    printlnMethodName();

    final int addr = 0;
    final Register b = null;
    final MemorySignalSet memSet = new MemorySignalSet();
    final CBusSignalSet cBusSet = new CBusSignalSet();
    final ALUSignalSet aluSet = new ALUSignalSet();
    final JMPSignalSet jmpSet = new JMPSignalSet();
    MicroInstruction other = new MicroInstruction(addr, jmpSet, aluSet, cBusSet, memSet, b);

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
    printlnMethodName();

    final JMPSignalSet jmpSignals = this.instruction.getJmpSignals();
    assertThat(this.instruction.getJmpSignals().isJmpN()).isFalse();

    jmpSignals.setJmpN(true);
    assertThat(this.instruction.getJmpSignals().isJmpN()).isFalse();
  }

  @Test
  public void testGetAluSignals() {
    printlnMethodName();

    final ALUSignalSet aluSignals = this.instruction.getAluSignals();
    assertThat(this.instruction.getAluSignals().isEnA()).isFalse();

    aluSignals.setEnA(true);
    assertThat(this.instruction.getAluSignals().isEnA()).isFalse();
  }

  @Test
  public void testGetBBusSelect() {
    printlnMethodName();
    assertThat(this.instruction.getbBusSelect()).isNull();
  }

  @Test
  public void testGetCBusSignals() {
    printlnMethodName();

    final CBusSignalSet cBusSignals = this.instruction.getCBusSignals();
    assertThat(this.instruction.getCBusSignals().isCpp()).isFalse();

    cBusSignals.setCpp(true);
    assertThat(this.instruction.getCBusSignals().isCpp()).isFalse();
  }

  @Test
  public void testGetMemorySignals() {
    printlnMethodName();

    final MemorySignalSet memSignals = this.instruction.getMemorySignals();
    assertThat(this.instruction.getMemorySignals().isFetch()).isFalse();

    memSignals.setFetch(true);
    assertThat(this.instruction.getMemorySignals().isFetch()).isFalse();
  }

  private MicroInstruction compareInstructionToOther(final int addr,
                                                     final Register b,
                                                     final MemorySignalSet memSet,
                                                     final CBusSignalSet cBusSet,
                                                     final ALUSignalSet aluSet,
                                                     final JMPSignalSet jmpSet,
                                                     MicroInstruction other) {
    this.instruction = new MicroInstruction(addr, jmpSet, aluSet, cBusSet, memSet, b);
    assertThat(this.instruction).isNotEqualTo(other);
    assertThat(this.instruction.hashCode()).isNotEqualTo(other.hashCode());
    other = new MicroInstruction(addr, jmpSet, aluSet, cBusSet, memSet, b); // make object equal to instruction
    return other;
  }

  @Test
  public void testToString() {
    printlnMethodName();

    assertThat(this.instruction.toString()).isEqualTo("0_000_00000000_000000000_000_null");

    // set all bits..
    final MemorySignalSet memSet = new MemorySignalSet();
    memSet.setFetch(true).setRead(true).setWrite(true);
    final CBusSignalSet cBusSet = new CBusSignalSet();
    cBusSet.setCpp(true).setH(true).setLv(true).setMar(true).setMdr(true);
    cBusSet.setOpc(true).setPc(true).setSp(true).setTos(true);
    final ALUSignalSet aluSet = new ALUSignalSet();
    aluSet.setEnA(true).setEnB(true).setF0(true).setF1(true);
    aluSet.setInc(true).setInvA(true).setSLL8(true).setSRA1(true);
    final JMPSignalSet jmpSet = new JMPSignalSet();
    jmpSet.setJmpC(true).setJmpN(true).setJmpZ(true);

    this.instruction = new MicroInstruction(42, jmpSet, aluSet, cBusSet, memSet, Register.MBR);

    assertThat(this.instruction.toString()).isEqualTo("101010_111_11111111_111111111_111_MBR");
  }

  @Test
  public void testIsNopOrHalt() {
    printlnMethodName();

    final JMPSignalSet jmpSet = new JMPSignalSet();
    final ALUSignalSet aluSet = new ALUSignalSet();
    final CBusSignalSet cBusSet = new CBusSignalSet();
    final MemorySignalSet memSet = new MemorySignalSet();

    this.instruction = new MicroInstruction(144, jmpSet, aluSet, cBusSet, memSet, Register.SP);
    assertThat(this.instruction.isNopOrHalt()).isTrue();

    jmpSet.setJmpC(true);
    this.instruction = new MicroInstruction(144, jmpSet, aluSet, cBusSet, memSet, Register.SP);
    assertThat(this.instruction.isNopOrHalt()).isFalse();

    jmpSet.setJmpC(false);
    this.instruction = new MicroInstruction(144, jmpSet, aluSet, cBusSet, memSet, Register.SP);
    assertThat(this.instruction.isNopOrHalt()).isTrue();
    aluSet.setF1(true);
    this.instruction = new MicroInstruction(144, jmpSet, aluSet, cBusSet, memSet, Register.SP);
    assertThat(this.instruction.isNopOrHalt()).isFalse();

    aluSet.setF1(false);
    this.instruction = new MicroInstruction(144, jmpSet, aluSet, cBusSet, memSet, Register.SP);
    assertThat(this.instruction.isNopOrHalt()).isTrue();
    cBusSet.setLv(true);
    this.instruction = new MicroInstruction(144, jmpSet, aluSet, cBusSet, memSet, Register.SP);
    assertThat(this.instruction.isNopOrHalt()).isFalse();

    cBusSet.setLv(false);
    this.instruction = new MicroInstruction(144, jmpSet, aluSet, cBusSet, memSet, Register.SP);
    assertThat(this.instruction.isNopOrHalt()).isTrue();
    memSet.setRead(true);
    this.instruction = new MicroInstruction(144, jmpSet, aluSet, cBusSet, memSet, Register.SP);
    assertThat(this.instruction.isNopOrHalt()).isFalse();
  }
}
