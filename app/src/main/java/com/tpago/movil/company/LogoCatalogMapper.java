package com.tpago.movil.company;

import android.net.Uri;

import com.tpago.movil.DisplayDensity;
import com.tpago.movil.util.ObjectHelper;
import com.tpago.movil.util.UriBuilder;

/**
 * @author hecvasro
 */
public final class LogoCatalogMapper {

  private static final String STYLE_PLACEHOLDER = "style";

  static LogoCatalogMapper create(DisplayDensity displayDensity) {
    return new LogoCatalogMapper(displayDensity);
  }

  private final DisplayDensity displayDensity;

  private LogoCatalogMapper(DisplayDensity displayDensity) {
    this.displayDensity = ObjectHelper.checkNotNull(displayDensity, "displayDensity");
  }

  public final LogoCatalog apply(String template) {
    final UriBuilder uriBuilder = UriBuilder.create(template)
      .replacement(this.displayDensity);
    final Uri colored24 = uriBuilder
      .replacement(STYLE_PLACEHOLDER, LogoStyle.COLORED_24)
      .build();
    final Uri gray20 = uriBuilder
      .replacement(STYLE_PLACEHOLDER, LogoStyle.GRAY_20)
      .build();
    final Uri gray36 = uriBuilder
      .replacement(STYLE_PLACEHOLDER, LogoStyle.GRAY_36)
      .build();
    final Uri white36 = uriBuilder
      .replacement(STYLE_PLACEHOLDER, LogoStyle.WHITE_36)
      .build();
    return LogoCatalog.builder()
      .colored24(colored24)
      .gray20(gray20)
      .gray36(gray36)
      .white36(white36)
      .build();
  }
}
