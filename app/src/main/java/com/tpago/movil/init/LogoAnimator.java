package com.tpago.movil.init;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;

import com.tpago.movil.util.Preconditions;

/**
 * @author hecvasro
 */
public final class LogoAnimator {
  private final Logo logo;
  private final long duration;

  private final float outOfScreenTranslationY;
  private final float movedAndScaledTranslationY;

  LogoAnimator(Logo logo, View anchor, long duration) {
    this.logo = Preconditions.checkNotNull(logo, "logo == null");
    this.duration = duration;
    Preconditions.checkNotNull(anchor, "anchor == null");
    final float aY = anchor.getY();
    final float aH = anchor.getHeight();
    final float lY = logo.getY();
    final float lH = logo.getHeight();
    outOfScreenTranslationY = -(lY + lH);
    movedAndScaledTranslationY = (Math.abs(aH - lH) / 2) - Math.abs(aY - lY);
  }

  private void animateTranslationY(float translationY) {
    ObjectAnimator.ofFloat(logo, View.TRANSLATION_Y, translationY)
      .setDuration(duration)
      .start();
  }

  private void animateTranslationYAndScaleXY(float translationY, float scaleX, float scaleY) {
    final AnimatorSet animator = new AnimatorSet();
    animator.playTogether(
      ObjectAnimator.ofFloat(logo, View.TRANSLATION_Y, translationY),
      ObjectAnimator.ofFloat(logo, View.SCALE_X, scaleX),
      ObjectAnimator.ofFloat(logo, View.SCALE_Y, scaleY));
    animator.setDuration(duration);
    animator.start();
  }

  final void reset() {
    animateTranslationYAndScaleXY(0.0F, 1.0F, 1.0F);
  }

  final void start() {
    logo.start();
  }

  final void stop() {
    logo.stop();
  }

  public final void moveTopAndScaleDown() {
    animateTranslationYAndScaleXY(movedAndScaledTranslationY, 0.6F, 0.6F);
  }

  public final void moveOutOfScreen() {
    animateTranslationY(outOfScreenTranslationY);
  }

  public final void moveBackToScreen() {
    animateTranslationY(movedAndScaledTranslationY);
  }
}
