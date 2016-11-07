package com.gbh.movil.ui.main.payments;

import android.support.annotation.NonNull;

import com.gbh.movil.domain.Recipient;
import com.gbh.movil.ui.Refreshable;

/**
 * Payments screen definition.
 *
 * @author hecvasro
 */
interface PaymentsScreen extends Refreshable {
  /**
   * TODO
   */
  void clear();

  /**
   * TODO
   *
   * @param recipient
   *   TODO
   */
  void add(@NonNull Recipient recipient);

  /**
   * TODO
   *
   * @param action
   *   TODO
   */
  void add(@NonNull Action action);
}
