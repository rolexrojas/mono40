package com.tpago.movil.domain.auth.alt;

import com.google.auto.value.AutoValue;
import com.google.auto.value.extension.memoized.Memoized;
import com.tpago.movil.user.User;
import com.tpago.movil.util.StringHelper;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hecvasro
 */
@AutoValue
public abstract class AltAuthMethodVerifyData {

  public static Builder builder() {
    return new AutoValue_AltAuthMethodVerifyData.Builder();
  }

  AltAuthMethodVerifyData() {
  }

  public abstract User user();

  public abstract String deviceId();

  @Memoized
  public long nonce() {
    return new SecureRandom()
      .nextLong();
  }

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

    public abstract AltAuthMethodVerifyData build();
  }
}
