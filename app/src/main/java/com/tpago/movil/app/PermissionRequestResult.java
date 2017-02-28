package com.tpago.movil.app;

import com.google.auto.value.AutoValue;
import com.tpago.movil.util.Preconditions;

import java.util.HashMap;
import java.util.Map;

/**
 * @author hecvasro
 */
@AutoValue
abstract class PermissionRequestResult {
  public static PermissionRequestResult create(String[] permissions, int[] results) {
    Preconditions.checkNotNull(permissions, "permissions == null");
    Preconditions.checkNotNull(results, "results == null");
    if (permissions.length != results.length) {
      throw new IllegalArgumentException("permissions.length != results.length");
    }
    final Map<String, Boolean> map = new HashMap<>();
    for (int i = 0; i < permissions.length; i++) {
      map.put(permissions[i], Permissions.checkIfGranted(results[i]));
    }
    return new AutoValue_PermissionRequestResult(map);
  }

  abstract Map<String, Boolean> getMap();

  public final boolean isSuccessful() {
    final Map<String, Boolean> map = getMap();
    for (String permission : getMap().keySet()) {
      if (!map.get(permission)) {
        return false;
      }
    }
    return true;
  }
}
