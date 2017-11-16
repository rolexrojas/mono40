package com.tpago.movil.company;

import android.support.annotation.StringDef;

/**
 * {@link Company} logo style.
 *
 * @author hecvasro
 */
@StringDef({
  LogoStyle.COLORED_24,
  LogoStyle.GRAY_20,
  LogoStyle.GRAY_36,
  LogoStyle.WHITE_36
})
public @interface LogoStyle {

  /**
   * Colored of 24 x 24
   */
  String COLORED_24 = "_24";

  /**
   * Gray of 20 x 20
   */
  String GRAY_20 = "_20";

  /**
   * Gray of 36 x 36
   */
  String GRAY_36 = "_36";

  /**
   * White of 36 x 26
   */
  String WHITE_36 = "_36_bln";
}
