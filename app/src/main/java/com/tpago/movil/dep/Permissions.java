package com.tpago.movil.dep;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

/**
 * @author hecvasro
 */
@Deprecated
public final class Permissions {
  static boolean checkIfGranted(int result) {
    return result == PackageManager.PERMISSION_GRANTED;
  }

  public static boolean checkIfGranted(Context context, String permission) {
    return checkIfGranted(ContextCompat.checkSelfPermission(context, permission));
  }

  public static void requestPermissions(Fragment fragment, int requestCode, String... permissions) {
    fragment.requestPermissions(permissions, requestCode);
  }

  private Permissions() {
    throw new AssertionError("Cannot be instantiated");
  }
}
