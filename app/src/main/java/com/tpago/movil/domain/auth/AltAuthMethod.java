package com.tpago.movil.domain.auth;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Alternative authentication method enumeration
 *
 * @author hecvasro
 */
@IntDef({
  AltAuthMethod.CODE,
  AltAuthMethod.FINGERPRINT
})
@Retention(RetentionPolicy.SOURCE)
public @interface AltAuthMethod {

  int CODE = 0;
  int FINGERPRINT = 1;
}
