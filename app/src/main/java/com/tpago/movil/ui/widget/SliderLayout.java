package com.tpago.movil.ui.widget;

import android.content.Context;
import android.support.compat.BuildConfig;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * TODO
 *
 * @author hecvasro
 */
public class SliderLayout extends PercentLinearLayout {
  private static final String TAG = SliderLayout.class.getSimpleName();

  /**
   * TODO
   */
  private static final float DEFAULT_SLIDER_WIDTH_PERCENT = 0.76F;

  /**
   * TODO
   */
  private View slider;

  /**
   * TODO
   */
  private View content;

  /**
   * TODO
   */
  private int sliderWidth = 0;

  /**
   * TODO
   */
  private int sliderOffset = 0;

  public SliderLayout(Context context) {
    this(context, null);
  }

  public SliderLayout(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public SliderLayout(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    // TODO
    final ViewGroup.LayoutParams params = getLayoutParams();
    if (params != null) {
      params.width = LayoutParams.MATCH_PARENT;
      params.height = LayoutParams.MATCH_PARENT;
      setLayoutParams(params);
    }
    // TODO
    setOrientation(HORIZONTAL);
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    final int width = getMeasuredWidth();
    final int height = getMeasuredHeight();
    setMeasuredDimension(width, height);
    slider = getChildAt(0);
    if (slider != null) {
      sliderWidth = (int)(width * DEFAULT_SLIDER_WIDTH_PERCENT);
      if (BuildConfig.DEBUG) {
        Log.d(TAG, "sliderWidth = " + sliderWidth);
      }
      slider.measure(MeasureSpec.makeMeasureSpec(sliderWidth, MeasureSpec.EXACTLY),
        MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
    }
    content = getChildAt(1);
    if (content != null) {
      content.measure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
        MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
    }
  }
}
