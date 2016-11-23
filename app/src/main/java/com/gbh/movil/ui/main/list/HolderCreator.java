package com.gbh.movil.ui.main.list;

import android.support.annotation.NonNull;
import android.view.ViewGroup;

/**
 * TODO
 *
 * @author hecvasro
 */
public interface HolderCreator<T extends Holder> {
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
