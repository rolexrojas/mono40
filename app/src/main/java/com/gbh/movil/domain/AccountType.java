package com.gbh.movil.domain;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Account type enumeration.
 *
 * @author hecvasro
 */
@IntDef({ AccountType.BANK_ACCOUNT, AccountType.CREDIT_CARD })
@Retention(RetentionPolicy.SOURCE)
public @interface AccountType {
  int BANK_ACCOUNT = 0;
  int CREDIT_CARD = 1;
}
