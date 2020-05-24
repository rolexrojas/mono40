package com.mono40.movil.app.ui.permission;

import com.google.auto.value.AutoValue;
import com.google.auto.value.extension.memoized.Memoized;
import com.mono40.movil.util.ObjectHelper;

import java.util.HashMap;
import java.util.Map;

/**
 * @author hecvasro
 */
@AutoValue
public abstract class PermissionRequestResult {

  public static PermissionRequestResult create(String[] permissions, int[] results) {
    ObjectHelper.checkNotNull(permissions, "permissions");
    ObjectHelper.checkNotNull(results, "results");

    if (permissions.length != results.length) {
      throw new IllegalArgumentException("permissions.length != results.length");
    }

    final Map<String, Boolean> map = new HashMap<>();
    for (int i = 0; i < permissions.length; i++) {
      map.put(permissions[i], PermissionHelper.isGranted(results[i]));
    }
    return new AutoValue_PermissionRequestResult(map);
  }

  abstract Map<String, Boolean> map();

  @Memoized
  public boolean isSuccessful() {
    final Map<String, Boolean> map = map();
    for (String permission : map().keySet()) {
      if (!map.get(permission)) {
        return false;
      }
    }
    return true;
  }
}
