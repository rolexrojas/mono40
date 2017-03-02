package com.tpago.movil.dep.data.api;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.tpago.movil.Bank;

import java.util.List;

/**
 * @author hecvasro
 */
@AutoValue
public abstract class BankListEmbeddedRequestResponse {
  public static TypeAdapter<BankListEmbeddedRequestResponse> typeAdapter(Gson gson) {
    return new AutoValue_BankListEmbeddedRequestResponse.GsonTypeAdapter(gson);
  }

  public abstract List<Bank> getBanks();
}
