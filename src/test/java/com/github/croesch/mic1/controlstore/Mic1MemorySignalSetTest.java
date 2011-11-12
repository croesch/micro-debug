package com.github.croesch.mic1.controlstore;

import org.junit.Test;

/**
 * Provides test cases for {@link Mic1MemorySignalSet}.
 * 
 * @author croesch
 * @since Date: Nov 12, 2011
 */
public class Mic1MemorySignalSetTest extends Mic1SignalSetTestUtil {

  @Test
  public void testSetIsSignal() throws Exception {
    final Mic1MemorySignalSet set = new Mic1MemorySignalSet();
    testIsSetSignals(set, new String[] { "Read", "Write", "Fetch" });
  }

  @Test
  public void testEqualsObject() throws Exception {
    final Mic1MemorySignalSet set = new Mic1MemorySignalSet();
    final Mic1MemorySignalSet other = new Mic1MemorySignalSet();

    testEquals(set, other, new String[] { "Read", "Write", "Fetch" });
  }
}
