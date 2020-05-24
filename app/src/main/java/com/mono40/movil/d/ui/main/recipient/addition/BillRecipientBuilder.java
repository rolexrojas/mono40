package com.mono40.movil.d.ui.main.recipient.addition;

import android.content.Context;
import android.net.Uri;

import com.mono40.movil.company.Company;
import com.mono40.movil.company.CompanyHelper;
import com.mono40.movil.company.partner.Partner;
import com.mono40.movil.d.domain.BillBalance;
import com.mono40.movil.d.domain.BillRecipient;
import com.mono40.movil.d.domain.api.ApiResult;
import com.mono40.movil.d.domain.api.DepApiBridge;
import com.mono40.movil.util.ObjectHelper;

import rx.Observable;
import rx.functions.Func1;
import timber.log.Timber;

/**
 * @author hecvasro
 */
class BillRecipientBuilder extends RecipientBuilder {

  private final DepApiBridge apiBridge;
  private final Partner partner;
  private final CompanyHelper companyHelper;

  BillRecipientBuilder(DepApiBridge apiBridge, Partner partner, CompanyHelper companyHelper) {
    this.apiBridge = apiBridge;
    this.partner = partner;
    this.companyHelper = ObjectHelper.checkNotNull(companyHelper, "companyHelper");
  }

  @Override
  public Uri getImageUri(Context context) {
    return this.companyHelper.getLogoUri(this.partner, Company.LogoStyle.COLORED_24);
  }

  @Override
  public String getTitle() {
    return this.partner.name();
  }

  @Override
  public String getCategoryName() {
    return "factura";
  }

  @Override
  public Observable<Result> build(final String number, final String pin) {
    return apiBridge.addBill(partner, number, pin)
      .map(new Func1<ApiResult<Void>, Result>() {
        @Override
        public Result call(ApiResult<Void> result) {
          if (result.isSuccessful()) {
            final BillRecipient recipient = new BillRecipient(partner, number);
            try {
              final ApiResult<BillBalance> queryBalanceResult = apiBridge
                .queryBalance(recipient);
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
