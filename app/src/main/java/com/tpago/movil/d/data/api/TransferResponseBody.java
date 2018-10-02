package com.tpago.movil.d.data.api;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.tpago.movil.util.digit.DigitUtil;

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
        return body.getTransactionMessage();
      }
    };
  }

  @SerializedName("transaction-message")
  public abstract String getTransactionMessage();
}
