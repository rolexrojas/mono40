package com.tpago.movil.dep.data.res;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.tpago.movil.Bank;
import com.tpago.movil.Partner;
import com.tpago.movil.app.DisplayDensity;
import com.tpago.movil.text.Texts;
import com.tpago.movil.util.Preconditions;

import timber.log.Timber;

/**
 * @author hecvasro
 */
public final class RemoteDepAssetProvider implements DepAssetProvider {
  private final LocalDepAssetProvider assetProvider;
  private final DisplayDensity displayDensity;

  public RemoteDepAssetProvider(LocalDepAssetProvider assetProvider, DisplayDensity displayDensity) {
    this.assetProvider = Preconditions.checkNotNull(assetProvider, "assetProvider == null");
    this.displayDensity = Preconditions.checkNotNull(displayDensity, "displayDensity == null");
  }

  private Uri createUri(String url, String code, @Style int style) {
    final String sanitizedUrl = url
      .substring(0, url.lastIndexOf('/') + 1)
      .replace("{size}", displayDensity.name().toLowerCase());
    final StringBuilder builder = new StringBuilder(sanitizedUrl);
    final String styleName;
    if (style == STYLE_20_GRAY) {
      styleName = "20";
    } else if (style == STYLE_24_PRIMARY) {
      styleName = "24";
    } else if (style == STYLE_36_GRAY) {
      styleName = "36";
    } else {
      styleName = "36_bln";
    }
    builder.append(Texts.join("_", code, styleName));
    builder.append(".png");
    return Uri.parse(builder.toString());
  }

  @Override
  public int getPrimaryColor(@NonNull Bank bank) {
    return assetProvider.getPrimaryColor(bank);
  }

  @Override
  public int getTextColor(@NonNull Bank bank) {
    return assetProvider.getTextColor(bank);
  }

  @Override
  public Uri getLogoUri(Bank bank, @Style int style) {
    return createUri(bank.getLogoUri(), bank.getId(), style);
  }

  @Override
  public Uri getLogoUri(Partner partner, @Style int style) {
    return createUri(partner.getLogoUri(), partner.getId(), style);
  }
}
