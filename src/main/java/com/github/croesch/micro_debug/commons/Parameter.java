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

import java.util.Locale;

import com.github.croesch.micro_debug.i18n.Text;
import com.github.croesch.micro_debug.mic1.register.Register;

/**
 * Represents the different types of parameter that are possible.
 * 
 * @author croesch
 * @since Date: Dec 3, 2011
 */
public enum Parameter {

  /** the numerical argument, can be decimal or every other basis */
  NUMBER {

    /**
     * Converts valid aliases like <code>0x</code> to their representation in the notation.
     * 
     * @since Date: Jan 28, 2012
     * @param num the number that might contain aliases
     * @return the number with aliases converted to notation
     */
    private String convertAliases(final String num) {
      if (num.length() > 1 && num.charAt(0) == '0') {
        switch (Character.toLowerCase(num.charAt(1))) {
          case 'b':
            return num.substring(2) + "_2";
          case 'o':
            return num.substring(2) + "_8";
          case 'x':
            return num.substring(2) + "_16";
          default:
            return num;
        }
      }
      return num;
    }

    @Override
    protected Object toValue(final String str) {

      try {
        // try parsing and if this works, it was a valid number
        return Integer.valueOf(str);
      } catch (final NumberFormatException nfe) {
        // number might be 0x.. or .._.., so split the number in radix and the number
        // convert aliases (0x,0b,..) to the correct notation
        final String[] num = convertAliases(str).split("_");
        if (num.length != 2) {
          // not a valid number - no idea what to do
          Printer.printErrorln(Text.INVALID_NUMBER.text(str));
          return null;
        }

        // number is a special number
        try {
          // try to parse the number with the specified radix
          return Integer.valueOf(num[0], Integer.parseInt(num[1]));
        } catch (final NumberFormatException nfe2) {
          // didn't work -> invalid number
          Printer.printErrorln(Text.INVALID_NUMBER.text(str));
          return null;
        }
      }
    }
  },

  /** a {@link Register} as argument */
  REGISTER {
    @Override
    protected Object toValue(final String str) {
      try {
        return Register.valueOf(str.toUpperCase(Locale.GERMAN));
      } catch (final IllegalArgumentException e) {
        Printer.printErrorln(Text.INVALID_REGISTER.text(str));
        return null;
      }
    }
  };

  /**
   * Converts the given {@link String} to an object that has the expected type. Returns an object with the logical type
   * of the enumeration that has the given value. Returns <code>null</code>, if the given string is not a valid value.<br>
   * Informs the user (per {@link Printer}) about a wrong value.
   * 
   * @since Date: Dec 3, 2011
   * @param str the value to convert into the correct data type, is not <code>null</code>
   * @return an {@link Object} with the logical type of the enumeration that has the value given by the given string,<br>
   *         or <code>null</code> if the given {@link String} is no valid representation for any value of the data type
   */
  protected abstract Object toValue(String str);

  /**
   * Converts the given {@link String} to an object that has the expected type. Returns an object with the logical type
   * of the enumeration that has the given value. Returns <code>null</code>, if the given string is not a valid value.
   * 
   * @since Date: Dec 3, 2011
   * @param str the value to convert into the correct data type, can be <code>null</code>
   * @return an {@link Object} with the logical type of the enumeration that has the value given by the given string,<br>
   *         or <code>null</code> if the given {@link String} is no valid representation for any value of the data type
   */
  public final Object getValue(final String str) {
    if (str == null) {
      // null is never valid
      return null;
    }

    return toValue(str);
  }
}
