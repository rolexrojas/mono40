package com.tpago.movil.session;

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
    // TODO: Evaluate if a copy of the file should be created.
  }

  @Override
  public void onRun() throws Throwable {
    // TODO
  }
}
