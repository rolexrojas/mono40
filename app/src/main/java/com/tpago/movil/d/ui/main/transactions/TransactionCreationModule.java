package com.tpago.movil.d.ui.main.transactions;

import android.support.annotation.NonNull;

import com.tpago.movil.d.domain.Recipient;
import com.tpago.movil.d.ui.ActivityScope;

import dagger.Module;
import dagger.Provides;

/**
 * TODO
 *
 * @author hecvasro
 */
@Module
class TransactionCreationModule {
  private final Recipient recipient;

  /**
   * TODO
   *
   * @param recipient
   *   TODO
   */
  TransactionCreationModule(@NonNull Recipient recipient) {
    this.recipient = recipient;
  }

  @Provides
  @ActivityScope
  Recipient provideRecipient() {
    return recipient;
  }
}
