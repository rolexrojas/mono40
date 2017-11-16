package com.tpago.movil.d.domain;

import android.net.Uri;

/**
 * @author hecvasro
 */
@Deprecated
public interface LogoUriProvider {
  Uri getLogoUri(@LogoStyle String assetStyle);
}
