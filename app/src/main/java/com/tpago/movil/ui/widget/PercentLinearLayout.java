package com.tpago.movil.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.percent.PercentLayoutHelper;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * @author hecvasro
 */
public class PercentLinearLayout extends LinearLayout {
  private final PercentLayoutHelper percentLayoutHelper;

  public PercentLinearLayout(Context context) {
    this(context, null);
  }

  public PercentLinearLayout(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public PercentLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    percentLayoutHelper = new PercentLayoutHelper(this);
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    percentLayoutHelper.adjustChildren(widthMeasureSpec, heightMeasureSpec);
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    if (percentLayoutHelper.handleMeasuredStateTooSmall()) {
      super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
  }

  @Override
  public LinearLayout.LayoutParams generateLayoutParams(AttributeSet attrs) {
    return new LayoutParams(getContext(), attrs);
  }

  @Override
  protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
    super.onLayout(changed, left, top, right, bottom);
    percentLayoutHelper.restoreOriginalParams();
  }

  public static class LayoutParams extends LinearLayout.LayoutParams
    implements PercentLayoutHelper.PercentLayoutParams {
    private PercentLayoutHelper.PercentLayoutInfo percentLayoutInfo;

    public LayoutParams(Context context, AttributeSet attrs) {
      super(context, attrs);
      percentLayoutInfo = PercentLayoutHelper.getPercentLayoutInfo(context, attrs);
    }

    public LayoutParams(int width, int height) {
      super(width, height);
    }

    public LayoutParams(ViewGroup.LayoutParams source) {
      super(source);
    }

    public LayoutParams(MarginLayoutParams source) {
      super(source);
    }

    @Override
    protected void setBaseAttributes(TypedArray a, int widthAttr, int heightAttr) {
      PercentLayoutHelper.fetchWidthAndHeight(this, a, widthAttr, heightAttr);
    }

    @Override
    public PercentLayoutHelper.PercentLayoutInfo getPercentLayoutInfo() {
      if (percentLayoutInfo == null) {
        percentLayoutInfo = new PercentLayoutHelper.PercentLayoutInfo();
      }
      return percentLayoutInfo;
    }
  }
}
