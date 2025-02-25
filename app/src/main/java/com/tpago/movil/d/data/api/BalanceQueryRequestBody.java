package com.tpago.movil.d.data.api;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.tpago.movil.company.bank.Bank;
import com.tpago.movil.d.domain.Product;

/**
 * TODO
 *
 * @author hecvasro
 */
@Deprecated
@AutoValue
public abstract class BalanceQueryRequestBody {

  static BalanceQueryRequestBody create(Product product, String pin) {
    return new AutoValue_BalanceQueryRequestBody(
      product.getType()
        .name(),
      product.getAlias(),
      product.getNumber(),
      product.getBank(),
      product.getCurrency(),
      pin
    );
  }

  public static TypeAdapter<BalanceQueryRequestBody> typeAdapter(Gson gson) {
    return new AutoValue_BalanceQueryRequestBody.GsonTypeAdapter(gson);
  }

  @SerializedName("account-type")
  abstract String getAccountType();

  @SerializedName("account-alias")
  abstract String getAccountAlias();

  @SerializedName("account-number")
  abstract String getAccountNumber();

  @SerializedName("bank")
  abstract Bank getBank();

  @SerializedName("currency")
  abstract String getCurrency();

  @SerializedName("pin")
  abstract String getPin();
}
