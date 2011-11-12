package com.github.croesch.mic1.controlstore;

import org.junit.Test;

/**
 * Provides test cases for {@link Mic1ALUSignalSet}.
 * 
 * @author croesch
 * @since Date: Nov 12, 2011
 */
public class Mic1ALUSignalSetTest extends Mic1SignalSetTestUtil {

  @Test
  public void testSetIsSignal() throws Exception {
    final Mic1ALUSignalSet set = new Mic1ALUSignalSet();
    testIsSetSignals(set, new String[] { "Sll8", "Sra1", "F0", "F1", "EnA", "EnB", "InvA", "Inc" });
  }

  @Test
  public void testEqualsObject() throws Exception {
    final Mic1ALUSignalSet set = new Mic1ALUSignalSet();
    final Mic1ALUSignalSet other = new Mic1ALUSignalSet();

    testEquals(set, other, new String[] { "Sll8", "Sra1", "F0", "F1", "EnA", "EnB", "InvA", "Inc" });
  }
}
