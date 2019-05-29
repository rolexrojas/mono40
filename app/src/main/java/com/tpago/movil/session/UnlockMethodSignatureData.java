package com.tpago.movil.session;

import com.google.auto.value.AutoValue;
import com.google.auto.value.extension.memoized.Memoized;
import com.tpago.movil.util.ObjectHelper;
import com.tpago.movil.util.StringHelper;

import java.nio.charset.StandardCharsets;
import java.security.Signature;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hecvasro
 */
@AutoValue
public abstract class UnlockMethodSignatureData {

  public static Builder builder() {
    return new AutoValue_UnlockMethodSignatureData.Builder();
  }

  UnlockMethodSignatureData() {
  }

  public abstract User user();

  public abstract String deviceId();

  @Memoized
  byte[] toByteArray() throws Exception {
    final List<Object> tokenList = new ArrayList<>();

    final User user = this.user();
    tokenList.add(user.id());
    tokenList.add(
      user.phoneNumber()
        .value()
    );

    tokenList.add(this.deviceId());

    return StringHelper.join(":", tokenList)
      .getBytes(StandardCharsets.UTF_8);
  }

  public final byte[] sign(Signature signature) throws Exception {
    ObjectHelper.checkNotNull(signature, "signature");

    signature.update(this.toByteArray());
    return signature.sign();
  }

  public final boolean verify(Signature signature, byte[] signedData) throws Exception {
    ObjectHelper.checkNotNull(signature, "signature");
    ObjectHelper.checkNotNull(signedData, "signedData");

    signature.update(this.toByteArray());
    return signature.verify(signedData);
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

    public abstract UnlockMethodSignatureData build();
  }
}
