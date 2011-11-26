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

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

/**
 * Provides test cases for {@link Mic1SignalSet}.
 * 
 * @author croesch
 * @since Date: Nov 12, 2011
 */
public class Mic1SignalSetTest {

  @Test
  public void testEqualsObject() {
    final Mic1SignalSet set1 = new Mic1SignalSet(12);
    final Mic1SignalSet set2 = new Mic1SignalSet(11);
    final Mic1SignalSet set3 = new Mic1MemorySignalSet();
    final Mic1SignalSet set4 = new Mic1CBusSignalSet();

    assertThat(set1).isNotEqualTo(set2);
    assertThat(set1).isNotEqualTo(set3);
    assertThat(set1).isNotEqualTo(set4);
    assertThat(set2).isNotEqualTo(set1);
    assertThat(set2).isNotEqualTo(set3);
    assertThat(set2).isNotEqualTo(set4);
    assertThat(set3).isNotEqualTo(set1);
    assertThat(set3).isNotEqualTo(set2);
    assertThat(set3).isNotEqualTo(set4);
    assertThat(set4).isNotEqualTo(set1);
    assertThat(set4).isNotEqualTo(set2);
    assertThat(set4).isNotEqualTo(set3);
  }
}
