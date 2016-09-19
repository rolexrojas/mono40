package com.tpago.movil.ui.widget;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.tpago.movil.ui.view.BaseAnimatorListener;

/**
 * TODO
 *
 * @author hecvasro
 */
public class SliderLayout extends LinearLayout {
  private static final String TAG = SliderLayout.class.getSimpleName();

  /**
   * TODO
   */
  private static final float SLIDER_SIZE_PERCENT = 0.76F;

  /**
   * TODO
   */
  private static final int STATE_CLOSING = 0x1;

  /**
   * TODO
   */
  private static final int STATE_CLOSED = 0x2;

  /**
   * TODO
   */
  private static final int STATE_OPENING = 0x4;

  /**
   * TODO
   */
  private static final int STATE_OPEN = 0x8;

  /**
   * TODO
   */
  private int sliderSize = 0;

  /**
   * TODO
   */
  private View slider;

  /**
   * TODO
   */
  private View container;

  /**
   * TODO
   */
  private Animator animator;

  /**
   * TODO
   */
  private int currentState = STATE_CLOSED;

  public SliderLayout(Context context) {
    this(context, null);
  }

  public SliderLayout(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public SliderLayout(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    final ViewGroup.LayoutParams params = getLayoutParams();
    if (params != null) {
      params.width = LayoutParams.MATCH_PARENT;
      params.height = LayoutParams.MATCH_PARENT;
      setLayoutParams(params);
    }
    setOrientation(HORIZONTAL);
  }

  /**
   * TODO
   *
   * @return TODO
   */
  private boolean isClosed() {
    return (currentState & STATE_CLOSED) == STATE_CLOSED;
  }

  /**
   * TODO
   *
   * @return TODO
   */
  private boolean isOpen() {
    return (currentState & STATE_OPEN) == STATE_OPEN;
  }

  /**
   * TODO
   *
   * @param open
   *   TODO
   */
  private void animateState(final boolean open) {
    if (animator != null) {
      animator.cancel();
      animator = null;
    }
    animator = ObjectAnimator.ofInt(this, "scrollX", open ? sliderSize : 0);
    animator.addListener(new BaseAnimatorListener() {
      @Override
      public void onAnimationStart(Animator animator) {
        currentState = open ? STATE_OPENING : STATE_CLOSING;
      }

      @Override
      public void onAnimationEnd(Animator animator) {
        currentState = open ? STATE_OPEN : STATE_CLOSED;
      }
    });
    animator.setDuration(300L);
    animator.start();
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    final int width = getMeasuredWidth();
    final int height = getMeasuredHeight();
    setMeasuredDimension(width, height);
    sliderSize = (int) (width * SLIDER_SIZE_PERCENT);
    slider = getChildAt(0);
    if (slider != null) {
      slider.measure(MeasureSpec.makeMeasureSpec(sliderSize, MeasureSpec.EXACTLY),
        MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
    }
    container = getChildAt(1);
    if (container != null) {
      container.measure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
        MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
    }
  }

  @Override
  protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
    super.onLayout(changed, left, top, right, bottom);
    final int scrollX = getScrollX();
    if (isOpen() && scrollX != 0) {
      setScrollX(0);
    } else if (isClosed() && scrollX != sliderSize) {
      setScrollX(sliderSize);
    }
  }

  /**
   * TODO
   */
  public void toggle() {
    if (isOpen()) {
      animateState(false);
    } else if (isClosed()) {
      animateState(true);
    }
  }
}
