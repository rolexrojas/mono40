package com.tpago.movil.dep.ui.main.products.transactions;

import android.support.annotation.NonNull;

import com.tpago.movil.dep.domain.Transaction;
import com.tpago.movil.dep.ui.Refreshable;
import com.tpago.movil.dep.ui.Screen;

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
