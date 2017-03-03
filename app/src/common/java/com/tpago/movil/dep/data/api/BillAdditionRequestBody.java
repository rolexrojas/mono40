package com.tpago.movil.dep.data.api;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.tpago.movil.Partner;

/**
 * @author hecvasro
 */
@AutoValue
public abstract class BillAdditionRequestBody {
  public static BillAdditionRequestBody create(Partner partner, String contractNumber, String pin) {
    return new AutoValue_BillAdditionRequestBody(partner, contractNumber, pin);
  }

  public static TypeAdapter<BillAdditionRequestBody> typeAdapter(Gson gson) {
    return new AutoValue_BillAdditionRequestBody.GsonTypeAdapter(gson);
  }

  @SerializedName("partner") public abstract Partner getPartner();
  @SerializedName("contract") public abstract String getContractNumber();
  @SerializedName("pin") public abstract String getPin();
}
