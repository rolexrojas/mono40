package com.gbh.movil.ui.main.payments;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.gbh.movil.R;
import com.gbh.movil.ui.main.list.ListItemHolder;

import butterknife.BindView;

/**
 * TODO
 *
 * @author hecvasro
 */
class ActionListItemHolder extends ListItemHolder {
  @BindView(R.id.action_text)
  TextView actionTextView;

  /**
   * TODO
   *
   * @param rootView
   *   TODO
   * @param onClickListener
   *   TODO
   */
  ActionListItemHolder(@NonNull View rootView, @NonNull OnClickListener onClickListener) {
    super(rootView, onClickListener);
  }
}
