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
public class Adapter extends RecyclerView.Adapter<Holder> {
  /**
   * TODO
   */
  private final HolderCreatorFactory holderCreatorFactory;
  /**
   * TODO
   */
  private final HolderBinderFactory holderBinderFactory;

  /**
   * TODO
   */
  private final List<Object> items = new ArrayList<>();

  /**
   * TODO
   *
   * @param holderCreatorFactory
   *   TODO
   * @param holderBinderFactory
   *   TODO
   */
  public Adapter(@NonNull HolderCreatorFactory holderCreatorFactory,
    @NonNull HolderBinderFactory holderBinderFactory) {
    this.holderCreatorFactory = holderCreatorFactory;
    this.holderBinderFactory = holderBinderFactory;
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
   *
   * @return TODO
   */
  public boolean contains(@NonNull Object item) {
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
  public void add(@NonNull Object item) {
    if (!contains(item)) {
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
  public void set(int position, @NonNull Object item) {
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
  public Object get(int position) {
    return items.get(position);
  }

  @Override
  public int getItemViewType(int position) {
    return holderCreatorFactory.getIdentifier(get(position).getClass());
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
    final Object item = get(position);
    final Class<?> itemType = item.getClass();
    final Class<? extends Holder> holderType = holder.getClass();
    final HolderBinder holderBinder = holderBinderFactory.getBinder(itemType, holderType);
    if (Utils.isNotNull(holderBinder)) {
      holderBinder.bind(item, holder);
    }
  }

  @Override
  public int getItemCount() {
    return items.size();
  }
}
