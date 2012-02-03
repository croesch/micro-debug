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

import java.io.IOException;
import java.util.InvalidPropertiesFormatException;
import java.util.Locale;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Properties that are filled with the properties fetched from the lang/testX.xml files, where X is defined by the
 * attributes of the {@link Locale}.
 * 
 * @author croesch
 * @since Date: Jan 25, 2012
 */
public class TextProperties extends Properties {

  /** generated serial version uid */
  private static final long serialVersionUID = -4384001694719486867L;

  /** logger for this class */
  private static final Logger LOGGER = Logger.getLogger(TextProperties.class.getName());

  /**
   * Properties that are filled with the properties fetched from the lang/testX.xml files, where X is defined by the
   * attributes of the {@link Locale}.
   * 
   * @since Date: Jan 25, 2012
   * @param loc the {@link Locale} to fetch the language, country and variant from
   */
  public TextProperties(final Locale loc) {
    final String language = loc.getLanguage();
    final String country = loc.getCountry();
    final String variant = loc.getVariant();

    final StringBuffer temp = new StringBuffer();
    loadProperties(temp);

    if (language.length() == 0) {
      return;
    }
    temp.append('_').append(language);
    loadProperties(temp);

    if (country.length() == 0) {
      return;
    }
    temp.append('_').append(country);
    loadProperties(temp);

    if (variant.length() == 0) {
      return;
    }
    temp.append('_').append(variant);
    loadProperties(temp);
  }

  /**
   * Fills the given properties with the key value pairs fetched from the file with the fiven appendix.
   * 
   * @since Date: Jan 24, 2012
   * @param appendix the appendix for the file that contains the key-value pairs
   */
  private void loadProperties(final StringBuffer appendix) {
    try {
      loadFromXML(getClass().getClassLoader().getResourceAsStream("lang/text" + appendix.toString() + ".xml"));
    } catch (final InvalidPropertiesFormatException e) {
      logException(e);
    } catch (final IOException e) {
      logException(e);
    } catch (final SecurityException e) {
      logException(e);
    } catch (final RuntimeException e) {
      logException(e);
    }
  }

  /**
   * Logs the exception that happened in critical part of the program.
   * 
   * @since Date: Feb 3, 2012
   * @param e the {@link Throwable} that was thrown
   */
  private void logException(final Throwable e) {
    final String className = Thread.currentThread().getStackTrace()[2].getClassName();
    final String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
    LOGGER.severe(e.getMessage());
    LOGGER.throwing(className, methodName, e);
  }
}
