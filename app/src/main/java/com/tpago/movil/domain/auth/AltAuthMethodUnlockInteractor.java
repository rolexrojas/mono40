package com.tpago.movil.domain.auth;

import android.util.Base64;

import com.tpago.movil.domain.user.User;
import com.tpago.movil.util.BuilderChecker;
import com.tpago.movil.util.ObjectHelper;
import com.tpago.movil.util.Placeholder;
import com.tpago.movil.util.Result;
import com.tpago.movil.util.StringHelper;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.Signature;

import io.reactivex.Single;

/**
 * @author hecvasro
 */
public final class AltAuthMethodUnlockInteractor implements UnlockInteractor {

  private final AltAuthMethodService altAuthMethodService;
  private final User user;
  private final String deviceId;

  private final PrivateKey privateKey;

  private AltAuthMethodUnlockInteractor(Creator creator, PrivateKey privateKey) {
    this.altAuthMethodService = creator.altAuthMethodService;
    this.user = creator.user;
    this.deviceId = creator.deviceId;

    this.privateKey = ObjectHelper.checkNotNull(privateKey, "privateKey");
  }

  private long createNonce() {
    return new SecureRandom()
      .nextLong();
  }

  private byte[] createSignatureData() throws IOException {
    final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    final DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
    dataOutputStream.writeLong(this.user.id());
    dataOutputStream.writeUTF(
      this.user.phoneNumber()
        .value()
    );
    dataOutputStream.writeUTF(this.deviceId);
    dataOutputStream.writeLong(this.createNonce());
    return byteArrayOutputStream.toByteArray();
  }

  private Single<String> createSignature() throws Exception {
    final Signature signature = Signature.getInstance("SHA1withRSA");
    signature.initSign(this.privateKey);
    signature.update(this.createSignatureData());
    return Single.just(Base64.encodeToString(signature.sign(), Base64.DEFAULT));
  }

  @Override
  public Single<Result<Placeholder>> unlock() {
    return Single.defer(this::createSignature)
      .flatMap(this.altAuthMethodService::verifySignature);
  }

  public static final class Creator {

    public static Builder builder() {
      return new Builder();
    }

    private final AltAuthMethodService altAuthMethodService;
    private final User user;
    private final String deviceId;

    private Creator(Builder builder) {
      this.altAuthMethodService = builder.altAuthMethodService;
      this.user = builder.user;
      this.deviceId = builder.deviceId;
    }

    public final AltAuthMethodUnlockInteractor create(PrivateKey privateKey) {
      return new AltAuthMethodUnlockInteractor(this, privateKey);
    }

    public static final class Builder {

      private AltAuthMethodService altAuthMethodService;
      private User user;
      private String deviceId;

      private Builder() {
      }

      public final Builder altAuthMethodService(AltAuthMethodService altAuthMethodService) {
        this.altAuthMethodService = ObjectHelper.checkNotNull(
          altAuthMethodService,
          "altAuthMethodService"
        );
        return this;
      }

      public final Builder user(User user) {
        this.user = ObjectHelper.checkNotNull(user, "user");
        return this;
      }

      public final Builder deviceId(String deviceId) {
        this.deviceId = ObjectHelper.checkNotNull(deviceId, "deviceId");
        return this;
      }

      public final Creator build() {
        BuilderChecker.create()
          .addPropertyNameIfMissing(
            "altAuthMethodService",
            ObjectHelper.isNull(this.altAuthMethodService)
          )
          .addPropertyNameIfMissing("user", ObjectHelper.isNull(this.user))
          .addPropertyNameIfMissing("deviceId", StringHelper.isNullOrEmpty(this.deviceId))
          .checkNoMissingProperties();
        return new Creator(this);
      }
    }
  }
}
