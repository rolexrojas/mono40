package com.tpago.movil.d.data.api;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.tpago.movil.d.domain.Product;
import com.tpago.movil.d.domain.Bank;

import java.math.BigDecimal;

/**
 * @author hecvasro
 */
@Deprecated
@AutoValue
public abstract class RecipientAccountRequestBody {
  public static RecipientAccountRequestBody create(Product product, String name) {
    return new AutoValue_RecipientAccountRequestBody(
      product.getBank(),
      product.getType().name(),
      product.getAlias(),
      product.getNumber(),
      product.getCurrency(),
      product.getQueryFee(),
      Product.isPaymentOption(product),
      Product.isDefaultPaymentOption(product),
      name);
  }

  public static TypeAdapter<RecipientAccountRequestBody> typeAdapter(Gson gson) {
    return new AutoValue_RecipientAccountRequestBody.GsonTypeAdapter(gson);
  }

  @SerializedName("bank") abstract Bank bank();
  @SerializedName("account-type") abstract String type();
  @SerializedName("account-alias") abstract String alias();
  @SerializedName("account-number") abstract String number();
  @SerializedName("currency") abstract String currency();
  @SerializedName("query-fee") abstract BigDecimal queryFee();
  @SerializedName("default-account") abstract boolean isDefaultPaymentMethod();
  @SerializedName("payable") abstract boolean isPaymentMethod();
  @SerializedName("recipient-name") abstract String name();
}
