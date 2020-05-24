package com.mono40.movil.d.ui.main.transaction;

import androidx.annotation.NonNull;

import com.mono40.movil.app.ui.activity.ActivityScope;
import com.mono40.movil.d.domain.Product;
import com.mono40.movil.d.domain.Recipient;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicReference;

import dagger.Module;
import dagger.Provides;

/**
 * @author hecvasro
 */
@Module
class TransactionCreationModule {

  private final TransactionCategory transactionCategory;
  private final Recipient recipient;

  TransactionCreationModule(TransactionCategory transactionCategory, @NonNull Recipient recipient) {
    this.transactionCategory = transactionCategory;
    this.recipient = recipient;
  }

  @Provides
  @ActivityScope
  TransactionCategory transactionCategory() {
    return this.transactionCategory;
  }

  @Provides
  @ActivityScope
  Recipient provideRecipient() {
    return recipient;
  }

  @Provides
  @ActivityScope
  AtomicReference<BigDecimal> provideValue() {
    return new AtomicReference<>(BigDecimal.ZERO);
  }

  @Provides
  @ActivityScope
  AtomicReference<Product> provideFundingAccount() {
    return new AtomicReference<>();
  }
}
