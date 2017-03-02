package com.tpago.movil.dep.ui.main.transactions;

import com.tpago.movil.app.AppComponent;
import com.tpago.movil.dep.data.SchedulerProvider;
import com.tpago.movil.dep.data.res.AssetProvider;
import com.tpago.movil.dep.domain.ProductManager;
import com.tpago.movil.dep.domain.Recipient;
import com.tpago.movil.dep.domain.TransactionManager;
import com.tpago.movil.dep.ui.ActivityScope;
import com.tpago.movil.dep.ui.main.transactions.bills.BillTransactionCreationPresenter;

import dagger.Component;

/**
 * @author hecvasro
 */
@ActivityScope
@Component(
  dependencies = AppComponent.class,
  modules = TransactionCreationModule.class)
public interface TransactionCreationComponent {
  void inject(TransactionCreationActivity activity);
  void inject(BillTransactionCreationPresenter presenter);

  SchedulerProvider provideSchedulerProvider();

  AssetProvider provideResourceProvider();

  ProductManager provideProductManager();

  TransactionManager provideTransactionManager();

  Recipient provideRecipient();
}
