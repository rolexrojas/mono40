package com.gbh.movil.ui.main.accounts.transactions;

import android.support.annotation.NonNull;

import com.gbh.movil.domain.Transaction;
import com.gbh.movil.ui.Refreshable;
import com.gbh.movil.ui.Screen;

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
