package com.tpago.movil.d.data.api;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.tpago.movil.Partner;

import java.util.List;

import rx.functions.Func1;

/**
 * @author hecvasro
 */
@Deprecated
@AutoValue
public abstract class PartnerListRequestResponse {
  public static TypeAdapter<PartnerListRequestResponse> typeAdapter(Gson gson) {
    return new AutoValue_PartnerListRequestResponse.GsonTypeAdapter(gson);
  }

  public static Func1<PartnerListRequestResponse, List<Partner>> mapFunc() {
    return new Func1<PartnerListRequestResponse, List<Partner>>() {
      @Override
      public List<Partner> call(PartnerListRequestResponse data) {
        return data._embedded().getPartners();
      }
    };
  }

  public abstract PartnerListEmbeddedRequestResponse _embedded();
}
