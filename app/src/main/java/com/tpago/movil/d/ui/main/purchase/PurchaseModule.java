package com.tpago.movil.d.ui.main.purchase;

import android.content.Context;

import com.tpago.movil.dep.User;
import com.tpago.movil.app.ui.FragmentScope;
import com.tpago.movil.d.data.StringHelper;
import com.tpago.movil.d.domain.ProductManager;
import com.tpago.movil.d.domain.api.DepApiBridge;
import com.tpago.movil.d.domain.pos.PosBridge;
import com.tpago.movil.d.domain.util.EventBus;
import com.tpago.movil.d.ui.AppDialog;
import com.tpago.movil.dep.net.NetworkService;

import dagger.Module;
import dagger.Provides;

/**
 * @author hecvasro
 */
@Module
class PurchaseModule {

  @Provides
  @FragmentScope
  PurchasePaymentOptionBinder providePaymentOptionBinder(
    User user,
    Context context,
    StringHelper stringHelper
  ) {
    return new PurchasePaymentOptionBinder(user, context, stringHelper);
  }

  @Provides
  @FragmentScope
  PurchasePresenter providePresenter(
    StringHelper stringHelper,
    ProductManager productManager,
    EventBus eventBus,
    AppDialog.Creator screenDialogCreator,
    PosBridge posBridge,
    NetworkService networkService,
    DepApiBridge depApiBridge,
    User user
  ) {
    return new PurchasePresenter(
      stringHelper,
      productManager,
      eventBus,
      screenDialogCreator,
      posBridge,
      networkService,
      depApiBridge,
      user.phoneNumber()
        .value()
    );
  }
}
