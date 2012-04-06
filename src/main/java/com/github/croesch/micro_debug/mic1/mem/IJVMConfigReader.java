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
package com.github.croesch.micro_debug.mic1.mem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.github.croesch.micro_debug.annotation.NotNull;
import com.github.croesch.micro_debug.commons.Utils;
import com.github.croesch.micro_debug.parser.IntegerParser;

/**
 * Reads the configuration file for the ijvm-code and can parse it.
 * 
 * @author croesch
 * @since Date: Jan 22, 2012
 */
public final class IJVMConfigReader {

  /** the {@link Logger} for this class */
  private static final Logger LOGGER = Logger.getLogger(IJVMConfigReader.class.getName());

  /** the pattern to match one line in the ijvm.conf file */
  private static final Pattern LINE_PATTERN = Pattern.compile("\\s*(0x[0-9a-fA-F]{2})\\s+(\\S+)\\s*(.*)?");

  /** regular expression to match the comment */
  private static final String COMMENT_REGEX = "//.*";

  /** pattern to match a single argument */
  private static final Pattern ARGUMENTS_PATTERN = Pattern.compile("([\\S&&[^/]]+)\\s*");

  /** the parser to parse an {@link Integer} from a {@link String} */
  @NotNull
  private final IntegerParser integerParser = new IntegerParser();

  /**
   * Tries to read configuration file from the given stream and returns the map with parsed entries. If an error occurs
   * this map may be empty.
   * 
   * @since Date: Jan 22, 2012
   * @param in the {@link InputStream} to read the configuration from
   * @return the {@link Map} that contains the memory addresses and the {@link IJVMCommand}s for that addresses, fetched
   *         from the configuration file<br>
   *         in case of error this map might be empty
   */
  @NotNull
  public Map<Integer, IJVMCommand> readConfig(final InputStream in) {
    final Map<Integer, IJVMCommand> map = new HashMap<Integer, IJVMCommand>();
    try {
      final BufferedReader reader = new BufferedReader(new InputStreamReader(in));
      fillMapWithCommands(map, reader);
    } catch (final Exception e) {
      Utils.logThrownThrowable(e);
    }
    return map;
  }

  /**
   * Tries to read configuration file from the given reader and puts the parsed entries into the given map.
   * 
   * @since Date: Jan 22, 2012
   * @param map the {@link Map} to put the parsed entries to
   * @param reader the reader to parse configuration from
   * @throws IOException if an error occurs
   */
  private void fillMapWithCommands(final Map<Integer, IJVMCommand> map, final BufferedReader reader) throws IOException {
    String line;
    while ((line = reader.readLine()) != null) {
      parseLine(map, line);
    }
  }

  /**
   * Parses a single line of the configuration file and puts it to the map, in case of success.
   * 
   * @since Date: Jan 22, 2012
   * @param map the map to put the parsed result into
   * @param ln the line to parse
   */
  private void parseLine(final Map<Integer, IJVMCommand> map, final String ln) {
    final String line = removeComment(ln).trim();

    if (!line.equals("")) {
      final Matcher m = LINE_PATTERN.matcher(line);
      if (m.matches()) {
        final Integer addr = this.integerParser.parse(m.group(1));
        final String name = m.group(2);
        map.put(addr, new IJVMCommand(name, parseArguments(m.group(3))));
      } else {
        LOGGER.warning("couldn't parse line in ijvm.conf: '" + line + "'");
      }
    }
  }

  /**
   * Returns a string without the comment.
   * 
   * @since Date: Jan 22, 2012
   * @param ln the line to remove the comment from
   * @return the line without the comment
   */
  @NotNull
  private String removeComment(final String ln) {
    return ln.replaceFirst(COMMENT_REGEX, "");
  }

  /**
   * Parses the arguments and returns them in an array.
   * 
   * @since Date: Jan 22, 2012
   * @param args the string representing the arguments
   * @return an array containing the {@link IJVMCommandArgument}s that were represented by the given string.
   */
  @NotNull
  private IJVMCommandArgument[] parseArguments(final String args) {
    final List<IJVMCommandArgument> list = new ArrayList<IJVMCommandArgument>();
    final Matcher m = ARGUMENTS_PATTERN.matcher(args);
    while (m.find()) {
      list.add(IJVMCommandArgument.valueOf(m.group(1).toUpperCase(Locale.GERMAN)));
    }
    return list.toArray(new IJVMCommandArgument[list.size()]);
  }
}
