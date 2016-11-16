package com.gbh.movil.domain;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Recipient type enumeration.
 *
 * @author hecvasro
 */
@Retention(RetentionPolicy.SOURCE)
@IntDef({ RecipientType.CONTACT, RecipientType.INVOICE })
public @interface RecipientType {
  int CONTACT = 0;
  int INVOICE = 1;
}
