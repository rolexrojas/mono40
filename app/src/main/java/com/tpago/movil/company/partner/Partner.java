package com.tpago.movil.company.partner;

import androidx.annotation.StringDef;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.tpago.movil.company.Company;
import com.tpago.movil.util.IncludeHashEquals;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Partner representation
 *
 * @author hecvasro
 */
@AutoValue
public abstract class Partner extends Company {

  public static TypeAdapter<Partner> typeAdapter(Gson gson) {
    return new AutoValue_Partner.GsonTypeAdapter(gson);
  }

  Partner() {
  }

  @IncludeHashEquals
  @SerializedName("partner-type")
  @Type
  public abstract String type();

  @IncludeHashEquals
  @SerializedName("partner-code")
  @Override
  public abstract int code();

  @SerializedName("partner-id")
  @Override
  public abstract String id();

  @SerializedName("partner-name")
  @Override
  public abstract String name();

  @SerializedName("image-url")
  @Override
  public abstract String logoTemplate();

  @StringDef({
    Type.CARRIER,
    Type.PROVIDER
  })
  @Retention(RetentionPolicy.SOURCE)
  public @interface Type {

    String CARRIER = "T";
    String PROVIDER = "L";
  }
}
