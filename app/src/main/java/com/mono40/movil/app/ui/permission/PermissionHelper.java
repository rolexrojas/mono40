package com.mono40.movil.app.ui.permission;

import android.content.Context;
import android.content.pm.PackageManager;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;

import com.mono40.movil.util.ObjectHelper;
import com.mono40.movil.util.StringHelper;

import java.util.List;

/**
 * @author hecvasro
 */
public final class PermissionHelper {

  static boolean isGranted(int result) {
    return result == PackageManager.PERMISSION_GRANTED;
  }

  public static boolean isGranted(Context context, String permission) {
    ObjectHelper.checkNotNull(context, "context");
    StringHelper.checkIsNotNullNorEmpty(permission, "permission");

    return isGranted(ContextCompat.checkSelfPermission(context, permission));
  }

  public static boolean areGranted(Context context, List<String> permissions) {
    ObjectHelper.checkNotNull(context, "context");
    ObjectHelper.checkNotNull(permissions, "permissions");

    if (permissions.isEmpty()) {
      throw new IllegalArgumentException("permissions.isEmpty()");
    }

    for (String permission : permissions) {
      if (!isGranted(ContextCompat.checkSelfPermission(context, permission))) {
        return false;
      }
    }
    return true;
  }

  public static void requestPermissions(
    Fragment fragment,
    int requestCode,
    List<String> permissions
  ) {
    ObjectHelper.checkNotNull(fragment, "fragment");
    ObjectHelper.checkNotNull(permissions, "permissions");

    if (permissions.isEmpty()) {
      throw new IllegalArgumentException("permissions.isEmpty()");
    }

    fragment.requestPermissions(permissions.toArray(new String[permissions.size()]), requestCode);
  }

  private PermissionHelper() {
  }
}
