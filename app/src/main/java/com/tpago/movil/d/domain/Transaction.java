package com.tpago.movil.d.domain;

import com.google.auto.value.AutoValue;
import com.google.auto.value.extension.memoized.Memoized;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

/**
 * @author Hector Vasquez
 */
@AutoValue
public abstract class Transaction {
  private static final DateFormat FORMAT = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

  public static TypeAdapter<Transaction> typeAdapter(Gson gson) {
    return new AutoValue_Transaction.GsonTypeAdapter(gson);
  }

  private final String id = UUID.randomUUID()
    .toString();

  @SerializedName("date") public abstract String dateString();
  @SerializedName("transaction-type") public abstract String type();
  @SerializedName("transaction-detail") public abstract String detail();
  @SerializedName("amount") public abstract BigDecimal amount();

  @Memoized
  public Date date() {
    try {
      return FORMAT.parse(dateString());
    } catch (ParseException exception) {
      throw new RuntimeException(exception);
    }
  }

  @Memoized
  public long time() {
    return this.date()
      .getTime();
  }

  @Override
  public boolean equals(Object object) {
    if (object == this) {
      return true;
    } else if (object instanceof Transaction) {
      return ((Transaction) object).id.equals(id);
    } else {
      return false;
    }
  }

  @Override
  public int hashCode() {
    return this.id.hashCode();
  }
}
