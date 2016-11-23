package com.gbh.movil.ui.main.payments;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.gbh.movil.R;
import com.gbh.movil.ui.main.list.Holder;

import butterknife.BindView;

/**
 * TODO
 *
 * @author hecvasro
 */
class ActionHolder extends Holder {
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
  ActionHolder(@NonNull View rootView, @NonNull OnClickListener onClickListener) {
    super(rootView, onClickListener);
  }
}
