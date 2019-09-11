package com.tpago.movil.d.ui.main.recipient.addition;

import android.content.Context;
import android.net.Uri;
import androidx.core.util.Pair;

import com.tpago.movil.company.Company;
import com.tpago.movil.company.CompanyHelper;
import com.tpago.movil.company.bank.Bank;
import com.tpago.movil.d.domain.AccountRecipient;
import com.tpago.movil.d.domain.Product;
import com.tpago.movil.d.domain.api.ApiResult;
import com.tpago.movil.d.domain.api.DepApiBridge;
import com.tpago.movil.util.ObjectHelper;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author hecvasro
 */
class ProductRecipientBuilder extends RecipientBuilder {

  private final DepApiBridge apiBridge;
  private final Bank bank;
  private String accountType;
  private final CompanyHelper companyHelper;

  ProductRecipientBuilder(
    DepApiBridge apiBridge,
    Bank bank,
    CompanyHelper companyHelper,
    String accountType
  ) {
    this.apiBridge = apiBridge;
    this.bank = bank;
    this.companyHelper = ObjectHelper.checkNotNull(companyHelper, "companyHelper");
    this.accountType = accountType;
  }

  ProductRecipientBuilder(
      DepApiBridge apiBridge,
      Bank bank,
      CompanyHelper companyHelper
  ) {
    this.apiBridge = apiBridge;
    this.bank = bank;
    this.companyHelper = ObjectHelper.checkNotNull(companyHelper, "companyHelper");
  }

  @Override
  public Uri getImageUri(Context context) {
    return this.companyHelper.getLogoUri(this.bank, Company.LogoStyle.COLORED_24);
  }

  @Override
  public String getTitle() {
    return this.bank.name();
  }

  @Override
  public String getCategoryName() {
    return "cuenta";
  }

  @Override
  public Observable<Result> build(final String number, final String pin) {
    if(ObjectHelper.isNotNull(accountType)) {
      return this.apiBridge.checkAccountNumber(this.bank, number, accountType)
          .map(new Func1<ApiResult<Pair<String, Product>>, Result>() {
            @Override
            public Result call(ApiResult<Pair<String, Product>> result) {
              if (result.isSuccessful()) {
                final Pair<String, Product> data = result.getData();
                final AccountRecipient recipient = AccountRecipient.builder()
                    .bank(bank)
                    .number(number)
                    .product(data.second)
                    .label(data.first)
                    .build();
                return new Result(recipient);
              } else {
                return new Result(
                    result.getError()
                        .getDescription()
                );
              }
            }
          });

    }
    return this.apiBridge.checkAccountNumber(this.bank, number)
      .map(new Func1<ApiResult<Pair<String, Product>>, Result>() {
        @Override
        public Result call(ApiResult<Pair<String, Product>> result) {
          if (result.isSuccessful()) {
            final Pair<String, Product> data = result.getData();
            final AccountRecipient recipient = AccountRecipient.builder()
              .bank(bank)
              .number(number)
              .product(data.second)
              .label(data.first)
              .build();
            return new Result(recipient);
          } else {
            return new Result(
              result.getError()
                .getDescription()
            );
          }
        }
      });
  }
}
