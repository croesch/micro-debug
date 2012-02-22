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

import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Provides properties read from xml files.
 * 
 * @author croesch
 * @since Date: Feb 22, 2012
 */
public final class XMLPropertiesProvider {

  /** contains the properties stored in the map with their base file names */
  private final Map<String, Properties> propertiesMap = new ConcurrentHashMap<String, Properties>();

  /**
   * Hidden constructor to be able to create a singleton.
   * 
   * @since Date: Feb 22, 2012
   */
  private XMLPropertiesProvider() {
    // private constructor
  }

  /**
   * Returns the value for the given key in the given file.
   * 
   * @since Date: Feb 22, 2012
   * @param file the path of the file, <b>without</b> file extension!
   * @param key the key to fetch the value for
   * @return the value read from the xml properties
   */
  public String get(final String file, final String key) {
    return getProperties(file).getProperty(key);
  }

  /**
   * Returns the properties belonging to the given file path.
   * 
   * @since Date: Feb 22, 2012
   * @param file the path to the file of the properties
   * @return the properties belonging to the given file path.
   */
  private Properties getProperties(final String file) {
    if (!this.propertiesMap.containsKey(file)) {
      this.propertiesMap.put(file, new TextProperties(file, Locale.getDefault()));
    }
    return this.propertiesMap.get(file);
  }

  /**
   * Provides the instance of this {@link XMLPropertiesProvider}.
   * 
   * @since Date: Feb 22, 2012
   * @return the single instance of this class.
   */
  public static XMLPropertiesProvider getInstance() {
    return LazyHolder.INSTANCE;
  }

  /**
   * Replaces all placeholders in the given string, with the given arguments.
   * 
   * @since Date: Feb 22, 2012
   * @param str the string to replace the placeholders in
   * @param args the arguments
   * @return the string filled with arguments
   */
  public static String replacePlaceholdersInString(final String str, final Object ... args) {
    String text = str;
    if (args != null) {
      for (int i = 0; i < args.length; ++i) {
        text = replacePlaceholderInString(i, text, args[i]);
      }
    }
    return replaceEscapedPlaceholder(text);
  }

  /**
   * Replaces the placeholder with the given number in the given string, with the given argument.
   * 
   * @since Date: Feb 22, 2012
   * @param number the number of the placeholder and argument
   * @param str the string to replace the placeholders in
   * @param arg the argument
   * @return the string with the placeholder replaced
   */
  private static String replacePlaceholderInString(final int number, final String str, final Object arg) {
    final String preparedArgument = prepareArgument(arg);
    return str.replaceAll("(^|[^{])\\{" + number + "\\}", "$1" + preparedArgument);
  }

  /**
   * Unescapes escaped placeholders in the given string.
   * 
   * @since Date: Feb 22, 2012
   * @param str the string, maybe contains escaped placeholders
   * @return string with unescaped placeholders.
   */
  private static String replaceEscapedPlaceholder(final String str) {
    return str.replaceAll("\\{\\{(\\d)\\}", "\\{$1\\}");
  }

  /**
   * Prepares the given argument for being a replacement.
   * 
   * @since Date: Feb 22, 2012
   * @param arg the argument to prepare
   * @return the prepared argument
   */
  private static String prepareArgument(final Object arg) {
    if (arg == null || arg.toString() == null) {
      return "null";
    }
    // prevent exceptions with using $
    return arg.toString().replaceAll("\\$", "\\\\\\$");
  }

  /**
   * Initialization on Demand Holder.
   * 
   * @author croesch
   * @since Date: Jan 25, 2012
   */
  private static final class LazyHolder {
    /** instance of {@link TextProperties} */
    public static final XMLPropertiesProvider INSTANCE = new XMLPropertiesProvider();

    /**
     * Hidden constructor..
     * 
     * @since Date: Jan 25, 2012
     */
    private LazyHolder() {
      throw new AssertionError();
    }
  }
}
