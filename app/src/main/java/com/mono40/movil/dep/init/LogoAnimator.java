package com.mono40.movil.dep.init;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;

import com.mono40.movil.util.ObjectHelper;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public final class LogoAnimator {

  private final Logo logo;
  private final Translation translation;

  private final long duration;

  LogoAnimator(Logo logo, View anchor, long duration) {
    this.logo = ObjectHelper.checkNotNull(logo, "logo");
    this.translation = Translation.create(this.logo, anchor);

    this.duration = duration;
  }

  private void animateTranslationYAndScaleXY(
    TranslationCalculator calculator,
    float scaleX,
    float scaleY
  ) {
    this.logo.post(() -> {
      final AnimatorSet animator = new AnimatorSet();
      animator.playTogether(
        ObjectAnimator.ofFloat(this.logo, View.TRANSLATION_Y, this.translation.get(calculator)),
        ObjectAnimator.ofFloat(this.logo, View.SCALE_X, scaleX),
        ObjectAnimator.ofFloat(this.logo, View.SCALE_Y, scaleY)
      );
      animator.setDuration(this.duration);
      animator.start();
    });
  }

  final void reset() {
    this.animateTranslationYAndScaleXY(TranslationCalculator.RESET, 1.0F, 1.0F);
  }

  final void start() {
    this.logo.post(this.logo::start);
  }

  final void stop() {
    this.logo.post(this.logo::stop);
  }

  public final void moveTopAndScaleDown() {
    this.animateTranslationYAndScaleXY(TranslationCalculator.MOVED_AND_SCALED, 0.6F, 0.6F);
  }

  public final void moveOutOfScreen() {
    this.animateTranslationYAndScaleXY(TranslationCalculator.OUT_OF_SCREEN, 0.6F, 0.6F);
  }

  private interface TranslationCalculator {

    TranslationCalculator RESET
      = (aY, aH, lY, lH) -> 0.0F;
    TranslationCalculator MOVED_AND_SCALED
      = (aY, aH, lY, lH) -> (Math.abs(aH - lH) / 2) - Math.abs(aY - lY);
    TranslationCalculator OUT_OF_SCREEN
      = (aY, aH, lY, lH) -> -(lY + lH);

    float calculate(float aY, float aH, float lY, float lH);
  }

  private static final class Translation {

    static Translation create(Logo logo, View anchor) {
      return new Translation(logo, anchor);
    }

    private final Logo logo;
    private final View anchor;

    private final AtomicBoolean isReady = new AtomicBoolean(false);
    private final AtomicReference<Float> aY = new AtomicReference<>();
    private final AtomicReference<Float> aH = new AtomicReference<>();
    private final AtomicReference<Float> lY = new AtomicReference<>();
    private final AtomicReference<Float> lH = new AtomicReference<>();

    private Translation(Logo logo, View anchor) {
      this.logo = ObjectHelper.checkNotNull(logo, "logo");
      this.anchor = ObjectHelper.checkNotNull(anchor, "anchor");
    }

    final float get(TranslationCalculator calculator) {
      if (!this.isReady.get()) {
        this.aY.set(this.anchor.getY());
        this.aH.set(Float.valueOf(this.anchor.getHeight()));
        this.lY.set(this.logo.getY());
        this.lH.set(Float.valueOf(this.logo.getHeight()));
        this.isReady.set(true);
      }
      return calculator.calculate(this.aY.get(), this.aH.get(), this.lY.get(), this.lH.get());
    }
  }
}
