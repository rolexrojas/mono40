package com.tpago.movil.domain.auth;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Alternative unlock method enumeration
 *
 * @author hecvasro
 */
@IntDef({
  AltUnlockMethod.CODE,
  AltUnlockMethod.FINGERPRINT
})
@Retention(RetentionPolicy.SOURCE)
public @interface AltUnlockMethod {

  int CODE = 0;
  int FINGERPRINT = 1;
}
