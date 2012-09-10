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
package com.github.croesch.micro_debug.i18n;

import com.github.croesch.micro_debug.annotation.NotNull;
import com.github.croesch.micro_debug.properties.XMLPropertiesProvider;

/**
 * This class provides access to the internationalized text resources.
 * 
 * @author croesch
 * @since Date: Dec 2, 2011
 */
public enum Text {

  /** the text for the version of the program. */
  VERSION,

  /** the text to visualize a border */
  BORDER,

  /** the text that is printed when the program is started, before any error message */
  GREETING,
  /** the text that is printed, when the program is starting. Only if no error occurred */
  WELCOME,

  /** the text that should be printed before user input is requested from debugger */
  INPUT_DEBUGGER,
  /** the text that should be printed before user input is requested from the mic1-processor */
  INPUT_MIC1,

  /** the text to view how many ticks have been executed by the processor */
  TICKS,

  /** the text to print the value of a register */
  REGISTER_VALUE,
  /** the text to print the value of a local variable */
  LOCAL_VARIABLE_VALUE,

  /** the text to print the executed code */
  EXECUTED_CODE,

  /** the text to print the content of the memory */
  MEMORY_CONTENT,
  /** the text to print an entry of the stack */
  STACK_CONTENT,
  /** the text to display an empty stack */
  STACK_EMPTY,

  /** the text to print only a line of macro code - without line number */
  MACRO_CODE_PLAIN,
  /** the text to print a single line of macro code */
  MACRO_CODE_LINE,
  /** the text to print a single line of micro code */
  MICRO_CODE_LINE,

  /** the text to print the breakpoint for a specific register and value */
  BREAKPOINT_REGISTER,
  /** the text to print the breakpoint for a specific register */
  BREAKPOINT_WRITE_REGISTER,
  /** the text to print the breakpoint for a specific line in macro code */
  BREAKPOINT_MACRO,
  /** the text to print the breakpoint for a specific line in micro code */
  BREAKPOINT_MICRO,

  /** the text that describes an unknown ijvm instruction */
  UNKNOWN_IJVM_INSTRUCTION,

  // descriptions of different problems

  /** the text to format an error line output */
  ERROR,

  /** describes an invalid memory address */
  INVALID_MEM_ADDR,
  /** describes an invalid number */
  INVALID_NUMBER,
  /** describes an invalid register */
  INVALID_REGISTER,
  /** describes an invalid debug mode */
  INVALID_DEBUG_MODE,

  /** describes an unknown instruction */
  UNKNOWN_INSTRUCTION,
  /** the text to describe an unknown argument */
  UNKNOWN_ARGUMENT,
  /** the text to describe that the argument has the wrong number of parameters */
  ARGUMENT_WITH_WRONG_PARAM_NUMBER,

  /** the text to describe that there were to few parameters */
  WRONG_PARAM_NUMBER,

  /** the text to describe that the argument for the ijvm-file is missing */
  MISSING_IJVM_FILE,
  /** the text to describe that the argument for the mic1-file is missing */
  MISSING_MIC1_FILE,

  /** describes that the mic1-file has a wrong file format */
  WRONG_FORMAT_MIC1,
  /** describes that the ijvm-file has a wrong file format */
  WRONG_FORMAT_IJVM,

  /** describes that the specific file couldn't be found */
  FILE_NOT_FOUND,

  /** the text to give a hint the user should try to read the help */
  TRY_HELP;

  /** the value of this instance */
  @NotNull
  private final String string;

  /**
   * Constructs a new instance of a text that is part of the i18n. Each key will be searched in the file
   * 'lang/text*.xml' (where '*' is a string build from the locales properties language, country and variant, so there
   * will be four file names and the most specific will be searched first). The name of this enumeration is the suffix
   * of the key where underscores will be replaced by minuses.
   * 
   * @since Date: Dec 2, 2011
   */
  private Text() {
    this.string = XMLPropertiesProvider.getInstance().get("lang/text", name());
  }

  @Override
  @NotNull
  public String toString() {
    return text();
  }

  /**
   * String representation of this object
   * 
   * @since Date: Dec 2, 2011
   * @return the String that represents the object
   */
  @NotNull
  public String text() {
    return this.string;
  }

  /**
   * String representation of this object, but {x} will be replaced by argument number x starting to count from 0.
   * 
   * @since Date: Dec 2, 2011
   * @param s the replacements
   * @return the String that represents the object with replaced placeholders
   * @see XMLPropertiesProvider#replacePlaceholdersInString(String, Object...)
   */
  @NotNull
  public String text(final Object ... s) {
    return XMLPropertiesProvider.replacePlaceholdersInString(this.string, s);
  }
}
