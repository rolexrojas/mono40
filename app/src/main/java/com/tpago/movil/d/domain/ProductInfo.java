package com.tpago.movil.d.domain;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.tpago.movil.domain.Bank;

import java.math.BigDecimal;

/**
 * @author hecvasro
 */
@Deprecated
@AutoValue
public abstract class ProductInfo {
  public static ProductInfo create(Product product) {
    return new AutoValue_ProductInfo(
      product.getBank(),
      product.getType(),
      product.getAlias(),
      product.getNumber(),
      product.getCurrency(),
      product.getQueryFee(),
      Product.isPaymentOption(product),
      Product.isDefaultPaymentOption(product),
      null);
  }

  public static TypeAdapter<ProductInfo> typeAdapter(Gson gson) {
    return new AutoValue_ProductInfo.GsonTypeAdapter(gson)
      .setDefaultQueryFee(BigDecimal.ZERO);
  }

  @SerializedName("bank") public abstract Bank getBank();
  @SerializedName("account-type") public abstract ProductType getType();
  @SerializedName("account-alias") public abstract String getAlias();
  @SerializedName("account-number") public abstract String getNumber();
  @SerializedName("currency") public abstract String getCurrency();
  @SerializedName("query-fee") public abstract BigDecimal getQueryFee();
  @SerializedName("payable") public abstract boolean isPaymentMethod();
  @SerializedName("default-account") public abstract boolean isDefaultPaymentMethod();
  @SerializedName("recipient-name") @Nullable public abstract String getRecipientName();
}
