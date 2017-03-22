package com.tpago.movil.d.ui.main.transactions;

import android.support.annotation.NonNull;

import com.tpago.movil.app.ActivityScope;
import com.tpago.movil.d.domain.Product;
import com.tpago.movil.d.domain.Recipient;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicReference;

import dagger.Module;
import dagger.Provides;

/**
 * @author hecvasro
 */
@Module
class TransactionCreationModule {
  private final Recipient recipient;

  TransactionCreationModule(@NonNull Recipient recipient) {
    this.recipient = recipient;
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
