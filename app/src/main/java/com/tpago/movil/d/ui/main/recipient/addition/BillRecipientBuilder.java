package com.tpago.movil.d.ui.main.recipient.addition;

import android.content.Context;
import android.net.Uri;

import com.tpago.movil.Partner;
import com.tpago.movil.api.ApiImageUriBuilder;
import com.tpago.movil.d.domain.BillBalance;
import com.tpago.movil.d.domain.BillRecipient;
import com.tpago.movil.d.domain.api.ApiResult;
import com.tpago.movil.d.domain.api.DepApiBridge;

import rx.Observable;
import rx.functions.Func1;
import timber.log.Timber;

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
    Partner partner
  ) {
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
  public String getCategoryName() {
    return "factura";
  }

  @Override
  public Observable<Result> build(final String number, final String pin) {
    return apiBridge.addBill(authToken, partner, number, pin)
      .map(new Func1<ApiResult<Void>, Result>() {
        @Override
        public Result call(ApiResult<Void> result) {
          if (result.isSuccessful()) {
            final BillRecipient recipient = new BillRecipient(partner, number);
            try {
              final ApiResult<BillBalance> queryBalanceResult = apiBridge
                .queryBalance(authToken, recipient);
              if (queryBalanceResult.isSuccessful()) {
                recipient.setBalance(queryBalanceResult.getData());
              }
            } catch (Exception exception) {
              Timber.i(exception);
            }
            return new Result(recipient);
          } else {
            return new Result(result.getError()
              .getDescription());
          }
        }
      });
  }
}
