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

import com.github.croesch.misc.Utils;

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

  /**
   * Properties that are filled with the properties fetched from the lang/testX.xml files, where X is defined by the
   * attributes of the {@link Locale}.
   * 
   * @since Date: Jan 25, 2012
   */
  public TextProperties() {
    final String language = Locale.getDefault().getLanguage();
    final String country = Locale.getDefault().getCountry();
    final String variant = Locale.getDefault().getVariant();

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
      Utils.logThrownThrowable(e);
    } catch (final IOException e) {
      Utils.logThrownThrowable(e);
    } catch (final NullPointerException e) {
      Utils.logThrownThrowable(e);
    } catch (final SecurityException e) {
      Utils.logThrownThrowable(e);
    }
  }
}
