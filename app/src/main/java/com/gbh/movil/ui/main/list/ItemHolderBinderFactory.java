package com.gbh.movil.ui.main.list;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.gbh.movil.Utils;

import java.util.HashMap;
import java.util.Map;

/**
 * TODO
 *
 * @author hecvasro
 */
public final class ItemHolderBinderFactory {
  /**
   * TODO
   */
  private final Map<Class<?>, Map<Class<? extends Holder>, HolderBinder<?, ? extends Holder>>> binderMap;

  /**
   * TODO
   *
   * @param binderMap
   *   TODO
   */
  private ItemHolderBinderFactory(@NonNull Map<Class<?>, Map<Class<? extends Holder>, HolderBinder<?, ? extends Holder>>> binderMap) {
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
  private static HolderBinder<?, ? extends Holder> getBinder(@NonNull Class<? extends Holder> holderType, @NonNull Map<Class<? extends Holder>, HolderBinder<?, ? extends Holder>> binderSubMap) {
    if (binderSubMap.containsKey(holderType)) {
      return binderSubMap.get(holderType);
    } else {
      final Class<?> holderSuperType = holderType.getSuperclass();
      if (Utils.isNotNull(holderSuperType) && Holder.class.isAssignableFrom(holderSuperType)) {
        return getBinder(holderSuperType.asSubclass(Holder.class), binderSubMap);
      } else {
        return null;
      }
    }
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
  final HolderBinder<?, ? extends Holder> getBinder(
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
    private final Map<Class<?>, Map<Class<? extends Holder>, HolderBinder<?, ? extends Holder>>> binderMap;

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
     * @param holderBinder
     *   TODO
     *
     * @return TODO
     */
    @NonNull
    public final Builder addBinder(@NonNull Class<?> itemType, @NonNull Class<? extends Holder> holderType, @NonNull HolderBinder<?, ? extends Holder> holderBinder) {
      final Map<Class<? extends Holder>, HolderBinder<?, ? extends Holder>> map;
      if (binderMap.containsKey(itemType)) {
        map = binderMap.get(itemType);
      } else {
        map = new HashMap<>();
        binderMap.put(itemType, map);
      }
      map.put(holderType, holderBinder);
      return this;
    }

    /**
     * TODO
     *
     * @return TODO
     */
    @NonNull
    public final ItemHolderBinderFactory build() {
      return new ItemHolderBinderFactory(binderMap);
    }
  }
}
