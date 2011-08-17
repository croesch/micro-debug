package com.github.croesch.i18n;

import static org.fest.assertions.Assertions.assertThat;

import java.io.IOException;
import java.util.Collections;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.junit.Test;

/**
 * Provides test cases for {@link XMLResourceBundle}.
 * 
 * @author croesch
 * @since Date: Aug 17, 2011
 */
public class XMLResourceBundleTest {

  /**
   * Test method for {@link XMLResourceBundle#getString(String)}.
   */
  @Test(expected = MissingResourceException.class)
  public void testGetString_MRE1() {
    final ResourceBundle bundle = ResourceBundle.getBundle("resourcebundle-test/unknown", new XMLBundleControl());
    bundle.getString("key");
  }

  /**
   * Test method for {@link XMLResourceBundle#getString(String)}.
   */
  @Test(expected = MissingResourceException.class)
  public void testGetString_MRE2() {
    final ResourceBundle bundle = ResourceBundle.getBundle("resourcebundle-test/file", new XMLBundleControl());
    bundle.getString("key1");
  }

  /**
   * Test method for {@link XMLResourceBundle#getString(String)}.
   */
  @Test
  public void testGetString_English() {
    Locale.setDefault(Locale.ENGLISH);
    final ResourceBundle bundle = ResourceBundle.getBundle("resourcebundle-test/file", new XMLBundleControl());
    assertThat(bundle.getString("key")).isEqualTo("content");
  }

  /**
   * Test method for {@link XMLResourceBundle#getString(String)}.
   */
  @Test
  public void testGetString_German() {
    Locale.setDefault(Locale.GERMAN);
    final ResourceBundle bundle = ResourceBundle.getBundle("resourcebundle-test/file", new XMLBundleControl());
    assertThat(bundle.getString("key")).isEqualTo("Inhalt");
    assertThat(bundle.getString("key")).isEqualTo("Inhalt");
  }

  /**
   * Test method for {@link XMLResourceBundle#XMLResourceBundle(java.io.InputStream)}.
   */
  @Test(expected = IOException.class)
  public void testXMLResourceBundle() throws IOException {
    new XMLResourceBundle(null);
  }

  /**
   * Test method for {@link XMLResourceBundle#getKeys()}.
   */
  @Test
  public void testGetKeys() throws IOException {
    Locale.setDefault(Locale.ENGLISH);
    final ResourceBundle bundle = ResourceBundle.getBundle("resourcebundle-test/file", new XMLBundleControl());
    assertThat(Collections.list(bundle.getKeys())).containsOnly("key", "yet another key", "other key");
  }
}
