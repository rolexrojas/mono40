package com.tpago.movil.dep;

import android.os.Parcelable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author hecvasro
 */
@Deprecated
@AutoValue
public abstract class Partner implements Parcelable {

  public static final String TYPE_CARRIER = "T";
  public static final String TYPE_PROVIDER = "L";

  public static TypeAdapter<Partner> typeAdapter(Gson gson) {
    return new $AutoValue_Partner.GsonTypeAdapter(gson);
  }

  @Deprecated
  public static void sort(List<Partner> partnerList) {
    Collections.sort(partnerList, new Comparator<Partner>() {
      @Override
      public int compare(Partner pa, Partner pb) {
        return pa.getName()
          .compareTo(pb.getName());
      }
    });
  }

  public static Builder builder() {
    return new AutoValue_Partner.Builder();
  }

  Partner() {
  }

  @SerializedName("partner-code")
  public abstract int getCode();

  @SerializedName("partner-type")
  public abstract String getType();

  @SerializedName("partner-id")
  public abstract String getId();

  @SerializedName("partner-updateName")
  public abstract String getName();

  @SerializedName("image-url")
  public abstract String getImageUriTemplate();

  @AutoValue.Builder
  public static abstract class Builder {

    Builder() {
    }

    public abstract Builder setCode(int code);

    public abstract Builder setType(String type);

    public abstract Builder setId(String id);

    public abstract Builder setName(String name);

    public abstract Builder setImageUriTemplate(String imageUriTemplate);

    public abstract Partner build();
  }
}
