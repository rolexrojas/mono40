package com.tpago.movil.util;

import com.tpago.movil.app.DisplayDensity;

/**
 * Builds {@link android.net.Uri uris} from a given {@link String template} by finding and replacing
 * placeholders contained by them.
 *
 * @author hecvasro
 */
public class UriBuilder {

  /**
   * Placeholder for the {@link DisplayDensity display density} of the device.
   */
  private static final String PLACEHOLDER_DISPLAY_DENSITY = "size";

  private final String template;

  /**
   * Constructs an instance for the given template.
   *
   * @throws NullPointerException
   *   If {@code template} is null.
   * @throws IllegalArgumentException
   *   If {@code template} is empty.
   * @throws IllegalArgumentException
   *   If {@code template} does not contains the {@link #PLACEHOLDER_DISPLAY_DENSITY placeholder}
   *   used for the {@link DisplayDensity display density} of the device.
   */
  private UriBuilder(String template) {
    throw new UnsupportedOperationException("not implemented");
  }

  /**
   * Adds the given string as a replacement the given placeholder.
   *
   * @throws NullPointerException
   *   If {@code placeholder} is null.
   * @throws IllegalArgumentException
   *   If {@code placeholder} is empty.
   * @throws NullPointerException
   *   If {@code replacement} is null.
   * @throws IllegalArgumentException
   *   If {@code replacement} is empty.
   * @throws IllegalArgumentException
   *   If {@code placeholder} isn't contained in {@link #template template}.
   */
  public UriBuilder replacement(String placeholder, String replacement) {
    throw new UnsupportedOperationException("not implemented");
  }

  /**
   * Adds the given as a replacement for the {@link #PLACEHOLDER_DISPLAY_DENSITY placeholder} used
   * for the {@link DisplayDensity display density} of the device.
   *
   * @throws NullPointerException
   *   If {@code displayDensity} is null.
   */
  public UriBuilder replacement(DisplayDensity displayDensity) {
    throw new UnsupportedOperationException("not implemented");
  }

  public String build() {
    throw new UnsupportedOperationException("not implemented");
  }
}
