package com.gbh.movil.ui.main.list;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO
 *
 * @author hecvasro
 */
public final class ItemViewCreatorFactory {
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
  private ItemViewCreatorFactory(@NonNull Map<Class<? extends Item>,
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
  public final int getIdentifier(@NonNull Class<? extends Item> type) {
    return identifiers.indexOf(type);
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
  public final ItemHolderCreator<? extends ItemHolder> getCreator(int identifier) {
    return creators.get(identifiers.get(identifier));
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
    public final ItemViewCreatorFactory build() {
      return new ItemViewCreatorFactory(creators);
    }
  }
}
