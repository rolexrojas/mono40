package com.mono40.movil.app.ui.main.transaction.disburse.product;

import android.os.Bundle;
import androidx.annotation.Nullable;

import com.mono40.movil.R;
import com.mono40.movil.app.di.ComponentBuilderSupplier;
import com.mono40.movil.app.di.ComponentBuilderSupplierContainer;
import com.mono40.movil.app.ui.activity.toolbar.ActivityToolbar;
import com.mono40.movil.app.ui.activity.toolbar.FragmentToolbarRetained;
import com.mono40.movil.product.disbursable.DisbursableProduct;
import com.mono40.movil.util.ObjectHelper;

import javax.inject.Inject;

/**
 * @author hecvasro
 */
public final class FragmentDisburseProduct extends FragmentToolbarRetained
  implements ComponentBuilderSupplierContainer, PresentationDisburseProduct {

  private static final String KEY_PRODUCT = "product";

  static FragmentDisburseProduct create(DisbursableProduct product) {
    final Bundle arguments = new Bundle();
    arguments.putParcelable(KEY_PRODUCT, product);
    final FragmentDisburseProduct fragment = new FragmentDisburseProduct();
    fragment.setArguments(arguments);
    return fragment;
  }

  private FragmentComponentDisburseProduct component;

  @Inject PresenterDisburseProduct presenter;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Initializes the argument of the fragment.
    final Bundle arguments = ObjectHelper.checkNotNull(this.getArguments(), "arguments");
    if (!arguments.containsKey(KEY_PRODUCT)) {
      throw new IllegalArgumentException("!arguments.containsKey(KEY_PRODUCT)");
    }
    final DisbursableProduct product = ObjectHelper
      .checkNotNull(arguments.getParcelable(KEY_PRODUCT), "product");

    // Instantiates the dependency injector.
    final FragmentModuleDisburseProduct disburseProductModule = FragmentModuleDisburseProduct
      .builder()
      .product(product)
      .presentation(this)
      .build();
    this.component = ActivityToolbar.get(this.getContext())
      .componentBuilderSupplier()
      .get(FragmentDisburseProduct.class, FragmentComponentDisburseProduct.Builder.class)
      .disburseProductModule(disburseProductModule)
      .build();

    // Injects all annotated dependencies.
    this.component.inject(this);
  }

  @Override
  public void onStart() {
    super.onStart();

    // Sets the title of the fragment.
    ActivityToolbar.get(this.getContext())
      .toolbarManager()
      .setTitleText(R.string.disburse);
  }

  @Override
  public void onResume() {
    super.onResume();

    // Resumes the presenter.
    this.presenter.onPresentationResumed();
  }

  @Override
  public void onPause() {
    // Pauses the presenter.
    this.presenter.onPresentationPaused();

    super.onPause();
  }

  @Override
  public ComponentBuilderSupplier componentBuilderSupplier() {
    return this.componentBuilderSupplier;
  }

  @Override
  public void setProductTypeAndNumber(String text) {
    ActivityToolbar.get(this.getContext())
      .toolbarManager()
      .setSubtitleText(text);
  }
}
