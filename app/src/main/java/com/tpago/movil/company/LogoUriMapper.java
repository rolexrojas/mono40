package com.tpago.movil.company;

import android.net.Uri;

import com.tpago.movil.app.DisplayDensity;

/**
 * Transforms the {@link Company#logoUriTemplate() template} of a given {@link Company company} into
 * an {@link Uri} for a given {@link LogoStyle style}.
 *
 * @author hecvasro
 */
public class LogoUriMapper {

  /**
   * Placeholder for the {@link LogoStyle style}.
   */
  private static final String PLACEHOLDER_STYLE = "style";

  static LogoUriMapper create(DisplayDensity displayDensity) {
    throw new UnsupportedOperationException("not implemented");
  }

  private final DisplayDensity displayDensity;

  private LogoUriMapper(DisplayDensity displayDensity) {
    throw new UnsupportedOperationException("not implemented");
  }

  /**
   * Transforms the {@link Company#logoUriTemplate() template} of the given {@link Company company}
   * into an {@link Uri} for the given {@link LogoStyle style}.
   *
   * @return An {@link Uri} for the given {@link Company#logoUriTemplate() template} and {@link
   * LogoStyle style}.
   *
   * @throws NullPointerException
   *   If {@code company} is null.
   * @throws NullPointerException
   *   If {@code style} is null.
   * @throws IllegalArgumentException
   *   If {@code style} is empty.
   * @throws IllegalArgumentException
   *   If {@code style} is not a valid {@link LogoStyle style}.
   */
  public final Uri apply(Company company, @LogoStyle String style) {
    throw new UnsupportedOperationException("not implemented");
  }
}
