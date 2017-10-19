package com.tpago.movil.session;

import android.support.annotation.StringRes;

import com.tpago.movil.R;

/**
 * Enumeration of all the alternatives method that can be used to open sessions.
 *
 * @author hecvasro
 */
public enum SessionOpeningMethod {
  CODE(R.string.code),
  FINGERPRINT(R.string.fingerprint);

  @StringRes public final int stringId;

  SessionOpeningMethod(@StringRes int stringId) {
    this.stringId = stringId;
  }
}
