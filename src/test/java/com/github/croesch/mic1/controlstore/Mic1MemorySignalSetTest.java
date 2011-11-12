package com.github.croesch.mic1.controlstore;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

/**
 * Provides test cases for {@link Mic1MemorySignalSet}.
 * 
 * @author croesch
 * @since Date: Nov 12, 2011
 */
public class Mic1MemorySignalSetTest {

  @Test
  public void testIsWrite() {
    final Mic1MemorySignalSet set = new Mic1MemorySignalSet();
    assertThat(set.isWrite()).isFalse();

    set.setWrite(false);
    assertThat(set.isWrite()).isFalse();
    assertThat(set.isWrite()).isFalse();

    set.setWrite(true);
    assertThat(set.isWrite()).isTrue();

    set.setWrite(true);
    assertThat(set.isWrite()).isTrue();
    assertThat(set.isWrite()).isTrue();

    set.setWrite(false);
    assertThat(set.isWrite()).isFalse();

    set.setWrite(true);
    assertThat(set.isWrite()).isTrue();
  }

  @Test
  public void testIsRead() {
    final Mic1MemorySignalSet set = new Mic1MemorySignalSet();
    assertThat(set.isRead()).isFalse();

    set.setRead(false);
    assertThat(set.isRead()).isFalse();
    assertThat(set.isRead()).isFalse();

    set.setRead(true);
    assertThat(set.isRead()).isTrue();

    set.setRead(true);
    assertThat(set.isRead()).isTrue();
    assertThat(set.isRead()).isTrue();

    set.setRead(false);
    assertThat(set.isRead()).isFalse();

    set.setRead(true);
    assertThat(set.isRead()).isTrue();
  }

  @Test
  public void testIsFetch() {
    final Mic1MemorySignalSet set = new Mic1MemorySignalSet();
    assertThat(set.isFetch()).isFalse();

    set.setFetch(false);
    assertThat(set.isFetch()).isFalse();
    assertThat(set.isFetch()).isFalse();

    set.setFetch(true);
    assertThat(set.isFetch()).isTrue();

    set.setFetch(true);
    assertThat(set.isFetch()).isTrue();
    assertThat(set.isFetch()).isTrue();

    set.setFetch(false);
    assertThat(set.isFetch()).isFalse();

    set.setFetch(true);
    assertThat(set.isFetch()).isTrue();
  }

  @Test
  public void testEqualsObject() {
    final Mic1MemorySignalSet set = new Mic1MemorySignalSet();

    assertThat(set).isNotEqualTo(null);
    assertThat(set).isNotEqualTo("set");
    assertThat(set).isEqualTo(set);
    assertThat(set.hashCode()).isEqualTo(set.hashCode());

    final Mic1MemorySignalSet other = new Mic1MemorySignalSet();
    assertThat(set).isEqualTo(other);
    assertThat(set.hashCode()).isEqualTo(other.hashCode());

    set.setFetch(true);
    assertThat(set).isNotEqualTo(other);
    assertThat(set.hashCode()).isNotEqualTo(other.hashCode());
    other.setFetch(true);

    set.setRead(true);
    assertThat(set).isNotEqualTo(other);
    assertThat(set.hashCode()).isNotEqualTo(other.hashCode());
    other.setRead(true);

    set.setWrite(true);
    assertThat(set).isNotEqualTo(other);
    assertThat(set.hashCode()).isNotEqualTo(other.hashCode());
    other.setWrite(true);

    set.setFetch(false);
    assertThat(set).isNotEqualTo(other);
    assertThat(set.hashCode()).isNotEqualTo(other.hashCode());
    other.setFetch(false);

    set.setRead(false);
    assertThat(set).isNotEqualTo(other);
    assertThat(set.hashCode()).isNotEqualTo(other.hashCode());
    other.setRead(false);

    set.setWrite(false);
    assertThat(set).isNotEqualTo(other);
    assertThat(set.hashCode()).isNotEqualTo(other.hashCode());
    other.setWrite(false);
  }
}
