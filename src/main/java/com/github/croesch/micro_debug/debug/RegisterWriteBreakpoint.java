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
    switch (getRegister()) {
      case CPP:
        return nextInstruction != null && nextInstruction.getCBusSignals().isCpp();
      case H:
        return nextInstruction != null && nextInstruction.getCBusSignals().isH();
      case LV:
        return nextInstruction != null && nextInstruction.getCBusSignals().isLv();
      case MAR:
        return nextInstruction != null && nextInstruction.getCBusSignals().isMar();
      case MBR:
        return currentInstruction != null && currentInstruction.getMemorySignals().isFetch();
      case MBRU:
        return currentInstruction != null && currentInstruction.getMemorySignals().isFetch();
      case MDR:
        return (currentInstruction != null && currentInstruction.getMemorySignals().isRead())
               || (nextInstruction != null && nextInstruction.getCBusSignals().isMdr());
      case OPC:
        return nextInstruction != null && nextInstruction.getCBusSignals().isOpc();
      case PC:
        return nextInstruction != null && nextInstruction.getCBusSignals().isPc();
      case SP:
        return nextInstruction != null && nextInstruction.getCBusSignals().isSp();
      case TOS:
        return nextInstruction != null && nextInstruction.getCBusSignals().isTos();
      default:
        throw new IllegalStateException("Unknown register: " + getRegister());
    }
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
