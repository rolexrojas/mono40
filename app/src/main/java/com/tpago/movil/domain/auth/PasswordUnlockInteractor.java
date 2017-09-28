package com.tpago.movil.domain.auth;

import com.tpago.movil.domain.Password;
import com.tpago.movil.domain.user.User;
import com.tpago.movil.util.BuilderChecker;
import com.tpago.movil.util.ObjectHelper;
import com.tpago.movil.util.Placeholder;
import com.tpago.movil.util.PlaceholderResultMapperFunction;
import com.tpago.movil.util.Result;
import com.tpago.movil.util.StringHelper;

import io.reactivex.Single;

/**
 * @author hecvasro
 */
public final class PasswordUnlockInteractor implements UnlockInteractor {

  private final AuthService authService;
  private final User user;
  private final String deviceId;

  private final Password password;

  private PasswordUnlockInteractor(Creator creator, Password password) {
    this.authService = creator.authService;
    this.user = creator.user;
    this.deviceId = creator.deviceId;

    this.password = ObjectHelper.checkNotNull(password, "password");
  }

  @Override
  public Single<Result<Placeholder>> unlock() {
    return this.authService.signIn(
      this.user.phoneNumber(),
      this.user.email(),
      this.password,
      false,
      this.deviceId
    )
      .map(PlaceholderResultMapperFunction.create());
  }

  public static final class Creator {

    public static Builder builder() {
      return new Builder();
    }

    private final AuthService authService;
    private final User user;
    private final String deviceId;

    private Creator(Builder builder) {
      this.authService = builder.authService;
      this.user = builder.user;
      this.deviceId = builder.deviceId;
    }

    public final PasswordUnlockInteractor create(Password password) {
      return new PasswordUnlockInteractor(this, password);
    }

    public static final class Builder {

      private AuthService authService;
      private User user;
      private String deviceId;

      private Builder() {
      }

      public final Builder authService(AuthService authService) {
        this.authService = ObjectHelper.checkNotNull(authService, "authService");
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
          .addPropertyNameIfMissing("authService", ObjectHelper.isNull(this.authService))
          .addPropertyNameIfMissing("user", ObjectHelper.isNull(this.user))
          .addPropertyNameIfMissing("deviceId", StringHelper.isNullOrEmpty(this.deviceId))
          .checkNoMissingProperties();
        return new Creator(this);
      }
    }
  }
}
