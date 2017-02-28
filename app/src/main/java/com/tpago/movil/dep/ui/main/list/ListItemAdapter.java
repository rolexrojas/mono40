package com.tpago.movil.dep.ui.main.list;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.tpago.movil.dep.misc.Utils;
import com.tpago.movil.dep.data.util.Binder;
import com.tpago.movil.dep.data.util.BinderFactory;
import com.tpago.movil.dep.data.util.Holder;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO
 *
 * @author hecvasro
 */
public class ListItemAdapter extends RecyclerView.Adapter<ListItemHolder> {
  /**
   * TODO
   */
  private final ListItemHolderCreatorFactory holderCreatorFactory;
  /**
   * TODO
   */
  private final BinderFactory binderFactory;

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
  public ListItemAdapter(@NonNull ListItemHolderCreatorFactory holderCreatorFactory,
    @NonNull BinderFactory binderFactory) {
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
   * @param index
   *   TODO
   * @param item
   *   TODO
   */
  public void add(int index, @NonNull Object item) {
    if (!contains(item)) {
      items.add(index, item);
      notifyItemInserted(index);
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
   * @param item
   *   TODO
   */
  public void updateOrAdd(@NonNull Object item) {
    if (items.contains(item)) {
      notifyItemChanged(items.indexOf(item));
    } else {
      add(item);
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
  public Object get(int position) {
    return items.get(position);
  }

  /**
   * TODO
   *
   * @param position
   *   TODO
   */
  public void remove(int position) {
    items.remove(position);
    notifyItemRemoved(position);
  }

  /**
   * TODO
   *
   * @param item
   *   TODO
   */
  public void remove(@NonNull Object item) {
    if (items.contains(item)) {
      remove(items.indexOf(item));
    }
  }

  @Override
  public int getItemViewType(int position) {
    return holderCreatorFactory.getIdentifier(get(position).getClass());
  }

  @Override
  public ListItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    final ListItemHolderCreator<? extends ListItemHolder> creator
      = holderCreatorFactory.getCreator(viewType);
    if (Utils.isNull(creator)) {
      throw new NullPointerException("Creator for '" + viewType + "' is missing");
    } else {
      return creator.create(parent);
    }
  }

  @Override
  @SuppressWarnings("unchecked")
  public void onBindViewHolder(ListItemHolder holder, int position) {
    final Object item = get(position);
    final Class<?> itemType = item.getClass();
    final Class<? extends Holder> holderType = holder.getClass();
    final Binder binder = binderFactory.getBinder(itemType, holderType);
    if (Utils.isNotNull(binder)) {
      binder.bind(item, holder);
    }
  }

  @Override
  public int getItemCount() {
    return items.size();
  }
}
