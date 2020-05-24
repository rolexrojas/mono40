package com.mono40.movil.d.ui.main.purchase;

import android.graphics.Rect;
import androidx.annotation.Dimension;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

/**
 * @author hecvasro
 */
class SpaceDividerItemDecoration extends RecyclerView.ItemDecoration {
  @Dimension
  private final int size;

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
      outRect.top = top;
      outRect.bottom = bottom;
    }
  }
}
