package com.mono40.movil.session;

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
public abstract class UnlockMethodConfigData {

  static Builder builder() {
    return new AutoValue_UnlockMethodConfigData.Builder();
  }

  UnlockMethodConfigData() {
  }

  public abstract String providerName();

  public abstract String keyAlias();

  public abstract String keyGenAlgName();

  public abstract AlgorithmParameterSpec keyGenAlgParamSpec();

  public abstract String signAlgName();

  public abstract X500Principal codeMethodSubject();

  public abstract BigInteger codeMethodSerialNumber();

  public abstract Date codeMethodStartDate();

  public abstract Date codeMethodEndDate();

  public abstract String codeCipherTransformation();

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

    abstract Builder codeMethodSubject(X500Principal subject);

    abstract Builder codeMethodSerialNumber(BigInteger serialNumber);

    abstract Builder codeMethodStartDate(Date date);

    abstract Builder codeMethodEndDate(Date date);

    abstract Builder codeCipherTransformation(String codeCipherTransformation);

    abstract UnlockMethodConfigData build();
  }
}
