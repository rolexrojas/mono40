package com.tpago.movil.d.domain;

import androidx.annotation.Nullable;

/**
 * TODO
 *
 * @author hecvasro
 */
@Deprecated
public interface Matchable {
  /**
   * Checks if any of the matchable properties matches the given {@code query}.
   *
   * @return True if any of the matchable properties matches the given {@code query}, false
   * otherwise.
   */
  boolean matches(@Nullable String query);
}
