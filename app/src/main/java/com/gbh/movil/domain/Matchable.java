package com.gbh.movil.domain;

import android.support.annotation.NonNull;

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
  boolean matches(@NonNull String query);
}
