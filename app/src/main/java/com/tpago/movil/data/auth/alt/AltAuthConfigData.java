package com.tpago.movil.data.auth.alt;

import com.google.auto.value.AutoValue;
import com.google.auto.value.extension.memoized.Memoized;

import java.math.BigInteger;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Date;

import javax.security.auth.x500.X500Principal;

/**
 * @author hecvasro
 */
@AutoValue
abstract class AltAuthConfigData {

  static Builder builder() {
    return new AutoValue_AltAuthConfigData.Builder();
  }

  AltAuthConfigData() {
  }

  abstract String providerName();

  abstract String keyAlias();

  abstract String keyGenAlgName();

  abstract AlgorithmParameterSpec keyGenAlgParamSpec();

  abstract String signAlgName();

  abstract String methodKey();

  abstract String codeMethodKey();

  abstract X500Principal codeMethodSubject();

  abstract BigInteger codeMethodSerialNumber();

  abstract Date codeMethodStartDate();

  abstract Date codeMethodEndDate();

  @Memoized
  @Override
  public abstract String toString();

  @Memoized
  @Override
  public abstract int hashCode();

  @AutoValue.Builder
  static abstract class Builder {

    Builder() {
    }

    abstract Builder providerName(String name);

    abstract Builder keyAlias(String alias);

    abstract Builder keyGenAlgName(String name);

    abstract Builder keyGenAlgParamSpec(AlgorithmParameterSpec paramSpec);

    abstract Builder signAlgName(String name);

    abstract Builder methodKey(String key);

    abstract Builder codeMethodKey(String key);

    abstract Builder codeMethodSubject(X500Principal subject);

    abstract Builder codeMethodSerialNumber(BigInteger serialNumber);

    abstract Builder codeMethodStartDate(Date date);

    abstract Builder codeMethodEndDate(Date date);

    abstract AltAuthConfigData build();
  }
}
