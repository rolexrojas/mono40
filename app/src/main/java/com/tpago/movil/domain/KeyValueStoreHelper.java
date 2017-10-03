package com.tpago.movil.domain;

import com.tpago.movil.util.ObjectHelper;
import com.tpago.movil.util.StringHelper;

import java.util.Arrays;

/**
 * @author hecvasro
 */
public final class KeyValueStoreHelper {

  public static String createKey(Class<?> containerType, String name) {
    ObjectHelper.checkNotNull(containerType, "containerType");
    ObjectHelper.checkNotNull(name, "name");

    return StringHelper.join(".", Arrays.asList(containerType.getCanonicalName(), name));
  }

  private KeyValueStoreHelper() {
  }
}
