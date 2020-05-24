package com.mono40.movil.app.ui.main.transaction.disburse.product.amount;

import com.mono40.movil.api.Api;
import com.mono40.movil.app.StringMapper;
import com.mono40.movil.app.ui.alert.AlertManager;
import com.mono40.movil.app.ui.fragment.FragmentScopeChild;
import com.mono40.movil.app.ui.loader.takeover.TakeoverLoader;
import com.mono40.movil.company.CompanyHelper;
import com.mono40.movil.product.disbursable.DisbursableProduct;
import com.mono40.movil.product.disbursable.DisbursableProductHelper;
import com.mono40.movil.util.Money;
import com.mono40.movil.util.ObjectHelper;

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
