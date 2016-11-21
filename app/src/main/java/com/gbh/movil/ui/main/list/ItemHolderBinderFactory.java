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
  private final Map<
    Class<? extends Item>,
    Map<
      Class<? extends ItemHolder>,
      ItemHolderBinder<? extends Item, ? extends ItemHolder>>> binderMap;

  /**
   * TODO
   *
   * @param binderMap
   *   TODO
   */
  private ItemHolderBinderFactory(@NonNull Map<
    Class<? extends Item>,
    Map<
      Class<? extends ItemHolder>,
      ItemHolderBinder<? extends Item, ? extends ItemHolder>>> binderMap) {
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
  private static ItemHolderBinder<? extends Item, ? extends ItemHolder> getBinder(
    @NonNull Class<? extends ItemHolder> holderType,
    @NonNull Map<
      Class<? extends ItemHolder>,
      ItemHolderBinder<? extends Item, ? extends ItemHolder>> binderSubMap) {
    if (binderSubMap.containsKey(holderType)) {
      return binderSubMap.get(holderType);
    } else {
      final Class<?> holderSuperType = holderType.getSuperclass();
      if (Utils.isNotNull(holderSuperType) && ItemHolder.class.isAssignableFrom(holderSuperType)) {
        return getBinder(holderSuperType.asSubclass(ItemHolder.class), binderSubMap);
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
  final ItemHolderBinder<? extends Item, ? extends ItemHolder> getBinder(
    @NonNull Class<? extends Item> itemType, @NonNull Class<? extends ItemHolder> holderType) {
    if (binderMap.containsKey(itemType)) {
      return getBinder(holderType, binderMap.get(itemType));
    } else {
      final Class<?> itemSuperType = itemType.getSuperclass();
      if (Utils.isNotNull(itemSuperType) && Item.class.isAssignableFrom(itemSuperType)) {
        return getBinder(itemSuperType.asSubclass(Item.class), holderType);
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
    private final Map<
      Class<? extends Item>,
      Map<
        Class<? extends ItemHolder>,
        ItemHolderBinder<? extends Item, ? extends ItemHolder>>> binderMap;

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
    public final Builder addBinder(@NonNull Class<? extends Item> itemType,
      @NonNull Class<? extends ItemHolder> holderType,
      @NonNull ItemHolderBinder<? extends Item, ? extends ItemHolder> binder) {
      final Map<
        Class<? extends ItemHolder>,
        ItemHolderBinder<? extends Item, ? extends ItemHolder>> map;
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
    public final ItemHolderBinderFactory build() {
      return new ItemHolderBinderFactory(binderMap);
    }
  }
}
