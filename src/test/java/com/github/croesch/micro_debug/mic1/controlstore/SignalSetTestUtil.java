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
package com.github.croesch.micro_debug.mic1.controlstore;

import static org.fest.assertions.Assertions.assertThat;

import java.lang.reflect.Method;

import org.junit.Ignore;

import com.github.croesch.micro_debug.DefaultTestCase;

/**
 * Provides utility methods for tests of {@link SignalSet}-objects.
 * 
 * @author croesch
 * @since Date: Nov 12, 2011
 */
@Ignore
public class SignalSetTestUtil extends DefaultTestCase {

  protected void testIsSetSignals(final SignalSet set, final String[] signals) throws Exception {
    printlnMethodName(1);

    for (final String signal : signals) {

      System.out.println("\tsignal=" + signal);

      final Method is = set.getClass().getMethod("is" + signal, new Class<?>[] {});
      final Method setM = set.getClass().getMethod("set" + signal, new Class<?>[] { boolean.class });

      assertThat(is.invoke(set, new Object[] {})).isEqualTo(Boolean.FALSE);

      assertThat(setM.invoke(set, new Object[] { false }));
      assertThat(is.invoke(set, new Object[] {})).isEqualTo(Boolean.FALSE);
      assertThat(is.invoke(set, new Object[] {})).isEqualTo(Boolean.FALSE);

      assertThat(setM.invoke(set, new Object[] { true }));
      assertThat(is.invoke(set, new Object[] {})).isEqualTo(Boolean.TRUE);

      assertThat(setM.invoke(set, new Object[] { true }));
      assertThat(is.invoke(set, new Object[] {})).isEqualTo(Boolean.TRUE);
      assertThat(is.invoke(set, new Object[] {})).isEqualTo(Boolean.TRUE);

      assertThat(setM.invoke(set, new Object[] { false }));
      assertThat(is.invoke(set, new Object[] {})).isEqualTo(Boolean.FALSE);

      assertThat(setM.invoke(set, new Object[] { true }));
      assertThat(is.invoke(set, new Object[] {})).isEqualTo(Boolean.TRUE);
    }
  }

  protected void testEquals(final SignalSet set, final SignalSet other, final String[] signals)
                                                                                                       throws Exception {
    printlnMethodName(1);

    assertThat(set).isNotEqualTo(null);
    assertThat(set).isNotEqualTo("set");
    assertThat(set).isEqualTo(set);
    assertThat(set.hashCode()).isEqualTo(set.hashCode());

    assertThat(set).isEqualTo(other);
    assertThat(set.hashCode()).isEqualTo(other.hashCode());

    for (final boolean value : new boolean[] { true, false, true, false }) {
      for (final String signal : signals) {

        System.out.println("\tsignal=" + signal + ", value=" + value);
        final Method setM = set.getClass().getMethod("set" + signal, new Class<?>[] { boolean.class });

        assertThat(setM.invoke(set, new Object[] { value }));
        assertThat(set).isNotEqualTo(other);
        assertThat(set.hashCode()).isNotEqualTo(other.hashCode());
        assertThat(setM.invoke(other, new Object[] { value }));
      }
    }
  }

}
