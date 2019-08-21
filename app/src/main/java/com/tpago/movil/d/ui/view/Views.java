package com.tpago.movil.d.ui.view;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.tpago.movil.util.ObjectHelper;

/**
 * @author hecvasro
 */
public final class Views {

  public static int[] getLocationOnScreen(View view) {
    ObjectHelper.checkNotNull(view, "view");
    final int[] location = new int[2];
    view.getLocationOnScreen(location);
    final int x = location[0];
    final int y = location[1];
    return new int[]{x, y};
  }

  public static int[] getLocationOnScreen(RecyclerView.ViewHolder viewHolder) {
    return getLocationOnScreen(
      ObjectHelper.checkNotNull(viewHolder, "viewHolder")
        .itemView
    );
  }

  private Views() {
    throw new AssertionError("Cannot be instantiated");
  }
}
