package com.tpago.movil.domain.company;

import android.net.Uri;

import com.tpago.movil.util.StringHelper;

import java.util.Map;

/**
 * Company representation
 * <p>
 *
 * @author hecvasro
 */
public abstract class Company {

  protected Company() {
  }

  public abstract String id();

  public abstract int code();

  public abstract String name();

  public abstract String logoTemplate();

  public abstract Map<String, Uri> styledLogos();

  /**
   * Transforms its {@link #logoTemplate() template} into an {@link Uri} for the given {@link
   * LogoStyle style}.
   *
   * @return An {@link Uri} for {@link LogoStyle style}.
   *
   * @throws IllegalArgumentException
   *   If {@code style} is {@code null} or empty.
   * @throws IllegalArgumentException
   *   If {@code style} is not a valid {@link LogoStyle style}.
   */
  public final Uri logo(@LogoStyle String style) {
    if (StringHelper.isNullOrEmpty(style)) {
      throw new IllegalArgumentException("StringHelper.isNullOrEmpty(style)");
    }
    final Map<String, Uri> styledLogos = this.styledLogos();
    if (!styledLogos.containsKey(style)) {
      throw new IllegalArgumentException(String.format(
        "!styledLogoUris.contains(\"%1$s\")",
        style
      ));
    }
    return styledLogos.get(style);
  }
}
