package com.tpago.movil.user;

import com.tpago.movil.util.ObjectHelper;

import java.io.File;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * @author hecvasro
 */
public final class UpdatePictureUserManagerJob extends UserManagerJob {

  static UpdatePictureUserManagerJob create(File pictureFile) {
    return new UpdatePictureUserManagerJob(pictureFile);
  }

  private final File picture;

  @Inject UserManager userManager;

  private UpdatePictureUserManagerJob(File picture) {
    super("UpdatePictureUserManagerJob");

    this.picture = ObjectHelper.checkNotNull(picture, "picture");
  }

  @Override
  public void onAdded() {
    // TODO: Evaluate if the file should be copied in order to guarantee existence.

//    final Uri pictureUri = null;
//    this.userManager.updatePicture(pictureUri);
  }

  @Override
  public void onRun() throws Throwable {
    Timber.d("UpdatePictureUserManagerJob started");

//    final Uri pictureUri = null;
//    this.userManager.updatePicture(pictureUri);

    Timber.d("UpdatePictureUserManagerJob started");
  }
}
