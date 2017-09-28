package com.tpago.movil.d.ui.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import static com.tpago.movil.dep.Preconditions.assertNotNull;

/**
 * @author hecvasro
 */
public final class Views {
  public static int[] getLocationOnScreen(View view) {
    assertNotNull(view, "view == null");
    final int[] location = new int[2];
    view.getLocationOnScreen(location);
    final int x = location[0];
    final int y = location[1];
    return new int[]{ x, y };
  }

  public static int[] getLocationOnScreen(RecyclerView.ViewHolder viewHolder) {
    return getLocationOnScreen(assertNotNull(viewHolder, "viewHolder == null").itemView);
  }

  private Views() {
    throw new AssertionError("Cannot be instantiated");
  }
}
