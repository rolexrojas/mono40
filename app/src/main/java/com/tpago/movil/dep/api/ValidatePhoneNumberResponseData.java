package com.tpago.movil.dep.api;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.tpago.movil.PhoneNumber;

import io.reactivex.functions.Function;

/**
 * @author hecvasro
 */
@Deprecated
@AutoValue
public abstract class ValidatePhoneNumberResponseData {

  static ValidatePhoneNumberResponseData create(@PhoneNumber.State int state) {
    return new AutoValue_ValidatePhoneNumberResponseData(state);
  }

  static Function<ValidatePhoneNumberResponseData, Integer> mapperFunc() {
    return (data) -> data.status();
  }

  public static TypeAdapter<ValidatePhoneNumberResponseData> typeAdapter(Gson gson) {
    return new AutoValue_ValidatePhoneNumberResponseData.GsonTypeAdapter(gson);
  }

  @PhoneNumber.State
  abstract int status();
}
