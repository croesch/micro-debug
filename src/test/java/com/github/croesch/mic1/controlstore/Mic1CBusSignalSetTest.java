package com.github.croesch.mic1.controlstore;

import org.junit.Test;

/**
 * Provides test cases for {@link Mic1CBusSignalSet}.
 * 
 * @author croesch
 * @since Date: Nov 12, 2011
 */
public class Mic1CBusSignalSetTest extends Mic1SignalSetTestUtil {

  @Test
  public void testSetIsSignal() throws Exception {
    final Mic1CBusSignalSet set = new Mic1CBusSignalSet();
    testIsSetSignals(set, new String[] { "Cpp", "H", "Lv", "Mar", "Mdr", "Opc", "Pc", "Sp", "Tos" });
  }

  @Test
  public void testEqualsObject() throws Exception {
    final Mic1CBusSignalSet set = new Mic1CBusSignalSet();
    final Mic1CBusSignalSet other = new Mic1CBusSignalSet();

    testEquals(set, other, new String[] { "Cpp", "H", "Lv", "Mar", "Mdr", "Opc", "Pc", "Sp", "Tos" });
  }
}
