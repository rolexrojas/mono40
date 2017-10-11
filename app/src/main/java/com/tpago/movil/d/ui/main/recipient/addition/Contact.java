package com.tpago.movil.d.ui.main.recipient.addition;

import android.net.Uri;
import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.auto.value.extension.memoized.Memoized;
import com.tpago.movil.PhoneNumber;
import com.tpago.movil.d.domain.util.StringUtils;
import com.tpago.movil.d.domain.Matchable;

/**
 * Contact representation.
 *
 * @author hecvasro
 */
@AutoValue
public abstract class Contact implements Matchable {

  public static Builder builder() {
    return new AutoValue_Contact.Builder();
  }

  public abstract PhoneNumber phoneNumber();

  public abstract String name();

  public abstract Uri pictureUri();

  @Memoized
  @Override
  public abstract String toString();

  @Memoized
  @Override
  public abstract int hashCode();

  @Override
  public boolean matches(@Nullable String query) {
    return StringUtils.matches(
      this.phoneNumber()
        .value(),
      query
    ) ||
      StringUtils.matches(this.name(), query);
  }

  @AutoValue.Builder
  public static abstract class Builder {

    public abstract Builder phoneNumber(PhoneNumber phoneNumber);

    public abstract Builder name(String name);

    public abstract Builder pictureUri(Uri pictureUri);

    public abstract Contact build();
  }
}
