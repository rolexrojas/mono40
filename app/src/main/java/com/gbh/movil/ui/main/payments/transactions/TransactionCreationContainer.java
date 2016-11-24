package com.gbh.movil.ui.main.payments.transactions;

import android.support.annotation.Nullable;

import com.gbh.movil.ui.Container;

/**
 * TODO
 *
 * @author hecvasro
 */
public interface TransactionCreationContainer extends Container<TransactionCreationComponent> {
  /**
   * TODO
   *
   * @param title
   *   TODO
   */
  void setTitle(@Nullable String title);

  /**
   * TODO
   *
   * @param subtitle
   *   TODO
   */
  void setSubTitle(@Nullable String subtitle);
}
