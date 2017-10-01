package com.tpago.movil.app.ui.main.settings.auth.alt;

import android.support.annotation.StringRes;

import com.tpago.movil.R;
import com.tpago.movil.domain.auth.alt.AltAuthMethod;
import com.tpago.movil.util.ObjectHelper;

/**
 * @author hecvasro
 */
public final class UiAltAuthMethodHelper {

  @StringRes
  public static int findStringResId(AltAuthMethod method) {
    if (ObjectHelper.isNull(method)) {
      return R.string.usePassword;
    } else if (method == AltAuthMethod.CODE) {
      return R.string.code;
    } else {
      return R.string.fingerprint;
    }
  }

  private UiAltAuthMethodHelper() {
  }
}
