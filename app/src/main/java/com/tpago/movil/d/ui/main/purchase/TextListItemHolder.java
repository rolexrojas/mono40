package com.tpago.movil.d.ui.main.purchase;

import androidx.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.tpago.movil.d.ui.main.list.ListItemHolder;

/**
 * TODO
 *
 * @author hecvasro
 */
class TextListItemHolder extends ListItemHolder {
  TextListItemHolder(@NonNull View rootView) {
    super(rootView);
  }

  /**
   * TODO
   *
   * @return TODO
   */
  @NonNull
  final TextView getTextView() {
    return (TextView) getRootView();
  }
}
