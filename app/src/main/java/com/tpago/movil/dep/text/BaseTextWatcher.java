package com.tpago.movil.dep.text;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * @author hecvasro
 */
@Deprecated
public abstract class BaseTextWatcher implements TextWatcher {
  @Override
  public void beforeTextChanged(CharSequence s, int start, int count, int after) {
  }

  @Override
  public void onTextChanged(CharSequence s, int start, int before, int count) {
  }

  @Override
  public void afterTextChanged(Editable s) {
  }
}
