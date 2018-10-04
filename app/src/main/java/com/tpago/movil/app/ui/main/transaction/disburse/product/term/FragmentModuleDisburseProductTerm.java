package com.tpago.movil.app.ui.main.transaction.disburse.product.term;

import com.tpago.movil.api.Api;
import com.tpago.movil.app.StringMapper;
import com.tpago.movil.app.ui.alert.AlertManager;
import com.tpago.movil.app.ui.fragment.FragmentScopeChild;
import com.tpago.movil.app.ui.loader.takeover.TakeoverLoader;
import com.tpago.movil.product.disbursable.DisbursableProduct;
import com.tpago.movil.util.Number;
import com.tpago.movil.util.ObjectHelper;

import dagger.Module;
import dagger.Provides;

/**
 * @author hecvasro
 */
@Module
public final class FragmentModuleDisburseProductTerm {

  static FragmentModuleDisburseProductTerm create(PresentationDisburseProductTerm presentation) {
    return new FragmentModuleDisburseProductTerm(presentation);
  }

  private final PresentationDisburseProductTerm presentation;

  private FragmentModuleDisburseProductTerm(PresentationDisburseProductTerm presentation) {
    this.presentation = ObjectHelper.checkNotNull(presentation, "presentation");
  }

  @Provides
  @FragmentScopeChild
  PresenterDisburseProductTerm presenter(
    DisbursableProduct product,
    Number.Creator term,
    Api api,
    StringMapper stringMapper,
    AlertManager alertManager,
    TakeoverLoader takeoverLoader
  ) {
    return PresenterDisburseProductTerm.build()
      .product(product)
      .term(term)
      .api(api)
      .stringMapper(stringMapper)
      .alertManager(alertManager)
      .takeoverLoader(takeoverLoader)
      .presentation(this.presentation)
      .build();
  }
}
