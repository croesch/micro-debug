// /*
// * Copyright (C) 2011-2012 Christian Roesch
// *
// * This file is part of micro-debug.
// *
// * micro-debug is free software: you can redistribute it and/or modify
// * it under the terms of the GNU General Public License as published by
// * the Free Software Foundation, either version 3 of the License, or
// * (at your option) any later version.
// *
// * micro-debug is distributed in the hope that it will be useful,
// * but WITHOUT ANY WARRANTY; without even the implied warranty of
// * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// * GNU General Public License for more details.
// *
// * You should have received a copy of the GNU General Public License
// * along with micro-debug. If not, see <http://www.gnu.org/licenses/>.
// */
// package com.github.croesch.i18n;
//
// import static org.fest.assertions.Assertions.assertThat;
//
// import java.io.IOException;
// import java.util.Locale;
//
// import org.junit.Test;
//
// import com.github.croesch.DefaultTestCase;
//
// /**
// * Provides test cases for {@link XMLBundleControl} and {@link XMLResourceBundle}.
// *
// * @author croesch
// * @since Date: Aug 17, 2011
// */
// public class XMLBundleControlTest extends DefaultTestCase {
//
// /**
// * Test method for {@link XMLBundleControl#getFormats(String)}.
// */
// @Test(expected = IllegalArgumentException.class)
// public void testGetFormatsString_IAE() {
// final XMLBundleControl control = new XMLBundleControl();
// control.getFormats(null);
// }
//
// /**
// * Test method for {@link XMLBundleControl#getFormats(String)}.
// */
// @Test
// public void testGetFormatsString() {
// final XMLBundleControl control = new XMLBundleControl();
// assertThat(control.getFormats("")).containsExactly("xml");
// }
//
// /**
// * Test method for {@link XMLBundleControl#newBundle(String, Locale, String, ClassLoader, boolean)} .
// *
// * @throws IOException in case of problems
// * @throws InstantiationException in case of problems
// * @throws IllegalAccessException in case of problems
// */
// @Test(expected = IllegalArgumentException.class)
// public void testNewBundleStringLocaleStringClassLoaderBoolean_IAE1() throws IllegalAccessException,
// InstantiationException, IOException {
// final XMLBundleControl control = new XMLBundleControl();
// control.newBundle(null, Locale.getDefault(), "format", ClassLoader.getSystemClassLoader(), true);
// }
//
// /**
// * Test method for {@link XMLBundleControl#newBundle(String, Locale, String, ClassLoader, boolean)} .
// *
// * @throws IOException in case of problems
// * @throws InstantiationException in case of problems
// * @throws IllegalAccessException in case of problems
// */
// @Test(expected = IllegalArgumentException.class)
// public void testNewBundleStringLocaleStringClassLoaderBoolean_IAE2() throws IllegalAccessException,
// InstantiationException, IOException {
// final XMLBundleControl control = new XMLBundleControl();
// control.newBundle("baseName", null, "format", ClassLoader.getSystemClassLoader(), true);
// }
//
// /**
// * Test method for {@link XMLBundleControl#newBundle(String, Locale, String, ClassLoader, boolean)} .
// *
// * @throws IOException in case of problems
// * @throws InstantiationException in case of problems
// * @throws IllegalAccessException in case of problems
// */
// @Test(expected = IllegalArgumentException.class)
// public void testNewBundleStringLocaleStringClassLoaderBoolean_IAE3() throws IllegalAccessException,
// InstantiationException, IOException {
// final XMLBundleControl control = new XMLBundleControl();
// control.newBundle("baseName", Locale.getDefault(), null, ClassLoader.getSystemClassLoader(), true);
// }
//
// /**
// * Test method for {@link XMLBundleControl#newBundle(String, Locale, String, ClassLoader, boolean)} .
// *
// * @throws IOException in case of problems
// * @throws InstantiationException in case of problems
// * @throws IllegalAccessException in case of problems
// */
// @Test(expected = IllegalArgumentException.class)
// public void testNewBundleStringLocaleStringClassLoaderBoolean_IAE4() throws IllegalAccessException,
// InstantiationException, IOException {
// final XMLBundleControl control = new XMLBundleControl();
// control.newBundle("baseName", Locale.getDefault(), "xml", null, true);
// }
//
// /**
// * Test method for {@link XMLBundleControl#newBundle(String, Locale, String, ClassLoader, boolean)} .
// *
// * @throws IOException in case of problems
// * @throws InstantiationException in case of problems
// * @throws IllegalAccessException in case of problems
// */
// @Test
// public void testNewBundleStringLocaleStringClassLoaderBoolean_Null1() throws IllegalAccessException,
// InstantiationException, IOException {
// final XMLBundleControl control = new XMLBundleControl();
// assertThat(control.newBundle("baseName", Locale.getDefault(), "XML", ClassLoader.getSystemClassLoader(), true))
// .isNull();
// }
//
// /**
// * Test method for {@link XMLBundleControl#newBundle(String, Locale, String, ClassLoader, boolean)} .
// *
// * @throws IOException in case of problems
// * @throws InstantiationException in case of problems
// * @throws IllegalAccessException in case of problems
// */
// @Test
// public void testNewBundleStringLocaleStringClassLoaderBoolean_Null2() throws IllegalAccessException,
// InstantiationException, IOException {
// final XMLBundleControl control = new XMLBundleControl();
// assertThat(control.newBundle("baseName", Locale.getDefault(), "xml", ClassLoader.getSystemClassLoader(), true))
// .isNull();
// }
// }
