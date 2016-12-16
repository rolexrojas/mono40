package com.gbh.movil.ui.main.purchase;

import android.content.Context;

import com.gbh.movil.data.SchedulerProvider;
import com.gbh.movil.data.StringHelper;
import com.gbh.movil.data.res.AssetProvider;
import com.gbh.movil.domain.ProductManager;
import com.gbh.movil.domain.util.EventBus;
import com.gbh.movil.ui.FragmentScope;
import com.gbh.movil.ui.AppDialog;

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
  PurchasePaymentOptionBinder providePaymentOptionBinder(Context context, StringHelper stringHelper,
    AssetProvider assetProvider) {
    return new PurchasePaymentOptionBinder(context, stringHelper, assetProvider);
  }

  /**
   * TODO
   *
   * @return TODO
   */
  @Provides
  @FragmentScope
  PurchasePresenter providePresenter(StringHelper stringHelper, SchedulerProvider schedulerProvider,
    ProductManager productManager, EventBus eventBus, AppDialog.Creator screenDialogCreator) {
    return new PurchasePresenter(stringHelper, schedulerProvider, productManager,
      eventBus, screenDialogCreator);
  }
}
