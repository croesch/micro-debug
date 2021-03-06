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
package com.github.croesch.micro_debug.console;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Locale;

import com.github.croesch.micro_debug.annotation.NotNull;
import com.github.croesch.micro_debug.annotation.Nullable;
import com.github.croesch.micro_debug.commons.Parameter;
import com.github.croesch.micro_debug.commons.Printer;
import com.github.croesch.micro_debug.commons.Utils;
import com.github.croesch.micro_debug.datatypes.DebugMode;
import com.github.croesch.micro_debug.i18n.Text;
import com.github.croesch.micro_debug.mic1.register.Register;
import com.github.croesch.micro_debug.settings.Settings;

/**
 * Enumeration of all possible command line instructions for the debugger.
 * 
 * @author croesch
 * @since Date: Dec 3, 2011
 */
enum UserInstruction {

  /** creates a break point - debugger will stop if the given register has the given value */
  BREAK {
    @Override
    public boolean execute(final Mic1Interpreter interpreter, final String ... params) {
      switch (getSize(params)) {
        case 1:
          interpreter.addRegisterBreakpoint((Register) Parameter.REGISTER.getValue(params[0]));
          break;
        case 2:
          final Register r = (Register) Parameter.REGISTER.getValue(params[0]);
          final Integer i = (Integer) Parameter.NUMBER.getValue(params[1]);
          interpreter.addRegisterBreakpoint(r, i);
          break;
        default:
          Printer.printErrorln(Text.WRONG_PARAM_NUMBER.text(2, getSize(params)));
          break;
      }
      return true;
    }
  },

  /** switches the debugging mode - to skip breakpoints not belonging to the current mode */
  DEBUG {
    @Override
    public boolean execute(final Mic1Interpreter interpreter, final String ... params) {
      if (getSize(params) == 1) {
        final DebugMode mode = (DebugMode) Parameter.DEBUG_MODE.getValue(params[0]);
        if (mode != null) {
          interpreter.setDebuggingMode(mode);
        }
      } else {
        Printer.printErrorln(Text.WRONG_PARAM_NUMBER.text(1, getSize(params)));
      }
      return true;
    }
  },

  /** ends the debugger */
  EXIT {
    @Override
    public boolean execute(final Mic1Interpreter interpreter, final String ... params) {
      // simply return to end the program
      return false;
    }
  },

  /** instruction to view a help about the usage of the debugger */
  HELP {
    /** path to the file containing the help text */
    private static final String HELP_FILE = "instruction-help.txt";

    @Override
    public boolean execute(final Mic1Interpreter interpreter, final String ... params) {
      final InputStream fileStream = Utils.class.getClassLoader().getResourceAsStream(HELP_FILE);
      Printer.printReader(new InputStreamReader(fileStream));
      return true;
    }
  },

  /** lists all breakpoints */
  LS_BREAK {
    @Override
    public boolean execute(final Mic1Interpreter interpreter, final String ... params) {
      interpreter.listBreakpoints();
      return true;
    }
  },

  /** instruction to print the macro code to the user */
  LS_MACRO_CODE {
    @Override
    public boolean execute(final Mic1Interpreter interpreter, final String ... params) {
      switch (getSize(params)) {
        case 0:
          interpreter.printMacroCode();
          break;
        case 1:
          final Integer num = (Integer) Parameter.NUMBER.getValue(params[0]);
          if (num != null) {
            interpreter.printMacroCode(num.intValue());
          }
          break;
        case 2:
          final Integer from = (Integer) Parameter.NUMBER.getValue(params[0]);
          final Integer to = (Integer) Parameter.NUMBER.getValue(params[1]);
          if (from != null && to != null) {
            interpreter.printMacroCode(from.intValue(), to.intValue());
          }
          break;
        default:
          Printer.printErrorln(Text.WRONG_PARAM_NUMBER.text(2, getSize(params)));
          break;
      }
      return true;
    }
  },

