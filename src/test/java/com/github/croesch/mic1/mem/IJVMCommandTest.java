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

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.github.croesch.DefaultTestCase;

/**
 * Provides test cases for {@link IJVMCommand}.
 * 
 * @author croesch
 * @since Date: Jan 22, 2012
 */
public class IJVMCommandTest extends DefaultTestCase {

  @Test(expected = IllegalArgumentException.class)
  public void testIJVMCommand_IAE_NameNull() {
    new IJVMCommand(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIJVMCommand_IAE_NameEmpty() {
    new IJVMCommand("");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIJVMCommand_IAE_NameWhitespaces() {
    new IJVMCommand("  \n\t  \t");
  }

  @Test
  public void testGetName() {
    assertThat(new IJVMCommand("abc").getName()).isEqualTo("abc");
    assertThat(new IJVMCommand(" abc as \t   ").getName()).isEqualTo("abc as");
    assertThat(new IJVMCommand("DUP").getName()).isEqualTo("DUP");
    assertThat(new IJVMCommand("LDC_W").getName()).isEqualTo("LDC_W");
  }

  @Test
  public void testGetArgs() {
    assertThat(
               new IJVMCommand("a", IJVMCommandArgument.BYTE, IJVMCommandArgument.BYTE, IJVMCommandArgument.INDEX)
                 .getArgs()).containsExactly(IJVMCommandArgument.BYTE, IJVMCommandArgument.BYTE,
                                             IJVMCommandArgument.INDEX);
    assertThat(
               new IJVMCommand("a",
                               IJVMCommandArgument.BYTE,
                               IJVMCommandArgument.BYTE,
                               IJVMCommandArgument.INDEX,
                               IJVMCommandArgument.LABEL).getArgs()).containsExactly(IJVMCommandArgument.BYTE,
                                                                                     IJVMCommandArgument.BYTE,
                                                                                     IJVMCommandArgument.INDEX,
                                                                                     IJVMCommandArgument.LABEL);
    assertThat(
               new IJVMCommand("a",
                               IJVMCommandArgument.BYTE,
                               null,
                               null,
                               IJVMCommandArgument.BYTE,
                               IJVMCommandArgument.INDEX,
                               null,
                               IJVMCommandArgument.LABEL,
                               null,
                               null).getArgs()).containsExactly(IJVMCommandArgument.BYTE, IJVMCommandArgument.BYTE,
                                                                IJVMCommandArgument.INDEX, IJVMCommandArgument.LABEL);
    assertThat(new IJVMCommand("a", IJVMCommandArgument.BYTE, IJVMCommandArgument.BYTE).getArgs())
      .containsExactly(IJVMCommandArgument.BYTE, IJVMCommandArgument.BYTE);
    assertThat(new IJVMCommand("a", IJVMCommandArgument.BYTE).getArgs()).containsExactly(IJVMCommandArgument.BYTE);
    assertThat(new IJVMCommand("a").getArgs()).containsExactly();
  }

  @Test
  public void testGetArgsModifiability() {
    List<IJVMCommandArgument> args = new ArrayList<IJVMCommandArgument>();
    final IJVMCommand cmd = new IJVMCommand("name", args.toArray(new IJVMCommandArgument[args.size()]));
    args.add(IJVMCommandArgument.LABEL);

    assertThat(cmd.getArgs()).isEmpty();

    args = cmd.getArgs();
    final List<IJVMCommandArgument> argsTmp = new ArrayList<IJVMCommandArgument>(args);

    args.add(IJVMCommandArgument.VARNUM);

    assertThat(argsTmp).isNotEqualTo(args);
    assertThat(cmd.getArgs()).isNotEqualTo(args);
    assertThat(cmd.getArgs()).isEqualTo(argsTmp);
  }

  @Test
  public void testToString() {
    assertThat(
               new IJVMCommand("a",
                               IJVMCommandArgument.BYTE,
                               IJVMCommandArgument.BYTE,
                               IJVMCommandArgument.INDEX,
                               IJVMCommandArgument.LABEL).toString()).isEqualTo("a BYTE BYTE INDEX LABEL");
    assertThat(new IJVMCommand("a").toString()).isEqualTo("a");
  }

  @Test
  public void testEquals() {

    final IJVMCommand cmd = new IJVMCommand("a",
                                            IJVMCommandArgument.BYTE,
                                            IJVMCommandArgument.BYTE,
                                            IJVMCommandArgument.INDEX,
                                            IJVMCommandArgument.LABEL);
    assertThat(cmd).isEqualTo(cmd);
    assertThat(cmd).isEqualTo(new IJVMCommand("a",
                                              IJVMCommandArgument.BYTE,
                                              IJVMCommandArgument.BYTE,
                                              IJVMCommandArgument.INDEX,
                                              IJVMCommandArgument.LABEL));
    assertThat(
               new IJVMCommand("a",
                               IJVMCommandArgument.BYTE,
                               IJVMCommandArgument.BYTE,
                               IJVMCommandArgument.INDEX,
                               IJVMCommandArgument.LABEL)).isEqualTo(cmd);
  }
}
