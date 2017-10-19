package com.tpago.movil.dep;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author hecvasro
 */
@StringDef({
  MimeType.IMAGE
})
@Retention(RetentionPolicy.SOURCE)
public @interface MimeType {

  String IMAGE = "image/*";
}