  /** instruction to print the micro code to the user */
  LS_MICRO_CODE {
    @Override
    public boolean execute(final Mic1Interpreter interpreter, final String ... params) {
      switch (getSize(params)) {
        case 0:
          interpreter.printMicroCode();
          break;
        case 1:
          final Integer num = (Integer) Parameter.NUMBER.getValue(params[0]);
          if (num != null) {
            interpreter.printMicroCode(num.intValue());
          }
          break;
        case 2:
          final Integer from = (Integer) Parameter.NUMBER.getValue(params[0]);
          final Integer to = (Integer) Parameter.NUMBER.getValue(params[1]);
          if (from != null && to != null) {
            interpreter.printMicroCode(from.intValue(), to.intValue());
          }
          break;
        default:
          Printer.printErrorln(Text.WRONG_PARAM_NUMBER.text(2, getSize(params)));
          break;
      }
      return true;
    }
  },

  /** lists the content of the memory between the given addresses (inclusive) */
  LS_MEM {
    @Override
    public boolean execute(final Mic1Interpreter interpreter, final String ... params) {
      if (getSize(params) == 2) {
        final Integer from = (Integer) Parameter.NUMBER.getValue(params[0]);
        final Integer to = (Integer) Parameter.NUMBER.getValue(params[1]);
        if (from != null && to != null) {
          interpreter.printContent(from.intValue(), to.intValue());
        }
      } else {
        Printer.printErrorln(Text.WRONG_PARAM_NUMBER.text(2, getSize(params)));
      }
      return true;
    }
  },

  /** list the values of all or a single register */
  LS_REG {
    @Override
    public boolean execute(final Mic1Interpreter interpreter, final String ... params) {
      switch (getSize(params)) {
        case 0:
          interpreter.listAllRegisters();
          break;
        case 1:
          final Register r = (Register) Parameter.REGISTER.getValue(params[0]);
          interpreter.listSingleRegister(r);
          break;
        default:
          Printer.printErrorln(Text.WRONG_PARAM_NUMBER.text(0, getSize(params)));
          break;
      }
      return true;
    }
  },

  /** prints the content of the stack */
  LS_STACK {
    @Override
    public boolean execute(final Mic1Interpreter interpreter, final String ... params) {
      interpreter.printStack(Settings.STACK_ELEMENTS_TO_HIDE.getValue());
      return true;
    }
  },

  /** adds a breakpoint at the given line in the macro code */
  MACRO_BREAK {
    @Override
    public boolean execute(final Mic1Interpreter interpreter, final String ... params) {
      if (getSize(params) == 1) {
        final Integer l = (Integer) Parameter.NUMBER.getValue(params[0]);
        interpreter.addMacroBreakpoint(l);
      } else {
        Printer.printErrorln(Text.WRONG_PARAM_NUMBER.text(1, getSize(params)));
      }
      return true;
    }
  },

  /** adds a breakpoint at the given line in the micro code */
  MICRO_BREAK {
    @Override
    public boolean execute(final Mic1Interpreter interpreter, final String ... params) {
      if (getSize(params) == 1) {
        final Integer l = (Integer) Parameter.NUMBER.getValue(params[0]);
        interpreter.addMicroBreakpoint(l);
      } else {
        Printer.printErrorln(Text.WRONG_PARAM_NUMBER.text(1, getSize(params)));
      }
      return true;
    }
  },

  /** executes one or the given number of micro instructions */
  MICRO_STEP {
    @Override
    public boolean execute(final Mic1Interpreter interpreter, final String ... params) {
      switch (getSize(params)) {
        case 0:
          interpreter.getProcessor().microStep();
          break;
        case 1:
          final Integer i = (Integer) Parameter.NUMBER.getValue(params[0]);
          if (i != null) {
            interpreter.getProcessor().microStep(i.intValue());
          }
          break;
        default:
          Printer.printErrorln(Text.WRONG_PARAM_NUMBER.text(0, getSize(params)));
          break;
      }

      return true;
    }
  },

  /** resets the processor to its initial state */
  RESET {
    @Override
    public boolean execute(final Mic1Interpreter interpreter, final String ... params) {
      interpreter.getProcessor().reset();
      return true;
    }
  },

  /** Removes all breakpoints */
  RM_ALL_BREAKPOINTS {
    @Override
    public boolean execute(final Mic1Interpreter interpreter, final String ... params) {
      interpreter.removeAllBreakpoints();
      return true;
    }
  },

