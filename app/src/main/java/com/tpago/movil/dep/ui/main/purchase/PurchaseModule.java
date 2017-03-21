package com.tpago.movil.dep.ui.main.purchase;

import android.content.Context;

import com.tpago.movil.User;
import com.tpago.movil.app.FragmentScope;
import com.tpago.movil.dep.data.StringHelper;
import com.tpago.movil.dep.domain.ProductManager;
import com.tpago.movil.dep.domain.pos.PosBridge;
import com.tpago.movil.dep.domain.util.EventBus;
import com.tpago.movil.dep.ui.AppDialog;

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
   *
   * @return TODO
   */
  @Provides
  @FragmentScope
  PurchasePaymentOptionBinder providePaymentOptionBinder(
    User user,
    Context context,
    StringHelper stringHelper) {
    return new PurchasePaymentOptionBinder(user, context, stringHelper);
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
