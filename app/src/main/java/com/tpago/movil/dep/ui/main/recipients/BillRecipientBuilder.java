package com.tpago.movil.dep.ui.main.recipients;

import android.content.Context;
import android.net.Uri;

import com.tpago.movil.Partner;
import com.tpago.movil.api.ApiImageUriBuilder;
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

  private final Partner partner;

  BillRecipientBuilder(
    String authToken,
    DepApiBridge apiBridge,
    Partner partner) {
    this.authToken = authToken;
    this.apiBridge = apiBridge;
    this.partner = partner;
  }

  @Override
  public Uri getImageUri(Context context) {
    return ApiImageUriBuilder.build(context, partner, ApiImageUriBuilder.Style.PRIMARY_24);
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
