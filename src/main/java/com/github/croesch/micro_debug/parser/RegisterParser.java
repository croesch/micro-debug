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
package com.github.croesch.micro_debug.parser;

import java.util.Locale;

import com.github.croesch.micro_debug.annotation.Nullable;
import com.github.croesch.micro_debug.mic1.register.Register;

/**
 * Parses {@link Register}s from {@link String}s.
 * 
 * @author croesch
 * @since Date: Feb 22, 2012
 */
public final class RegisterParser implements IParser {

  /**
   * {@inheritDoc}
   */
  @Nullable
  public Register parse(final String toParse) {
    if (toParse == null) {
      return null;
    }

    try {
      return Register.valueOf(toParse.toUpperCase(Locale.GERMAN));
    } catch (final IllegalArgumentException e) {
      return null;
    }
  }
}
