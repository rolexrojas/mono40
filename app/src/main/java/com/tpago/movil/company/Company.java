package com.tpago.movil.company;

import android.net.Uri;

import com.tpago.movil.util.StringHelper;

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

  public abstract LogoCatalog logoCatalog();

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
    final LogoCatalog catalog = this.logoCatalog();
    switch (StringHelper.checkIsNotNullNorEmpty(style, "style")) {
      case LogoStyle.COLORED_24:
        return catalog.colored24();
      case LogoStyle.GRAY_20:
        return catalog.gray20();
      case LogoStyle.GRAY_36:
        return catalog.gray36();
      case LogoStyle.WHITE_36:
        return catalog.white36();
      default:
        return Uri.EMPTY;
    }
  }
}
