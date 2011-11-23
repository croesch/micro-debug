package com.github.croesch.mic1.mem;

import com.github.croesch.mic1.register.Register;

/**
 * Represents the main memory of the processor.
 * 
 * @author croesch
 * @since Date: Nov 21, 2011
 */
public final class Memory {

  /** mask to select a byte from an int */
  private static final int BYTE_MASK = 0xFF;

  /** address which isn't an address in the memory, but is connected to memory mapped io */
  public static final int MEMORY_MAPPED_IO_ADDRESS = 0xFFFFFFFD;

  /** the representation of the memory */
  private final int[] memory;

  /** the input signal that enforces the memory to read a word */
  private boolean read = false;

  /** the input signal that enforces the memory to write a word */
  private boolean write = false;

  /** the input signal that enforces the memory to read a byte */
  private boolean fetch = false;

  /** the address of the word, where to read/write */
  private int wordAddress = -1;

  /** the word to write, or read from the memory */
  private int wordValue = -1;

  /** the address of the byte, where to read */
  private int byteAddress = -1;

  /** the byte read from the memory */
  private byte byteValue = -1;

  /**
   * Constructs a new memory containing the given number of words.
   * 
   * @since Date: Nov 23, 2011
   * @param maxSize the size of the memory in words (32-bit-values)
   */
  public Memory(final int maxSize) {
    this.memory = new int[maxSize];
  }

  /**
   * Sets the input signal <code>read</code>, that enforces the main memory to read a word from the memory.
   * 
   * @since Date: Nov 21, 2011
   * @param rd <code>true</code>, if the memory should read a word.
   * @see #setWordAddress(int)
   */
  public void setRead(final boolean rd) {
    this.read = rd;
  }

  /**
   * Sets the input signal <code>write</code>, that enforces the main memory to write a word to the memory.
   * 
   * @since Date: Nov 21, 2011
   * @param wr <code>true</code>, if the memory should write a word.
   * @see #setWordAddress(int)
   * @see #setWordValue(int)
   */
  public void setWrite(final boolean wr) {
    this.write = wr;
  }

  /**
   * Sets the input signal <code>fetch</code>, that enforces the main memory to read a byte from the memory.
   * 
   * @since Date: Nov 21, 2011
   * @param ft <code>true</code>, if the memory should read a byte.
   * @see #setByteAddress(int)
   */
  public void setFetch(final boolean ft) {
    this.fetch = ft;
  }

  /**
   * Sets the address of the word, where to read from or write to in the memory.
   * 
   * @since Date: Nov 21, 2011
   * @param addr 32-bit-value that defines the address where to read from or write to a word.
   * @see #setRead(boolean)
   * @see #setWrite(boolean)
   */
  public void setWordAddress(final int addr) {
    this.wordAddress = addr;
  }

  /**
   * Sets the value of the word to write to the memory.
   * 
   * @since Date: Nov 21, 2011
   * @param value the word value to write to the memory.
   * @see #setWrite(boolean)
   * @see #setWordAddress(int)
   */
  public void setWordValue(final int value) {
    this.wordValue = value;
  }

  /**
   * Sets the address of the byte, where to read from in the memory.
   * 
   * @since Date: Nov 21, 2011
   * @param addr 32-bit-value that defines the address where to read a byte from.
   * @see #setFetch(boolean)
   */
  public void setByteAddress(final int addr) {
    this.byteAddress = addr;
  }

  /**
   * If at least the signal <code>read</code> or <code>fetch</code> has been set in the memory. The values read will be
   * stored in the given registers.<br />
   * <b>Note:</b> You have to call {@link #poke()} before invoking this method.
   * 
   * @since Date: Nov 21, 2011
   * @param wordRegister if the signal <code>read</code> has been set, will be filled with the value read from the
   *        memory
   * @param byteRegister if the signal <code>fetch</code> has been set, will be filled with the value read from the
   *        memory
   * @see #poke()
   */
  public void fillRegisters(final Register wordRegister, final Register byteRegister) {
    if (this.read) {
      wordRegister.setValue(this.wordValue);
    }
    if (this.fetch) {
      byteRegister.setValue(this.byteValue & BYTE_MASK);
    }
  }

  /**
   * Tells the memory to do its work. Based on the signals <code>read</code>, <code>write</code> and <code>fetch</code>
   * it will do some work. It will store the value to write into the memory and the values read from the memory into
   * variables.
   * 
   * @since Date: Nov 21, 2011
   * @see #fillRegisters(Register, Register)
   */
  public void poke() {
    if (this.write) {
      write();
    }
    if (this.read) {
      read();
    }
    if (this.fetch) {
      fetch();
    }
  }

  /**
   * Performs the fetch operation of a byte on the memory.
   * 
   * @since Date: Nov 23, 2011
   */
  private void fetch() {
    int word = this.memory[this.byteAddress / 4];
    switch (this.byteAddress % 4) {
      case 0:
        word >>= Byte.SIZE * 3;
        break;
      case 1:
        word >>= Byte.SIZE * 2;
        break;
      case 2:
        word >>= Byte.SIZE;
        break;
      default:
        // nothing to do, because the value we want is already at word[7:0]
        break;
    }
    this.byteValue = (byte) word;
  }

  /**
   * Performs the read operation of a word on the memory.
   * 
   * @since Date: Nov 23, 2011
   */
  private void read() {
    if (this.wordAddress == MEMORY_MAPPED_IO_ADDRESS) {
      // TODO implement me.
    } else {
      this.wordValue = this.memory[this.wordAddress];
    }
  }

  /**
   * Performs the write operation of a word on the memory.
   * 
   * @since Date: Nov 23, 2011
   */
  private void write() {
    if (this.wordAddress == MEMORY_MAPPED_IO_ADDRESS) {
      // TODO implement me.
    } else {
      this.memory[this.wordAddress] = this.wordValue;
    }
  }
}