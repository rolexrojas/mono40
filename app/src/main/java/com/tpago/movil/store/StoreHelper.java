package com.tpago.movil.store;

import com.tpago.movil.util.ObjectHelper;
import com.tpago.movil.util.StringHelper;

import java.util.Arrays;

/**
 * @author hecvasro
 */
@Deprecated
public final class StoreHelper {

  @Deprecated
  public static String createKey(Class<?> containerType, String name) {
    ObjectHelper.checkNotNull(containerType, "containerType");
    ObjectHelper.checkNotNull(name, "updateName");

    return StringHelper.join(".", Arrays.asList(containerType.getCanonicalName(), name));
  }

  private StoreHelper() {
  }
}
