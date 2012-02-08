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
package com.github.croesch.ui;

import java.util.EnumMap;
import java.util.Map;

import com.github.croesch.i18n.Text;
import com.github.croesch.mic1.api.IReadableMemory;
import com.github.croesch.mic1.controlstore.Mic1Instruction;
import com.github.croesch.mic1.controlstore.Mic1InstructionDecoder;
import com.github.croesch.mic1.register.Register;
import com.github.croesch.misc.Printer;
import com.github.croesch.misc.Utils;
import com.github.croesch.ui.api.Mic1View;

/**
 * Console view for the {@link com.github.croesch.mic1.Mic1}.
 * 
 * @author croesch
 * @since Date: Jan 15, 2012
 */
public final class TraceManager implements Mic1View {

  /** contains which registers are traced and which aren't */
  private final Map<Register, Boolean> tracingRegisters = new EnumMap<Register, Boolean>(Register.class);

  /** determines whether the micro code is currently traced */
  private boolean microTracing = false;

  /** determines whether the macro code is currently traced */
  private boolean macroTracing = false;

  /** the main memory of the processor being traced */
  private final IReadableMemory memory;

  /**
   * A manager that is able to trace some things of the processors current state.
   * 
   * @since Date: Feb 8, 2012
   * @param mem the main memory of the processor
   */
  public TraceManager(final IReadableMemory mem) {
    this.memory = mem;
  }

  /**
   * Lists the values of all {@link Register}s.
   * 
   * @since Date: Jan 15, 2012
   */
  public void listAllRegisters() {
    for (final Register r : Register.values()) {
      listRegister(r);
    }
  }

  /**
   * Lists the value of a single {@link Register}.
   * 
   * @since Date: Jan 15, 2012
   * @param r the {@link Register} to print with its value.
   */
  public void listRegister(final Register r) {
    Printer.println(Text.REGISTER_VALUE.text(String.format("%-4s", r), Utils.toHexString(r.getValue())));
  }

  /**
   * Performs to trace all {@link Register}s.
   * 
   * @since Date: Jan 15, 2012
   */
  public void traceRegister() {
    for (final Register r : Register.values()) {
      traceRegister(r);
    }
  }

  /**
   * Performs to trace the given {@link Register}.
   * 
   * @since Date: Jan 15, 2012
   * @param r the {@link Register} to trace.
   */
  public void traceRegister(final Register r) {
    this.tracingRegisters.put(r, Boolean.TRUE);
  }

  /**
   * Performs to not trace any {@link Register}.
   * 
   * @since Date: Jan 15, 2012
   */
  public void untraceRegister() {
    for (final Register r : Register.values()) {
      untraceRegister(r);
    }
  }

  /**
   * Performs to not trace the given {@link Register} anymore.
   * 
   * @since Date: Jan 15, 2012
   * @param r the {@link Register} not being traced anymore.
   */
  public void untraceRegister(final Register r) {
    this.tracingRegisters.put(r, Boolean.FALSE);
  }

  /**
   * Returns whether the given {@link Register} is currently traced.
   * 
   * @since Date: Jan 15, 2012
   * @param r the {@link Register} to check, if it's traced
   * @return <code>true</code>, if the {@link Register} is currently traced<br>
   *         <code>false</code> otherwise.
   */
  public boolean isTracing(final Register r) {
    return this.tracingRegisters.containsKey(r) && this.tracingRegisters.get(r);
  }

  /**
   * Performs to trace the micro code.
   * 
   * @since Date: Jan 21, 2012
   */
  public void traceMicro() {
    this.microTracing = true;
  }

  /**
   * Performs to not trace the micro code anymore.
   * 
   * @since Date: Jan 21, 2012
   */
  public void untraceMicro() {
    this.microTracing = false;
  }

  /**
   * Performs to trace the micro code.
   * 
   * @since Date: Jan 21, 2012
   */
  public void traceMacro() {
    this.macroTracing = true;
  }

  /**
   * Performs to not trace the macro code anymore.
   * 
   * @since Date: Feb 3, 2012
   */
  public void untraceMacro() {
    this.macroTracing = false;
  }

  /**
   * Returns whether the micro code is currently traced.
   * 
   * @since Date: Jan 21, 2012
   * @return <code>true</code>, if the micro code is currently traced<br>
   *         <code>false</code> otherwise.
   */
  public boolean isTracingMicro() {
    return this.microTracing;
  }

  /**
   * Returns whether the macro code is currently traced.
   * 
   * @since Date: Feb 3, 2012
   * @return <code>true</code>, if the macro code is currently traced<br>
   *         <code>false</code> otherwise.
   */
  public boolean isTracingMacro() {
    return this.macroTracing;
  }

  /**
   * Tells the view to update itself.
   * 
   * @since Date: Jan 15, 2012
   * @param currentInstruction the instruction that is now executed
   * @param macroCodeNumber the line number of the macro instruction being executed
   */
  public void update(final Mic1Instruction currentInstruction, final int macroCodeNumber) {
    // trace macro code
    if (macroCodeNumber >= 0 && isTracingMacro()) {
      Printer.println(Text.EXECUTED_CODE.text(this.memory.getFormattedLine(macroCodeNumber)));
    }

    // trace micro code
    if (isTracingMicro()) {
      Printer.println(Text.EXECUTED_CODE.text(Mic1InstructionDecoder.decode(currentInstruction)));
    }

    // trace register
    for (final Register r : Register.values()) {
      if (isTracing(r)) {
        listRegister(r);
      }
    }
  }
}
