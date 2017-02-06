package com.tpago.movil.ui.main.list;

import android.support.annotation.NonNull;
import android.view.ViewGroup;

/**
 * TODO
 *
 * @author hecvasro
 */
public interface ListItemHolderCreator<T extends ListItemHolder> {
  /**
   * TODO
   *
   * @param parent
   *   TODO
   *
   * @return TODO
   */
  @NonNull
  T create(@NonNull ViewGroup parent);
}
