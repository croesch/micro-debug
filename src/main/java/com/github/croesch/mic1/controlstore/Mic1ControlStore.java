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
package com.github.croesch.mic1.controlstore;

import java.io.IOException;
import java.io.InputStream;

import com.github.croesch.error.FileFormatException;
import com.github.croesch.i18n.Text;
import com.github.croesch.misc.Utils;

/**
 * The store for {@link Mic1Instruction}s.
 * 
 * @author croesch
 * @since Date: Nov 19, 2011
 */
public final class Mic1ControlStore {

  /** the number of micro code instructions that are stored in this store */
  private static final int INSTRUCTIONS_PER_STORE = 512;

  /** the store of 512 micro code instructions */
  private final Mic1Instruction[] store = new Mic1Instruction[INSTRUCTIONS_PER_STORE];

  /**
   * Constructs a {@link Mic1ControlStore} with the {@link Mic1Instruction} fetched from the given stream. If the magic
   * number is incorrect, or if there are too few or too many bytes to read, a {@link FileFormatException} will be
   * thrown.
   * 
   * @since Date: Nov 19, 2011
   * @param in the stream to read the instructions from
   * @throws FileFormatException if
   *         <ul>
   *         <li>the stream does only contain less or equal than four bytes</li>
   *         <li>the magic number isn't correct</li>
   *         <li>the stream contains too much data</li>
   *         <li>an {@link IOException} occurs</li>
   *         </ul>
   */
  public Mic1ControlStore(final InputStream in) throws FileFormatException {
    Utils.checkMagicNumber(in, Mic1InstructionReader.MIC1_MAGIC_NUMBER);

    boolean eof = false;
    // read the instructions from the stream
    for (int i = 0; !eof; ++i) {

      Mic1Instruction instr = null;
      try {
        instr = Mic1InstructionReader.read(in);
      } catch (final IOException e) {
        throw new FileFormatException(e);
      }

      if (instr == null) {
        // reached the end of input stream
        if (i == 0) {
          // only the magic number has been found
          throw new FileFormatException(Text.WRONG_FORMAT_EMPTY);
        }
        eof = true;
      } else if (i >= this.store.length) {
        // more instructions to read than capacity in the store
        throw new FileFormatException(Text.WRONG_FORMAT_TOO_BIG);
      } else {
        // save the instruction
        this.store[i] = instr;
      }
    }
  }

  /**
   * Returns the instruction from the store that is stored under the given mpc (address).
   * 
   * @since Date: Nov 20, 2011
   * @param mpc the address of the instruction to fetch - only the least nine bits will be used.
   * @return the {@link Mic1Instruction} that is stored at the given address, or <code>null</code> if there is no
   *         instruction at the given address.
   */
  public Mic1Instruction getInstruction(final int mpc) {
    final int nineBitMask = 0x1FF;
    return this.store[mpc & nineBitMask];
  }
}
