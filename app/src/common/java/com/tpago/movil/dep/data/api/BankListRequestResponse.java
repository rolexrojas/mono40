package com.tpago.movil.dep.data.api;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.tpago.movil.Bank;

import java.util.List;

import rx.functions.Func1;

/**
 * @author hecvasro
 */
@AutoValue
public abstract class BankListRequestResponse {
  public static TypeAdapter<BankListRequestResponse> typeAdapter(Gson gson) {
    return new AutoValue_BankListRequestResponse.GsonTypeAdapter(gson);
  }

  public static Func1<BankListRequestResponse, List<Bank>> mapFunc() {
    return new Func1<BankListRequestResponse, List<Bank>>() {
      @Override
      public List<Bank> call(BankListRequestResponse data) {
        return data._embedded().getBanks();
      }
    };
  }

  public abstract BankListEmbeddedRequestResponse _embedded();
}
