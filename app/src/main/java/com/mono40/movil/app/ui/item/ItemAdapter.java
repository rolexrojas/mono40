package com.mono40.movil.app.ui.item;

import androidx.recyclerview.widget.RecyclerView;
import android.view.ViewGroup;

import com.mono40.movil.util.BuilderChecker;
import com.mono40.movil.util.ObjectHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Single;

/**
 * @author hecvasro
 */
public final class ItemAdapter extends RecyclerView.Adapter<ItemHolder> {

  public static Builder builder(ItemTypeFactory typeFactory) {
    return new Builder(typeFactory);
  }

  private final ItemTypeFactory typeFactory;

  private final Map<Class, ItemHolderCreator> creators;
  private final Map<Class, ItemHolderBinder> binders;

  private final List<Item> items = new ArrayList<>();

  private ItemAdapter(Builder builder) {
    this.typeFactory = builder.typeFactory;

    this.creators = builder.creators;
    this.binders = builder.binders;
  }

  private int size() {
    return this.items.size();
  }

  private void checkIndex(int index) {
    final int size = this.size();
    if (index < 0 || index >= size) {
      throw new IllegalArgumentException(String.format("index < 0 || index > %1$s", size));
    }
  }

  private <I extends Item> void checkItem(I item) {
    ObjectHelper.checkNotNull(item, "item");
    if (!this.typeFactory.contains(item)) {
      throw new IllegalArgumentException("!this.typeFactory.contains(item)");
    }
  }

  public final <I extends Item> boolean contains(I item) {
    return this.items.contains(ObjectHelper.checkNotNull(item, "item"));
  }

  public final <I extends Item> int indexOf(I item) {
    return this.items.indexOf(ObjectHelper.checkNotNull(item, "item"));
  }

  public final <I extends Item> I get(int index, Class<I> type) {
    this.checkIndex(index);
    final Class actualType = this.typeFactory.get(type);
    final Item item = this.items.get(index);
    final Class<? extends Item> itemType = item.getClass();
    if (!actualType.equals(itemType)) {
      throw new IllegalArgumentException(String.format("!type.equals(%1$s)", itemType));
    }
    return type.cast(item);
  }

  public final <I extends Item> void add(I item) {
    this.checkItem(item);
    this.items.add(item);
    this.notifyItemInserted(this.size());
  }

  public final <I extends Item> void add(int index, I item) {
    this.checkIndex(index);
    this.checkItem(item);
    this.items.add(index, item);
    this.notifyItemInserted(index);
  }

  public final <I extends Item> void addOrUpdate(I item) {
    if (this.contains(item)) {
      this.notifyItemChanged(this.indexOf(item));
    } else {
      this.add(item);
    }
  }

  public final <I extends Item> void set(int index, I item) {
    this.checkIndex(index);
    this.checkItem(item);
    this.items.set(index, item);
    this.notifyItemChanged(index);
  }

  public final void clear() {
    final int size = this.size();
    if (size > 0) {
      this.items.clear();
      this.notifyItemRangeRemoved(0, size);
    }
  }

  public final void remove(int index) {
    this.checkIndex(index);
    this.items.remove(index);
    this.notifyItemRemoved(index);
  }

  public final <I extends Item> void remove(I item) {
    this.checkItem(item);
    this.remove(this.indexOf(item));
  }

  @Override
  public int getItemCount() {
    return this.size();
  }

  @Override
  public int getItemViewType(int position) {
    return Single.just(position)
      .map(this.items::get)
      .map(Item::getClass)
      .map(this.typeFactory::indexOf)
      .blockingGet();
  }

  @Override
  public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return this.creators.get(this.typeFactory.get(viewType))
      .create(parent);
  }

  @SuppressWarnings("unchecked")
  @Override
  public void onBindViewHolder(ItemHolder holder, int position) {
    this.binders.get(this.typeFactory.get(this.getItemViewType(position)))
      .bind(this.items.get(position), holder);
  }

  public static final class Builder {

    private final ItemTypeFactory typeFactory;

    private final Map<Class, ItemHolderCreator> creators;
    private final Map<Class, ItemHolderBinder> binders;

    private Builder(ItemTypeFactory typeFactory) {
      this.typeFactory = ObjectHelper.checkNotNull(typeFactory, "typeFactory");

      this.creators = new HashMap<>();
      this.binders = new HashMap<>();
    }

    public final <I extends Item, IH extends ItemHolder> Builder itemType(
      Class<I> type,
      ItemHolderCreator<IH> creator,
      ItemHolderBinder<I, IH> binder
    ) {
      final Class actualType = this.typeFactory.get(type);
      this.creators.put(actualType, ObjectHelper.checkNotNull(creator, "creator"));
      this.binders.put(actualType, ObjectHelper.checkNotNull(binder, "binder"));
      return this;
    }

    public final ItemAdapter build() {
      BuilderChecker.create()
        .addPropertyNameIfMissing("creators", this.creators.isEmpty())
        .addPropertyNameIfMissing("binders", this.binders.isEmpty())
        .checkNoMissingProperties();
      return new ItemAdapter(this);
    }
  }
}
