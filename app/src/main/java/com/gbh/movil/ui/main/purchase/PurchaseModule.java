package com.gbh.movil.ui.main.purchase;

import android.content.Context;

import com.gbh.movil.data.SchedulerProvider;
import com.gbh.movil.data.StringHelper;
import com.gbh.movil.data.res.AssetProvider;
import com.gbh.movil.domain.ProductManager;
import com.gbh.movil.ui.FragmentScope;

import dagger.Module;
import dagger.Provides;

/**
 * TODO
 *
 * @author hecvasro
 */
@Module
class PurchaseModule {
  /**
   * TODO
   *
   * @param context
   *   TODO
   * @param assetProvider
   *   TODO
   *
   * @return TODO
   */
  @Provides
  @FragmentScope
  CommercePaymentOptionBinder providePaymentOptionBinder(Context context, StringHelper stringHelper,
    AssetProvider assetProvider) {
    return new CommercePaymentOptionBinder(context, stringHelper, assetProvider);
  }

  /**
   * TODO
   *
   * @return TODO
   */
  @Provides
  @FragmentScope
  PurchasePresenter providePresenter(SchedulerProvider schedulerProvider,
    ProductManager productManager) {
    return new PurchasePresenter(schedulerProvider, productManager);
  }
}
