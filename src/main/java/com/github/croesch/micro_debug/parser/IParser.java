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

/**
 * Parses {@link String}s to {@link Object}s.
 * 
 * @author croesch
 * @since Date: Feb 22, 2012
 */
public interface IParser {

  /**
   * Parses the given {@link String} and returns an {@link Object} of the specific type. If the input cannot be parsed
   * into a valid object, than it will return <code>null</code>.
   * 
   * @since Date: Feb 22, 2012
   * @param toParse the {@link String} to convert to an {@link Object}.
   * @return the {@link Object} read from the {@link String},<br>
   *         or <code>null</code> if the input was invalid
   */
  Object parse(String toParse);

}
