package com.tpago.movil.ui.onboarding;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;

import com.tpago.movil.util.Preconditions;

/**
 * @author hecvasro
 */
final class LogoViewAnimator {
  private final LogoView logoView;
  private final View anchorView;
  private final long duration;

  private float movedAndScaledTranslationY = -1.0F;

  LogoViewAnimator(LogoView logoView, View anchorView, long duration) {
    this.logoView = Preconditions.checkNotNull(logoView, "logoView == null");
    this.anchorView = Preconditions.checkNotNull(anchorView, "anchorView == null");
    this.duration = duration;
  }

  private void animate(float translationY, float scaleX, float scaleY) {
    final AnimatorSet animator = new AnimatorSet();
    animator.playTogether(
      ObjectAnimator.ofFloat(logoView, View.TRANSLATION_Y, translationY),
      ObjectAnimator.ofFloat(logoView, View.SCALE_X, scaleX),
      ObjectAnimator.ofFloat(logoView, View.SCALE_Y, scaleY));
    animator.setDuration(duration);
    animator.start();
  }

  final void moveAndScale() {
    if (movedAndScaledTranslationY < 0F) {
      final float aY = anchorView.getY();
      final float aH = anchorView.getHeight();
      final float lY = logoView.getY();
      final float lH = logoView.getHeight();
      movedAndScaledTranslationY = (Math.abs(aH - lH) / 2) - Math.abs(aY - lY);
    }
    animate(movedAndScaledTranslationY, 0.6F, 0.6F);
  }

  final void reset() {
    animate(0.0F, 1.0F, 1.0F);
  }
}
