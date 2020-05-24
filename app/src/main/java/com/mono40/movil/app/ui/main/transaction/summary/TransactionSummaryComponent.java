package com.mono40.movil.app.ui.main.transaction.summary;

import com.mono40.movil.app.di.ComponentBuilder;
import com.mono40.movil.app.ui.fragment.FragmentScope;

import dagger.Subcomponent;

/**
 * @author hecvasro
 */
@FragmentScope
@Subcomponent(modules = TransactionSummaryModule.class)
public interface TransactionSummaryComponent {

  void inject(TransactionSummaryDialogFragment fragment);

  @Subcomponent.Builder
  interface Builder extends ComponentBuilder<TransactionSummaryComponent> {

    Builder transactionSummaryModule(TransactionSummaryModule module);
  }
}
