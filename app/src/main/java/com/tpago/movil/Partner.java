package com.tpago.movil;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author hecvasro
 */
@AutoValue
public abstract class Partner implements Serializable {
  public static Partner create(int code, String id, String name, String logoUri) {
    return new AutoValue_Partner(code, id, name, logoUri);
  }

  public static TypeAdapter<Partner> typeAdapter(Gson gson) {
    return new AutoValue_Partner.GsonTypeAdapter(gson);
  }

  @Deprecated
  public static void sort(List<Partner> partnerList) {
    Collections.sort(partnerList, new Comparator<Partner>() {
      @Override
      public int compare(Partner pa, Partner pb) {
        return pa.getName().compareTo(pb.getName());
      }
    });
  }

  @SerializedName("partner-code") public abstract int getCode();
  @SerializedName("partner-id") public abstract String getId();
  @SerializedName("partner-name") public abstract String getName();
  @SerializedName("image-url") public abstract String getImageUriTemplate();
}
