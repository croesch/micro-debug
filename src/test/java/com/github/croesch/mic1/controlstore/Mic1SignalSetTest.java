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

import com.github.croesch.TestUtil;

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

  @Test
  public void testGetSize() {
    TestUtil.printMethodName();

    for (int i = 0; i < 100; ++i) {
      final Mic1SignalSet set = new Mic1SignalSet(i);
      assertThat(set.getSize()).isEqualTo(i);
      TestUtil.printStep();
    }

    TestUtil.printEndOfMethod();
  }

  @Test
  public void testIsSetSomething() {
    final Mic1SignalSet set = new Mic1SignalSet(5);
    set.set(0, true);
    set.set(1, true);
    set.set(2, true);
    set.set(3, true);
    set.set(4, true);
    assertThat(set.isAnythingSet()).isTrue();

    set.set(0, false);
    assertThat(set.isAnythingSet()).isTrue();

    set.set(1, false);
    assertThat(set.isAnythingSet()).isTrue();

    set.set(2, false);
    assertThat(set.isAnythingSet()).isTrue();

    set.set(3, false);
    assertThat(set.isAnythingSet()).isTrue();

    set.set(4, false);
    assertThat(set.isAnythingSet()).isFalse();
  }

  @Test
  public void testIsSetSomething_BruteForce() {
    TestUtil.printMethodName();

    for (int size = 0; size < 100; ++size) {
      final Mic1SignalSet set = new Mic1SignalSet(size);
      for (int i = 0; i < set.getSize(); ++i) {
        assertThat(set.isAnythingSet()).isFalse();

        set.set(i, true);
        assertThat(set.isAnythingSet()).isTrue();

        set.set(i, false);
        assertThat(set.isAnythingSet()).isFalse();

        TestUtil.printStep();
      }
      TestUtil.printLoopEnd();
    }
    TestUtil.printEndOfMethod();
  }
}
