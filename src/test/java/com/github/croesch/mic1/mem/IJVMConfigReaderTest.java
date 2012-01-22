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
package com.github.croesch.mic1.mem;

import static org.fest.assertions.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.github.croesch.DefaultTestCase;

/**
 * Provides test cases for {@link IJVMConfigReader}.
 * 
 * @author croesch
 * @since Date: Jan 22, 2012
 */
public class IJVMConfigReaderTest extends DefaultTestCase {

  private static final IJVMConfigReader READER = new IJVMConfigReader();

  @Test
  public void testReadConfig_Empty_EmptyFile() {
    assertThat(READER.readConfig(new ByteArrayInputStream("".getBytes()))).isEmpty();
  }

  @Test
  public void testReadConfig_Empty_OnlyComments() {
    assertThat(READER.readConfig(new ByteArrayInputStream(" // nothing\n\n//also empty here".getBytes()))).isEmpty();
  }

  @Test
  public void testReadConfig_Empty_Null() {
    assertThat(READER.readConfig(null)).isEmpty();
  }

  @Test
  public void testReadConfig_SingleLine() {
    final Map<Integer, IJVMCommand> expected = new HashMap<Integer, IJVMCommand>();
    expected.put(16, new IJVMCommand("BIPUSH", IJVMCommandArgument.BYTE));
    assertThat(READER.readConfig(new ByteArrayInputStream("0x10 BIPUSH byte // Push byte onto stack".getBytes())))
      .isEqualTo(expected);
    assertThat(READER.readConfig(new ByteArrayInputStream("0x10 BIPUSH byte// Push byte onto stack".getBytes())))
      .isEqualTo(expected);
    assertThat(READER.readConfig(new ByteArrayInputStream("0x10 BIPUSH byte".getBytes()))).isEqualTo(expected);
  }

  @Test
  public void testReadConfig() throws FileNotFoundException {
    final Map<Integer, IJVMCommand> expected = new HashMap<Integer, IJVMCommand>();
    expected.put(0x00, new IJVMCommand("NOP"));
    expected.put(0x10, new IJVMCommand("BIPUSH", IJVMCommandArgument.BYTE));
    expected.put(0x13, new IJVMCommand("LDC_W", IJVMCommandArgument.INDEX));
    expected.put(0x15, new IJVMCommand("ILOAD", IJVMCommandArgument.VARNUM));
    expected.put(0x36, new IJVMCommand("ISTORE", IJVMCommandArgument.VARNUM));
    expected.put(0x57, new IJVMCommand("POP"));
    expected.put(0x59, new IJVMCommand("DUP"));
    expected.put(0x5F, new IJVMCommand("SWAP"));
    expected.put(0x60, new IJVMCommand("IADD"));
    expected.put(0x64, new IJVMCommand("ISUB"));
    expected.put(0x7E, new IJVMCommand("IAND"));
    expected.put(0x80, new IJVMCommand("IOR"));
    expected.put(0x84, new IJVMCommand("IINC", IJVMCommandArgument.VARNUM, IJVMCommandArgument.CONST));
    expected.put(0x99, new IJVMCommand("IFEQ", IJVMCommandArgument.LABEL));
    expected.put(0x9B, new IJVMCommand("IFLT", IJVMCommandArgument.LABEL));
    expected.put(0x9F, new IJVMCommand("IF_ICMPEQ", IJVMCommandArgument.LABEL));
    expected.put(0xA7, new IJVMCommand("GOTO", IJVMCommandArgument.LABEL));
    expected.put(0xAC, new IJVMCommand("IRETURN"));
    expected.put(0xB6, new IJVMCommand("INVOKEVIRTUAL", IJVMCommandArgument.OFFSET));
    expected.put(0xC4, new IJVMCommand("WIDE"));
    expected.put(0xF0, new IJVMCommand("SRA1"));
    expected.put(0xF1, new IJVMCommand("SLL8"));
    expected.put(0xFC, new IJVMCommand("IN"));
    expected.put(0xFD, new IJVMCommand("OUT"));
    expected.put(0xFE, new IJVMCommand("ERR"));
    expected.put(0xFF, new IJVMCommand("HALT"));
    assertThat(READER.readConfig(getClass().getClassLoader().getResourceAsStream("ijvm.conf"))).isEqualTo(expected);
  }
}
