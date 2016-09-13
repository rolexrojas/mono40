package com.gbh.tpago;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * TODO
 *
 * @author hecvasro
 */
@IntDef({ AccountType.BANK_ACCOUNT, AccountType.CREDIT_CARD })
@Retention(RetentionPolicy.SOURCE)
public @interface AccountType {
  /**
   * TODO
   */
  int BANK_ACCOUNT = 0;

  /**
   * TODO
   */
  int CREDIT_CARD = 1;
}
