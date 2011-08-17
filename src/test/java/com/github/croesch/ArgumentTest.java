package com.github.croesch;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

/**
 * Provides several test methods for {@link Argument}.
 * 
 * @author croesch
 * @since Date: Aug 13, 2011
 */
public class ArgumentTest {

  /**
   * Test method for {@link Argument#of(String)}.
   */
  @Test
  public final void testOf_DifferentValues() {
    assertThat(Argument.of("--help")).isSameAs(Argument.HELP);
    assertThat(Argument.of("-h")).isSameAs(Argument.HELP);

    assertThat(Argument.of("--version")).isSameAs(Argument.VERSION);
    assertThat(Argument.of("-v")).isSameAs(Argument.VERSION);

    assertThat(Argument.of("--debug-level")).isSameAs(Argument.DEBUG_LEVEL);
    assertThat(Argument.of("-d")).isSameAs(Argument.DEBUG_LEVEL);
  }

  /**
   * Test method for {@link Argument#of(String)}.
   */
  @Test
  public final void testOf_Unkown() {
    assertThat(Argument.of(null)).isNull();
    assertThat(Argument.of("")).isNull();
    assertThat(Argument.of(" ")).isNull();
    assertThat(Argument.of("HELP")).isNull();
    assertThat(Argument.of("-help")).isNull();
    assertThat(Argument.of("-debug-level")).isNull();
    assertThat(Argument.of("--debug_level")).isNull();
  }

  /**
   * Test method for {@link Argument#createArgumentList(String[])}.
   */
  @Test
  public final void testCreateArgumentList_NullDeliversEmptyMap() {
    assertThat(Argument.createArgumentList(null)).isEmpty();
  }

  /**
   * Test method for {@link Argument#createArgumentList(String[])}.
   */
  @Test
  public final void testCreateArgumentList_EmptyArrayDeliversEmptyMap() {
    assertThat(Argument.createArgumentList(new String[] {})).isEmpty();
  }

  /**
   * Test method for {@link Argument#createArgumentList(String[])}.
   */
  @Test
  public final void testCreateArgumentList_NullArgumentProducesNoEntry() {
    assertThat(Argument.createArgumentList(new String[] { null })).isEmpty();
  }

  /**
   * Test method for {@link Argument#createArgumentList(String[])}.
   */
  @Test
  public final void testCreateArgumentList_UnknownArgumentProducesNoEntry() {
    assertThat(Argument.createArgumentList(new String[] { "" })).isEmpty();
    assertThat(Argument.createArgumentList(new String[] { " " })).isEmpty();
    assertThat(Argument.createArgumentList(new String[] { "-help" })).isEmpty();
  }

  /**
   * Test method for {@link Argument#createArgumentList(String[])}.
   */
  @Test
  public final void testCreateArgumentList_HelpInArray() {
    String[] args = new String[] { "-h" };

    assertThat(Argument.createArgumentList(args).keySet()).containsOnly(Argument.HELP);
    assertThat(Argument.createArgumentList(args).get(Argument.HELP)).isEmpty();

    args = new String[] { "--help" };
    assertThat(Argument.createArgumentList(args).keySet()).containsOnly(Argument.HELP);
    assertThat(Argument.createArgumentList(args).get(Argument.HELP)).isEmpty();
  }

  /**
   * Test method for {@link Argument#createArgumentList(String[])}.
   */
  @Test
  public final void testCreateArgumentList_VersionInArray() {
    String[] args = new String[] { "-v" };

    assertThat(Argument.createArgumentList(args).keySet()).containsOnly(Argument.VERSION);
    assertThat(Argument.createArgumentList(args).get(Argument.VERSION)).isEmpty();

    args = new String[] { "--version" };
    assertThat(Argument.createArgumentList(args).keySet()).containsOnly(Argument.VERSION);
    assertThat(Argument.createArgumentList(args).get(Argument.VERSION)).isEmpty();
  }

  /**
   * Test method for {@link Argument#createArgumentList(String[])}.
   */
  @Test
  public final void testCreateArgumentList_DebugLevelInArray() {
    String[] args = new String[] { "-d" };

    assertThat(Argument.createArgumentList(args).keySet()).isEmpty();

    args = new String[] { "-d", "2" };
    assertThat(Argument.createArgumentList(args).keySet()).containsOnly(Argument.DEBUG_LEVEL);
    assertThat(Argument.createArgumentList(args).get(Argument.DEBUG_LEVEL)).containsOnly("2");

    args = new String[] { "--debug-level", "2" };
    assertThat(Argument.createArgumentList(args).keySet()).containsOnly(Argument.DEBUG_LEVEL);
    assertThat(Argument.createArgumentList(args).get(Argument.DEBUG_LEVEL)).containsOnly("2");
  }

  /**
   * Test method for {@link Argument#createArgumentList(String[])}.
   */
  @Test
  public final void testCreateArgumentList() {
    final String[] args = new String[] { "-h", "-v", null, "--help", "--xxno-argument", "null" };

    assertThat(Argument.createArgumentList(args)).hasSize(2);
    assertThat(Argument.createArgumentList(args).keySet()).containsOnly(Argument.VERSION, Argument.HELP);
    assertThat(Argument.createArgumentList(args).get(Argument.VERSION)).isEmpty();
    assertThat(Argument.createArgumentList(args).get(Argument.HELP)).isEmpty();
  }

  /**
   * Test method for {@link Argument#createArgumentList(String[])}.
   */
  @Test
  public final void testCreateArgumentList2() {
    final String[] args = new String[] { "-h", "-v", null, "--xxno-argument", "null" };

    assertThat(Argument.createArgumentList(args)).hasSize(2);
    assertThat(Argument.createArgumentList(args).keySet()).containsOnly(Argument.VERSION, Argument.HELP);
    assertThat(Argument.createArgumentList(args).get(Argument.HELP)).isEmpty();
    assertThat(Argument.createArgumentList(args).get(Argument.VERSION)).isEmpty();
  }
}
