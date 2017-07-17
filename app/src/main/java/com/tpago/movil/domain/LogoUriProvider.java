package com.tpago.movil.domain;

import android.net.Uri;

/**
 * @author hecvasro
 */
public interface LogoUriProvider {
  Uri getLogoUri(@LogoStyle String assetStyle);
}
