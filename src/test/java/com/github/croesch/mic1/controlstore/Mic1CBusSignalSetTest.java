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
package com.github.croesch.mic1.controlstore;

import org.junit.Test;

/**
 * Provides test cases for {@link CBusSignalSet}.
 * 
 * @author croesch
 * @since Date: Nov 12, 2011
 */
public class Mic1CBusSignalSetTest extends Mic1SignalSetTestUtil {

  @Test
  public void testSetIsSignal() throws Exception {
    final CBusSignalSet set = new CBusSignalSet();
    testIsSetSignals(set, new String[] { "Cpp", "H", "Lv", "Mar", "Mdr", "Opc", "Pc", "Sp", "Tos" });
  }

  @Test
  public void testEqualsObject() throws Exception {
    final CBusSignalSet set = new CBusSignalSet();
    final CBusSignalSet other = new CBusSignalSet();

    testEquals(set, other, new String[] { "Cpp", "H", "Lv", "Mar", "Mdr", "Opc", "Pc", "Sp", "Tos" });
  }
}
