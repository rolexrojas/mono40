package com.tpago.movil.d.data.api;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.tpago.movil.util.DigitHelper;

import rx.functions.Func1;

/**
 * @author hecvasro
 */
@Deprecated
@AutoValue
public abstract class TransferResponseBody {

  public static TypeAdapter<TransferResponseBody> typeAdapter(Gson gson) {
    return new AutoValue_TransferResponseBody.GsonTypeAdapter(gson);
  }

  public static Func1<TransferResponseBody, String> mapFunc() {
    return new Func1<TransferResponseBody, String>() {
      @Override
      public String call(TransferResponseBody body) {
        return DigitHelper.removeNonDigits(body.getTransactionId());
      }
    };
  }

  @SerializedName("transaction-id")
  public abstract String getTransactionId();
}
