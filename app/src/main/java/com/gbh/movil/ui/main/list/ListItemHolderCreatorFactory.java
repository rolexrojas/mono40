package com.gbh.movil.ui.main.list;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.gbh.movil.misc.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO
 *
 * @author hecvasro
 */
public final class ListItemHolderCreatorFactory {
  /**
   * TODO
   */
  private final Map<Class<?>, ListItemHolderCreator<? extends ListItemHolder>> creators;
  /**
   * TODO
   */
  private final List<Class<?>> identifiers;

  /**
   * TODO
   *
   * @param creators
   *   TODO
   */
  private ListItemHolderCreatorFactory(@NonNull Map<Class<?>, ListItemHolderCreator<? extends ListItemHolder>> creators) {
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
  final int getIdentifier(@NonNull Class<?> type) {
    final int identifier = identifiers.indexOf(type);
    if (identifier >= 0) {
      return identifier;
    } else {
      final Class<?> superType = type.getSuperclass();
      if (Utils.isNotNull(superType)) {
        return getIdentifier(superType);
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
  final ListItemHolderCreator<? extends ListItemHolder> getCreator(int identifier) {
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
    private final Map<Class<?>, ListItemHolderCreator<? extends ListItemHolder>> creators;

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
    public final Builder addCreator(@NonNull Class<?> type, @NonNull ListItemHolderCreator<? extends ListItemHolder> creator) {
      creators.put(type, creator);
      return this;
    }

    /**
     * TODO
     *
     * @return TODO
     */
    @NonNull
    public final ListItemHolderCreatorFactory build() {
      return new ListItemHolderCreatorFactory(creators);
    }
  }
}
