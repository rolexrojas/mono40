package com.tpago.movil.dep.data.res;

import android.net.Uri;
import android.support.annotation.ColorInt;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;

import com.tpago.movil.Bank;
import com.tpago.movil.Partner;
import com.tpago.movil.dep.domain.Product;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * TODO
 *
 * @author hecvasro
 */
@Deprecated
public interface DepAssetProvider {
  int STYLE_20_GRAY = 0;
  int STYLE_24_PRIMARY = 1;
  int STYLE_36_GRAY = 2;
  int STYLE_36_WHITE = 3;

  @ColorInt int getPrimaryColor(@NonNull Bank bank);
  @ColorInt int getTextColor(@NonNull Bank bank);

  Uri getLogoUri(Bank bank, @Style int style);
  Uri getLogoUri(Partner partner, @Style int style);

  Uri getImageUri(Product product);

  @Retention(RetentionPolicy.SOURCE)
  @IntDef({ STYLE_20_GRAY, STYLE_24_PRIMARY, STYLE_36_GRAY, STYLE_36_WHITE })
  @interface Style {
  }
}
