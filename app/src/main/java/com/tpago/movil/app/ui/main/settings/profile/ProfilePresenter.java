package com.tpago.movil.app.ui.main.settings.profile;

import com.tpago.movil.dep.User;
import com.tpago.movil.app.ui.Presenter;
import com.tpago.movil.util.BuilderChecker;
import com.tpago.movil.util.ObjectHelper;

/**
 * @author hecvasro
 */
final class ProfilePresenter extends Presenter<ProfilePresentation> {

  static Builder builder() {
    return new Builder();
  }

  private final User user;

  private ProfilePresenter(Builder builder) {
    super(builder.presentation);

    this.user = builder.user;
  }

  @Override
  public void onPresentationResumed() {
    super.onPresentationResumed();

    this.presentation.setFirstNameTextInputContent(user.firstName());
    this.presentation.setLastNameTextInputContent(user.lastName());
    this.presentation.setPhoneNumberTextInputContent(
      user.phoneNumber()
        .formattedValued()
    );
    this.presentation.setEmailTextInputContent(
      user.email()
        .value()
    );
  }

  static final class Builder {

    private User user;
    private ProfilePresentation presentation;

    private Builder() {
    }

    final Builder user(User user) {
      this.user = ObjectHelper.checkNotNull(user, "user");
      return this;
    }

    final Builder presentation(ProfilePresentation presentation) {
      this.presentation = ObjectHelper.checkNotNull(presentation, "presentation");
      return this;
    }

    final ProfilePresenter build() {
      BuilderChecker.create()
        .addPropertyNameIfMissing("user", ObjectHelper.isNull(this.user))
        .addPropertyNameIfMissing("presentation", ObjectHelper.isNull(this.presentation))
        .checkNoMissingProperties();

      return new ProfilePresenter(this);
    }
  }
}
