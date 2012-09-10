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
package com.github.croesch.micro_debug.debug;

import static org.fest.assertions.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.junit.Test;

import com.github.croesch.micro_debug.DefaultTestCase;
import com.github.croesch.micro_debug.datatypes.DebugMode;
import com.github.croesch.micro_debug.i18n.Text;
import com.github.croesch.micro_debug.mic1.controlstore.MicroInstruction;
import com.github.croesch.micro_debug.mic1.controlstore.MicroInstructionReader;
import com.github.croesch.micro_debug.mic1.register.Register;

/**
 * Provides test cases for {@link RegisterWriteBreakpoint}.
 * 
 * @author croesch
 * @since Date: Apr 11, 2012
 */
public class RegisterWriteBreakpointTest extends DefaultTestCase {

  @Test
  public void testHashCode_Equals() {
    final RegisterWriteBreakpoint rbp1 = new RegisterWriteBreakpoint(Register.H);
    final RegisterWriteBreakpoint rbp1Copy = new RegisterWriteBreakpoint(Register.H);
    final RegisterWriteBreakpoint rbp3 = new RegisterWriteBreakpoint(Register.CPP);
    final RegisterWriteBreakpoint rbp4 = new RegisterWriteBreakpoint(Register.LV);

    assertThat(rbp1).isNotEqualTo("rbp1");
    assertThat(rbp1).isNotEqualTo(null);

    assertThat(rbp1).isEqualTo(rbp1);
    assertThat(rbp1).isEqualTo(rbp1Copy);
    assertThat(rbp1).isNotEqualTo(rbp3);
    assertThat(rbp1).isNotEqualTo(rbp4);

    assertThat(rbp1.hashCode()).isEqualTo(rbp1.hashCode());
    assertThat(rbp1.hashCode()).isEqualTo(rbp1Copy.hashCode());
    assertThat(rbp1.hashCode()).isNotEqualTo(rbp3.hashCode());
    assertThat(rbp1.hashCode()).isNotEqualTo(rbp4.hashCode());

    assertThat(rbp1Copy).isEqualTo(rbp1);
    assertThat(rbp3).isNotEqualTo(rbp1);
    assertThat(rbp4).isNotEqualTo(rbp1);

    assertThat(rbp1Copy.hashCode()).isEqualTo(rbp1.hashCode());
    assertThat(rbp3.hashCode()).isNotEqualTo(rbp1.hashCode());
    assertThat(rbp4.hashCode()).isNotEqualTo(rbp1.hashCode());
  }

  @Test
  public void testShouldBreak() throws IOException {
    final RegisterWriteBreakpoint rbp = new RegisterWriteBreakpoint(Register.H);

    Register.H.setValue(12);
    // instruction writes all registers
    final MicroInstruction in = MicroInstructionReader.read(new ByteArrayInputStream(new byte[] { 0,
                                                                                                 0,
                                                                                                 (byte) 0xFF,
                                                                                                 (byte) 0xFF,
                                                                                                 0 }));
    assertThat(rbp.shouldBreak(DebugMode.BOTH, 0, 0, in, null)).isFalse();
    assertThat(rbp.shouldBreak(DebugMode.BOTH, 0, 0, null, in)).isTrue();
    assertThat(rbp.shouldBreak(DebugMode.MICRO, 0, 0, in, null)).isFalse();
    assertThat(rbp.shouldBreak(DebugMode.MICRO, 0, 0, null, in)).isTrue();
    assertThat(rbp.shouldBreak(DebugMode.MACRO, 0, 0, in, null)).isFalse();
    assertThat(rbp.shouldBreak(DebugMode.MACRO, 0, 0, null, in)).isFalse();
  }

