package com.gbh.movil.data.res;

import android.net.Uri;
import android.support.annotation.ColorInt;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;

import com.gbh.movil.domain.Bank;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * TODO
 *
 * @author hecvasro
 */
public interface ResourceProvider {
  /**
   * TODO
   */
  int STYLE_20_GRAY = 0;
  /**
   * TODO
   */
  int STYLE_24_PRIMARY = 1;
  /**
   * TODO
   */
  int STYLE_36_GRAY = 2;
  /**
   * TODO
   */
  int STYLE_36_WHITE = 3;

  /**
   * TODO
   *
   * @param bank
   *   TODO
   *
   * @return TODO
   */
  @ColorInt
  int getPrimaryColor(@NonNull Bank bank);

  /**
   * TODO
   *
   * @param bank
   *   TODO
   *
   * @return TODO
   */
  @ColorInt
  int getTextColor(@NonNull Bank bank);

  /**
   * TODO
   *
   * @return TODO
   */
  @NonNull
  Uri getLogoUri(@NonNull Bank bank, @Style int style);

  /**
   * TODO
   */
  @Retention(RetentionPolicy.SOURCE)
  @IntDef({ STYLE_20_GRAY, STYLE_24_PRIMARY, STYLE_36_GRAY, STYLE_36_WHITE })
  @interface Style {
  }
}
