package com.mono40.movil.d.ui.main.recipient.index.category;

import androidx.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.mono40.movil.R;
import com.mono40.movil.d.ui.main.list.ListItemHolder;

import butterknife.BindView;

/**
 * TODO
 *
 * @author hecvasro
 */
class ActionListItemHolder extends ListItemHolder {
  @BindView(R.id.action_title)
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
