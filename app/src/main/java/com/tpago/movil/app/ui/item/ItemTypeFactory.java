package com.tpago.movil.app.ui.item;

import com.tpago.movil.util.BuilderChecker;
import com.tpago.movil.util.ObjectHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Single;

/**
 * @author hecvasro
 */
public final class ItemTypeFactory {

  public static Builder builder() {
    return new Builder();
  }

  private final Map<Class<? extends Item>, Class> types;
  private final List<Class> actualTypes;

  private ItemTypeFactory(Builder builder) {
    this.types = builder.types;
    this.actualTypes = new ArrayList<>(this.types.values());
  }

  final <I extends Item> boolean contains(I item) {
    return Single.just(ObjectHelper.checkNotNull(item, "item"))
      .map(Item::getClass)
      .map(this.actualTypes::contains)
      .blockingGet();
  }

  final <I extends Item> int indexOf(Class<I> type) {
    return this.actualTypes.indexOf(type);
  }

  final Class get(int index) {
    return this.actualTypes.get(index);
  }

  final <I extends Item> Class get(Class<I> type) {
    if (!this.types.containsKey(type)) {
      throw new IllegalArgumentException("!this.types.containsKey(type)");
    }
    return this.types.get(type);
  }

  public static final class Builder {

    private final Map<Class<? extends Item>, Class> types = new HashMap<>();

    private Builder() {
    }

    public final <I extends Item> Builder type(Class<I> type, Class actualType) {
      ObjectHelper.checkNotNull(type, "type");
      ObjectHelper.checkNotNull(actualType, "actualType");
      this.types.put(type, actualType);
      return this;
    }

    public final ItemTypeFactory build() {
      BuilderChecker.create()
        .addPropertyNameIfMissing("types", this.types.isEmpty())
        .checkNoMissingProperties();
      return new ItemTypeFactory(this);
    }
  }
}
