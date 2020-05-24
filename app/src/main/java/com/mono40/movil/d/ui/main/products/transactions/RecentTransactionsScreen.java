package com.mono40.movil.d.ui.main.products.transactions;

import androidx.annotation.NonNull;

import com.mono40.movil.d.domain.Transaction;
import com.mono40.movil.d.ui.Refreshable;
import com.mono40.movil.d.ui.Screen;

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
