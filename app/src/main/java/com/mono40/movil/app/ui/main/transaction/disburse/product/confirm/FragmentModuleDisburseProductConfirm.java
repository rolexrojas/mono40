package com.mono40.movil.app.ui.main.transaction.disburse.product.confirm;

import com.mono40.movil.api.Api;
import com.mono40.movil.app.StringMapper;
import com.mono40.movil.app.ui.alert.AlertManager;
import com.mono40.movil.app.ui.fragment.FragmentScopeChild;
import com.mono40.movil.app.ui.loader.takeover.TakeoverLoader;
import com.mono40.movil.product.disbursable.DisbursableProduct;
import com.mono40.movil.product.disbursable.DisbursableProductHelper;
import com.mono40.movil.util.ObjectHelper;

import dagger.Module;
import dagger.Provides;

/**
 * @author hecvasro
 */
@Module
public final class FragmentModuleDisburseProductConfirm {

  static FragmentModuleDisburseProductConfirm create(PresentationDisburseProductConfirm presentation) {
    return new FragmentModuleDisburseProductConfirm(presentation);
  }

  private final PresentationDisburseProductConfirm presentation;

  private FragmentModuleDisburseProductConfirm(PresentationDisburseProductConfirm presentation) {
    this.presentation = ObjectHelper.checkNotNull(presentation, "presentation");
  }

  @Provides
  @FragmentScopeChild
  PresenterDisburseProductConfirm presenter(
    DisbursableProduct product,
    Api api,
    DisbursableProductHelper productHelper,
    StringMapper stringMapper,
    AlertManager alertManager,
    TakeoverLoader takeoverLoader
  ) {
    return PresenterDisburseProductConfirm.builder()
      .product(product)
      .api(api)
      .productHelper(productHelper)
      .stringMapper(stringMapper)
      .alertManager(alertManager)
      .takeoverLoader(takeoverLoader)
      .presentation(this.presentation)
      .build();
  }
}
