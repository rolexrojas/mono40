package com.tpago.movil.dep.ui.main.recipients;

import com.tpago.movil.Partner;
import com.tpago.movil.dep.domain.BillRecipient;
import com.tpago.movil.dep.domain.api.ApiResult;
import com.tpago.movil.dep.domain.api.DepApiBridge;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author hecvasro
 */

public class BillRecipientBuilder extends RecipientBuilder {
  private final String authToken;
  private final DepApiBridge apiBridge;

  private final Partner partner;

  public BillRecipientBuilder(String authToken, DepApiBridge apiBridge, Partner partner) {
    this.authToken = authToken;
    this.apiBridge = apiBridge;
    this.partner = partner;
  }

  @Override
  public String getImagePath() {
    return null;
  }

  @Override
  public String getTitle() {
    return partner.getName();
  }

  @Override
  public Observable<Result> build(String number) {
    return apiBridge.addBill(authToken, partner, number)
      .map(new Func1<ApiResult<BillRecipient>, Result>() {
        @Override
        public Result call(ApiResult<BillRecipient> result) {
          if (result.isSuccessful()) {
            return new Result(result.getData());
          } else {
            return new Result(result.getError().getDescription());
          }
        }
      });
  }
}
