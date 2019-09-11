package com.tpago.movil.dep.text;

import android.content.Context;
import android.graphics.Typeface;

import com.tpago.movil.util.ObjectHelper;

import io.github.inflationx.calligraphy3.TypefaceUtils;

/**
 * @author hecvasro
 */
@Deprecated
public final class Typefaces {

  public static Typeface load(Context context, String path) {
    return TypefaceUtils.load(
      ObjectHelper.checkNotNull(context, "context")
        .getAssets(),
      ObjectHelper.checkNotNull(path, "path")
    );
  }

  private Typefaces() {
    throw new AssertionError("Cannot be instantiated");
  }
}
