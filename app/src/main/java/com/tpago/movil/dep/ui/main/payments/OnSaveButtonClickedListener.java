package com.tpago.movil.dep.ui.main.payments;

import com.tpago.movil.dep.domain.Recipient;

/**
 * @author hecvasro
 */
public interface OnSaveButtonClickedListener {
  void onSaveButtonClicked(Recipient recipient, String label);
}
