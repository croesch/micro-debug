package com.github.croesch.i18n;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.ResourceBundle.Control;

/**
 * Implementation of {@link Control} from its javadoc-example for loading XML-based bundles.
 * 
 * @author croesch
 * @since Date: Aug 17, 2011
 */
final class XMLBundleControl extends Control {

  @Override
  public List<String> getFormats(final String baseName) {
    if (baseName == null) {
      throw new IllegalArgumentException();
    }
    return Arrays.asList("xml");
  }

  @Override
  public ResourceBundle newBundle(final String baseName,
                                  final Locale locale,
                                  final String format,
                                  final ClassLoader loader,
                                  final boolean reload) throws IllegalAccessException, InstantiationException,
                                                       IOException {
    if (baseName == null || locale == null || format == null || loader == null) {
      throw new IllegalArgumentException();
    }
    if (!format.equals("xml")) {
      return null;
    }
    ResourceBundle bundle = null;
    final String resourceName = toResourceName(toBundleName(baseName, locale), format);
    InputStream stream = null;
    if (reload) {
      final URL url = loader.getResource(resourceName);
      if (url == null) {
        return null;
      }
      final URLConnection connection = url.openConnection();
      if (connection == null) {
        return null;
      }
      // Disable caches to get fresh data for
      // reloading.
      connection.setUseCaches(false);
      stream = connection.getInputStream();
    } else {
      stream = loader.getResourceAsStream(resourceName);
    }
    if (stream != null) {
      final BufferedInputStream bis = new BufferedInputStream(stream);
      try {
        bundle = new XMLResourceBundle(bis);
      } finally {
        bis.close();
      }
    }
    return bundle;
  }
}

/**
 * Resource bundle from javadoc-example of {@link Control} for XML-based bundles.
 * 
 * @author croesch
 * @since Date: Aug 17, 2011
 */
final class XMLResourceBundle extends ResourceBundle {

  /** the properties that contain the content of the xml file */
  private final Properties props;

  /**
   * Constructs the resource bundle from javadoc-example of {@link Control} for XML-based bundles.
   * 
   * @since Date: Aug 17, 2011
   * @param stream the xml-stream to read the data from
   * @throws IOException in case of IO problem, or if stream is <code>null</code>
   */
  XMLResourceBundle(final InputStream stream) throws IOException {
    if (stream == null) {
      throw new IOException();
    }
    this.props = new Properties();
    this.props.loadFromXML(stream);
  }

  @Override
  protected Object handleGetObject(final String key) {
    return this.props.getProperty(key);
  }

  @Override
  public Enumeration<String> getKeys() {
    return Collections.enumeration(this.props.stringPropertyNames());
  }
}
