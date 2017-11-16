package com.tpago.movil.bank;

import com.google.auto.value.AutoValue;
import com.google.auto.value.extension.memoized.Memoized;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.tpago.movil.company.CompanyList;

import java.util.List;

/**
 * @author hecvasro
 */
@AutoValue
public abstract class BankList extends CompanyList<Bank> {

  public static TypeAdapter<BankList> typeAdapter(Gson gson) {
    return new AutoValue_BankList.GsonTypeAdapter(gson);
  }

  public static Builder builder() {
    return new AutoValue_BankList.Builder();
  }

  BankList() {
  }

  public abstract List<Bank> value();

  @Memoized
  @Override
  public abstract String toString();

  @Memoized
  @Override
  public abstract int hashCode();

  @AutoValue.Builder
  public static abstract class Builder {

    Builder() {
    }

    public abstract Builder queryTime(long queryTime);

    public abstract Builder value(List<Bank> value);

    public abstract BankList build();
  }
}
