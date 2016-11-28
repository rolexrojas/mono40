package com.gbh.movil.ui.main.payments.transactions;

import com.gbh.movil.AppComponent;
import com.gbh.movil.data.SchedulerProvider;
import com.gbh.movil.data.res.AssetProvider;
import com.gbh.movil.domain.ProductManager;
import com.gbh.movil.domain.Recipient;
import com.gbh.movil.domain.TransactionManager;
import com.gbh.movil.ui.ActivityScope;

import dagger.Component;

/**
 * @author hecvasro
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = TransactionCreationModule.class)
public interface TransactionCreationComponent {
  /**
   * TODO
   *
   * @param activity
   *   TODO
   */
  void inject(TransactionCreationActivity activity);

  SchedulerProvider provideSchedulerProvider();

  AssetProvider provideResourceProvider();

  ProductManager provideProductManager();

  TransactionManager provideTransactionManager();

  Recipient provideRecipient();
}
