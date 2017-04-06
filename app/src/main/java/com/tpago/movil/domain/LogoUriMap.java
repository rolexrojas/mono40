package com.tpago.movil.domain;

import android.net.Uri;
import android.os.Parcelable;

import com.google.auto.value.AutoValue;
import com.tpago.movil.util.Preconditions;

import java.io.Serializable;

/**
 * @author hecvasro
 */
@AutoValue
public abstract class LogoUriMap implements LogoUriProvider, Parcelable, Serializable {
  public static Builder builder(){
    return new AutoValue_LogoUriMap.Builder();
  }

  abstract Uri getUriForGray20();
  abstract Uri getUriForGray36();
  abstract Uri getUriForPrimary24();
  abstract Uri getUriForWhite36();

  @Override
  public Uri getLogoUri(@LogoStyle String codeStyle) {
    Preconditions.assertNotNull(codeStyle, "codeStyle == null");
    switch (codeStyle) {
      case LogoStyle.GRAY_20:
        return getUriForGray20();
      case LogoStyle.GRAY_36:
        return getUriForGray36();
      case LogoStyle.PRIMARY_24:
        return getUriForPrimary24();
      case LogoStyle.WHITE_36:
        return getUriForWhite36();
      default:
        return Uri.EMPTY;
    }
  }

  @AutoValue.Builder
  public static abstract class Builder {
    public abstract Builder setUriForGray20(Uri uri);
    public abstract Builder setUriForGray36(Uri uri);
    public abstract Builder setUriForPrimary24(Uri uri);
    public abstract Builder setUriForWhite36(Uri uri);

    public abstract LogoUriMap build();
  }
}