  /** Removes the breakpoint with the given number */
  RM_BREAK {
    @Override
    public boolean execute(final Mic1Interpreter interpreter, final String ... params) {
      if (getSize(params) != 1) {
        Printer.printErrorln(Text.WRONG_PARAM_NUMBER.text(1, getSize(params)));
      } else {
        final Integer i = (Integer) Parameter.NUMBER.getValue(params[0]);
        if (i != null) {
          interpreter.removeBreakpoint(i.intValue());
        }
      }
      return true;
    }
  },

  /** runs the program to the end */
  RUN {
    @Override
    public boolean execute(final Mic1Interpreter interpreter, final String ... params) {
      interpreter.getProcessor().run();
      return true;
    }
  },

  /** instruction to set the value of a register */
  SET {
    /** the number of expected parameters for this instruction */
    private static final int EXPECTED_PARAMETERS = 2;

    @Override
    public boolean execute(final Mic1Interpreter interpreter, final String ... params) {
      if (getSize(params) != EXPECTED_PARAMETERS) {
        Printer.printErrorln(Text.WRONG_PARAM_NUMBER.text(EXPECTED_PARAMETERS, getSize(params)));
      } else {
        final Register r = (Register) Parameter.REGISTER.getValue(params[0]);
        final Integer i = (Integer) Parameter.NUMBER.getValue(params[1]);
        if (r != null && i != null) {
          r.setValue(i.intValue());
        }
      }
      return true;
    }
  },

  /** instruction to set the value of the memory at a specific address */
  SET_MEM {
    /** the number of expected parameters for this instruction */
    private static final int EXPECTED_PARAMETERS = 2;

    @Override
    public boolean execute(final Mic1Interpreter interpreter, final String ... params) {
      if (getSize(params) != EXPECTED_PARAMETERS) {
        Printer.printErrorln(Text.WRONG_PARAM_NUMBER.text(EXPECTED_PARAMETERS, getSize(params)));
      } else {
        final Integer a = (Integer) Parameter.NUMBER.getValue(params[0]);
        final Integer v = (Integer) Parameter.NUMBER.getValue(params[1]);
        if (a != null && v != null) {
          interpreter.getProcessor().setMemoryValue(a.intValue(), v.intValue());
        }
      }
      return true;
    }
  },

  /** executes the given number of macro instructions - or by default one, if no number is given */
  STEP {
    @Override
    public boolean execute(final Mic1Interpreter interpreter, final String ... params) {
      switch (getSize(params)) {
        case 0:
          interpreter.getProcessor().step();
          break;
        case 1:
          final Integer i = (Integer) Parameter.NUMBER.getValue(params[0]);
          if (i != null) {
            interpreter.getProcessor().step(i.intValue());
          }
          break;
        default:
          Printer.printErrorln(Text.WRONG_PARAM_NUMBER.text(0, getSize(params)));
          break;
      }

      return true;
    }
  },

  /** instruction to trace the micro code */
  TRACE_MAC {
    @Override
    public boolean execute(final Mic1Interpreter interpreter, final String ... params) {
      interpreter.traceMacro();
      return true;
    }
  },

  /** instruction to trace the micro code */
  TRACE_MIC {
    @Override
    public boolean execute(final Mic1Interpreter interpreter, final String ... params) {
      interpreter.traceMicro();
      return true;
    }
  },

  /** instruction to trace one or all registers */
  TRACE_REG {
    @Override
    public boolean execute(final Mic1Interpreter interpreter, final String ... params) {
      switch (getSize(params)) {
        case 0:
          interpreter.traceRegister();
          break;
        case 1:
          final Register r = (Register) Parameter.REGISTER.getValue(params[0]);
          interpreter.traceRegister(r);
          break;
        default:
          Printer.printErrorln(Text.WRONG_PARAM_NUMBER.text(0, getSize(params)));
          break;
      }
      return true;
    }
  },

  /** instruction to trace the given local variable */
  TRACE_VAR {
    @Override
    public boolean execute(final Mic1Interpreter interpreter, final String ... params) {
      if (getSize(params) == 1) {
        final Integer num = (Integer) Parameter.NUMBER.getValue(params[0]);
        if (num != null) {
          interpreter.traceLocalVariable(num.intValue());
        }
      } else {
        Printer.printErrorln(Text.WRONG_PARAM_NUMBER.text(1, getSize(params)));
      }
      return true;
    }
  },

