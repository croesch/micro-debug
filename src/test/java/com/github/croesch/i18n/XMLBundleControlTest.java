package com.github.croesch.i18n;

import static org.fest.assertions.Assertions.assertThat;

import java.io.IOException;
import java.util.Collections;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.junit.Test;

/**
 * Provides test cases for {@link XMLBundleControl} and {@link XMLResourceBundle}.
 * 
 * @author croesch
 * @since Date: Aug 17, 2011
 */
public class XMLBundleControlTest {

  /**
   * Test method for {@link XMLBundleControl#getFormats(String)}.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testGetFormatsString_IAE() {
    final XMLBundleControl control = new XMLBundleControl();
    control.getFormats(null);
  }

  /**
   * Test method for {@link XMLBundleControl#getFormats(String)}.
   */
  @Test
  public void testGetFormatsString() {
    final XMLBundleControl control = new XMLBundleControl();
    assertThat(control.getFormats("")).containsExactly("xml");
  }

  /**
   * Test method for {@link XMLBundleControl#newBundle(String, Locale, String, ClassLoader, boolean)} .
   * 
   * @throws IOException in case of problems
   * @throws InstantiationException in case of problems
   * @throws IllegalAccessException in case of problems
   */
  @Test(expected = IllegalArgumentException.class)
  public void testNewBundleStringLocaleStringClassLoaderBoolean_IAE1() throws IllegalAccessException,
                                                                      InstantiationException, IOException {
    final XMLBundleControl control = new XMLBundleControl();
    control.newBundle(null, Locale.getDefault(), "format", ClassLoader.getSystemClassLoader(), true);
  }

  /**
   * Test method for {@link XMLBundleControl#newBundle(String, Locale, String, ClassLoader, boolean)} .
   * 
   * @throws IOException in case of problems
   * @throws InstantiationException in case of problems
   * @throws IllegalAccessException in case of problems
   */
  @Test(expected = IllegalArgumentException.class)
  public void testNewBundleStringLocaleStringClassLoaderBoolean_IAE2() throws IllegalAccessException,
                                                                      InstantiationException, IOException {
    final XMLBundleControl control = new XMLBundleControl();
    control.newBundle("baseName", null, "format", ClassLoader.getSystemClassLoader(), true);
  }

  /**
   * Test method for {@link XMLBundleControl#newBundle(String, Locale, String, ClassLoader, boolean)} .
   * 
   * @throws IOException in case of problems
   * @throws InstantiationException in case of problems
   * @throws IllegalAccessException in case of problems
   */
  @Test(expected = IllegalArgumentException.class)
  public void testNewBundleStringLocaleStringClassLoaderBoolean_IAE3() throws IllegalAccessException,
                                                                      InstantiationException, IOException {
    final XMLBundleControl control = new XMLBundleControl();
    control.newBundle("baseName", Locale.getDefault(), null, ClassLoader.getSystemClassLoader(), true);
  }

  /**
   * Test method for {@link XMLBundleControl#newBundle(String, Locale, String, ClassLoader, boolean)} .
   * 
   * @throws IOException in case of problems
   * @throws InstantiationException in case of problems
   * @throws IllegalAccessException in case of problems
   */
  @Test(expected = IllegalArgumentException.class)
  public void testNewBundleStringLocaleStringClassLoaderBoolean_IAE4() throws IllegalAccessException,
                                                                      InstantiationException, IOException {
    final XMLBundleControl control = new XMLBundleControl();
    control.newBundle("baseName", Locale.getDefault(), "xml", null, true);
  }

  /**
   * Test method for {@link XMLBundleControl#newBundle(String, Locale, String, ClassLoader, boolean)} .
   * 
   * @throws IOException in case of problems
   * @throws InstantiationException in case of problems
   * @throws IllegalAccessException in case of problems
   */
  @Test
  public void testNewBundleStringLocaleStringClassLoaderBoolean_Null1() throws IllegalAccessException,
                                                                       InstantiationException, IOException {
    final XMLBundleControl control = new XMLBundleControl();
    assertThat(control.newBundle("baseName", Locale.getDefault(), "XML", ClassLoader.getSystemClassLoader(), true))
      .isNull();
  }

  /**
   * Test method for {@link XMLBundleControl#newBundle(String, Locale, String, ClassLoader, boolean)} .
   * 
   * @throws IOException in case of problems
   * @throws InstantiationException in case of problems
   * @throws IllegalAccessException in case of problems
   */
  @Test
  public void testNewBundleStringLocaleStringClassLoaderBoolean_Null2() throws IllegalAccessException,
                                                                       InstantiationException, IOException {
    final XMLBundleControl control = new XMLBundleControl();
    assertThat(control.newBundle("baseName", Locale.getDefault(), "xml", ClassLoader.getSystemClassLoader(), true))
      .isNull();
  }

  /**
   * Test method for {@link XMLResourceBundle}.
   */
  @Test(expected = MissingResourceException.class)
  public void testGetString_MRE1() {
    final ResourceBundle bundle = ResourceBundle.getBundle("resourcebundle-test/unknown", new XMLBundleControl());
    bundle.getString("key");
  }

  /**
   * Test method for {@link XMLResourceBundle}.
   */
  @Test(expected = MissingResourceException.class)
  public void testGetString_MRE2() {
    final ResourceBundle bundle = ResourceBundle.getBundle("resourcebundle-test/file", new XMLBundleControl());
    bundle.getString("key1");
  }

  /**
   * Test method for {@link XMLResourceBundle}.
   */
  @Test
  public void testGetString_English() {
    Locale.setDefault(Locale.ENGLISH);
    final ResourceBundle bundle = ResourceBundle.getBundle("resourcebundle-test/file", new XMLBundleControl());
    assertThat(bundle.getString("key")).isEqualTo("content");
  }

  /**
   * Test method for {@link XMLResourceBundle}.
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
