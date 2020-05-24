package com.mono40.movil.app.ui.main.transaction.disburse.product.term;

import com.mono40.movil.api.Api;
import com.mono40.movil.app.StringMapper;
import com.mono40.movil.app.ui.alert.AlertManager;
import com.mono40.movil.app.ui.fragment.FragmentScopeChild;
import com.mono40.movil.app.ui.loader.takeover.TakeoverLoader;
import com.mono40.movil.product.disbursable.DisbursableProduct;
import com.mono40.movil.util.Number;
import com.mono40.movil.util.ObjectHelper;

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
