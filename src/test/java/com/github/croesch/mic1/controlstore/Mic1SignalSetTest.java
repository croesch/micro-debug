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

import com.github.croesch.DefaultTestCase;

/**
 * Provides test cases for {@link SignalSet}.
 * 
 * @author croesch
 * @since Date: Nov 12, 2011
 */
public class Mic1SignalSetTest extends DefaultTestCase {

  @Test
  public void testCopyOf() {
    final SignalSet set1 = new SignalSet(12);
    final SignalSet set2 = new SignalSet(11);
    final SignalSet set3 = new SignalSet(11);
    set1.set(0, true);
    set2.set(1, true);

    assertThat(set2.is(0)).isFalse();
    assertThat(set3.is(0)).isFalse();
    assertThat(set3.is(1)).isFalse();

    // assert that this is no problem
    set3.copyOf(null);
    set3.copyOf(set1);
    assertThat(set3.is(0)).isFalse();

    set3.copyOf(set2);
    assertThat(set3.is(1)).isTrue();
  }

  @Test
  public void testEqualsObject() {
    final SignalSet set1 = new SignalSet(12);
    final SignalSet set2 = new SignalSet(11);
    final SignalSet set3 = new Mic1MemorySignalSet();
    final SignalSet set4 = new CBusSignalSet();

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
    printMethodName();

    for (int i = 0; i < 100; ++i) {
      final SignalSet set = new SignalSet(i);
      assertThat(set.getSize()).isEqualTo(i);
      printStep();
    }

    printEndOfMethod();
  }

  @Test
  public void testIsSetSomething() {
    final SignalSet set = new SignalSet(5);
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
    printMethodName();

    for (int size = 0; size < 100; ++size) {
      final SignalSet set = new SignalSet(size);
      for (int i = 0; i < set.getSize(); ++i) {
        assertThat(set.isAnythingSet()).isFalse();

        set.set(i, true);
        assertThat(set.isAnythingSet()).isTrue();

        set.set(i, false);
        assertThat(set.isAnythingSet()).isFalse();

        printStep();
      }
      printLoopEnd();
    }
    printEndOfMethod();
  }
}
