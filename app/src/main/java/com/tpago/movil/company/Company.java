package com.tpago.movil.company;

import android.net.Uri;

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

  public abstract String logoUriTemplate();

  public abstract Map<String, Uri> logoUriMap();

  /**
   * Transforms its {@link #logoUriTemplate() template} into an {@link Uri} for the given {@link
   * LogoStyle style}.
   *
   * @return An {@link Uri} for {@link LogoStyle style}.
   *
   * @throws NullPointerException
   *   If {@code style} is null.
   * @throws IllegalArgumentException
   *   If {@code style} is empty.
   * @throws IllegalArgumentException
   *   If {@code style} is not a valid {@link LogoStyle style}.
   */
  public final Uri logoUri(@LogoStyle String style) {
    throw new UnsupportedOperationException("not implemented");
  }
}
