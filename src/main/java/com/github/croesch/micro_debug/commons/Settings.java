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
package com.github.croesch.micro_debug.commons;

import com.github.croesch.micro_debug.parser.IntegerParser;
import com.github.croesch.micro_debug.properties.PropertiesProvider;

/**
 * An enumeration of some settings that are made in a property-file.<br>
 * 
 * @author croesch
 * @since Date: Jan 14, 2012
 */
public enum Settings {

  /** the number of elements to hide, when printing the stack to the user */
  STACK_ELEMENTS_TO_HIDE (1),

  /** contains the default value for the {@link com.github.croesch.micro_debug.mic1.register.Register#CPP} */
  MIC1_REGISTER_CPP_DEFVAL (0x4000),
  /** contains the default value for the {@link com.github.croesch.micro_debug.mic1.register.Register#H} */
  MIC1_REGISTER_H_DEFVAL (0),
  /** contains the default value for the {@link com.github.croesch.micro_debug.mic1.register.Register#LV} */
  MIC1_REGISTER_LV_DEFVAL (0x8000),
  /** contains the default value for the {@link com.github.croesch.micro_debug.mic1.register.Register#MAR} */
  MIC1_REGISTER_MAR_DEFVAL (0),
  /** contains the default value for the {@link com.github.croesch.micro_debug.mic1.register.Register#MBR} */
  MIC1_REGISTER_MBR_DEFVAL (0),
  /** contains the default value for the {@link com.github.croesch.micro_debug.mic1.register.Register#MDR} */
  MIC1_REGISTER_MDR_DEFVAL (0),
  /** contains the default value for the {@link com.github.croesch.micro_debug.mic1.register.Register#OPC} */
  MIC1_REGISTER_OPC_DEFVAL (0),
  /** contains the default value for the {@link com.github.croesch.micro_debug.mic1.register.Register#PC} */
  MIC1_REGISTER_PC_DEFVAL (-1),
  /** contains the default value for the {@link com.github.croesch.micro_debug.mic1.register.Register#SP} */
  MIC1_REGISTER_SP_DEFVAL (0xC000),
  /** contains the default value for the {@link com.github.croesch.micro_debug.mic1.register.Register#TOS} */
  MIC1_REGISTER_TOS_DEFVAL (0),

  /** the width of a formatted address of a micro code instruction */
  MIC1_MEM_MICRO_ADDR_WIDTH (5),
  /** the width of a formatted address of a macro/ijvm code instruction */
  MIC1_MEM_MACRO_ADDR_WIDTH (8),
  /** contains the maximum size of the {@link com.github.croesch.micro_debug.mic1.mem.Memory} */
  MIC1_MEM_MACRO_MAXSIZE (0x10000),

  /** the address of micro assembler code that reads the next ijvm instruction */
  MIC1_MICRO_ADDRESS_IJVM (0x2);

  /** the value set up in the properties file */
  private int value;

  /**
   * Constructs this setting. Loads the properties from file, if not yet done and fetches the value for this setting.
   * The key is the name of the setting.
   * 
   * @since Date: Jan 15, 2012
   * @param defaultValue value of the setting if the properties file doesn't contain a valid value
   */
  private Settings(final int defaultValue) {
    final String val = PropertiesProvider.getInstance().get("micro-debug", name());
    final Integer number = new IntegerParser().parse(val);

    if (number == null) {
      this.value = defaultValue;
    } else {
      this.value = number.intValue();
    }
  }

  /**
   * Returns the value of this setting.
   * 
   * @since Date: Jan 15, 2012
   * @return the value of this setting, read from the properties file.
   */
  public int getValue() {
    return this.value;
  }
}
