package com.mono40.movil.dep.anim;

import android.animation.Animator;

/**
 * @author hecvasro
 */
@Deprecated
public abstract class BaseAnimatorListener implements Animator.AnimatorListener {
  @Override
  public void onAnimationStart(Animator animation) {
  }

  @Override
  public void onAnimationEnd(Animator animation) {
  }

  @Override
  public void onAnimationCancel(Animator animation) {
  }

  @Override
  public void onAnimationRepeat(Animator animation) {
  }
}
