package com.gbh.movil.ui.main.list;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.gbh.movil.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO
 *
 * @author hecvasro
 */
public final class ItemHolderCreatorFactory {
  /**
   * TODO
   */
  private final Map<Class<? extends Item>,
    ItemHolderCreator<? extends ItemHolder>> creators;
  /**
   * TODO
   */
  private final List<Class<? extends Item>> identifiers;

  /**
   * TODO
   *
   * @param creators
   *   TODO
   */
  private ItemHolderCreatorFactory(@NonNull Map<Class<? extends Item>,
    ItemHolderCreator<? extends ItemHolder>> creators) {
    this.creators = creators;
    this.identifiers = new ArrayList<>();
    this.identifiers.addAll(this.creators.keySet());
  }

  /**
   * TODO
   *
   * @param type
   *   TODO
   *
   * @return TODO
   */
  final int getIdentifier(@NonNull Class<? extends Item> type) {
    final int identifier = identifiers.indexOf(type);
    if (identifier >= 0) {
      return identifier;
    } else {
      final Class<?> superType = type.getSuperclass();
      if (Utils.isNotNull(superType) && Item.class.isAssignableFrom(superType)) {
        return getIdentifier(superType.asSubclass(Item.class));
      } else {
        return -1;
      }
    }
  }

  /**
   * TODO
   *
   * @param identifier
   *   TODO
   *
   * @return TODO
   */
  @Nullable
  final ItemHolderCreator<? extends ItemHolder> getCreator(int identifier) {
    if (identifier >= 0 && identifier < identifiers.size()) {
      return creators.get(identifiers.get(identifier));
    } else {
      return null;
    }
  }

  /**
   * TODO
   */
  public static class Builder {
    /**
     * TODO
     */
    private final Map<Class<? extends Item>,
      ItemHolderCreator<? extends ItemHolder>> creators;

    /**
     * TODO
     */
    public Builder() {
      creators = new HashMap<>();
    }

    /**
     * TODO
     *
     * @param type
     *   TODO
     * @param creator
     *   TODO
     *
     * @return TODO
     */
    @NonNull
    public final Builder addCreator(@NonNull Class<? extends Item> type,
      @NonNull ItemHolderCreator<? extends ItemHolder> creator) {
      creators.put(type, creator);
      return this;
    }

    /**
     * TODO
     *
     * @return TODO
     */
    @NonNull
    public final ItemHolderCreatorFactory build() {
      return new ItemHolderCreatorFactory(creators);
    }
  }
}
