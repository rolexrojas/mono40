package com.tpago.movil.d.ui.main.recipient.addition;

import android.content.Context;
import android.net.Uri;
import android.support.v4.util.Pair;

import com.tpago.movil.d.domain.Banks;
import com.tpago.movil.d.domain.AccountRecipient;
import com.tpago.movil.d.domain.Product;
import com.tpago.movil.d.domain.api.ApiResult;
import com.tpago.movil.d.domain.api.DepApiBridge;
import com.tpago.movil.d.domain.Bank;
import com.tpago.movil.d.domain.LogoStyle;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author hecvasro
 */
class ProductRecipientBuilder extends RecipientBuilder {

  private final String authToken;
  private final DepApiBridge apiBridge;
  private final Bank bank;

  ProductRecipientBuilder(
    String authToken,
    DepApiBridge apiBridge,
    Bank bank
  ) {
    this.authToken = authToken;
    this.apiBridge = apiBridge;
    this.bank = bank;
  }

  @Override
  public Uri getImageUri(Context context) {
    return this.bank.getLogoUri(LogoStyle.PRIMARY_24);
  }

  @Override
  public String getTitle() {
    return Banks.getName(this.bank);
  }

  @Override
  public String getCategoryName() {
    return "cuenta";
  }

  @Override
  public Observable<Result> build(final String number, final String pin) {
    return this.apiBridge.checkAccountNumber(this.authToken, this.bank, number)
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
