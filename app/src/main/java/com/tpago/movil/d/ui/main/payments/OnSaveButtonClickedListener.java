package com.tpago.movil.d.ui.main.payments;

import com.tpago.movil.d.domain.Recipient;

/**
 * @author hecvasro
 */
public interface OnSaveButtonClickedListener {
  void onSaveButtonClicked(Recipient recipient, String label);
}
