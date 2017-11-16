package com.tpago.movil.d.misc;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * TODO
 *
 * @author hecvasro
 */
@Deprecated
public class FactoryFactory<A, B, C> {
  /**
   * TODO
   */
  private final Map<Class<A>, Map<Class<B>, C>> map;

  /**
   * TODO
   *
   * @param map
   *   TODO
   */
  private FactoryFactory(@NonNull Map<Class<A>, Map<Class<B>, C>> map) {
    this.map = map;
  }

  /**
   * TODO
   *
   * @param type
   *   TODO
   * @param map
   *   TODO
   *
   * @return TODO
   */
  @Nullable
  private static <B, C> C get(@NonNull Class<B> type, @NonNull Map<Class<B>, C> map) {
    return map.containsKey(type) ? map.get(type) : null;
  }

  /**
   * TODO
   *
   * @param aType
   *   TODO
   * @param bType
   *   TODO
   *
   * @return TODO
   */
  @Nullable
  public final C get(@NonNull Class<A> aType, @NonNull Class<B> bType) {
    return map.containsKey(aType) ? get(bType, map.get(aType)) : null;
  }

  /**
   * TODO
   */
  public static class Builder<A, B, C> {
    /**
     * TODO
     */
    private final Map<Class<A>, Map<Class<B>, C>> map;

    /**
     * TODO
     */
    public Builder() {
      map = new HashMap<>();
    }

    /**
     * TODO
     *
     * @param aType
     *   TODO
     * @param bType
     *   TODO
     * @param c
     *   TODO
     *
     * @return TODO
     */
    @NonNull
    public final Builder add(@NonNull Class<A> aType, @NonNull Class<B> bType, @NonNull C c) {
      final Map<Class<B>, C> subMap;
      if (map.containsKey(aType)) {
        subMap = map.get(aType);
      } else {
        subMap = new HashMap<>();
        map.put(aType, subMap);
      }
      subMap.put(bType, c);
      return this;
    }

    /**
     * TODO
     *
     * @return TODO
     */
    @NonNull
    public final FactoryFactory<A, B, C> build() {
      return new FactoryFactory<>(map);
    }
  }
}
