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
package com.github.croesch;

import static org.fest.assertions.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.Test;

import com.github.croesch.console.Printer;
import com.github.croesch.i18n.Text;

/**
 * Contains tests for {@link MicroDebug}.
 * 
 * @author croesch
 * @since Date: Dec 2, 2011
 */
public class MicroDebugTest {

  @Test
  public final void testMain_FileNotFound() {
    final ByteArrayOutputStream out = new ByteArrayOutputStream();
    Printer.setPrintStream(new PrintStream(out));

    MicroDebug.main(new String[] { "asd", "bas" });

    assertThat(out.toString()).isEqualTo(Text.ERROR.text(Text.FILE_NOT_FOUND.text("asd")) + "\n"
                                                 + Text.ERROR.text(Text.FILE_NOT_FOUND.text("bas")) + "\n");

    Printer.setPrintStream(System.out);
  }

  @Test
  public final void testMain_TooFewArgs() {
    final ByteArrayOutputStream out = new ByteArrayOutputStream();
    Printer.setPrintStream(new PrintStream(out));

    MicroDebug.main(new String[] { "-u" });
    assertThat(out.toString()).isEqualTo(Text.ERROR.text(Text.MISSING_IJVM_FILE) + "\n" + Text.TRY_HELP + "\n");

    out.reset();

    MicroDebug.main(new String[] { "u" });
    assertThat(out.toString()).isEqualTo(Text.ERROR.text(Text.MISSING_IJVM_FILE) + "\n" + Text.TRY_HELP + "\n");

    out.reset();

    MicroDebug.main(new String[] {});
    assertThat(out.toString()).isEqualTo(Text.ERROR.text(Text.MISSING_MIC1_FILE) + "\n"
                                                 + Text.ERROR.text(Text.MISSING_IJVM_FILE) + "\n" + Text.TRY_HELP
                                                 + "\n");

    out.reset();

    MicroDebug.main(null);
    assertThat(out.toString()).isEqualTo(Text.ERROR.text(Text.MISSING_MIC1_FILE) + "\n"
                                                 + Text.ERROR.text(Text.MISSING_IJVM_FILE) + "\n" + Text.TRY_HELP
                                                 + "\n");
  }

  @Test
  public final void testMain_Version() {
    final ByteArrayOutputStream out = new ByteArrayOutputStream();
    Printer.setPrintStream(new PrintStream(out));

    MicroDebug.main(new String[] { "-v" });
    assertThat(out.toString()).isEqualTo(Text.VERSION.text() + "\n");

    out.reset();

    MicroDebug.main(new String[] { "--version" });
    assertThat(out.toString()).isEqualTo(Text.VERSION.text() + "\n");

    out.reset();

    MicroDebug.main(new String[] { "--version", "mic1", "ijvm" });
    assertThat(out.toString()).isEqualTo(Text.VERSION.text() + "\n");

    out.reset();

    MicroDebug.main(new String[] { "--version", "mic1" });
    assertThat(out.toString()).isEqualTo(Text.ERROR.text(Text.FILE_NOT_FOUND.text("--version")) + "\n"
                                                 + Text.ERROR.text(Text.FILE_NOT_FOUND.text("mic1")) + "\n");
  }

  @Test
  public final void testMain_Unknown() {
    final ByteArrayOutputStream out = new ByteArrayOutputStream();
    Printer.setPrintStream(new PrintStream(out));

    MicroDebug.main(new String[] { "-xxx", "", "" });
    assertThat(out.toString()).isEqualTo(Text.ERROR.text(Text.UNKNOWN_ARGUMENT.text("-xxx")) + "\n");

    out.reset();

    MicroDebug.main(new String[] { "asd", "efgh", "xy", "as" });
    assertThat(out.toString()).isEqualTo(Text.ERROR.text(Text.UNKNOWN_ARGUMENT.text("asd")) + "\n"
                                                 + Text.ERROR.text(Text.UNKNOWN_ARGUMENT.text("efgh")) + "\n");
  }
}
