package com.tpago.movil.d.ui.main.purchase;

import android.content.Context;

import com.tpago.movil.d.data.StringHelper;
import com.tpago.movil.d.data.res.AssetProvider;
import com.tpago.movil.d.domain.ProductManager;
import com.tpago.movil.d.domain.pos.PosBridge;
import com.tpago.movil.d.domain.util.EventBus;
import com.tpago.movil.d.ui.FragmentScope;
import com.tpago.movil.d.ui.AppDialog;

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
