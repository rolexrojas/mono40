package com.tpago.movil.init;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;

import com.tpago.movil.util.Preconditions;

/**
 * @author hecvasro
 */
final class LogoAnimator {
  private final Logo logo;
  private final View anchor;
  private final long duration;

  private float movedAndScaledTranslationY = -1.0F;

  LogoAnimator(Logo logo, View anchor, long duration) {
    this.logo = Preconditions.checkNotNull(logo, "logo == null");
    this.anchor = Preconditions.checkNotNull(anchor, "anchor == null");
    this.duration = duration;
  }

  private void animate(float translationY, float scaleX, float scaleY) {
    final AnimatorSet animator = new AnimatorSet();
    animator.playTogether(
      ObjectAnimator.ofFloat(logo, View.TRANSLATION_Y, translationY),
      ObjectAnimator.ofFloat(logo, View.SCALE_X, scaleX),
      ObjectAnimator.ofFloat(logo, View.SCALE_Y, scaleY));
    animator.setDuration(duration);
    animator.start();
  }

  final void moveAndScale() {
    if (movedAndScaledTranslationY < 0F) {
      final float aY = anchor.getY();
      final float aH = anchor.getHeight();
      final float lY = logo.getY();
      final float lH = logo.getHeight();
      movedAndScaledTranslationY = (Math.abs(aH - lH) / 2) - Math.abs(aY - lY);
    }
    animate(movedAndScaledTranslationY, 0.6F, 0.6F);
  }

  final void start() {
    logo.start();
  }

  final void stop() {
    logo.stop();
  }

  final void reset() {
    animate(0.0F, 1.0F, 1.0F);
  }
}
