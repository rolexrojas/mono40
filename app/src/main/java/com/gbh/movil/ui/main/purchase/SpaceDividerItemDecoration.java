package com.gbh.movil.ui.main.purchase;

import android.graphics.Rect;
import android.support.annotation.Dimension;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * TODO
 *
 * @author hecvasro
 */
class SpaceDividerItemDecoration extends RecyclerView.ItemDecoration {
  /**
   * TODO
   */
  @Dimension
  private final int size;

  /**
   * TODO
   *
   * @param size
   *   TODO
   */
  SpaceDividerItemDecoration(@Dimension int size) {
    this.size = size;
  }

  @Override
  public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
    RecyclerView.State state) {
    super.getItemOffsets(outRect, view, parent, state);
    final int position = parent.getChildAdapterPosition(view);
    if (position != RecyclerView.NO_POSITION) {
      final int top;
      final int bottom;
      if (position == 0) {
        top = size;
        bottom = size / 2;
      } else if (position == (state.getItemCount() - 1)) {
        top = size / 2;
        bottom = size;
      } else {
        top = size / 2;
        bottom = size / 2;
      }
      outRect.left = size;
      outRect.top = top;
      outRect.right = size;
      outRect.bottom = bottom;
    }
  }
}