  @Test
  public void testIsConditionMet() throws IOException {
    final RegisterWriteBreakpoint rbp = new RegisterWriteBreakpoint(Register.H);

    Register.H.setValue(12);
    assertThat(rbp.isConditionMet(0, 0, null, null)).isFalse();

    // instruction writes all registers
    MicroInstruction in = MicroInstructionReader.read(new ByteArrayInputStream(new byte[] { 0,
                                                                                           0,
                                                                                           (byte) 0xFF,
                                                                                           (byte) 0xFF,
                                                                                           0 }));
    assertThat(rbp.isConditionMet(0, 0, in, null)).isFalse();
    assertThat(rbp.isConditionMet(0, 0, null, in)).isTrue();
    assertThat(new RegisterWriteBreakpoint(Register.CPP).isConditionMet(0, 0, null, in)).isTrue();
    assertThat(new RegisterWriteBreakpoint(Register.H).isConditionMet(0, 0, null, in)).isTrue();
    assertThat(new RegisterWriteBreakpoint(Register.LV).isConditionMet(0, 0, null, in)).isTrue();
    assertThat(new RegisterWriteBreakpoint(Register.MAR).isConditionMet(0, 0, null, in)).isTrue();
    assertThat(new RegisterWriteBreakpoint(Register.MBR).isConditionMet(0, 0, in, null)).isTrue();
    assertThat(new RegisterWriteBreakpoint(Register.MBRU).isConditionMet(0, 0, in, null)).isTrue();
    assertThat(new RegisterWriteBreakpoint(Register.MDR).isConditionMet(0, 0, in, null)).isTrue();
    assertThat(new RegisterWriteBreakpoint(Register.OPC).isConditionMet(0, 0, null, in)).isTrue();
    assertThat(new RegisterWriteBreakpoint(Register.PC).isConditionMet(0, 0, null, in)).isTrue();
    assertThat(new RegisterWriteBreakpoint(Register.SP).isConditionMet(0, 0, null, in)).isTrue();
    assertThat(new RegisterWriteBreakpoint(Register.TOS).isConditionMet(0, 0, null, in)).isTrue();

    assertThat(new RegisterWriteBreakpoint(Register.CPP).isConditionMet(0, 0, in, null)).isFalse();
    assertThat(new RegisterWriteBreakpoint(Register.H).isConditionMet(0, 0, in, null)).isFalse();
    assertThat(new RegisterWriteBreakpoint(Register.LV).isConditionMet(0, 0, in, null)).isFalse();
    assertThat(new RegisterWriteBreakpoint(Register.MAR).isConditionMet(0, 0, in, null)).isFalse();
    assertThat(new RegisterWriteBreakpoint(Register.MBR).isConditionMet(0, 0, null, in)).isFalse();
    assertThat(new RegisterWriteBreakpoint(Register.MBRU).isConditionMet(0, 0, null, in)).isFalse();
    assertThat(new RegisterWriteBreakpoint(Register.MDR).isConditionMet(0, 0, null, in)).isTrue();
    assertThat(new RegisterWriteBreakpoint(Register.OPC).isConditionMet(0, 0, in, null)).isFalse();
    assertThat(new RegisterWriteBreakpoint(Register.PC).isConditionMet(0, 0, in, null)).isFalse();
    assertThat(new RegisterWriteBreakpoint(Register.SP).isConditionMet(0, 0, in, null)).isFalse();
    assertThat(new RegisterWriteBreakpoint(Register.TOS).isConditionMet(0, 0, in, null)).isFalse();

    // instruction writes no register
    in = MicroInstructionReader.read(new ByteArrayInputStream(new byte[] { 0, 0, 0, 0, 0 }));
    assertThat(new RegisterWriteBreakpoint(Register.CPP).isConditionMet(0, 0, in, in)).isFalse();
    assertThat(new RegisterWriteBreakpoint(Register.H).isConditionMet(0, 0, in, in)).isFalse();
    assertThat(new RegisterWriteBreakpoint(Register.LV).isConditionMet(0, 0, in, in)).isFalse();
    assertThat(new RegisterWriteBreakpoint(Register.MAR).isConditionMet(0, 0, in, in)).isFalse();
    assertThat(new RegisterWriteBreakpoint(Register.MBR).isConditionMet(0, 0, in, in)).isFalse();
    assertThat(new RegisterWriteBreakpoint(Register.MBRU).isConditionMet(0, 0, in, in)).isFalse();
    assertThat(new RegisterWriteBreakpoint(Register.MDR).isConditionMet(0, 0, in, in)).isFalse();
    assertThat(new RegisterWriteBreakpoint(Register.OPC).isConditionMet(0, 0, in, in)).isFalse();
    assertThat(new RegisterWriteBreakpoint(Register.PC).isConditionMet(0, 0, in, in)).isFalse();
    assertThat(new RegisterWriteBreakpoint(Register.SP).isConditionMet(0, 0, in, in)).isFalse();
    assertThat(new RegisterWriteBreakpoint(Register.TOS).isConditionMet(0, 0, in, in)).isFalse();

    // instruction writes only LV
    in = MicroInstructionReader.read(new ByteArrayInputStream(new byte[] { 0, 0, 0, (byte) 0x80, 0 }));
    assertThat(new RegisterWriteBreakpoint(Register.CPP).isConditionMet(0, 0, in, in)).isFalse();
    assertThat(new RegisterWriteBreakpoint(Register.H).isConditionMet(0, 0, in, in)).isFalse();
    assertThat(new RegisterWriteBreakpoint(Register.LV).isConditionMet(0, 0, in, in)).isTrue();
    assertThat(new RegisterWriteBreakpoint(Register.LV).isConditionMet(0, 0, null, in)).isTrue();
    assertThat(new RegisterWriteBreakpoint(Register.LV).isConditionMet(0, 0, in, null)).isFalse();
    assertThat(new RegisterWriteBreakpoint(Register.LV).isConditionMet(0, 0, null, null)).isFalse();
    assertThat(new RegisterWriteBreakpoint(Register.MAR).isConditionMet(0, 0, in, in)).isFalse();
    assertThat(new RegisterWriteBreakpoint(Register.MBR).isConditionMet(0, 0, in, in)).isFalse();
    assertThat(new RegisterWriteBreakpoint(Register.MBRU).isConditionMet(0, 0, in, in)).isFalse();
    assertThat(new RegisterWriteBreakpoint(Register.MDR).isConditionMet(0, 0, in, in)).isFalse();
    assertThat(new RegisterWriteBreakpoint(Register.OPC).isConditionMet(0, 0, in, in)).isFalse();
    assertThat(new RegisterWriteBreakpoint(Register.PC).isConditionMet(0, 0, in, in)).isFalse();
    assertThat(new RegisterWriteBreakpoint(Register.SP).isConditionMet(0, 0, in, in)).isFalse();
    assertThat(new RegisterWriteBreakpoint(Register.TOS).isConditionMet(0, 0, in, in)).isFalse();

    // instruction writes only MDR
    in = MicroInstructionReader.read(new ByteArrayInputStream(new byte[] { 0, 0, 0, (byte) 0x10, 0 }));
    assertThat(new RegisterWriteBreakpoint(Register.CPP).isConditionMet(0, 0, in, in)).isFalse();
    assertThat(new RegisterWriteBreakpoint(Register.H).isConditionMet(0, 0, in, in)).isFalse();
    assertThat(new RegisterWriteBreakpoint(Register.LV).isConditionMet(0, 0, in, in)).isFalse();
    assertThat(new RegisterWriteBreakpoint(Register.MAR).isConditionMet(0, 0, in, in)).isFalse();
    assertThat(new RegisterWriteBreakpoint(Register.MBR).isConditionMet(0, 0, in, in)).isFalse();
    assertThat(new RegisterWriteBreakpoint(Register.MBRU).isConditionMet(0, 0, in, in)).isFalse();
    assertThat(new RegisterWriteBreakpoint(Register.MDR).isConditionMet(0, 0, in, in)).isTrue();
    assertThat(new RegisterWriteBreakpoint(Register.MDR).isConditionMet(0, 0, in, null)).isFalse();
    assertThat(new RegisterWriteBreakpoint(Register.MDR).isConditionMet(0, 0, null, in)).isTrue();
    assertThat(new RegisterWriteBreakpoint(Register.MDR).isConditionMet(0, 0, null, null)).isFalse();
    assertThat(new RegisterWriteBreakpoint(Register.OPC).isConditionMet(0, 0, in, in)).isFalse();
    assertThat(new RegisterWriteBreakpoint(Register.PC).isConditionMet(0, 0, in, in)).isFalse();
    assertThat(new RegisterWriteBreakpoint(Register.SP).isConditionMet(0, 0, in, in)).isFalse();
    assertThat(new RegisterWriteBreakpoint(Register.TOS).isConditionMet(0, 0, in, in)).isFalse();

    // instruction writes only MDR - via read
    in = MicroInstructionReader.read(new ByteArrayInputStream(new byte[] { 0, 0, 0, (byte) 0x02, 0 }));
    assertThat(new RegisterWriteBreakpoint(Register.CPP).isConditionMet(0, 0, in, in)).isFalse();
    assertThat(new RegisterWriteBreakpoint(Register.H).isConditionMet(0, 0, in, in)).isFalse();
    assertThat(new RegisterWriteBreakpoint(Register.LV).isConditionMet(0, 0, in, in)).isFalse();
    assertThat(new RegisterWriteBreakpoint(Register.MAR).isConditionMet(0, 0, in, in)).isFalse();
    assertThat(new RegisterWriteBreakpoint(Register.MBR).isConditionMet(0, 0, in, in)).isFalse();
    assertThat(new RegisterWriteBreakpoint(Register.MBRU).isConditionMet(0, 0, in, in)).isFalse();
    assertThat(new RegisterWriteBreakpoint(Register.MDR).isConditionMet(0, 0, in, in)).isTrue();
    assertThat(new RegisterWriteBreakpoint(Register.MDR).isConditionMet(0, 0, in, null)).isTrue();
    assertThat(new RegisterWriteBreakpoint(Register.MDR).isConditionMet(0, 0, null, in)).isFalse();
    assertThat(new RegisterWriteBreakpoint(Register.MDR).isConditionMet(0, 0, null, null)).isFalse();
    assertThat(new RegisterWriteBreakpoint(Register.OPC).isConditionMet(0, 0, in, in)).isFalse();
    assertThat(new RegisterWriteBreakpoint(Register.PC).isConditionMet(0, 0, in, in)).isFalse();
    assertThat(new RegisterWriteBreakpoint(Register.SP).isConditionMet(0, 0, in, in)).isFalse();
    assertThat(new RegisterWriteBreakpoint(Register.TOS).isConditionMet(0, 0, in, in)).isFalse();

    // instruction writes only H
    in = MicroInstructionReader.read(new ByteArrayInputStream(new byte[] { 0, 0, (byte) 0x08, 0, 0 }));
    assertThat(new RegisterWriteBreakpoint(Register.H).isConditionMet(0, 0, null, in)).isTrue();

    // instruction writes only OPC
    in = MicroInstructionReader.read(new ByteArrayInputStream(new byte[] { 0, 0, (byte) 0x04, 0, 0 }));
    assertThat(new RegisterWriteBreakpoint(Register.OPC).isConditionMet(0, 0, null, in)).isTrue();

    // instruction writes only TOS
    in = MicroInstructionReader.read(new ByteArrayInputStream(new byte[] { 0, 0, (byte) 0x02, 0, 0 }));
    assertThat(new RegisterWriteBreakpoint(Register.TOS).isConditionMet(0, 0, null, in)).isTrue();

    // instruction writes only CPP
    in = MicroInstructionReader.read(new ByteArrayInputStream(new byte[] { 0, 0, (byte) 0x01, 0, 0 }));
    assertThat(new RegisterWriteBreakpoint(Register.CPP).isConditionMet(0, 0, null, in)).isTrue();

    // instruction writes only LV
    in = MicroInstructionReader.read(new ByteArrayInputStream(new byte[] { 0, 0, 0, (byte) 0x80, 0 }));
    assertThat(new RegisterWriteBreakpoint(Register.LV).isConditionMet(0, 0, null, in)).isTrue();

    // instruction writes only SP
    in = MicroInstructionReader.read(new ByteArrayInputStream(new byte[] { 0, 0, 0, (byte) 0x40, 0 }));
    assertThat(new RegisterWriteBreakpoint(Register.SP).isConditionMet(0, 0, null, in)).isTrue();

    // instruction writes only PC
    in = MicroInstructionReader.read(new ByteArrayInputStream(new byte[] { 0, 0, 0, (byte) 0x20, 0 }));
    assertThat(new RegisterWriteBreakpoint(Register.PC).isConditionMet(0, 0, null, in)).isTrue();

    // instruction writes only MDR
    in = MicroInstructionReader.read(new ByteArrayInputStream(new byte[] { 0, 0, 0, (byte) 0x10, 0 }));
    assertThat(new RegisterWriteBreakpoint(Register.MDR).isConditionMet(0, 0, null, in)).isTrue();

    // instruction writes only MAR
    in = MicroInstructionReader.read(new ByteArrayInputStream(new byte[] { 0, 0, 0, (byte) 0x08, 0 }));
    assertThat(new RegisterWriteBreakpoint(Register.MAR).isConditionMet(0, 0, null, in)).isTrue();

    // instruction writes only write
    in = MicroInstructionReader.read(new ByteArrayInputStream(new byte[] { 0, 0, 0, (byte) 0x04, 0 }));
    assertThat(new RegisterWriteBreakpoint(Register.MDR).isConditionMet(0, 0, in, in)).isFalse();
    assertThat(new RegisterWriteBreakpoint(Register.MBR).isConditionMet(0, 0, in, in)).isFalse();
    assertThat(new RegisterWriteBreakpoint(Register.MBRU).isConditionMet(0, 0, in, in)).isFalse();

    // instruction writes only read
    in = MicroInstructionReader.read(new ByteArrayInputStream(new byte[] { 0, 0, 0, (byte) 0x02, 0 }));
    assertThat(new RegisterWriteBreakpoint(Register.MDR).isConditionMet(0, 0, in, null)).isTrue();

    // instruction writes only fetch
    in = MicroInstructionReader.read(new ByteArrayInputStream(new byte[] { 0, 0, 0, (byte) 0x01, 0 }));
    assertThat(new RegisterWriteBreakpoint(Register.MBR).isConditionMet(0, 0, in, null)).isTrue();
    assertThat(new RegisterWriteBreakpoint(Register.MBRU).isConditionMet(0, 0, in, null)).isTrue();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRegisterWriteBreakpoint() {
    new RegisterWriteBreakpoint(null);
  }

  @Test
  public void testToString() {
    final RegisterWriteBreakpoint rbp = new RegisterWriteBreakpoint(Register.H);
    assertThat(rbp.toString()).isEqualTo(Text.BREAKPOINT_WRITE_REGISTER.text(rbp.getId(), "H"));
  }

  @Test
  public void testGetRegister() {
    final RegisterWriteBreakpoint rbp = new RegisterWriteBreakpoint(Register.OPC);
    assertThat(rbp.getRegister()).isEqualTo(Register.OPC);
  }

  @Test
  public void testGetId() {
    final RegisterWriteBreakpoint rbp1 = new RegisterWriteBreakpoint(Register.H);
    final RegisterWriteBreakpoint rbp2 = new RegisterWriteBreakpoint(Register.H);
    final RegisterWriteBreakpoint rbp3 = new RegisterWriteBreakpoint(Register.OPC);

    assertThat(rbp1.getId()).isNotEqualTo(rbp2.getId());
    assertThat(rbp1.getId()).isNotEqualTo(rbp3.getId());
    assertThat(rbp2.getId()).isNotEqualTo(rbp3.getId());
  }

  @Test
  public void testIsBreakpointForMode() {
    assertThat(new RegisterWriteBreakpoint(Register.H).isBreakpointForMode(DebugMode.MACRO)).isFalse();
    assertThat(new RegisterWriteBreakpoint(Register.LV).isBreakpointForMode(DebugMode.MACRO)).isFalse();
    assertThat(new RegisterWriteBreakpoint(Register.MBR).isBreakpointForMode(DebugMode.MACRO)).isFalse();

    assertThat(new RegisterWriteBreakpoint(Register.H).isBreakpointForMode(DebugMode.MICRO)).isTrue();
    assertThat(new RegisterWriteBreakpoint(Register.LV).isBreakpointForMode(DebugMode.BOTH)).isTrue();
    assertThat(new RegisterWriteBreakpoint(Register.MBR).isBreakpointForMode(DebugMode.BOTH)).isTrue();
  }
}
