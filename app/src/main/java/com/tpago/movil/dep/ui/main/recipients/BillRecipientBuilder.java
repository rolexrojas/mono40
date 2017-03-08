package com.tpago.movil.dep.ui.main.recipients;

import android.net.Uri;

import com.tpago.movil.Partner;
import com.tpago.movil.dep.data.res.DepAssetProvider;
import com.tpago.movil.dep.domain.BillRecipient;
import com.tpago.movil.dep.domain.api.ApiResult;
import com.tpago.movil.dep.domain.api.DepApiBridge;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author hecvasro
 */
class BillRecipientBuilder extends RecipientBuilder {
  private final String authToken;
  private final DepApiBridge apiBridge;
  private final DepAssetProvider assetProvider;

  private final Partner partner;

  BillRecipientBuilder(
    String authToken,
    DepApiBridge apiBridge,
    Partner partner,
    DepAssetProvider assetProvider) {
    this.authToken = authToken;
    this.apiBridge = apiBridge;
    this.partner = partner;
    this.assetProvider = assetProvider;
  }

  @Override
  public Uri getImageUri() {
    return assetProvider.getLogoUri(partner, DepAssetProvider.STYLE_24_PRIMARY);
  }

  @Override
  public String getTitle() {
    return partner.getName();
  }

  @Override
  public Observable<Result> build(final String number, final String pin) {
    return apiBridge.addBill(authToken, partner, number, pin)
      .map(new Func1<ApiResult<Void>, Result>() {
        @Override
        public Result call(ApiResult<Void> result) {
          if (result.isSuccessful()) {
            return new Result(new BillRecipient(partner, number));
          } else {
            return new Result(result.getError().getDescription());
          }
        }
      });
  }
}
