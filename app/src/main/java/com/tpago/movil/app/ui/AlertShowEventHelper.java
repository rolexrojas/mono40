package com.tpago.movil.app.ui;

import com.tpago.movil.R;
import com.tpago.movil.data.StringMapper;

/**
 * @author hecvasro
 */
public class AlertShowEventHelper {

  public static AlertShowEvent createForUnexpectedFailure(StringMapper stringMapper) {
    return AlertShowEvent.builder(stringMapper)
      .title(R.string.error_unexpected_dialog_title)
      .message(R.string.error_unexpected_dialog_message)
      .positiveButton(R.string.error_unexpected_dialog_positiveButton)
      .build();
  }

  private AlertShowEventHelper() {
  }
}
