/*
 * Copyright (C) 2011-2012 Christian Roesch
 * This file is part of micro-debug.
 * micro-debug is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * micro-debug is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with micro-debug. If not, see <http://www.gnu.org/licenses/>.
 */
package com.github.croesch;

import static org.fest.assertions.Assertions.assertThat;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.StringReader;

import org.junit.Test;

import com.github.croesch.i18n.Text;
import com.github.croesch.mic1.io.Output;
import com.github.croesch.misc.Printer;
import com.github.croesch.misc.Reader;

/**
 * Contains tests for {@link MicroDebug}.
 * 
 * @author croesch
 * @since Date: Dec 2, 2011
 */
public class MicroDebugTest extends DefaultTestCase {

  private static final String GREETING = Text.GREETING.text() + TestUtil.getLineSeparator();

  private static final String BORDER = Text.BORDER.text() + TestUtil.getLineSeparator();

  private static final String WELCOME = Text.WELCOME.text() + TestUtil.getLineSeparator() + BORDER;

  @Test
  public final void testMain_FileNotFound() {
    final ByteArrayOutputStream out = new ByteArrayOutputStream();
    Printer.setPrintStream(new PrintStream(out));

    MicroDebug.main(new String[] {"asd", "bas"});

    assertThat(out.toString()).isEqualTo(GREETING + WELCOME + Text.ERROR.text(Text.FILE_NOT_FOUND.text("asd"))
                                         + TestUtil.getLineSeparator()
                                         + Text.ERROR.text(Text.FILE_NOT_FOUND.text("bas"))
                                         + TestUtil.getLineSeparator());

    Printer.setPrintStream(System.out);
  }

  @Test
  public final void testMain_TooFewArgs() {
    final ByteArrayOutputStream out = new ByteArrayOutputStream();
    Printer.setPrintStream(new PrintStream(out));

    MicroDebug.main(new String[] {"-u"});
    assertThat(out.toString()).isEqualTo(GREETING + BORDER + Text.ERROR.text(Text.MISSING_IJVM_FILE)
                                         + TestUtil.getLineSeparator() + Text.TRY_HELP + TestUtil.getLineSeparator());

    out.reset();

    MicroDebug.main(new String[] {"u"});
    assertThat(out.toString()).isEqualTo(GREETING + BORDER + Text.ERROR.text(Text.MISSING_IJVM_FILE)
                                         + TestUtil.getLineSeparator() + Text.TRY_HELP + TestUtil.getLineSeparator());

    out.reset();

    MicroDebug.main(new String[] {});
    assertThat(out.toString()).isEqualTo(GREETING + BORDER + Text.ERROR.text(Text.MISSING_MIC1_FILE)
                                         + TestUtil.getLineSeparator() + Text.ERROR.text(Text.MISSING_IJVM_FILE)
                                         + TestUtil.getLineSeparator() + Text.TRY_HELP + TestUtil.getLineSeparator());

    out.reset();

    MicroDebug.main(null);
    assertThat(out.toString()).isEqualTo(GREETING + BORDER + Text.ERROR.text(Text.MISSING_MIC1_FILE)
                                         + TestUtil.getLineSeparator() + Text.ERROR.text(Text.MISSING_IJVM_FILE)
                                         + TestUtil.getLineSeparator() + Text.TRY_HELP + TestUtil.getLineSeparator());
  }

  @Test
  public final void testMain_Version() {
    final ByteArrayOutputStream out = new ByteArrayOutputStream();
    Printer.setPrintStream(new PrintStream(out));

    MicroDebug.main(new String[] {"-v"});
    assertThat(out.toString()).isEqualTo(GREETING + BORDER + Text.VERSION.text() + TestUtil.getLineSeparator());

    out.reset();

    MicroDebug.main(new String[] {"--version"});
    assertThat(out.toString()).isEqualTo(GREETING + BORDER + Text.VERSION.text() + TestUtil.getLineSeparator());

    out.reset();

    MicroDebug.main(new String[] {"--version", "mic1", "ijvm"});
    assertThat(out.toString()).isEqualTo(GREETING + WELCOME + Text.VERSION.text() + TestUtil.getLineSeparator());

    out.reset();

    MicroDebug.main(new String[] {"--version", "mic1"});
    assertThat(out.toString()).isEqualTo(GREETING + WELCOME + Text.ERROR.text(Text.FILE_NOT_FOUND.text("--version"))
                                         + TestUtil.getLineSeparator()
                                         + Text.ERROR.text(Text.FILE_NOT_FOUND.text("mic1"))
                                         + TestUtil.getLineSeparator());
  }

  @Test
  public final void testMain_Unknown() {
    final ByteArrayOutputStream out = new ByteArrayOutputStream();
    Printer.setPrintStream(new PrintStream(out));

    MicroDebug.main(new String[] {"-xxx", "", ""});
    assertThat(out.toString()).isEqualTo(GREETING + WELCOME + Text.ERROR.text(Text.UNKNOWN_ARGUMENT.text("-xxx"))
                                         + TestUtil.getLineSeparator());

    out.reset();

    MicroDebug.main(new String[] {"asd", "efgh", "xy", "as"});
    assertThat(out.toString()).isEqualTo(GREETING + WELCOME + Text.ERROR.text(Text.UNKNOWN_ARGUMENT.text("asd"))
                                         + TestUtil.getLineSeparator()
                                         + Text.ERROR.text(Text.UNKNOWN_ARGUMENT.text("efgh"))
                                         + TestUtil.getLineSeparator());
  }

  @Test
  public final void testMain_Hi() {
    final ByteArrayOutputStream out = new ByteArrayOutputStream();
    Printer.setPrintStream(new PrintStream(out));
    Output.setOut(new PrintStream(out));
    Reader.setReader(new StringReader("run\nexit"));

    MicroDebug.main(new String[] {"--unbuffered-output",
                                  "src/test/resources/mic1/hi.mic1",
                                  "src/test/resources/mic1/hi.ijvm"});
    assertThat(out.toString()).isEqualTo(GREETING + WELCOME + "Hi!\n" + Text.TICKS.text(14)
                                         + TestUtil.getLineSeparator());
  }

  @Test
  public final void testMain_Hi_OutputFile() throws IOException {
    final ByteArrayOutputStream out = new ByteArrayOutputStream();
    Printer.setPrintStream(new PrintStream(out));
    Reader.setReader(new StringReader("run" + TestUtil.getLineSeparator() + "exit"));

    final String filePath = "tmp-micro-debug-test.del";
    MicroDebug.main(new String[] {"--unbuffered-output",
                                  "-o",
                                  filePath,
                                  "src/test/resources/mic1/hi.mic1",
                                  "src/test/resources/mic1/hi.ijvm"});
    assertThat(out.toString()).isEqualTo(GREETING + WELCOME + Text.TICKS.text(14) + TestUtil.getLineSeparator());

    BufferedReader in = null;
    FileInputStream fileInputStream = null;
    try {
      fileInputStream = new FileInputStream(filePath);
      in = new BufferedReader(new InputStreamReader(fileInputStream));
      assertThat(in.readLine()).isEqualTo("Hi!");
      assertThat(in.readLine()).isNull();
    } finally {
      if (in != null) {
        in.close();
      }
      if (fileInputStream != null) {
        fileInputStream.close();
      }
    }

    new File(filePath).delete();
  }
}
