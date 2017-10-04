package com.tpago.movil.domain.auth.alt;

import com.google.auto.value.AutoValue;
import com.google.auto.value.extension.memoized.Memoized;
import com.tpago.movil.user.User;
import com.tpago.movil.util.ObjectHelper;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.security.SecureRandom;

import timber.log.Timber;

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
    final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

    DataOutputStream dataOutputStream = null;
    try {
      dataOutputStream = new DataOutputStream(byteArrayOutputStream);

      final User user = this.user();
      dataOutputStream.writeLong(user.id());
      dataOutputStream.writeUTF(
        user.phoneNumber()
          .value()
      );

      dataOutputStream.writeUTF(this.deviceId());

      dataOutputStream.writeLong(this.nonce());

      return byteArrayOutputStream.toByteArray();
    } catch (IOException exception) {
      throw new RuntimeException(exception);
    } finally {
      try {
        if (ObjectHelper.isNotNull(dataOutputStream)) {
          dataOutputStream.close();
        }
      } catch (IOException exception) {
        Timber.e(exception, "dataOutputStream.close()");
      }
      try {
        byteArrayOutputStream.close();
      } catch (IOException exception) {
        Timber.e(exception, "byteArrayOutputStream.close()");
      }
    }
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
