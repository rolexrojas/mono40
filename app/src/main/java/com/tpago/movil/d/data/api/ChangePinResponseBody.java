package com.tpago.movil.d.data.api;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

import rx.functions.Func1;

@AutoValue
public abstract class ChangePinResponseBody {

  public static TypeAdapter<ChangePinResponseBody> typeAdapter(Gson gson) {
    return new AutoValue_ChangePinResponseBody.GsonTypeAdapter(gson);
  }

  public static Func1<ChangePinResponseBody, String> mapFunc() {
    return new Func1<ChangePinResponseBody, String>() {
      @Override
      public String call(ChangePinResponseBody body) {
        return body.getMessage();
      }
    };
  }

  @SerializedName("dynaMsgIns")
  public abstract String getMessage();

  @SerializedName("respKey")
  public abstract String getRespKey();

  @SerializedName("dynaMsgContent")
  public abstract String getDynaMsgContent();

}
