package com.tpago.movil.app.ui.main.settings.profile;

import com.tpago.movil.dep.User;
import com.tpago.movil.dep.UserStore;
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

  private final UserStore userStore;

  private ProfilePresenter(Builder builder) {
    super(builder.presentation);

    this.userStore = builder.userStore;
  }

  @Override
  public void onPresentationResumed() {
    super.onPresentationResumed();

    final User user = this.userStore.get();
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

    private UserStore userStore;
    private ProfilePresentation presentation;

    private Builder() {
    }

    final Builder userStore(UserStore userStore) {
      this.userStore = ObjectHelper.checkNotNull(userStore, "userStore");
      return this;
    }

    final Builder presentation(ProfilePresentation presentation) {
      this.presentation = ObjectHelper.checkNotNull(presentation, "presentation");
      return this;
    }

    final ProfilePresenter build() {
      BuilderChecker.create()
        .addPropertyNameIfMissing("userStore", ObjectHelper.isNull(this.userStore))
        .addPropertyNameIfMissing("presentation", ObjectHelper.isNull(this.presentation))
        .checkNoMissingProperties();

      return new ProfilePresenter(this);
    }
  }
}
