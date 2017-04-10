package com.tpago.movil.d.data.api;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

import rx.functions.Func1;

/**
 * @author hecvasro
 */
@AutoValue
public abstract class RecipientInfoResponseBody {
  static Func1<RecipientInfoResponseBody, String> mapFunc() {
    return new Func1<RecipientInfoResponseBody, String>() {
      @Override
      public String call(RecipientInfoResponseBody recipientInfoResponseBody) {
        return recipientInfoResponseBody.name();
      }
    };
  }

  public static TypeAdapter<RecipientInfoResponseBody> typeAdapter(Gson gson) {
    return new AutoValue_RecipientInfoResponseBody.GsonTypeAdapter(gson);
  }

  @SerializedName("Beneficiario") abstract String name();
}
