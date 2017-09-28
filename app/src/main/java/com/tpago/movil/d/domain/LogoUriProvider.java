package com.tpago.movil.d.domain;

import android.net.Uri;

/**
 * @author hecvasro
 */
public interface LogoUriProvider {
  Uri getLogoUri(@LogoStyle String assetStyle);
}
