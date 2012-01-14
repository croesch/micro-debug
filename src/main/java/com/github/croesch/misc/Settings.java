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
package com.github.croesch.misc;

import java.io.IOException;
import java.util.Properties;

/**
 * An enumeration of some settings that are made in a property-file.<br>
 * TODO provide default values to avoid user errors
 * 
 * @author croesch
 * @since Date: Jan 14, 2012
 */
public enum Settings {

  /** contains the default value for the {@link com.github.croesch.mic1.register.Register#CPP} */
  MIC1_REGISTER_CPP_DEFVAL,
  /** contains the default value for the {@link com.github.croesch.mic1.register.Register#H} */
  MIC1_REGISTER_H_DEFVAL,
  /** contains the default value for the {@link com.github.croesch.mic1.register.Register#LV} */
  MIC1_REGISTER_LV_DEFVAL,
  /** contains the default value for the {@link com.github.croesch.mic1.register.Register#MAR} */
  MIC1_REGISTER_MAR_DEFVAL,
  /** contains the default value for the {@link com.github.croesch.mic1.register.Register#MBR} */
  MIC1_REGISTER_MBR_DEFVAL,
  /** contains the default value for the {@link com.github.croesch.mic1.register.Register#MDR} */
  MIC1_REGISTER_MDR_DEFVAL,
  /** contains the default value for the {@link com.github.croesch.mic1.register.Register#OPC} */
  MIC1_REGISTER_OPC_DEFVAL,
  /** contains the default value for the {@link com.github.croesch.mic1.register.Register#PC} */
  MIC1_REGISTER_PC_DEFVAL,
  /** contains the default value for the {@link com.github.croesch.mic1.register.Register#SP} */
  MIC1_REGISTER_SP_DEFVAL,
  /** contains the default value for the {@link com.github.croesch.mic1.register.Register#TOS} */
  MIC1_REGISTER_TOS_DEFVAL;

  /** the value set up in the properties file */
  private int value;

  /** the properties loaded from the file */
  private static Properties props = null;

  /**
   * Constructs this setting. Loads the properties from file, if not yet done and fetches the value for this setting.
   * The key is the name of the setting.
   * 
   * @since Date: Jan 15, 2012
   */
  private Settings() {
    final String key = name().toLowerCase().replaceAll("_", ".");
    final String val = getProperties().getProperty(key);
    final Integer number = (Integer) Parameter.NUMBER.getValue(val);
    this.value = number.intValue();
  }

  /**
   * Returns the properties read from the properties file.
   * 
   * @since Date: Jan 15, 2012
   * @return {@link Properties} read from the specific file.
   */
  private static synchronized Properties getProperties() {
    if (props == null) {
      props = new Properties();
      try {
        props.load(ClassLoader.getSystemResourceAsStream("micro-debug.properties"));
      } catch (final IOException e) {
        e.printStackTrace();
      }
    }
    return props;
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
