package com.tpago.movil.app.ui.main.transaction.disburse.product.amount;

import com.tpago.movil.api.Api;
import com.tpago.movil.app.StringMapper;
import com.tpago.movil.app.ui.alert.AlertManager;
import com.tpago.movil.app.ui.fragment.FragmentScopeChild;
import com.tpago.movil.app.ui.loader.takeover.TakeoverLoader;
import com.tpago.movil.company.CompanyHelper;
import com.tpago.movil.product.disbursable.DisbursableProduct;
import com.tpago.movil.product.disbursable.DisbursableProductHelper;
import com.tpago.movil.util.Money;
import com.tpago.movil.util.ObjectHelper;

import dagger.Module;
import dagger.Provides;

/**
 * @author hecvasro
 */
@Module
public final class FragmentModuleDisburseProductAmount {

  static FragmentModuleDisburseProductAmount create(PresentationDisburseProductAmount presentation) {
    return new FragmentModuleDisburseProductAmount(presentation);
  }

  private final PresentationDisburseProductAmount presentation;

  private FragmentModuleDisburseProductAmount(PresentationDisburseProductAmount presentation) {
    this.presentation = ObjectHelper.checkNotNull(presentation, "presentation");
  }

  @Provides
  @FragmentScopeChild
  PresenterDisburseProductAmount presenter(
    DisbursableProduct product,
    Money.Creator amount,
    Api api,
    StringMapper stringMapper,
    CompanyHelper companyHelper,
    DisbursableProductHelper productHelper,
    AlertManager alertManager,
    TakeoverLoader takeoverLoader
  ) {
    return PresenterDisburseProductAmount.build()
      .product(product)
      .amount(amount)
      .api(api)
      .stringMapper(stringMapper)
      .companyHelper(companyHelper)
      .productHelper(productHelper)
      .alertManager(alertManager)
      .takeoverLoader(takeoverLoader)
      .presentation(this.presentation)
      .build();
  }
}
