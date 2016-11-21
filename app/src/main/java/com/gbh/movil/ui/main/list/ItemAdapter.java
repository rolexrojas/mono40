package com.gbh.movil.ui.main.list;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.gbh.movil.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO
 *
 * @author hecvasro
 */
public class ItemAdapter extends RecyclerView.Adapter<ItemHolder> {
  /**
   * TODO
   */
  private final ItemHolderCreatorFactory holderCreatorFactory;
  /**
   * TODO
   */
  private final ItemHolderBinderFactory binderFactory;

  /**
   * TODO
   */
  private final List<Item> items = new ArrayList<>();

  /**
   * TODO
   *
   * @param holderCreatorFactory
   *   TODO
   * @param binderFactory
   *   TODO
   */
  public ItemAdapter(@NonNull ItemHolderCreatorFactory holderCreatorFactory,
    @NonNull ItemHolderBinderFactory binderFactory) {
    this.holderCreatorFactory = holderCreatorFactory;
    this.binderFactory = binderFactory;
  }

  /**
   * TODO
   */
  public void clearItems() {
    final int count = getItemCount();
    if (count > 0) {
      items.clear();
      notifyItemRangeRemoved(0, count);
    }
  }

  /**
   * TODO
   *
   * @param item
   *   TODO
   *
   * @return TODo
   */
  public boolean containsItem(@NonNull Item item) {
    return items.contains(item);
  }

  /**
   * TODO
   *
   * @param item
   *   TODO
   */
  public void addItem(@NonNull Item item) {
    if (!containsItem(item)) {
      items.add(item);
      notifyItemInserted(getItemCount());
    }
  }

  /**
   * TODO
   *
   * @param position
   *   TODO
   *
   * @return TODO
   */
  @NonNull
  public Item getItem(int position) {
    return items.get(position);
  }

  @Override
  public int getItemViewType(int position) {
    return holderCreatorFactory.getIdentifier(getItem(position).getClass());
  }

  @Override
  public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    final ItemHolderCreator<? extends ItemHolder> creator
      = holderCreatorFactory.getCreator(viewType);
    if (Utils.isNull(creator)) {
      throw new NullPointerException("Creator for '" + viewType + "' is missing");
    } else {
      return creator.create(parent);
    }
  }

  @Override
  @SuppressWarnings("unchecked")
  public void onBindViewHolder(ItemHolder holder, int position) {
    final Item item = getItem(position);
    final Class<? extends Item> itemType = item.getClass();
    final Class<? extends ItemHolder> holderType = holder.getClass();
    final ItemHolderBinder binder = binderFactory.getBinder(itemType, holderType);
    if (Utils.isNotNull(binder)) {
      binder.bind(item, holder);
    }
  }

  @Override
  public int getItemCount() {
    return items.size();
  }
}
