package com.tpago.movil.d.ui.main.transaction;

import com.tpago.movil.app.StringMapper;
import com.tpago.movil.app.ui.activity.base.ActivityModule;
import com.tpago.movil.app.ui.activity.ActivityScope;
import com.tpago.movil.d.data.SchedulerProvider;
import com.tpago.movil.d.data.StringHelper;
import com.tpago.movil.d.domain.Product;
import com.tpago.movil.d.domain.ProductManager;
import com.tpago.movil.d.domain.Recipient;
import com.tpago.movil.d.domain.api.DepApiBridge;
import com.tpago.movil.d.ui.main.transaction.bills.BillTransactionCreationFragment;
import com.tpago.movil.d.ui.main.transaction.bills.BillTransactionCreationPresenter;
import com.tpago.movil.d.ui.main.transaction.contacts.CarrierSelectionFragment;
import com.tpago.movil.d.ui.main.transaction.contacts.NonAffiliatedPhoneNumberBanReservasTransactionCreationFragment;
import com.tpago.movil.d.ui.main.transaction.contacts.NonAffiliatedPhoneNumberTransactionCreation1Fragment;
import com.tpago.movil.d.ui.main.transaction.contacts.NonAffiliatedPhoneNumberTransactionCreation2Fragment;
import com.tpago.movil.d.ui.main.transaction.products.CreditCardTransactionCreationFragment;
import com.tpago.movil.d.ui.main.transaction.products.CreditCardTransactionCreationPresenter;
import com.tpago.movil.d.ui.main.transaction.products.LoanTransactionCreationFragment;
import com.tpago.movil.d.ui.main.transaction.products.LoanTransactionCreationPresenter;
import com.tpago.movil.dep.AppComponent;
import com.tpago.movil.dep.net.NetworkService;
import com.tpago.movil.session.SessionManager;

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
    com.tpago.movil.dep.ActivityModule.class,
    TransactionCreationModule.class
  }
)
public interface TransactionCreationComponent {

  void inject(TransactionCreationActivityBase activity);

  void inject(NonAffiliatedPhoneNumberBanReservasTransactionCreationFragment fragment);

  void inject(NonAffiliatedPhoneNumberTransactionCreation1Fragment fragment);

  void inject(NonAffiliatedPhoneNumberTransactionCreation2Fragment fragment);

  void inject(BillTransactionCreationFragment fragment);

  void inject(BillTransactionCreationPresenter presenter);

  void inject(CreditCardTransactionCreationFragment fragment);

  void inject(CreditCardTransactionCreationPresenter presenter);

  void inject(LoanTransactionCreationFragment fragment);

  void inject(LoanTransactionCreationPresenter presenter);

  void inject(CarrierSelectionFragment fragment);

  AtomicReference<BigDecimal> provideAmount();

  AtomicReference<Product> provideFundingAccount();

  DepApiBridge provideDepApiBridge();

  NetworkService provideNetworkService();

  ProductManager provideProductManager();

  Recipient provideRecipient();

  SchedulerProvider provideSchedulerProvider();

  StringHelper provideStringHelper();

  TransactionCategory transactionCategory();

  @Deprecated
  StringMapper stringMapper();

  SessionManager sessionManager();
}
