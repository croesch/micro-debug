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
package com.github.croesch.i18n;

import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.logging.Logger;

/**
 * This class provides access to the internationalized text resources.
 * 
 * @author croesch
 * @since Date: Dec 2, 2011
 */
public enum Text {

  /** the text for the version of the program. */
  VERSION;

  /** the {@link Logger} for this class */
  private final Logger logger = Logger.getLogger(Text.class.getName());

  /** the value of this instance */
  private final String string;

  /**
   * Constructs a new instance of a text that is part of the i18n. Each key will be searched in the file
   * 'lang/text*.properties' (where '*' is a string build from the locales properties language, country and variant, so
   * there will be four file names and the most specific will be searched first). The name of this enumeration is the
   * suffix of the key where underscores will be replaced by minuses.
   * 
   * @since Date: Dec 2, 2011
   */
  private Text() {
    final ResourceBundle b = ResourceBundle.getBundle("lang/text", new XMLBundleControl());
    final String key = name().toLowerCase().replace('_', '-');
    String value;
    try {
      value = b.getString(key);
    } catch (final MissingResourceException mre) {
      this.logger.warning("missing ressource key=" + key);
      value = "!!missing-key=" + key + "!!";
    }
    this.string = value;
  }

  @Override
  public String toString() {
    return text();
  }

  /**
   * String representation of this object
   * 
   * @since Date: Dec 2, 2011
   * @return the String that represents the object
   */
  public String text() {
    return this.string;
  }

  /**
   * String representation of this object, but {x} will be replaced by argument number x starting to count from 0.
   * 
   * @since Date: Dec 2, 2011
   * @param s the replacements
   * @return the String that represents the object with replaced placeholders
   */
  public String text(final Object ... s) {
    String text = this.string;
    for (int i = 0; i < s.length; ++i) {
      // prevent exceptions with using $
      final String param = s[i].toString().replaceAll("\\$", "\\\\\\$");
      text = text.replaceAll("(^|[^{])\\{" + i + "\\}", "$1" + param);
      text = text.replaceAll("\\{\\{" + i + "\\}", "\\{" + i + "\\}");
    }
    return text;
  }
}
