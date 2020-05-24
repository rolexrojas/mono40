package com.mono40.movil.app.ui.main.transaction.disburse.product;

import com.google.auto.value.AutoValue;
import com.mono40.movil.app.ui.activity.toolbar.ActivityToolbar;
import com.mono40.movil.app.ui.activity.toolbar.FragmentToolbarRetained;
import com.mono40.movil.app.ui.fragment.base.FragmentBase;
import com.mono40.movil.app.ui.main.transaction.disburse.product.amount.FragmentDisburseProductAmount;
import com.mono40.movil.product.disbursable.DisbursableProduct;

/**
 * @author hecvasro
 */
@AutoValue
public abstract class ArgumentDisburseProduct extends ActivityToolbar.Argument {

  public static Builder builder() {
    return new AutoValue_ArgumentDisburseProduct.Builder();
  }

  ArgumentDisburseProduct() {
  }

  public abstract DisbursableProduct product();

  @Override
  public FragmentToolbarRetained createFragmentRetained() {
    return FragmentDisburseProduct.create(this.product());
  }

  @Override
  public FragmentBase createFragment() {
    return FragmentDisburseProductAmount.create();
  }

  @AutoValue.Builder
  public static abstract class Builder {

    Builder() {
    }

    public abstract Builder product(DisbursableProduct product);

    public abstract ArgumentDisburseProduct build();
  }
}
