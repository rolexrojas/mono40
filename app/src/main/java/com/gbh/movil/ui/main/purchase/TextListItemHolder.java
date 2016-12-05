package com.gbh.movil.ui.main.purchase;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.gbh.movil.ui.main.list.ListItemHolder;

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
