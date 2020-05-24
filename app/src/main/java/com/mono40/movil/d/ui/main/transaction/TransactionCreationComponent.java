package com.mono40.movil.d.ui.main.transaction;

import com.mono40.movil.app.StringMapper;
import com.mono40.movil.app.ui.activity.base.ActivityModule;
import com.mono40.movil.app.ui.activity.ActivityScope;
import com.mono40.movil.d.data.SchedulerProvider;
import com.mono40.movil.d.data.StringHelper;
import com.mono40.movil.d.domain.Product;
import com.mono40.movil.d.domain.ProductManager;
import com.mono40.movil.d.domain.Recipient;
import com.mono40.movil.d.domain.api.DepApiBridge;
import com.mono40.movil.d.ui.main.transaction.bills.BillTransactionCreationFragment;
import com.mono40.movil.d.ui.main.transaction.bills.BillTransactionCreationPresenter;
import com.mono40.movil.d.ui.main.transaction.contacts.CarrierSelectionFragment;
import com.mono40.movil.d.ui.main.transaction.contacts.NonAffiliatedPhoneNumberBanReservasTransactionCreationFragment;
import com.mono40.movil.d.ui.main.transaction.contacts.NonAffiliatedPhoneNumberTransactionCreation1Fragment;
import com.mono40.movil.d.ui.main.transaction.contacts.NonAffiliatedPhoneNumberTransactionCreation2Fragment;
import com.mono40.movil.d.ui.main.transaction.products.CreditCardTransactionCreationFragment;
import com.mono40.movil.d.ui.main.transaction.products.CreditCardTransactionCreationPresenter;
import com.mono40.movil.d.ui.main.transaction.products.LoanTransactionCreationFragment;
import com.mono40.movil.d.ui.main.transaction.products.LoanTransactionCreationPresenter;
import com.mono40.movil.dep.AppComponent;
import com.mono40.movil.dep.net.NetworkService;
import com.mono40.movil.session.SessionManager;

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
    com.mono40.movil.dep.ActivityModule.class,
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
