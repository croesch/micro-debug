package com.github.croesch.mic1.controlstore;

/**
 * Enumeration that defines different values. Each stands for a combination of registers that should be written on the
 * B-Bus. TODO remove this when type Registers has been created
 * 
 * @author croesch
 * @since Date: Nov 12, 2011
 */
enum Mic1BBusRegister {
  /** MDR is being written on the B-Bus */
  MDR,
  /** PC is being written on the B-Bus */
  PC,
  /** MBR is being written on the B-Bus */
  MBR,
  /** MBRU is being written on the B-Bus */
  MBRU,
  /** SP is being written on the B-Bus */
  SP,
  /** LV is being written on the B-Bus */
  LV,
  /** CPP is being written on the B-Bus */
  CPP,
  /** TOS is being written on the B-Bus */
  TOS,
  /** OPC is being written on the B-Bus */
  OPC;
}
