package com.tpago.movil.dep.domain;

import android.support.annotation.Nullable;

/**
 * TODO
 *
 * @author hecvasro
 */
public interface Matchable {
  /**
   * Checks if any of the matchable properties matches the given {@code query}.
   *
   * @return True if any of the matchable properties matches the given {@code query}, false
   * otherwise.
   */
  boolean matches(@Nullable String query);
}
