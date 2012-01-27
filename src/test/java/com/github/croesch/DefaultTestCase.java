package com.github.croesch;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Reader;
import java.util.Locale;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;

import com.github.croesch.mic1.io.Output;
import com.github.croesch.misc.Printer;

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

/**
 * Default test case to be extended by all test classes.
 * 
 * @author croesch
 * @since Date: Jan 22, 2012
 */
@Ignore("Just default case")
public class DefaultTestCase {

  protected static final ByteArrayOutputStream out = new ByteArrayOutputStream();

  protected static final ByteArrayOutputStream micOut = new ByteArrayOutputStream();

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    Locale.setDefault(new Locale("test", "tst", " "));
  }

  @Before
  public void setUp() throws Exception {
    Printer.setPrintStream(new PrintStream(out));
    Output.setOut(new PrintStream(micOut));
    micOut.reset();
    out.reset();
    setUpDetails();
  }

  @AfterClass
  public static final void after() throws Exception {
    Printer.setPrintStream(System.out);
  }

  protected void setUpDetails() throws Exception {
    // let that be defined by subclasses
  }

  protected StringBuilder readFile(final String name) throws IOException {
    final StringBuilder sb = new StringBuilder();
    final Reader r = new InputStreamReader(ClassLoader.getSystemResourceAsStream(name));
    int c;
    while ((c = r.read()) != -1) {
      sb.append((char) c);
    }
    return sb;
  }
}
