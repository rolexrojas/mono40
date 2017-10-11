package com.tpago.movil.dep.graphics;

import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.tpago.movil.util.ObjectHelper;

/**
 * @author hecvasro
 */
@Deprecated
public final class Drawables {

  private static void setAnimationDrawableState(ImageView imageView, boolean flag) {
    final Drawable drawable = ObjectHelper.checkNotNull(imageView, "imageView")
      .getDrawable();
    if (ObjectHelper.isNotNull(drawable) && drawable instanceof AnimationDrawable) {
      final AnimationDrawable animationDrawable = (AnimationDrawable) drawable;
      if (flag) {
        animationDrawable.start();
      } else {
        animationDrawable.stop();
      }
    }
  }

  public static void startAnimationDrawable(ImageView imageView) {
    setAnimationDrawableState(imageView, true);
  }

  public static void stopAnimationDrawable(ImageView imageView) {
    setAnimationDrawableState(imageView, false);
  }

  private Drawables() {
    throw new AssertionError("Cannot be instantiated");
  }
}
