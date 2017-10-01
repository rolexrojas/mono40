package com.tpago.movil.app;

import com.tpago.movil.util.ObjectHelper;

import java.util.Map;

/**
 * @author hecvasro
 */
public final class ComponentBuilderSupplier {

  public static ComponentBuilderSupplier create(Map<Class<?>, ComponentBuilder> map) {
    return new ComponentBuilderSupplier(map);
  }

  private final Map<Class<?>, ComponentBuilder> map;

  private ComponentBuilderSupplier(Map<Class<?>, ComponentBuilder> map) {
    this.map = ObjectHelper.checkNotNull(map, "map");
  }

  public final <T extends ComponentBuilder> T get(Class<?> keyType, Class<T> builderType) {
    ObjectHelper.checkNotNull(keyType, "keyType");
    ObjectHelper.checkNotNull(builderType, "builderType");

    return builderType.cast(this.map.get(keyType));
  }
}
