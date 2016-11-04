package com.gbh.movil.ui.main.accounts.transactions;

import android.support.annotation.NonNull;

import com.gbh.movil.domain.Transaction;
import com.gbh.movil.ui.Refreshable;

import java.util.Date;

/**
 * TODO
 *
 * @author hecvasro
 */
interface RecentTransactionsScreen extends Refreshable {
  void clear();

  void add(@NonNull Date date);

  void add(@NonNull Transaction transaction);
}
