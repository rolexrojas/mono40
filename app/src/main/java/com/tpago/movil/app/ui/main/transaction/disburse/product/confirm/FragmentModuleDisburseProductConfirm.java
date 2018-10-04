package com.tpago.movil.app.ui.main.transaction.disburse.product.confirm;

import com.tpago.movil.api.Api;
import com.tpago.movil.app.StringMapper;
import com.tpago.movil.app.ui.alert.AlertManager;
import com.tpago.movil.app.ui.fragment.FragmentScopeChild;
import com.tpago.movil.app.ui.loader.takeover.TakeoverLoader;
import com.tpago.movil.product.disbursable.DisbursableProduct;
import com.tpago.movil.product.disbursable.DisbursableProductHelper;
import com.tpago.movil.util.ObjectHelper;

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
