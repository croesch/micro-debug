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
package com.github.croesch.mic1;

import static org.fest.assertions.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.Test;

import com.github.croesch.error.FileFormatException;
import com.github.croesch.mic1.io.Output;
import com.github.croesch.mic1.register.Register;

/**
 * Provides test cases for {@link Mic1}.
 * 
 * @author croesch
 * @since Date: Dec 1, 2011
 */
public class Mic1Test {

  @Test(timeout = 1000)
  public void testPerformanceOfProcessor() throws FileFormatException {
    final Mic1 processor = new Mic1(ClassLoader.getSystemResourceAsStream("mic1/performance.mic1"),
                                    ClassLoader.getSystemResourceAsStream("mic1/empty.ijvm"));

    Register.PC.setValue(-1);
    for (int i = 0; i < 1000; ++i) {
      // MAR = PC = PC + 1; rd; goto 0;
      processor.doTick();
      assertThat(Register.PC.getValue()).isEqualTo(i);
      assertThat(Register.MAR.getValue()).isEqualTo(i);
      assertThat(Register.MDR.getValue()).isEqualTo(0);
    }
  }

  @Test
  public void testHi() throws FileFormatException {
    final Mic1 processor = new Mic1(ClassLoader.getSystemResourceAsStream("mic1/hi.mic1"),
                                    ClassLoader.getSystemResourceAsStream("mic1/hi.ijvm"));

    final ByteArrayOutputStream out = new ByteArrayOutputStream();
    Output.setBuffered(false);
    Output.setOut(new PrintStream(out));

    // 00: MAR = PC = 0; rd; goto 1;
    processor.doTick();
    assertThat(Register.MAR.getValue()).isEqualTo(0);
    assertThat(Register.PC.getValue()).isEqualTo(0);
    assertThat(out.toString()).isEmpty();

    // 01: LV = H = -1; goto 2;
    processor.doTick();
    assertThat(Register.MAR.getValue()).isEqualTo(0);
    assertThat(Register.PC.getValue()).isEqualTo(0);
    assertThat(Register.LV.getValue()).isEqualTo(-1);
    assertThat(Register.H.getValue()).isEqualTo(-1);
    assertThat(out.toString()).isEmpty();

    // 02: LV = H + LV; goto 3;
    processor.doTick();
    assertThat(Register.MDR.getValue()).isEqualTo('H');
    assertThat(Register.MAR.getValue()).isEqualTo(0);
    assertThat(Register.PC.getValue()).isEqualTo(0);
    assertThat(Register.LV.getValue()).isEqualTo(-2);
    assertThat(Register.H.getValue()).isEqualTo(-1);
    assertThat(out.toString()).isEmpty();

    // 03: MAR = LV - 1; wr; goto 4;
    processor.doTick();
    assertThat(Register.MDR.getValue()).isEqualTo('H');
    assertThat(Register.MAR.getValue()).isEqualTo(-3);
    assertThat(Register.PC.getValue()).isEqualTo(0);
    assertThat(Register.LV.getValue()).isEqualTo(-2);
    assertThat(Register.H.getValue()).isEqualTo(-1);
    assertThat(out.toString()).isEqualTo("H");

    // 04: MAR = PC = PC + 1; rd; goto 5;
    processor.doTick();
    assertThat(Register.MDR.getValue()).isEqualTo('H');
    assertThat(Register.MAR.getValue()).isEqualTo(1);
    assertThat(Register.PC.getValue()).isEqualTo(1);
    assertThat(Register.LV.getValue()).isEqualTo(-2);
    assertThat(Register.H.getValue()).isEqualTo(-1);
    assertThat(out.toString()).isEqualTo("H");

    // 05: MAR = LV - 1; goto 6;
    processor.doTick();
    assertThat(Register.MDR.getValue()).isEqualTo('i');
    assertThat(Register.MAR.getValue()).isEqualTo(-3);
    assertThat(Register.PC.getValue()).isEqualTo(1);
    assertThat(Register.LV.getValue()).isEqualTo(-2);
    assertThat(Register.H.getValue()).isEqualTo(-1);
    assertThat(out.toString()).isEqualTo("H");

    // 06: wr; goto 7;
    processor.doTick();
    assertThat(Register.MDR.getValue()).isEqualTo('i');
    assertThat(Register.MAR.getValue()).isEqualTo(-3);
    assertThat(Register.PC.getValue()).isEqualTo(1);
    assertThat(Register.LV.getValue()).isEqualTo(-2);
    assertThat(Register.H.getValue()).isEqualTo(-1);
    assertThat(out.toString()).isEqualTo("Hi");

    // 07: MAR = PC = PC + 1; rd; goto 8;
    processor.doTick();
    assertThat(Register.MDR.getValue()).isEqualTo('i');
    assertThat(Register.MAR.getValue()).isEqualTo(2);
    assertThat(Register.PC.getValue()).isEqualTo(2);
    assertThat(Register.LV.getValue()).isEqualTo(-2);
    assertThat(Register.H.getValue()).isEqualTo(-1);
    assertThat(out.toString()).isEqualTo("Hi");

    // 08: MAR = LV - 1; goto 9;
    processor.doTick();
    assertThat(Register.MDR.getValue()).isEqualTo('!');
    assertThat(Register.MAR.getValue()).isEqualTo(-3);
    assertThat(Register.PC.getValue()).isEqualTo(2);
    assertThat(Register.LV.getValue()).isEqualTo(-2);
    assertThat(Register.H.getValue()).isEqualTo(-1);
    assertThat(out.toString()).isEqualTo("Hi");

    // 09: wr; goto 10;
    processor.doTick();
    assertThat(Register.MDR.getValue()).isEqualTo('!');
    assertThat(Register.MAR.getValue()).isEqualTo(-3);
    assertThat(Register.PC.getValue()).isEqualTo(2);
    assertThat(Register.LV.getValue()).isEqualTo(-2);
    assertThat(Register.H.getValue()).isEqualTo(-1);
    assertThat(out.toString()).isEqualTo("Hi!");

    // 10: MAR = PC = PC + 1; rd; goto 11;
    processor.doTick();
    assertThat(Register.MDR.getValue()).isEqualTo('!');
    assertThat(Register.MAR.getValue()).isEqualTo(3);
    assertThat(Register.PC.getValue()).isEqualTo(3);
    assertThat(Register.LV.getValue()).isEqualTo(-2);
    assertThat(Register.H.getValue()).isEqualTo(-1);
    assertThat(out.toString()).isEqualTo("Hi!");

    // 11: MAR = LV - 1; goto 12;
    processor.doTick();
    assertThat(Register.MDR.getValue()).isEqualTo('\n');
    assertThat(Register.MAR.getValue()).isEqualTo(-3);
    assertThat(Register.PC.getValue()).isEqualTo(3);
    assertThat(Register.LV.getValue()).isEqualTo(-2);
    assertThat(Register.H.getValue()).isEqualTo(-1);
    assertThat(out.toString()).isEqualTo("Hi!");

    // 12: wr; goto 13;
    processor.doTick();
    assertThat(Register.MDR.getValue()).isEqualTo('\n');
    assertThat(Register.MAR.getValue()).isEqualTo(-3);
    assertThat(Register.PC.getValue()).isEqualTo(3);
    assertThat(Register.LV.getValue()).isEqualTo(-2);
    assertThat(Register.H.getValue()).isEqualTo(-1);
    assertThat(out.toString()).isEqualTo("Hi!\n");

    // 13: halt;
    processor.doTick();
    assertThat(Register.MDR.getValue()).isEqualTo('\n');
    assertThat(Register.MAR.getValue()).isEqualTo(-3);
    assertThat(Register.PC.getValue()).isEqualTo(3);
    assertThat(Register.LV.getValue()).isEqualTo(-2);
    assertThat(Register.H.getValue()).isEqualTo(-1);
    assertThat(out.toString()).isEqualTo("Hi!\n");

    Output.setOut(System.out);
  }
}
