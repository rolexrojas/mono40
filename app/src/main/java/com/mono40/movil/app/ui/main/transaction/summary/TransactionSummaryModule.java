package com.mono40.movil.app.ui.main.transaction.summary;

import com.mono40.movil.app.ui.fragment.FragmentScope;
import com.mono40.movil.transaction.TransactionSummary;
import com.mono40.movil.util.ObjectHelper;

import dagger.Module;
import dagger.Provides;

/**
 * @author hecvasro
 */
@Module
public final class TransactionSummaryModule {

  static TransactionSummaryModule create(TransactionSummary transactionSummary) {
    return new TransactionSummaryModule(transactionSummary);
  }

  private final TransactionSummary transactionSummary;

  private TransactionSummaryModule(TransactionSummary transactionSummary) {
    this.transactionSummary = ObjectHelper.checkNotNull(transactionSummary, "transactionSummary");
  }

  @Provides
  @FragmentScope
  TransactionSummary transactionSummary() {
    return this.transactionSummary;
  }
}
