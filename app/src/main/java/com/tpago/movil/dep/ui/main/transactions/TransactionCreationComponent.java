package com.tpago.movil.dep.ui.main.transactions;

import com.tpago.movil.app.AppComponent;
import com.tpago.movil.dep.data.SchedulerProvider;
import com.tpago.movil.dep.data.res.AssetProvider;
import com.tpago.movil.dep.domain.Product;
import com.tpago.movil.dep.domain.ProductManager;
import com.tpago.movil.dep.domain.Recipient;
import com.tpago.movil.dep.domain.TransactionManager;
import com.tpago.movil.dep.ui.ActivityScope;
import com.tpago.movil.dep.ui.main.transactions.bills.BillTransactionCreationPresenter;
import com.tpago.movil.dep.ui.main.transactions.contacts.NonAffiliatedPhoneNumberTransactionCreation1Fragment;
import com.tpago.movil.dep.ui.main.transactions.contacts.NonAffiliatedPhoneNumberTransactionCreation2Fragment;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicReference;

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
  void inject(NonAffiliatedPhoneNumberTransactionCreation1Fragment fragment);
  void inject(NonAffiliatedPhoneNumberTransactionCreation2Fragment fragment);
  void inject(BillTransactionCreationPresenter presenter);

  AssetProvider provideResourceProvider();
  AtomicReference<BigDecimal> provideAmount();
  AtomicReference<Product> provideFundingAccount();
  ProductManager provideProductManager();
  Recipient provideRecipient();
  SchedulerProvider provideSchedulerProvider();
  TransactionManager provideTransactionManager();
}
