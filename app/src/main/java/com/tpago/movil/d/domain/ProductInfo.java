package com.tpago.movil.d.domain;

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
  @SerializedName("recipient-name") public abstract String getRecipientName();
}
