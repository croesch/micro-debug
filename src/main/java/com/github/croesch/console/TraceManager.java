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
package com.github.croesch.console;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import com.github.croesch.commons.Printer;
import com.github.croesch.commons.Settings;
import com.github.croesch.commons.Utils;
import com.github.croesch.i18n.Text;
import com.github.croesch.mic1.api.IReadableMemory;
import com.github.croesch.mic1.controlstore.MicroInstruction;
import com.github.croesch.mic1.controlstore.MicroInstructionDecoder;
import com.github.croesch.mic1.register.Register;

/**
 * Console view for the {@link com.github.croesch.mic1.Mic1}.
 * 
 * @author croesch
 * @since Date: Jan 15, 2012
 */
public final class TraceManager {

  /** contains which registers are traced and which aren't */
  private final Map<Register, Boolean> tracingRegisters = new EnumMap<Register, Boolean>(Register.class);

  /** contains the old/current values of the registers */
  private final Map<Register, Integer> tracingRegistersValues = new EnumMap<Register, Integer>(Register.class);

  /** contains the variables that are currently traced */
  private final List<MacroVariable> tracingVariables = new ArrayList<MacroVariable>();

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
   * @param mem the main memory of the processor, mustn't be <code>null</code>
   */
  public TraceManager(final IReadableMemory mem) {
    if (mem == null) {
      throw new IllegalArgumentException();
    }
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
    if (r != null) {
      Printer.println(Text.REGISTER_VALUE.text(String.format("%-4s", r), Utils.toHexString(r.getValue())));
    }
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
    if (r != null) {
      this.tracingRegisters.put(r, Boolean.TRUE);
      this.tracingRegistersValues.put(r, Integer.valueOf(r.getValue()));
    }
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
    if (r != null) {
      this.tracingRegisters.put(r, Boolean.FALSE);
    }
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
   * @param macroCodeLine the formatted macro code line being executed, or <code>null</code> if no new macro code line
   *        has been reached
   */
  public void update(final MicroInstruction currentInstruction, final String macroCodeLine) {
    // trace macro code
    if (macroCodeLine != null && isTracingMacro()) {
      Printer.println(Text.EXECUTED_CODE.text(macroCodeLine));
    }

    // trace micro code
    if (isTracingMicro()) {
      Printer.println(Text.EXECUTED_CODE.text(MicroInstructionDecoder.decode(currentInstruction)));
    }

    // trace register
    for (final Register r : Register.values()) {
      if (isTracing(r) && r.getValue() != this.tracingRegistersValues.get(r).intValue()) {
        this.tracingRegistersValues.put(r, Integer.valueOf(r.getValue()));
        listRegister(r);
      }
    }

    //trace local variables
    for (final MacroVariable var : this.tracingVariables) {
      final int addr = getAddressOfLocalVariable(var.getNumber());
      if (addr == var.getAddress()) {
        final int newVal = this.memory.getWord(addr);
        if (var.getValue() != newVal) {
          var.setValue(newVal);
          Printer.println(Text.LOCAL_VARIABLE_VALUE.text(var.getNumber(), var.getValue()));
        }
      }
    }
  }

  /**
   * Start tracing the value of the local variable with the given number. This will create a variable based on the
   * current LV value so that we can differentiate the variable if we return from this method.
   * 
   * @since Date: Feb 8, 2012
   * @param varNum the local number of this variable as an offset to the LV.
   */
  public void traceLocalVariable(final int varNum) {
    if (canTraceLocalVariable(varNum) && !isTracingLocalVariable(varNum)) {
      final int addr = getAddressOfLocalVariable(varNum);
      this.tracingVariables.add(new MacroVariable(varNum, addr, this.memory.getWord(addr)));
    }
  }

  /**
   * Ends tracing the value of the local variable with the given number. This will remove the variable based on the
   * current LV value so that we don't stop tracing a variable that is traced outside the current method.
   * 
   * @since Date: Feb 8, 2012
   * @param varNum the local number of this variable as an offset to the LV.
   */
  public void untraceLocalVariable(final int varNum) {
    if (canTraceLocalVariable(varNum)) {
      final int addr = getAddressOfLocalVariable(varNum);
      this.tracingVariables.remove(new MacroVariable(varNum, addr, 0));
    }
  }

  /**
   * Returns whether it should be possible to trace a local variable with the given number.
   * 
   * @since Date: Feb 9, 2012
   * @param varNum the number of the local variable to check
   * @return <code>true</code>, if there is a local variable with the given number that can be traced, <br>
   *         <code>false</code> otherwise
   */
  private boolean canTraceLocalVariable(final int varNum) {
    return isRegularLocalVariable(varNum) || isLocalVariableInMainFunction(varNum);
  }

  /**
   * Returns whether we are currently in main function and if the given number is a valid variable in main function.
   * 
   * @since Date: Feb 9, 2012
   * @param varNum the number of the local variable
   * @return <code>true</code> if we are in the main function of macro code and if the given number could refer to a
   *         local variable in main function,<br>
   *         <code>false</code> otherwise
   */
  private boolean isLocalVariableInMainFunction(final int varNum) {
    return Register.LV.getValue() == Settings.MIC1_REGISTER_LV_DEFVAL.getValue()
           && varNum >= 0
           && varNum < Utils.getNextHigherValue(Settings.MIC1_REGISTER_LV_DEFVAL.getValue(), this.memory.getSize(),
                                                Settings.MIC1_REGISTER_CPP_DEFVAL.getValue(),
                                                Settings.MIC1_REGISTER_SP_DEFVAL.getValue(),
                                                Settings.MIC1_REGISTER_PC_DEFVAL.getValue());
  }

  /**
   * Returns whether the given number is a valid local variable, assuming to be not in the main function of the macro
   * code.
   * 
   * @since Date: Feb 9, 2012
   * @param varNum the number of the local variable
   * @return <code>true</code>, if the given number could refer to a local variable in the current function,<br>
   *         <code>false</code> otherwise
   */
  private boolean isRegularLocalVariable(final int varNum) {
    return varNum > 0 && varNum <= getNumberOfLocalVariables();
  }

  /**
   * Returns whether we are tracing the value of the local variable with the given number. This will also check the
   * value of the LV, so that we are sure we are really tracing this variable and not a variable in a different method
   * with the same local number.
   * 
   * @since Date: Feb 8, 2012
   * @param varNum the local number of this variable as an offset to the LV.
   * @return <code>true</code> if we are tracing the local variable with the given number in the current macro code
   *         method,<br>
   *         <code>false</code> otherwise
   */
  public boolean isTracingLocalVariable(final int varNum) {
    if (canTraceLocalVariable(varNum)) {
      final int addr = getAddressOfLocalVariable(varNum);
      return this.tracingVariables.contains(new MacroVariable(varNum, addr, 0));
    }
    return false;
  }

  /**
   * Returns the number of local variables in the current method, based on the value of the LV.
   * 
   * @since Date: Feb 8, 2012
   * @return the number of local variables in the current method. Means number of parameters plus the number of local
   *         variables, of this method.
   */
  private int getNumberOfLocalVariables() {
    return this.memory.getWord(Register.LV.getValue()) - Register.LV.getValue() - 1;
  }

  /**
   * Returns the physical address in the main memory of the local variable with the given number (in the current macro
   * code method).
   * 
   * @since Date: Feb 8, 2012
   * @param varNum the number of the local variable to fetch the address of
   * @return the address of the local variable in the main memory
   */
  private int getAddressOfLocalVariable(final int varNum) {
    return Register.LV.getValue() + varNum;
  }
}
