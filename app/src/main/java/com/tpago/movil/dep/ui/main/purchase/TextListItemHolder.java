package com.tpago.movil.dep.ui.main.purchase;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.tpago.movil.dep.ui.main.list.ListItemHolder;

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
