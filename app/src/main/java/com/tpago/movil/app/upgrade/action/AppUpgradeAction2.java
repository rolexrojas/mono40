package com.tpago.movil.app.upgrade.action;

import android.content.Context;

import com.tpago.movil.io.FileHelper;
import com.tpago.movil.util.ObjectHelper;

/**
 * @author hecvasro
 */
final class AppUpgradeAction2 extends AppUpgradeAction {

  static AppUpgradeAction2 create(Context context) {
    return new AppUpgradeAction2(context);
  }

  private final Context context;

  private AppUpgradeAction2(Context context) {
    this.context = ObjectHelper.checkNotNull(context, "context");
  }

  @Override
  public int id() {
    return 2;
  }

  @Override
  public void run() throws Exception {
    // Delete the external picture directory.
    FileHelper.deleteDir(FileHelper.createExtPicDir(this.context));

    // Deletes the internal picture directory.
    FileHelper.deleteDir(FileHelper.createIntPicDir(this.context));

    // Delete the internal cache directory.
    FileHelper.deleteDir(FileHelper.createIntCacheDir(this.context));
  }
}
