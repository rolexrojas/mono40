package com.tpago.movil.domain;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author hecvasro
 */
@Retention(RetentionPolicy.SOURCE)
@StringDef({
  LogoStyle.GRAY_20,
  LogoStyle.GRAY_36,
  LogoStyle.PRIMARY_24,
  LogoStyle.WHITE_36
})
public @interface LogoStyle {
  String GRAY_20 = "_20";
  String GRAY_36 = "_36";
  String PRIMARY_24 = "_24";
  String WHITE_36 = "_36_bln";
}
