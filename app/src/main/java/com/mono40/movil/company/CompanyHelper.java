package com.mono40.movil.company;

import android.net.Uri;

import com.google.auto.value.AutoValue;
import com.mono40.movil.DisplayDensity;
import com.mono40.movil.util.function.Function;
import com.mono40.movil.util.ObjectHelper;
import com.mono40.movil.util.StringHelper;
import com.mono40.movil.util.UriBuilder;

import java.util.HashMap;
import java.util.Map;

/**
 * @author hecvasro
 */
public final class CompanyHelper {

  static CompanyHelper create(DisplayDensity displayDensity) {
    return new CompanyHelper(displayDensity);
  }

  private final LogoMapper mapper;

  private final Map<Company, Logo> logos = new HashMap<>();

  private CompanyHelper(DisplayDensity displayDensity) {
    this.mapper = LogoMapper.create(displayDensity);
  }

  public final <T extends Company> Uri getLogoUri(T company, @Company.LogoStyle String style) {
    ObjectHelper.checkNotNull(company, "company");
    StringHelper.checkIsNotNullNorEmpty(style, "style");
    if (!this.logos.containsKey(company)) {
      this.logos.put(company, this.mapper.apply(company.logoTemplate()));
    }
    final Logo logo = this.logos.get(company);
    switch (style) {
      case Company.LogoStyle.COLORED_24:
        return logo.colored24();
      case Company.LogoStyle.GRAY_20:
        return logo.gray20();
      case Company.LogoStyle.GRAY_36:
        return logo.gray36();
      case Company.LogoStyle.WHITE_36:
        return logo.white36();
      default:
        return Uri.EMPTY;
    }
  }

  @AutoValue
  abstract static class Logo {

    static Builder builder() {
      return new AutoValue_CompanyHelper_Logo.Builder();
    }

    Logo() {
    }

    abstract Uri colored24();

    abstract Uri gray20();

    abstract Uri gray36();

    abstract Uri white36();

    @AutoValue.Builder
    static abstract class Builder {

      Builder() {
      }

      abstract Builder colored24(Uri uri);

      abstract Builder gray20(Uri uri);

      abstract Builder gray36(Uri uri);

      abstract Builder white36(Uri uri);

      abstract Logo build();
    }
  }

  private static final class LogoMapper implements Function<String, Logo> {

    private static final String STYLE_PLACEHOLDER = "style";

    private static LogoMapper create(DisplayDensity displayDensity) {
      return new LogoMapper(displayDensity);
    }

    private final DisplayDensity displayDensity;

    private LogoMapper(DisplayDensity displayDensity) {
      this.displayDensity = ObjectHelper.checkNotNull(displayDensity, "displayDensity");
    }

    @Override
    public Logo apply(String template) {
      final UriBuilder uriBuilder = UriBuilder.create(template)
        .replacement(this.displayDensity);
      final Uri colored24 = uriBuilder
        .replacement(STYLE_PLACEHOLDER, Company.LogoStyle.COLORED_24)
        .build();
      final Uri gray20 = uriBuilder
        .replacement(STYLE_PLACEHOLDER, Company.LogoStyle.GRAY_20)
        .build();
      final Uri gray36 = uriBuilder
        .replacement(STYLE_PLACEHOLDER, Company.LogoStyle.GRAY_36)
        .build();
      final Uri white36 = uriBuilder
        .replacement(STYLE_PLACEHOLDER, Company.LogoStyle.WHITE_36)
        .build();
      return Logo.builder()
        .colored24(colored24)
        .gray20(gray20)
        .gray36(gray36)
        .white36(white36)
        .build();
    }
  }
}
