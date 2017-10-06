package com.tpago.movil.domain.auth.alt;

import android.support.annotation.Nullable;

import com.tpago.movil.api.Api;
import com.tpago.movil.KeyValueStore;
import com.tpago.movil.KeyValueStoreHelper;
import com.tpago.movil.util.BuilderChecker;
import com.tpago.movil.util.ObjectHelper;
import com.tpago.movil.util.Placeholder;
import com.tpago.movil.util.Result;
import com.tpago.movil.util.StringHelper;

import java.security.PrivateKey;
import java.security.Signature;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.functions.Action;

/**
 * @author hecvasro
 */
public final class AltAuthMethodManager {

  private static final String KEY = KeyValueStoreHelper
    .createKey(AltAuthMethodManager.class, "Method");

  public static Builder builder() {
    return new Builder();
  }

  private final KeyValueStore keyValueStore;
  private final Api api;

  private final String signAlgName;
  private final List<Action> rollbackActionList;

  private AltAuthMethodManager(Builder builder) {
    this.keyValueStore = builder.keyValueStore;
    this.api = builder.api;

    this.signAlgName = builder.signAlgName;

    this.rollbackActionList = builder.rollbackActionList;
    this.rollbackActionList.add(() -> this.keyValueStore.remove(KEY));
  }

  private void checkEnabled() {
    if (!this.isEnabled()) {
      throw new IllegalStateException("!this.isEnabled()");
    }
  }

  private void executeRollback() throws Exception {
    for (Action rollbackAction : this.rollbackActionList) {
      rollbackAction.run();
    }
  }

  /**
   * Checks whether an {@link AltAuthMethod alternative authentication method} is enabled or not.
   *
   * @return True if an {@link AltAuthMethod alternative authentication method} is enabled, or
   * otherwise false.
   */
  public final boolean isEnabled() {
    return this.keyValueStore.isSet(KEY);
  }

  /**
   * @throws IllegalStateException
   *   If it is {@link #isEnabled() enabled}.
   */
  public final Completable enable(AltAuthMethodKeyGenerator generator) {
    ObjectHelper.checkNotNull(generator, "generator");

    final AltAuthMethod method = generator.method();
    final AltAuthMethod currentMethod = this.getActiveMethod();
    if (method.equals(currentMethod)) {
      return Completable.complete();
    } else {
      final Completable completable = ObjectHelper.checkNotNull(generator, "generator")
        .generate()
        .flatMapCompletable(this.api::enableAltAuthMethod)
        .doOnComplete(() -> this.keyValueStore.set(KEY, method.name()))
        .doOnError((throwable) -> this.executeRollback());

      if (ObjectHelper.isNull(currentMethod)) {
        return completable;
      } else {
        return this.disable()
          .concatWith(completable);
      }
    }
  }


  @Nullable
  public final AltAuthMethod getActiveMethod() {
    if (this.keyValueStore.isSet(KEY)) {
      return AltAuthMethod.valueOf(this.keyValueStore.get(KEY));
    } else {
      return null;
    }
  }

  private byte[] createSignedData(Signature signature, byte[] data) throws Exception {
    signature.update(data);
    return signature.sign();
  }

  private byte[] createSignedData(PrivateKey key, byte[] data) throws Exception {
    final Signature signature = Signature.getInstance(this.signAlgName);
    signature.initSign(key);
    return createSignedData(signature, data);
  }

  public final Single<Result<Placeholder>> verify(
    AltAuthMethodVerifyData data,
    Signature signature
  ) {
    ObjectHelper.checkNotNull(data, "data");
    ObjectHelper.checkNotNull(signature, "signature");

    this.checkEnabled();

    return Single.defer(() -> Single.just(this.createSignedData(signature, data.toByteArray())))
      .flatMap((signedData) -> this.api.verifyAltAuthMethod(data, signedData));
  }

  /**
   * @throws IllegalStateException
   *   If it isn't {@link #isEnabled() enabled}.
   */
  public final Single<Result<Placeholder>> verify(AltAuthMethodVerifyData data, PrivateKey key) {
    ObjectHelper.checkNotNull(data, "data");
    ObjectHelper.checkNotNull(key, "key");

    this.checkEnabled();

    return Single.defer(() -> Single.just(this.createSignedData(key, data.toByteArray())))
      .flatMap((signedData) -> this.api.verifyAltAuthMethod(data, signedData));
  }

  public final Completable disable() {
    this.checkEnabled();

    return this.api.disableAltAuthMethod()
      .doOnComplete(this::executeRollback);
  }

  public static final class Builder {

    private KeyValueStore keyValueStore;
    private Api api;

    private String signAlgName;
    private List<Action> rollbackActionList = new ArrayList<>();

    private Builder() {
    }

    public final Builder api(Api api) {
      this.api = ObjectHelper.checkNotNull(api, "api");
      return this;
    }

    public final Builder keyValueStore(KeyValueStore keyValueStore) {
      this.keyValueStore = ObjectHelper.checkNotNull(keyValueStore, "keyValueStore");
      return this;
    }

    public final Builder signAlgName(String sigAlgName) {
      this.signAlgName = ObjectHelper.checkNotNull(sigAlgName, "signAlgName");
      return this;
    }

    public final Builder addRollbackAction(Action rollbackAction) {
      this.rollbackActionList.add(ObjectHelper.checkNotNull(rollbackAction, "rollbackAction"));
      return this;
    }

    public final AltAuthMethodManager build() {
      BuilderChecker.create()
        .addPropertyNameIfMissing("keyValueStore", ObjectHelper.isNull(this.keyValueStore))
        .addPropertyNameIfMissing("api", ObjectHelper.isNull(this.api))
        .addPropertyNameIfMissing("signAlgName", StringHelper.isNullOrEmpty(this.signAlgName))
        .checkNoMissingProperties();

      return new AltAuthMethodManager(this);
    }
  }
}
