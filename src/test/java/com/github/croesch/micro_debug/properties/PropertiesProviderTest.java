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
package com.github.croesch.micro_debug.properties;

import static org.fest.assertions.Assertions.assertThat;

import java.util.Properties;

import org.junit.Test;

import com.github.croesch.micro_debug.DefaultTestCase;

/**
 * Provides test cases for {@link PropertiesProvider}.
 * 
 * @author croesch
 * @since Date: Jul 11, 2012
 */
public class PropertiesProviderTest extends DefaultTestCase {

  @Test
  public void testFileNotFound() {
    final Properties props = PropertiesProvider.getInstance().createNewProperties("hui-does-this-exist");
    assertThat(props.isEmpty()).isTrue();
  }
}
