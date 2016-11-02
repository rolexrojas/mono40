package com.gbh.movil.domain;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Recipient's type enumeration.
 *
 * @author hecvasro
 */
@Retention(RetentionPolicy.SOURCE)
@IntDef({ RecipientType.CONTACT, RecipientType.CONTRACT, RecipientType.CREDIT_CARD})
public @interface RecipientType {
  int CONTACT = 0;
  int CONTRACT = 1;
  int CREDIT_CARD = 2;
}
