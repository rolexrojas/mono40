package com.tpago.movil.d.ui.main.purchase;

import android.content.Context;
import androidx.fragment.app.FragmentManager;

import com.tpago.movil.app.ui.activity.ActivityScope;
import com.tpago.movil.app.ui.loader.takeover.TakeoverLoader;
import com.tpago.movil.company.CompanyHelper;
import com.tpago.movil.d.ui.DepActivityBase;
import com.tpago.movil.dep.User;
import com.tpago.movil.app.ui.fragment.FragmentScope;
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
public class PurchaseModule {
  private final DepActivityBase activity;

  public PurchaseModule(DepActivityBase activity) {
    this.activity = activity;
  }

  @Provides
  @FragmentScope
  PurchasePaymentOptionBinder providePaymentOptionBinder(
    User user,
    Context context,
    StringHelper stringHelper,
    CompanyHelper companyHelper
  ) {
    return new PurchasePaymentOptionBinder(user, context, stringHelper, companyHelper);
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

  @Provides
  @FragmentScope
  TakeoverLoader takeoverLoader(FragmentManager fragmentManager) {
    return TakeoverLoader.create(fragmentManager);
  }

  @Provides
  @FragmentScope
  FragmentManager fragmentManager() {
    return this.activity.getSupportFragmentManager();
  }
}
