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
  public void clear() {
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
   */
  public void add(@NonNull Item item) {
    if (!items.contains(item)) {
      items.add(item);
      notifyItemInserted(getItemCount());
    }
  }

  @Override
  public int getItemViewType(int position) {
    return holderCreatorFactory.getIdentifier(items.get(position).getClass());
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
    final Item item = items.get(position);
    final Class<? extends Item> itemType = item.getClass();
    final Class<? extends ItemHolder> holderType = holder.getClass();
    final ItemHolderBinder binder = binderFactory.getBinder(itemType, holderType);
    if (Utils.isNull(binder)) {
      throw new NullPointerException("Binder for '" + itemType + "' and '" + holderType
        + "' is missing");
    } else {
      binder.bind(item, holder);
    }
  }

  @Override
  public int getItemCount() {
    return items.size();
  }
}
