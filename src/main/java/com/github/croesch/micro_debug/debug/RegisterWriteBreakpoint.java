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

import com.github.croesch.micro_debug.annotation.NotNull;
import com.github.croesch.micro_debug.i18n.Text;
import com.github.croesch.micro_debug.mic1.controlstore.MicroInstruction;
import com.github.croesch.micro_debug.mic1.register.Register;

/**
 * A breakpoint for a specific {@link Register}. Breakpoint checks if the {@link Register} will be written by execution
 * of the next {@link MicroInstruction}.
 * 
 * @author croesch
 * @since Date: Apr 11, 2012
 */
final class RegisterWriteBreakpoint extends AbstractRegisterBreakpoint {

  /**
   * Constructs a breakpoint with the condition that the given {@link Register} will be written by the next instruction
   * of the processor.
   * 
   * @since Date: Apr 11, 2012
   * @param r the {@link Register} to check for a possible write access
   */
  RegisterWriteBreakpoint(final Register r) {
    super(r);
  }

  @Override
  boolean isConditionMet(final int microLine,
                         final int macroLine,
                         final MicroInstruction currentInstruction,
                         final MicroInstruction nextInstruction) {
    return (nextInstruction != null && isRegisterWrittenByNextInstruction(nextInstruction))
           || (currentInstruction != null && isRegisterWrittenByCurrentInstruction(currentInstruction));
  }

  /**
   * Returns whether the next instruction to be executed by the processor will set a value for the {@link Register} this
   * breakpoint watches.
   * 
   * @since Date: Apr 11, 2012
   * @param instruction the instruction to check, if it'll set a value for the watched {@link Register}.
   * @return <code>true</code> if the given instruction will set a value for the watched {@link Register},<br>
   *         or <code>false</code> otherwise
   */
  private boolean isRegisterWrittenByNextInstruction(final MicroInstruction instruction) {
    switch (getRegister()) {
      case CPP:
        return instruction.getCBusSignals().isCpp();
      case H:
        return instruction.getCBusSignals().isH();
      case LV:
        return instruction.getCBusSignals().isLv();
      case MAR:
        return instruction.getCBusSignals().isMar();
      case MDR:
        return instruction.getCBusSignals().isMdr();
      case OPC:
        return instruction.getCBusSignals().isOpc();
      case PC:
        return instruction.getCBusSignals().isPc();
      case SP:
        return instruction.getCBusSignals().isSp();
      case TOS:
        return instruction.getCBusSignals().isTos();
      default:
        return false;
    }
  }

  /**
   * Returns whether the instruction that has been executed last by the processor will set a value for the
   * {@link Register} this breakpoint watches.
   * 
   * @since Date: Apr 11, 2012
   * @param instruction the instruction to check, if it'll set a value for the watched {@link Register}, when the next
   *        tick'll be done by the processor.
   * @return <code>true</code> if the given instruction will set a value for the watched {@link Register},<br>
   *         or <code>false</code> otherwise
   */
  private boolean isRegisterWrittenByCurrentInstruction(final MicroInstruction instruction) {
    if (getRegister() == Register.MBR || getRegister() == Register.MBRU) {
      return instruction.getMemorySignals().isFetch();
    }
    if (getRegister() == Register.MDR) {
      return instruction.getMemorySignals().isRead();
    }
    return false;
  }

  @Override
  @NotNull
  public String toString() {
    return Text.BREAKPOINT_WRITE_REGISTER.text(getId(), getRegister());
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + getRegister().hashCode();
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
    final RegisterWriteBreakpoint other = (RegisterWriteBreakpoint) obj;
    if (getRegister() != other.getRegister()) {
      return false;
    }
    return true;
  }
}
