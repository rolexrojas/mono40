package com.tpago.movil.dep.text;

import android.content.Context;
import android.graphics.Typeface;

import com.tpago.movil.dep.Preconditions;

import uk.co.chrisjenx.calligraphy.TypefaceUtils;

/**
 * @author hecvasro
 */
@Deprecated
public final class Typefaces {
  public static Typeface load(Context context, String path) {
    return TypefaceUtils.load(
      Preconditions.assertNotNull(context, "context == null").getAssets(),
      Preconditions.assertNotNull(path, "path == null"));
  }

  private Typefaces() {
    throw new AssertionError("Cannot be instantiated");
  }
}
