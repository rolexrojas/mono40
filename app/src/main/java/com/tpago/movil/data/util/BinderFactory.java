package com.tpago.movil.data.util;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tpago.movil.misc.Utils;

import java.util.HashMap;
import java.util.Map;

/**
 * TODO
 *
 * @author hecvasro
 */
public final class BinderFactory {
  /**
   * TODO
   */
  private final Map<Class<?>, Map<Class<? extends Holder>, Binder<?, ? extends Holder>>> binderMap;

  /**
   * TODO
   *
   * @param binderMap
   *   TODO
   */
  private BinderFactory(@NonNull Map<Class<?>, Map<Class<? extends Holder>, Binder<?, ? extends Holder>>> binderMap) {
    this.binderMap = binderMap;
  }

  /**
   * TODO
   *
   * @param holderType
   *   TODO
   * @param binderSubMap
   *   TODO
   *
   * @return TODO
   */
  @Nullable
  private static Binder<?, ? extends Holder> getBinder(@NonNull Class<? extends Holder> holderType, @NonNull Map<Class<? extends Holder>, Binder<?, ? extends Holder>> binderSubMap) {
    Binder<?, ? extends Holder> binder = null;
    if (binderSubMap.containsKey(holderType)) {
      binder = binderSubMap.get(holderType);
    } else {
      final Class<?>[] holderTypeInterfaces = holderType.getInterfaces();
      if (holderTypeInterfaces.length > 0) {
        for (Class<?> holderTypeInterface : holderTypeInterfaces) {
          if (Holder.class.isAssignableFrom(holderTypeInterface)) {
            binder = getBinder(holderTypeInterface.asSubclass(Holder.class), binderSubMap);
            if (Utils.isNotNull(binder)) {
              break;
            }
          }
        }
      }
      if (Utils.isNull(binder)) {
        final Class<?> holderSuperType = holderType.getSuperclass();
        if (Utils.isNotNull(holderSuperType) && Holder.class.isAssignableFrom(holderSuperType)) {
          binder = getBinder(holderSuperType.asSubclass(Holder.class), binderSubMap);
        }
      }
    }
    return binder;
  }

  /**
   * TODO
   *
   * @param itemType
   *   TODO
   * @param holderType
   *   TODO
   *
   * @return TODO
   */
  @Nullable
  public final Binder<?, ? extends Holder> getBinder(
    @NonNull Class<?> itemType, @NonNull Class<? extends Holder> holderType) {
    if (binderMap.containsKey(itemType)) {
      return getBinder(holderType, binderMap.get(itemType));
    } else {
      final Class<?> itemSuperType = itemType.getSuperclass();
      if (Utils.isNotNull(itemSuperType)) {
        return getBinder(itemSuperType, holderType);
      } else {
        return null;
      }
    }
  }

  /**
   * TODO
   */
  public static class Builder {
    /**
     * TODO
     */
    private final Map<Class<?>, Map<Class<? extends Holder>, Binder<?, ? extends Holder>>> binderMap;

    /**
     * TODO
     */
    public Builder() {
      binderMap = new HashMap<>();
    }

    /**
     * TODO
     *
     * @param itemType
     *   TODO
     * @param holderType
     *   TODO
     * @param binder
     *   TODO
     *
     * @return TODO
     */
    @NonNull
    public final Builder addBinder(@NonNull Class<?> itemType, @NonNull Class<? extends Holder> holderType, @NonNull Binder<?, ? extends Holder> binder) {
      final Map<Class<? extends Holder>, Binder<?, ? extends Holder>> map;
      if (binderMap.containsKey(itemType)) {
        map = binderMap.get(itemType);
      } else {
        map = new HashMap<>();
        binderMap.put(itemType, map);
      }
      map.put(holderType, binder);
      return this;
    }

    /**
     * TODO
     *
     * @return TODO
     */
    @NonNull
    public final BinderFactory build() {
      return new BinderFactory(binderMap);
    }
  }
}
