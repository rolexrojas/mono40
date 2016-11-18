package com.gbh.movil.ui.main.list;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;

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
  private final Map<Pair<Class<? extends Item>, Class<? extends ItemHolder>>,
    ItemHolderBinder<? extends Item, ? extends ItemHolder>> binders;

  /**
   * TODO
   *
   * @param binders
   *   TODO
   */
  private ItemHolderBinderFactory(@NonNull Map<
    Pair<Class<? extends Item>, Class<? extends ItemHolder>>,
    ItemHolderBinder<? extends Item, ? extends ItemHolder>> binders) {
    this.binders = binders;
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
    return binders.get(Pair.<Class<? extends Item>, Class<? extends ItemHolder>>create(itemType,
      holderType));
  }

  /**
   * TODO
   */
  public static class Builder {
    /**
     * TODO
     */
    private final Map<Pair<Class<? extends Item>, Class<? extends ItemHolder>>,
      ItemHolderBinder<? extends Item, ? extends ItemHolder>> binders;

    /**
     * TODO
     */
    public Builder() {
      binders = new HashMap<>();
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
      binders.put(Pair.<Class<? extends Item>, Class<? extends ItemHolder>>create(itemType,
        holderType), binder);
      return this;
    }

    /**
     * TODO
     *
     * @return TODO
     */
    @NonNull
    public final ItemHolderBinderFactory build() {
      return new ItemHolderBinderFactory(binders);
    }
  }
}
