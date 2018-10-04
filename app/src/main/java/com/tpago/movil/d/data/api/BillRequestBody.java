package com.tpago.movil.d.data.api;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.tpago.movil.company.partner.Partner;

/**
 * @author hecvasro
 */
@Deprecated
@AutoValue
public abstract class BillRequestBody {

  public static BillRequestBody create(Partner partner, String contractNumber, String pin) {
    return new AutoValue_BillRequestBody(partner, contractNumber, pin);
  }

  public static TypeAdapter<BillRequestBody> typeAdapter(Gson gson) {
    return new AutoValue_BillRequestBody.GsonTypeAdapter(gson);
  }

  @SerializedName("partner")
  public abstract Partner getPartner();

  @SerializedName("contract")
  public abstract String getContractNumber();

  @Nullable
  @SerializedName("pin")
  public abstract String getPin();
}
