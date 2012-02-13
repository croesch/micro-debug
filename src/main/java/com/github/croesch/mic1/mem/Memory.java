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
package com.github.croesch.mic1.mem;

import java.io.IOException;
import java.io.InputStream;

import com.github.croesch.commons.Printer;
import com.github.croesch.commons.Utils;
import com.github.croesch.error.FileFormatException;
import com.github.croesch.i18n.Text;
import com.github.croesch.mic1.api.IReadableMemory;
import com.github.croesch.mic1.io.Input;
import com.github.croesch.mic1.io.Output;
import com.github.croesch.mic1.register.Register;

/**
 * Represents the main memory of the processor.
 * 
 * @author croesch
 * @since Date: Nov 21, 2011
 */
public final class Memory implements IReadableMemory {

  /** a mask to deselect bytes */
  private static final int[] MASK_BYTE = new int[] { 0xFFFFFF00, 0xFFFF00FF, 0xFF00FFFF, 0x00FFFFFF };

  /** mask to select a byte from an int */
  private static final int BYTE_MASK = 0xFF;

  /** address which isn't an address in the memory, but is connected to memory mapped io */
  public static final int MEMORY_MAPPED_IO_ADDRESS = 0xFFFFFFFD;

  /** the representation of the memory */
  private final int[] memory;

  /** stores the initial state of the memory for reset purpose */
  private final int[] initialMemory;

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

  /** the magic number that is needed at the begin of a binary ijvm-file */
  public static final int IJVM_MAGIC_NUMBER = 0x1DEADFAD;

  /**
   * Constructs a new memory containing the given number of words. The initial memory will contain only zeros and then
   * filled with the bytes read from the given input stream.
   * 
   * @since Date: Nov 23, 2011
   * @param maxSize the size of the memory in words (32-bit-values)
   * @param programStream the input stream
   * @throws FileFormatException if the stream doesn't provide valid data.
   */
  public Memory(final int maxSize, final InputStream programStream) throws FileFormatException {
    this.memory = new int[maxSize];
    this.initialMemory = new int[maxSize];
    initMemory(programStream);
    System.arraycopy(this.memory, 0, this.initialMemory, 0, maxSize);
  }

  /**
   * Resets the {@link Memory} so that it behaves as when started.
   * 
   * @since Date: Jan 27, 2012
   */
  public void reset() {
    // copy initial memory state
    System.arraycopy(this.initialMemory, 0, this.memory, 0, getSize());
    // set values
    this.read = false;
    this.fetch = false;
    this.write = false;
    this.wordAddress = -1;
    this.wordValue = -1;
    this.byteAddress = -1;
    this.byteValue = -1;
  }

  /**
   * Initialises the memory with the data fetched from the given {@link InputStream}.
   * 
   * @since Date: Nov 26, 2011
   * @param stream the stream that provides the data to fill the memory with
   * @throws FileFormatException if
   *         <ul>
   *         <li>the stream does only contain less or equal than four bytes</li>
   *         <li>the magic number isn't correct</li>
   *         <li>the format of the data is invalid</li>
   *         <li>an {@link IOException} occurs</li>
   *         </ul>
   */
  private void initMemory(final InputStream stream) throws FileFormatException {
    Utils.checkMagicNumber(stream, IJVM_MAGIC_NUMBER);

    final byte[] bytes = new byte[4];
    try {
      while (stream.read(bytes) != -1) {
        final int startAddress = Utils.bytesToInt(bytes[0], bytes[1], bytes[2], bytes[3]);

        if (stream.read(bytes) == -1) {
          throw new FileFormatException("unexpected end of file");
        }
        final int blockLength = Utils.bytesToInt(bytes[0], bytes[1], bytes[2], bytes[3]);

        readBlock(startAddress, blockLength, stream);
      }
    } catch (final IOException e) {
      throw new FileFormatException(e.getMessage(), e);
    }
  }

  /**
   * Reads a block of data from the given stream and stores it in the memory.
   * 
   * @since Date: Nov 26, 2011
   * @param start a byte address in the memory where to start storing the data
   * @param length the number of bytes to read
   * @param stream the {@link InputStream} to read the data from
   * @throws FileFormatException if an error occurs
   */
  private void readBlock(final int start, final int length, final InputStream stream) throws FileFormatException {
    try {
      for (int i = 0; i < length; ++i) {
        final int val = stream.read();

        if (val == -1) {
          throw new FileFormatException("unexpected end of block");
        }

        setByte(start + i, val);
      }
    } catch (final IOException e) {
      throw new FileFormatException(e.getMessage(), e);
    }
  }

  /**
   * Sets the byte value at the given address to the given value. The address is the byte address.
   * 
   * @since Date: Feb 13, 2012
   * @param addr the address, where to write the byte to
   * @param value the byte to write to the memory
   */
  private void setByte(final int addr, final int value) {
    final int offs = 3 - (addr % 4);
    final int mask = MASK_BYTE[offs];
    final int word = this.memory[addr / 4];

    this.memory[addr / 4] = (word & mask) | value << offs * Byte.SIZE;
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
   * <b>Note:</b> You have to call {@link #doTick()} before invoking this method.
   * 
   * @since Date: Nov 21, 2011
   * @param wordRegister if the signal <code>read</code> has been set, will be filled with the value read from the
   *        memory
   * @param byteRegister if the signal <code>fetch</code> has been set, will be filled with the value read from the
   *        memory
   * @see #doTick()
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
  public void doTick() {
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
    this.byteValue = (byte) getByte(this.byteAddress);
  }

  /**
   * Returns a single byte, unsigned, read from the address in the memory.
   * 
   * @since Date: Jan 22, 2012
   * @param addr the byte address of the byte to read from the memory
   * @return the byte from the memory at the given address.
   */
  public int getByte(final int addr) {
    int word = this.memory[addr / 4];
    switch (addr % 4) {
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
    return word & BYTE_MASK;
  }

  /**
   * Performs the read operation of a word on the memory.
   * 
   * @since Date: Nov 23, 2011
   */
  private void read() {
    if (this.wordAddress == MEMORY_MAPPED_IO_ADDRESS) {
      this.wordValue = Input.read() & BYTE_MASK;
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
      Output.print((byte) this.wordValue);
    } else {
      this.memory[this.wordAddress] = this.wordValue;
    }
  }

  /**
   * Returns the word value at the given address.
   * 
   * @since Date: Jan 21, 2012
   * @param addr the address, from where to fetch the word
   * @return the word value read from the memory
   */
  public int getWord(final int addr) {
    if (isAddressValid(addr)) {
      return this.memory[addr];
    }
    return -1;
  }

  /**
   * Sets the word value at the given address to the given value.
   * 
   * @since Date: Jan 21, 2012
   * @param addr the address, where to write the word to
   * @param value the word to write to the memory
   */
  public void setWord(final int addr, final int value) {
    if (isAddressValid(addr)) {
      this.memory[addr] = value;
    }
  }

  /**
   * Returns whether the given address is a valid address in the memory.
   * 
   * @since Date: Jan 21, 2012
   * @param addr the address to check
   * @return <code>true</code> if the address is valid<br>
   *         <code>false</code> otherwise
   */
  private boolean isAddressValid(final int addr) {
    final boolean valid = addr >= 0 && addr < getSize();
    if (!valid) {
      Printer.printErrorln(Text.INVALID_MEM_ADDR.text(Utils.toHexString(addr)));
    }
    return valid;
  }

  /**
   * Returns the size of the memory.
   * 
   * @since Date: Feb 9, 2012
   * @return the size of the memory in <em>words</em>
   */
  public int getSize() {
    return this.memory.length;
  }
}
