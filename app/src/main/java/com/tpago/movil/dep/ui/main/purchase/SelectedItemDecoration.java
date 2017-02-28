package com.tpago.movil.dep.ui.main.purchase;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.ColorInt;
import android.support.annotation.Dimension;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tpago.movil.dep.misc.Utils;

/**
 * TODO
 *
 * @author hecvasro
 */
class SelectedItemDecoration extends RecyclerView.ItemDecoration {
  /**
   * TODO
   */
  private final Provider provider;

  /**
   * TODO
   */
  private final Paint borderPaint;

  /**
   * TODO
   */
  @Dimension
  private final int borderRadius;

  /**
   * TODO
   *
   * @param provider
   *   TODO
   * @param borderWidth
   *   TODO
   * @param borderColor
   *   TODO
   * @param borderRadius
   *   TODO
   */
  SelectedItemDecoration(@NonNull Provider provider, @Dimension int borderWidth,
    @ColorInt int borderColor, @Dimension int borderRadius) {
    this.provider = provider;
    this.borderPaint = new Paint();
    this.borderPaint.setStyle(Paint.Style.STROKE);
    this.borderPaint.setStrokeWidth(borderWidth);
    this.borderPaint.setColor(borderColor);
    this.borderRadius = borderRadius;
  }

  @Override
  public void onDrawOver(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
    super.onDrawOver(canvas, parent, state);
    final RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
    final int selectedPosition = provider.getSelectedItemPosition();
    final View child = layoutManager.findViewByPosition(selectedPosition);
    if (Utils.isNotNull(child)) {
      final RectF childRect = new RectF(child.getLeft(), child.getTop(), child.getRight(),
        child.getBottom());
      canvas.drawRoundRect(childRect, borderRadius, borderRadius, borderPaint);
    }
  }

  /**
   * TODO: Find a better name for this interface.
   */
  interface Provider {
    /**
     * TODO
     *
     * @return TODO
     */
    int getSelectedItemPosition();
  }
}
