package com.tpago.movil.dep.api;

import android.content.Context;
import android.net.Uri;

import com.tpago.movil.DisplayDensity;
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
        .toString()
    );
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
