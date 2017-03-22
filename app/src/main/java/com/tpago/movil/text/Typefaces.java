package com.tpago.movil.text;

import android.content.Context;
import android.graphics.Typeface;

import com.tpago.movil.util.Preconditions;

import uk.co.chrisjenx.calligraphy.TypefaceUtils;

/**
 * @author hecvasro
 */
public final class Typefaces {
  public static Typeface load(Context context, String path) {
    return TypefaceUtils.load(
      Preconditions.checkNotNull(context, "context == null").getAssets(),
      Preconditions.checkNotNull(path, "path == null"));
  }

  private Typefaces() {
    throw new AssertionError("Cannot be instantiated");
  }
}
