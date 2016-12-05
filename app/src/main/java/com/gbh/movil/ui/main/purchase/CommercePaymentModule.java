package com.gbh.movil.ui.main.purchase;

import com.gbh.movil.data.StringHelper;
import com.gbh.movil.ui.ChildFragmentScope;

import dagger.Module;
import dagger.Provides;

/**
 * @author hecvasro
 */
@Module
class CommercePaymentModule {
  @Provides
  @ChildFragmentScope
  PurchasePaymentPresenter providePresenter(StringHelper stringHelper) {
    return new PurchasePaymentPresenter(stringHelper);
  }
}
