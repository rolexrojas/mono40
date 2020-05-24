package com.mono40.movil.d.ui.main.purchase;

import android.content.Context;
import androidx.fragment.app.FragmentManager;

import com.mono40.movil.app.ui.activity.ActivityScope;
import com.mono40.movil.app.ui.loader.takeover.TakeoverLoader;
import com.mono40.movil.company.CompanyHelper;
import com.mono40.movil.d.ui.DepActivityBase;
import com.mono40.movil.dep.User;
import com.mono40.movil.app.ui.fragment.FragmentScope;
import com.mono40.movil.d.data.StringHelper;
import com.mono40.movil.d.domain.ProductManager;
import com.mono40.movil.d.domain.api.DepApiBridge;
import com.mono40.movil.d.domain.pos.PosBridge;
import com.mono40.movil.d.domain.util.EventBus;
import com.mono40.movil.d.ui.AppDialog;
import com.mono40.movil.dep.net.NetworkService;

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
