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
// import java.io.BufferedInputStream;
// import java.io.IOException;
// import java.io.InputStream;
// import java.net.URL;
// import java.net.URLConnection;
// import java.util.Arrays;
// import java.util.List;
// import java.util.Locale;
// import java.util.ResourceBundle;
// import java.util.ResourceBundle.Control;
//
// /**
// * Implementation of {@link Control} from its javadoc-example for loading XML-based bundles.
// *
// * @author croesch
// * @since Date: Aug 17, 2011
// */
// final class XMLBundleControl extends Control {
//
// @Override
// public List<String> getFormats(final String baseName) {
// if (baseName == null) {
// throw new IllegalArgumentException();
// }
// return Arrays.asList("xml");
// }
//
// @Override
// public ResourceBundle newBundle(final String baseName,
// final Locale locale,
// final String format,
// final ClassLoader loader,
// final boolean reload) throws IllegalAccessException, InstantiationException,
// IOException {
// if (baseName == null || locale == null || format == null || loader == null) {
// throw new IllegalArgumentException();
// }
// if (!format.equals("xml")) {
// return null;
// }
// ResourceBundle bundle = null;
// final String resourceName = toResourceName(toBundleName(baseName, locale), format);
// InputStream stream = null;
// if (reload) {
// final URL url = loader.getResource(resourceName);
// if (url == null) {
// return null;
// }
// final URLConnection connection = url.openConnection();
// if (connection == null) {
// return null;
// }
// // Disable caches to get fresh data for
// // reloading.
// connection.setUseCaches(false);
// stream = connection.getInputStream();
// } else {
// stream = loader.getResourceAsStream(resourceName);
// }
// if (stream != null) {
// final BufferedInputStream bis = new BufferedInputStream(stream);
// try {
// bundle = new XMLResourceBundle(bis);
// } finally {
// bis.close();
// }
// }
// return bundle;
// }
// }
