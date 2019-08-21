package com.tpago.movil.d.ui.main.products.transactions;

import androidx.annotation.NonNull;

import com.tpago.movil.d.domain.Transaction;
import com.tpago.movil.d.ui.Refreshable;
import com.tpago.movil.d.ui.Screen;

import java.util.Date;

/**
 * TODO
 *
 * @author hecvasro
 */
interface RecentTransactionsScreen extends Screen, Refreshable {
  /**
   * TODO
   */
  void clear();

  /**
   * TODO
   *
   * @param date
   *   TODO
   */
  void add(@NonNull Date date);

  /**
   * TODO
   *
   * @param transaction
   *   TODO
   */
  void add(@NonNull Transaction transaction);
}
