package com.tpago.movil.domain.auth.alt;

import com.google.auto.value.AutoValue;
import com.google.auto.value.extension.memoized.Memoized;
import com.tpago.movil.domain.user.User;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.security.SecureRandom;

/**
 * @author hecvasro
 */
@AutoValue
public abstract class AltAuthSignData {

  public static Builder builder() {
    return new AutoValue_AltAuthSignData.Builder();
  }

  AltAuthSignData() {
  }

  abstract User user();

  abstract String deviceId();

  @Memoized
  public byte[] toByteArray() throws Exception {
    final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    final DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
    final User user = this.user();
    dataOutputStream.writeLong(user.id());
    dataOutputStream.writeUTF(
      user.phoneNumber()
        .value()
    );
    dataOutputStream.writeUTF(this.deviceId());
    dataOutputStream.writeLong(
      new SecureRandom()
        .nextLong()
    );
    return byteArrayOutputStream.toByteArray();
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

    public abstract AltAuthSignData build();
  }
}
