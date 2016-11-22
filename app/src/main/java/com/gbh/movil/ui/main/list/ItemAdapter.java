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
public class ItemAdapter extends RecyclerView.Adapter<Holder> {
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
  private final List<Object> items = new ArrayList<>();

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
   * @return TODO
   */
  public boolean containsItem(@NonNull Object item) {
    return items.contains(item);
  }

  /**
   * TODO
   *
   * @param item
   *   TODO
   *
   * @return TODO
   */
  public int indexOf(@NonNull Object item) {
    return items.indexOf(item);
  }

  /**
   * TODO
   *
   * @param item
   *   TODO
   */
  public void addItem(@NonNull Object item) {
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
   * @param item
   *   TODO
   */
  public void setItem(int position, @NonNull Object item) {
    items.set(position, item);
    notifyItemChanged(position);
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
  public Object getItem(int position) {
    return items.get(position);
  }

  @Override
  public int getItemViewType(int position) {
    return holderCreatorFactory.getIdentifier(getItem(position).getClass());
  }

  @Override
  public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
    final HolderCreator<? extends Holder> creator
      = holderCreatorFactory.getCreator(viewType);
    if (Utils.isNull(creator)) {
      throw new NullPointerException("Creator for '" + viewType + "' is missing");
    } else {
      return creator.create(parent);
    }
  }

  @Override
  @SuppressWarnings("unchecked")
  public void onBindViewHolder(Holder holder, int position) {
    final Object item = getItem(position);
    final Class<?> itemType = item.getClass();
    final Class<? extends Holder> holderType = holder.getClass();
    final HolderBinder holderBinder = binderFactory.getBinder(itemType, holderType);
    if (Utils.isNotNull(holderBinder)) {
      holderBinder.bind(item, holder);
    }
  }

  @Override
  public int getItemCount() {
    return items.size();
  }
}
