package com.tpago.movil.util;

import android.net.Uri;

import com.tpago.movil.DisplayDensity;

import java.util.HashMap;
import java.util.Map;

/**
 * Builds {@link Uri uris} from a given {@link String template} by finding and replacing
 * placeholders.
 *
 * @author hecvasro
 */
public final class UriBuilder {

  /**
   * Placeholder for the {@link DisplayDensity display density} of the device.
   */
  private static final String DISPLAY_DENSITY_PLACEHOLDER = wrapPlaceholder("size");

  private static String wrapPlaceholder(String placeholder) {
    return String.format("{%1$s}", StringHelper.checkIsNotNullNorEmpty(placeholder, "placeholder"));
  }

  private static boolean isContained(String template, String placeholder) {
    return StringHelper.checkIsNotNullNorEmpty(template, "template")
      .contains(placeholder);
  }

  private static void checkIsContained(String template, String wrappedPlaceholder) {
    if (!isContained(template, wrappedPlaceholder)) {
      throw new IllegalArgumentException(
        String.format(
          "!isContained(\"%1$s\",\"%2$s\")",
          template,
          wrappedPlaceholder
        )
      );
    }
  }

  /**
   * Constructs an instance for the given template.
   *
   * @return An instance for the given template.
   *
   * @throws IllegalArgumentException
   *   If {@code template} is {@code null} or empty.
   * @throws IllegalArgumentException
   *   If {@code template} does not contains the {@link #DISPLAY_DENSITY_PLACEHOLDER placeholder}
   *   used for the {@link DisplayDensity display density} of the device.
   */
  public static UriBuilder create(String template) {
    return new UriBuilder(template);
  }

  private final String template;
  private final Map<String, String> replacements = new HashMap<>();

  /**
   * Constructs an instance for the given template.
   *
   * @throws IllegalArgumentException
   *   If {@code template} is {@code null} or empty.
   */
  private UriBuilder(String template) {
    this.template = StringHelper.checkIsNotNullNorEmpty(template, "template");
  }

  private void checkIsContained(String wrappedPlaceholder) {
    checkIsContained(this.template, wrappedPlaceholder);
  }

  /**
   * Adds the given string as a replacement the given placeholder.
   *
   * @throws IllegalArgumentException
   *   If {@code placeholder} is {@code null} or empty.
   * @throws IllegalArgumentException
   *   If {@code replacement} is {@code null} or empty.
   * @throws IllegalArgumentException
   *   If {@code placeholder} isn't contained in {@link #template template}.
   */
  public final UriBuilder replacement(String placeholder, String replacement) {
    StringHelper.checkIsNotNullNorEmpty(placeholder, "placeholder");
    StringHelper.checkIsNotNullNorEmpty(replacement, "replacement");
    final String wrappedPlaceholder = wrapPlaceholder(placeholder);
    this.checkIsContained(wrappedPlaceholder);
    this.replacements.put(wrappedPlaceholder, replacement);
    return this;
  }

  /**
   * Adds the given as a replacement for the {@link #DISPLAY_DENSITY_PLACEHOLDER placeholder} used
   * for the {@link DisplayDensity display density} of the device.
   *
   * @throws NullPointerException
   *   If {@code displayDensity} is null.
   */
  public final UriBuilder replacement(DisplayDensity displayDensity) {
    ObjectHelper.checkNotNull(displayDensity, "displayDensity");
    return this.replacement(DISPLAY_DENSITY_PLACEHOLDER, displayDensity.toString());
  }

  public final Uri build() {
    String s = this.template;
    for (String placeholder : this.replacements.keySet()) {
      s = s.replace(placeholder, this.replacements.get(placeholder));
    }
    return Uri.parse(s);
  }
}
