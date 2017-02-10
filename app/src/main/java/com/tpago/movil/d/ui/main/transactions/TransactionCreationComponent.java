package com.tpago.movil.d.ui.main.transactions;

import com.tpago.movil.d.AppComponent;
import com.tpago.movil.d.data.SchedulerProvider;
import com.tpago.movil.d.data.res.AssetProvider;
import com.tpago.movil.d.domain.ProductManager;
import com.tpago.movil.d.domain.Recipient;
import com.tpago.movil.d.domain.TransactionManager;
import com.tpago.movil.d.ui.ActivityScope;

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
