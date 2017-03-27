package com.tpago.movil.d.data.api;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.tpago.movil.Partner;
import com.tpago.movil.d.domain.BillRecipient;
import com.tpago.movil.d.domain.Recipient;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Func1;

/**
 * @author hecvasro
 */
@AutoValue
public abstract class BillResponseBody {
  public static TypeAdapter<BillResponseBody> typeAdapter(Gson gson) {
    return new AutoValue_BillResponseBody.GsonTypeAdapter(gson);
  }

  public static Recipient map(BillResponseBody body) {
    return new BillRecipient(body.getPartner(), body.getContractNumber());
  }

  public static Func1<List<BillResponseBody>, List<Recipient>> mapFunc() {
    return new Func1<List<BillResponseBody>, List<Recipient>>() {
      @Override
      public List<Recipient> call(List<BillResponseBody> billList) {
        final List<Recipient> recipientList = new ArrayList<>();
        for (BillResponseBody bill : billList) {
          recipientList.add(map(bill));
        }
        return recipientList;
      }
    };
  }

  @SerializedName("partner") public abstract Partner getPartner();
  @SerializedName("contract") public abstract String getContractNumber();
}
