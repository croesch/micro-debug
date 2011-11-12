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
