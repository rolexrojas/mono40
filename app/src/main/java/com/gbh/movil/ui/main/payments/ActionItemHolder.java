package com.gbh.movil.ui.main.payments;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.gbh.movil.R;
import com.gbh.movil.ui.main.list.ItemHolder;

import butterknife.BindView;

/**
 * TODO
 *
 * @author hecvasro
 */
class ActionItemHolder extends ItemHolder {
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
  ActionItemHolder(@NonNull View rootView, @NonNull OnClickListener onClickListener) {
    super(rootView, onClickListener);
  }
}
