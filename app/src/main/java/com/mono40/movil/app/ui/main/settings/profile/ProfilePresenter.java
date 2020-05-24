package com.mono40.movil.app.ui.main.settings.profile;

import android.net.Uri;

import com.mono40.movil.Name;
import com.mono40.movil.app.ui.Presenter;
import com.mono40.movil.app.ui.picture.PictureCreator;
import com.mono40.movil.session.SessionManager;
import com.mono40.movil.session.User;
import com.mono40.movil.util.BuilderChecker;
import com.mono40.movil.util.ObjectHelper;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * @author hecvasro
 */
final class ProfilePresenter extends Presenter<ProfilePresentation> {

  static Builder builder() {
    return new Builder();
  }

  private final SessionManager sessionManager;
  private final PictureCreator pictureCreator;

  private CompositeDisposable compositeDisposable;

  private ProfilePresenter(Builder builder) {
    super(builder.presentation);

    this.sessionManager = builder.sessionManager;
    this.pictureCreator = builder.pictureCreator;
  }

  final void onUserPictureClicked() {
    final User user = this.sessionManager.getUser();
    this.pictureCreator
      .create(ObjectHelper.isNotNull(user.picture()), this.sessionManager::updatePicture);
  }

  private void updateUserPicture(Uri picture) {
    this.presentation.setUserPicture(picture);
  }

  private void updateUserName(Name name) {
    this.presentation.setUserFirstName(name.first());
    this.presentation.setUserLastName(name.last());
  }

  @Override
  public void onPresentationResumed() {
    super.onPresentationResumed();

    final User user = this.sessionManager.getUser();

    Disposable disposable;
    this.compositeDisposable = new CompositeDisposable();

    this.updateUserPicture(user.picture());
    disposable = user.pictureChanges()
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(this::updateUserPicture);
    this.compositeDisposable.add(disposable);

    this.updateUserName(user.name());
    disposable = user.nameChanges()
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(this::updateUserName);
    this.compositeDisposable.add(disposable);

    this.presentation.setUserPhoneNumber(
      user.phoneNumber()
        .formattedValued()
    );
    this.presentation.setUserEmail(
      user.email()
        .value()
    );
  }

  @Override
  public void onPresentationPaused() {
    this.compositeDisposable.dispose();
    this.compositeDisposable = null;

    super.onPresentationPaused();
  }

  static final class Builder {

    private SessionManager sessionManager;
    private PictureCreator pictureCreator;
    private ProfilePresentation presentation;

    private Builder() {
    }

    final Builder sessionManager(SessionManager sessionManager) {
      this.sessionManager = ObjectHelper.checkNotNull(sessionManager, "sessionManager");
      return this;
    }

    final Builder pictureCreator(PictureCreator pictureCreator) {
      this.pictureCreator = ObjectHelper.checkNotNull(pictureCreator, "pictureCreator");
      return this;
    }

    final Builder presentation(ProfilePresentation presentation) {
      this.presentation = ObjectHelper.checkNotNull(presentation, "presentation");
      return this;
    }

    final ProfilePresenter build() {
      BuilderChecker.create()
        .addPropertyNameIfMissing("sessionManager", ObjectHelper.isNull(this.sessionManager))
        .addPropertyNameIfMissing("pictureCreator", ObjectHelper.isNull(this.pictureCreator))
        .addPropertyNameIfMissing("presentation", ObjectHelper.isNull(this.presentation))
        .checkNoMissingProperties();
      return new ProfilePresenter(this);
    }
  }
}
