package com.tpago.movil.d.data.api;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.tpago.movil.Partner;

import java.util.List;

/**
 * @author hecvasro
 */

@AutoValue
public abstract class PartnerListEmbeddedRequestResponse {
    public static TypeAdapter<PartnerListEmbeddedRequestResponse> typeAdapter(Gson gson) {
    return new AutoValue_PartnerListEmbeddedRequestResponse.GsonTypeAdapter(gson);
  }

  public abstract List<Partner> getPartners();
}
