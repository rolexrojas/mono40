package com.tpago.movil.domain.auth.alt;

import com.tpago.movil.domain.KeyValueStore;
import com.tpago.movil.util.BuilderChecker;
import com.tpago.movil.util.ObjectHelper;
import com.tpago.movil.util.Placeholder;
import com.tpago.movil.util.Result;
import com.tpago.movil.util.StringHelper;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.functions.Action;

/**
 * @author hecvasro
 */
public final class AltAuthManager {

  public static Builder builder() {
    return new Builder();
  }

  private final KeyValueStore store;
  private final AltAuthService service;
  private final String methodKey;
  private final String signAlgName;
  private final List<Action> onDisabledActionList;

  private AltAuthManager(Builder builder) {
    this.store = builder.store;
    this.service = builder.service;
    this.methodKey = builder.methodKey;
    this.signAlgName = builder.signAlgName;
    this.onDisabledActionList = builder.onDisabledActionList;
  }

  /**
   * Checks whether an {@link AltAuthMethod alternative authentication method} is enabled or not.
   *
   * @return True if an {@link AltAuthMethod alternative authentication method} is enabled, or
   * otherwise false.
   */
  public final boolean isEnabled() {
    return this.store.isSet(this.methodKey);
  }

  private void checkEnabled() {
    if (!this.isEnabled()) {
      throw new IllegalStateException("!this.isEnabled()");
    }
  }

  /**
   * @throws IllegalStateException
   *   If it is {@link #isEnabled() enabled}.
   */
  public final Completable enable(AltAuthMethod method, PublicKey publicKey) {
    ObjectHelper.checkNotNull(method, "method");
    ObjectHelper.checkNotNull(publicKey, "publicKey");

    this.checkEnabled();

    return this.service.enable(publicKey)
      .doOnComplete(() -> this.store.set(this.methodKey, method.name()));
  }

  /**
   * @throws IllegalStateException
   *   If it isn't {@link #isEnabled() enabled}.
   */
  public final AltAuthMethod getEnabledMethod() {
    this.checkEnabled();

    return AltAuthMethod.valueOf(this.store.get(this.methodKey));
  }

  private byte[] createSignature(PrivateKey privateKey, AltAuthSignData data) throws Exception {
    final Signature signature = Signature.getInstance(this.signAlgName);
    signature.initSign(privateKey);
    signature.update(data.toByteArray());
    return signature.sign();
  }

  /**
   * @throws IllegalStateException
   *   If it isn't {@link #isEnabled() enabled}.
   */
  public final Single<Result<Placeholder>> verify(PrivateKey privateKey, AltAuthSignData data) {
    ObjectHelper.checkNotNull(privateKey, "privateKey");
    ObjectHelper.checkNotNull(data, "data");

    this.checkEnabled();

    return Single.defer(() -> Single.just(this.createSignature(privateKey, data)))
      .flatMap(this.service::verify);
  }

  public final Completable disable() {
    this.checkEnabled();

    Completable completable = this.service.disable()
      .doOnComplete(() -> this.store.remove(this.methodKey));

    for (Action action : this.onDisabledActionList) {
      completable = completable.doOnComplete(action);
    }

    return completable;
  }

  public static final class Builder {

    private KeyValueStore store;
    private AltAuthService service;
    private String methodKey;
    private String signAlgName;
    private List<Action> onDisabledActionList;

    private Builder() {
    }

    public final Builder store(KeyValueStore store) {
      this.store = ObjectHelper.checkNotNull(store, "store");
      return this;
    }

    public final Builder service(AltAuthService service) {
      this.service = ObjectHelper.checkNotNull(service, "service");
      return this;
    }

    public final Builder methodKey(String methodKey) {
      this.methodKey = ObjectHelper.checkNotNull(methodKey, "methodKey");
      return this;
    }

    public final Builder signAlgName(String sigAlgName) {
      this.signAlgName = ObjectHelper.checkNotNull(sigAlgName, "signAlgName");
      return this;
    }

    public final Builder addOnDisabledAction(Action onDisabledAction) {
      this.onDisabledActionList.add(
        ObjectHelper.checkNotNull(
          onDisabledAction,
          "onDisabledAction"
        )
      );
      return this;
    }

    public final AltAuthManager build() {
      BuilderChecker.create()
        .addPropertyNameIfMissing("store", ObjectHelper.isNull(this.store))
        .addPropertyNameIfMissing("service", ObjectHelper.isNull(this.service))
        .addPropertyNameIfMissing("methodKey", StringHelper.isNullOrEmpty(this.methodKey))
        .addPropertyNameIfMissing("signAlgName", StringHelper.isNullOrEmpty(this.signAlgName))
        .checkNoMissingProperties();

      return new AltAuthManager(this);
    }
  }
}
