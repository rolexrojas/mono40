package com.tpago.movil.graphics;

import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.tpago.movil.util.Objects;
import com.tpago.movil.util.Preconditions;

/**
 * @author hecvasro
 */
public final class Drawables {
  private static void setAnimationDrawableState(ImageView imageView, boolean flag) {
    final Drawable drawable = Preconditions.checkNotNull(imageView, "imageView == null")
      .getDrawable();
    if (Objects.isNotNull(drawable)) {
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
