package com.tpago.movil.ui.main.transactions;

import com.tpago.movil.AppComponent;
import com.tpago.movil.data.SchedulerProvider;
import com.tpago.movil.data.res.AssetProvider;
import com.tpago.movil.domain.ProductManager;
import com.tpago.movil.domain.Recipient;
import com.tpago.movil.domain.TransactionManager;
import com.tpago.movil.ui.ActivityScope;

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
