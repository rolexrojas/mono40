package com.gbh.movil.ui.main.payments.commerce;

import android.support.annotation.NonNull;

import com.gbh.movil.ui.Screen;

/**
 * TODO
 *
 * @author hecvasro
 */
interface CommercePaymentsScreen extends Screen {
  /**
   * TODO
   */
  void clearItemList();

  /**
   * TODO
   *
   * @param item
   *   TODO
   */
  void addItemToList(@NonNull Object item);
}
