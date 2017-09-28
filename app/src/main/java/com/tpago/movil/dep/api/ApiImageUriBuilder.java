package com.tpago.movil.dep.api;

import android.content.Context;
import android.net.Uri;

import com.tpago.movil.dep.Partner;
import com.tpago.movil.dep.DisplayDensity;
import com.tpago.movil.d.domain.Product;
import com.tpago.movil.dep.text.Texts;

/**
 * @author hecvasro
 */
@Deprecated
public final class ApiImageUriBuilder {

  private static String applyDisplayDensity(Context context, String template) {
    return template.replace(
      "{size}",
      DisplayDensity.get(context)
        .name()
        .toLowerCase()
    );
  }

  private static String applyStyle(String template, String code, Style style) {
    final String sanitizedUrl = template.substring(0, template.lastIndexOf('/') + 1);
    final StringBuilder builder = new StringBuilder(sanitizedUrl);
    final String styleName;
    if (style.equals(Style.GRAY_20)) {
      styleName = "20";
    } else if (style.equals(Style.GRAY_36)) {
      styleName = "36";
    } else if (style.equals(Style.PRIMARY_24)) {
      styleName = "24";
    } else {
      styleName = "36_bln";
    }
    return builder
      .append(code)
      .append("_")
      .append(styleName)
      .append(".png")
      .toString();
  }

  public static Uri build(Context context, Partner partner, Style style) {
    return Uri.parse(
      applyDisplayDensity(
        context,
        applyStyle(
          partner.getImageUriTemplate(),
          partner.getId(),
          style
        )
      ));
  }

  public static Uri build(Context context, Product product) {
    final String template = product.getImageUriTemplate();
    if (Texts.checkIfEmpty(template)) {
      return Uri.EMPTY;
    } else {
      return Uri.parse(applyDisplayDensity(context, template));
    }
  }

  private ApiImageUriBuilder() {
    throw new AssertionError("Cannot be instantiated");
  }

  public enum Style {
    GRAY_20,
    GRAY_36,
    PRIMARY_24,
    WHITE_36
  }
}
