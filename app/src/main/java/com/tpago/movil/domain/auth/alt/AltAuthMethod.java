package com.tpago.movil.domain.auth.alt;

import android.support.annotation.StringRes;

import com.tpago.movil.R;

/**
 * Alternative authentication method enumeration
 *
 * @author hecvasro
 */
public enum AltAuthMethod {
  CODE(R.string.code),
  FINGERPRINT(R.string.fingerprint);

  @StringRes public final int stringId;

  AltAuthMethod(@StringRes int stringId) {
    this.stringId = stringId;
  }
}
