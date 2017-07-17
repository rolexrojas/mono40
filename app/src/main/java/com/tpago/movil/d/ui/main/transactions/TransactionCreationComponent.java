package com.tpago.movil.d.ui.main.transactions;

import com.tpago.movil.app.ActivityModule;
import com.tpago.movil.app.ActivityScope;
import com.tpago.movil.app.AppComponent;
import com.tpago.movil.d.data.SchedulerProvider;
import com.tpago.movil.d.data.StringHelper;
import com.tpago.movil.d.domain.Product;
import com.tpago.movil.d.domain.ProductManager;
import com.tpago.movil.d.domain.Recipient;
import com.tpago.movil.d.domain.api.DepApiBridge;
import com.tpago.movil.d.domain.session.SessionManager;
import com.tpago.movil.d.ui.main.transactions.bills.BillTransactionCreationFragment;
import com.tpago.movil.d.ui.main.transactions.bills.BillTransactionCreationPresenter;
import com.tpago.movil.d.ui.main.transactions.contacts.NonAffiliatedPhoneNumberTransactionCreation1Fragment;
import com.tpago.movil.d.ui.main.transactions.contacts.NonAffiliatedPhoneNumberTransactionCreation2Fragment;
import com.tpago.movil.d.ui.main.transactions.products.CreditCardTransactionCreationFragment;
import com.tpago.movil.d.ui.main.transactions.products.CreditCardTransactionCreationPresenter;
import com.tpago.movil.d.ui.main.transactions.products.LoanTransactionCreationFragment;
import com.tpago.movil.d.ui.main.transactions.products.LoanTransactionCreationPresenter;
import com.tpago.movil.net.NetworkService;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicReference;

import dagger.Component;

/**
 * @author hecvasro
 */
@ActivityScope
@Component(
  dependencies = AppComponent.class,
  modules = {
    ActivityModule.class,
    TransactionCreationModule.class
  })
public interface TransactionCreationComponent {
  void inject(TransactionCreationActivity activity);
  void inject(NonAffiliatedPhoneNumberTransactionCreation1Fragment fragment);
  void inject(NonAffiliatedPhoneNumberTransactionCreation2Fragment fragment);
  void inject(BillTransactionCreationFragment fragment);
  void inject(BillTransactionCreationPresenter presenter);
  void inject(CreditCardTransactionCreationFragment fragment);
  void inject(CreditCardTransactionCreationPresenter presenter);
  void inject(LoanTransactionCreationFragment fragment);
  void inject(LoanTransactionCreationPresenter presenter);

  AtomicReference<BigDecimal> provideAmount();
  AtomicReference<Product> provideFundingAccount();
  DepApiBridge provideDepApiBridge();
  NetworkService provideNetworkService();
  ProductManager provideProductManager();
  Recipient provideRecipient();
  SchedulerProvider provideSchedulerProvider();
  StringHelper provideStringHelper();
  SessionManager provideSessionManager();
}
