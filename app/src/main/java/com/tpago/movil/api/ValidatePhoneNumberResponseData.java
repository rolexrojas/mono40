package com.tpago.movil.api;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.tpago.movil.PhoneNumber;

import io.reactivex.functions.Function;

/**
 * @author hecvasro
 */
@AutoValue
public abstract class ValidatePhoneNumberResponseData {
  static ValidatePhoneNumberResponseData create(PhoneNumber.State state) {
    return new AutoValue_ValidatePhoneNumberResponseData(state);
  }

  static Function<ValidatePhoneNumberResponseData, PhoneNumber.State> mapperFunc() {
    return new Function<ValidatePhoneNumberResponseData, PhoneNumber.State>() {
      @Override
      public PhoneNumber.State apply(ValidatePhoneNumberResponseData data) throws Exception {
        return data.status();
      }
    };
  }

  public static TypeAdapter<ValidatePhoneNumberResponseData> typeAdapter(Gson gson) {
    return new AutoValue_ValidatePhoneNumberResponseData.GsonTypeAdapter(gson);
  }

  abstract PhoneNumber.State status();
}
