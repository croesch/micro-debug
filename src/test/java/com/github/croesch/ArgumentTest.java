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
  public final void testOf() {
    assertThat(Argument.of("--help")).isSameAs(Argument.HELP);
    assertThat(Argument.of("-h")).isSameAs(Argument.HELP);

    assertThat(Argument.of("--version")).isSameAs(Argument.VERSION);
    assertThat(Argument.of("-v")).isSameAs(Argument.VERSION);

    assertThat(Argument.of(null)).isNull();
    assertThat(Argument.of("")).isNull();
    assertThat(Argument.of(" ")).isNull();
    assertThat(Argument.of("HELP")).isNull();
    assertThat(Argument.of("-help")).isNull();
  }
}
