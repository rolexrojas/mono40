package com.tpago.movil.app.ui.item;

import android.content.Context;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tpago.movil.R;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

/**
 * @author hecvasro
 */
public final class ItemHelper {

  public static LinearLayoutManager layoutManagerLinearVertical(Context context) {
    return new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
  }

  public static RecyclerView.ItemDecoration dividerLineHorizontal(Context context) {
    return new HorizontalDividerItemDecoration.Builder(context)
      .drawable(R.drawable.divider_line_horizontal)
      .marginResId(R.dimen.divider_margin_horizontal)
      .showLastDivider()
      .build();
  }

  private ItemHelper() {
  }
}
