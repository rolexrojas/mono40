package com.tpago.movil.session;

import android.net.Uri;

//import com.tpago.movil.io.FileHelper;
import com.tpago.movil.util.ObjectHelper;

import java.io.File;

/**
 * @author hecvasro
 */
public final class UpdateUserPictureJob extends SessionJob {

  public static final String TYPE = "UpdateUserPictureJob";

  static UpdateUserPictureJob create(File picture) {
    return new UpdateUserPictureJob(picture);
  }

  final File picture;

  private UpdateUserPictureJob(File picture) {
    super(TYPE);

    this.picture = ObjectHelper.checkNotNull(picture, "picture");
  }

  @Override
  public void onAdded() {
    this.sessionManager.getUser()
      .updatePicture(Uri.fromFile(this.picture));
  }

  @Override
  public void onRun() throws Throwable {
//    final User user = this.sessionManager.getUser();
//    final Uri newPicture = this.api.updateUserPicture(user, this.picture)
//      .blockingGet();
//    user.updatePicture(newPicture);
//    FileHelper.deleteFile(this.picture);
  }
}
