package com.tpago.movil.domain.auth.alt;

import com.google.auto.value.AutoValue;
import com.google.auto.value.extension.memoized.Memoized;
import com.tpago.movil.session.User;
import com.tpago.movil.util.StringHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hecvasro
 */
@AutoValue
public abstract class AltOpenSessionSignatureData {

  public static Builder builder() {
    return new AutoValue_AltOpenSessionSignatureData.Builder();
  }

  AltOpenSessionSignatureData() {
  }

  public abstract User user();

  public abstract String deviceId();

  @Memoized
  public byte[] toByteArray() throws Exception {
    final List<Object> tokenList = new ArrayList<>();

    final User user = this.user();
    tokenList.add(user.id());
    tokenList.add(
      user.phoneNumber()
        .value()
    );

    tokenList.add(this.deviceId());

    return StringHelper.join(":", tokenList)
      .getBytes("UTF-8");
  }

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

    public abstract Builder user(User user);

    public abstract Builder deviceId(String deviceId);

    public abstract AltOpenSessionSignatureData build();
  }
}
