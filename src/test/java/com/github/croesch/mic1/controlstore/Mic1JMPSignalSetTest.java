package com.github.croesch.mic1.controlstore;

import org.junit.Test;

/**
 * Provides test cases for {@link Mic1JMPSignalSet}.
 * 
 * @author croesch
 * @since Date: Nov 13, 2011
 */
public class Mic1JMPSignalSetTest extends Mic1SignalSetTestUtil {

  @Test
  public void testSetIsSignal() throws Exception {
    final Mic1JMPSignalSet set = new Mic1JMPSignalSet();
    testIsSetSignals(set, new String[] { "JmpN", "JmpC", "JmpZ" });
  }

  @Test
  public void testEqualsObject() throws Exception {
    final Mic1JMPSignalSet set = new Mic1JMPSignalSet();
    final Mic1JMPSignalSet other = new Mic1JMPSignalSet();

    testEquals(set, other, new String[] { "JmpN", "JmpC", "JmpZ" });
  }
}
