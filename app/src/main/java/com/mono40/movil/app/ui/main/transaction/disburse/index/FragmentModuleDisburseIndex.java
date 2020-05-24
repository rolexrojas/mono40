package com.mono40.movil.app.ui.main.transaction.disburse.index;

import com.mono40.movil.app.ui.fragment.FragmentScope;
import com.mono40.movil.app.ui.alert.AlertManager;
import com.mono40.movil.company.CompanyHelper;
import com.mono40.movil.app.StringMapper;
import com.mono40.movil.product.ProductHelper;
import com.mono40.movil.product.ProductStore;
import com.mono40.movil.product.disbursable.DisbursableProductHelper;
import com.mono40.movil.product.disbursable.DisbursableProductStore;
import com.mono40.movil.util.ObjectHelper;

import java.util.Set;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoSet;

/**
 * @author hecvasro
 */
@Module
public final class FragmentModuleDisburseIndex {

  static FragmentModuleDisburseIndex create(PresentationDisburseIndex presentation) {
    return new FragmentModuleDisburseIndex(presentation);
  }

  private final PresentationDisburseIndex presentation;

  private FragmentModuleDisburseIndex(PresentationDisburseIndex presentation) {
    this.presentation = ObjectHelper.checkNotNull(presentation, "presentation");
  }

  @Provides
  @FragmentScope
  @IntoSet
  DisburseItemsSupplier productsSupplier(
    AlertManager alertManager,
    StringMapper stringMapper,
    CompanyHelper companyHelper,
    ProductHelper productHelper,
    ProductStore productStore
  ) {
    return DisburseItemsSupplierCreditCards.builder()
      .stringMapper(stringMapper)
      .alertManager(alertManager)
      .companyHelper(companyHelper)
      .productHelper(productHelper)
      .productStore(productStore)
      .actionConsumer(this.presentation::startProductTransaction)
      .build();
  }

  @Provides
  @FragmentScope
  @IntoSet
  DisburseItemsSupplier disbursableProductsSupplier(
    StringMapper stringMapper,
    CompanyHelper companyHelper,
    DisbursableProductHelper productHelper,
    DisbursableProductStore productStore
  ) {
    return DisburseItemsSupplierProducts.builder()
      .stringMapper(stringMapper)
      .companyHelper(companyHelper)
      .productHelper(productHelper)
      .productStore(productStore)
      .actionConsumer(this.presentation::startDisbursableProductTransaction)
      .build();
  }

  @Provides
  @FragmentScope
  PresenterDisburseIndex presenter(
    AlertManager alertManager,
    Set<DisburseItemsSupplier> itemsSuppliers
  ) {
    return PresenterDisburseIndex.builder()
      .alertManager(alertManager)
      .itemsSupplier(DisburseItemsSupplierImpl.create(itemsSuppliers))
      .presentation(this.presentation)
      .build();
  }
}