  /** instruction to not trace the macro code anymore */
  UNTRACE_MAC {
    @Override
    public boolean execute(final Mic1Interpreter interpreter, final String ... params) {
      interpreter.untraceMacro();
      return true;
    }
  },

  /** instruction to not trace the micro code anymore */
  UNTRACE_MIC {
    @Override
    public boolean execute(final Mic1Interpreter interpreter, final String ... params) {
      interpreter.untraceMicro();
      return true;
    }
  },

  /** instruction to not trace one or all registers anymore */
  UNTRACE_REG {
    @Override
    public boolean execute(final Mic1Interpreter interpreter, final String ... params) {
      switch (getSize(params)) {
        case 0:
          interpreter.untraceRegister();
          break;
        case 1:
          final Register r = (Register) Parameter.REGISTER.getValue(params[0]);
          interpreter.untraceRegister(r);
          break;
        default:
          Printer.printErrorln(Text.WRONG_PARAM_NUMBER.text(0, getSize(params)));
          break;
      }
      return true;
    }
  },

  /** instruction to not trace the given local variable anymore */
  UNTRACE_VAR {
    @Override
    public boolean execute(final Mic1Interpreter interpreter, final String ... params) {
      if (getSize(params) == 1) {
        final Integer num = (Integer) Parameter.NUMBER.getValue(params[0]);
        if (num != null) {
          interpreter.untraceLocalVariable(num.intValue());
        }
      } else {
        Printer.printErrorln(Text.WRONG_PARAM_NUMBER.text(1, getSize(params)));
      }
      return true;
    }
  };

  /** the different ways this argument can be called */
  @NotNull
  private final String instruction;

  /**
   * Constructs a new instruction. Its name is used to execute the instruction. For example INSTRUCTION can be executed
   * with: <code>instruction</code> <br>
   * <b>Note:</b> A <code>_</code> in the name will be translated to a <code>-</code>.<br>
   * 
   * @since Date: Dec 3, 2011
   */
  private UserInstruction() {
    this.instruction = this.name().toLowerCase(Locale.GERMAN).replaceAll("_", "-");
  }

  /**
   * Returns whether this argument can be called with the given {@link String}. Will return <code>false</code>, if the
   * given {@link String} is <code>null</code> or if the {@link UserInstruction} is a pseudo-argument that cannot be
   * called.
   * 
   * @since Date: Dec 3, 2011
   * @param argStr the {@link String} to test if it's a possible call for this argument, mustn't be <code>null</code>
   * @return <code>true</code>, if this argument can be called with the given {@link String}.<br>
   *         For example <code>--argument</code> will return <code>true</code> for the argument <code>ARGUMENT</code>.
   */
  private boolean matches(final String argStr) {
    return argStr.equals(this.instruction);
  }

  /**
   * Returns the {@link UserInstruction} that matches with the given {@link String}.
   * 
   * @since Date: Aug 13, 2011
   * @param s the {@link String} that is able to call the returned {@link UserInstruction}.
   * @return the {@link UserInstruction} that matches the given {@link String}, or<br>
   *         <code>null</code> if no {@link UserInstruction} can be called with the given {@link String}.
   * @see UserInstruction#matches(String)
   */
  @Nullable
  static UserInstruction of(final String s) {
    if (s != null) {
      final String instruction = s.toLowerCase(Locale.GERMAN);
      for (final UserInstruction a : values()) {
        if (a.matches(instruction)) {
          return a;
        }
      }
    }
    return null;
  }

  /**
   * Executes the instruction with the given parameters.
   * 
   * @since Date: Dec 3, 2011
   * @param interpreter the interpreter to operate with, is not needed for every {@link UserInstruction}.
   * @param params the parameters of that {@link UserInstruction}.
   * @return <code>true</code>, if the application can continue<br>
   *         <code>false</code>, if the {@link UserInstruction} enforces the application to stop.
   */
  public abstract boolean execute(Mic1Interpreter interpreter, String ... params);

  /**
   * Returns the size of the given array or <code>0</code>, if the array is <code>null</code>.
   * 
   * @since Date: Jan 15, 2012
   * @param array the array to determine the size of
   * @return the size of the given array or zero, if the given array is <code>null</code>.
   */
  protected static int getSize(final Object[] array) {
    if (array == null) {
      return 0;
    }
    return array.length;
  }
}
