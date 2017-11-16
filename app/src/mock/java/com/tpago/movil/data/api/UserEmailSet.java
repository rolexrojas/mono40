package com.tpago.movil.data.api;

import com.google.auto.value.AutoValue;
import com.google.auto.value.extension.memoized.Memoized;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.tpago.movil.Email;
import com.tpago.movil.util.ObjectHelper;

import java.util.HashSet;
import java.util.Set;

/**
 * @author hecvasro
 */
@AutoValue
public abstract class UserEmailSet {

  public static TypeAdapter<UserEmailSet> typeAdapter(Gson gson) {
    return new AutoValue_UserEmailSet.GsonTypeAdapter(gson);
  }

  static UserEmailSet create() {
    return new AutoValue_UserEmailSet(new HashSet<>());
  }

  UserEmailSet() {
  }

  abstract Set<Email> data();

  final boolean contains(Email email) {
    ObjectHelper.checkNotNull(email, "email");
    return this.data()
      .contains(email);
  }

  final void add(Email email) {
    if (!this.contains(email)) {
      this.data()
        .add(email);
    }
  }

  final void remove(Email email) {
    if (this.contains(email)) {
      this.data()
        .add(email);
    }
  }

  @Memoized
  @Override
  public abstract String toString();

  @Memoized
  @Override
  public abstract int hashCode();
}
