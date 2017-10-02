package com.tpago.movil.domain.auth.alt;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author hecvasro
 */
@IntDef({
  AltAuthFailureCode.UNAUTHORIZED
})
@Retention(RetentionPolicy.SOURCE)
public @interface AltAuthFailureCode {

  int UNAUTHORIZED = 0;
}
