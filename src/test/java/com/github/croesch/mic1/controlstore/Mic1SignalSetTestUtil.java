package com.github.croesch.mic1.controlstore;

import static org.fest.assertions.Assertions.assertThat;

import java.lang.reflect.Method;

import org.junit.Ignore;

import com.github.croesch.TestUtil;

/**
 * Provides utility methods for tests of {@link Mic1SignalSet}-objects.
 * 
 * @author croesch
 * @since Date: Nov 12, 2011
 */
@Ignore
public class Mic1SignalSetTestUtil {

  protected void testIsSetSignals(final Mic1SignalSet set, final String[] signals) throws Exception {
    TestUtil.printlnMethodName(1);

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

  protected void testEquals(final Mic1SignalSet set, final Mic1SignalSet other, final String[] signals)
                                                                                                       throws Exception {
    TestUtil.printlnMethodName(1);

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
