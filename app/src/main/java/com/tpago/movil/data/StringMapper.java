package com.tpago.movil.data;

import android.support.annotation.StringRes;

/**
 * Functional interface that transforms a given {@link StringRes string resource identifier} into a
 * {@link String string}.
 */
public interface StringMapper {

  /**
   * Transforms a given {@link StringRes string resource identifier} into a {@link String string}.
   *
   * @param stringId
   *   {@link StringRes String resource identifier} that will be transformed.
   *
   * @return A {@link String string} for the given {@link StringRes string resource identifier}.
   */
  String apply(@StringRes int stringId);
}
