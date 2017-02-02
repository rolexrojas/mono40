package com.tpago.movil.ui.main.purchase;

import android.content.Context;

import com.tpago.movil.data.StringHelper;
import com.tpago.movil.data.res.AssetProvider;
import com.tpago.movil.domain.ProductManager;
import com.tpago.movil.domain.pos.PosBridge;
import com.tpago.movil.domain.util.EventBus;
import com.tpago.movil.ui.FragmentScope;
import com.tpago.movil.ui.AppDialog;

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
  PurchasePresenter providePresenter(StringHelper stringHelper,
    ProductManager productManager, EventBus eventBus, AppDialog.Creator screenDialogCreator,
    PosBridge posBridge) {
    return new PurchasePresenter(stringHelper, productManager, eventBus, screenDialogCreator, posBridge);
  }
}
