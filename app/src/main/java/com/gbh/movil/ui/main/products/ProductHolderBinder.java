package com.gbh.movil.ui.main.products;

import android.support.annotation.NonNull;
import android.view.View;

import com.gbh.movil.data.res.ResourceProvider;
import com.gbh.movil.domain.Balance;
import com.gbh.movil.domain.Bank;
import com.gbh.movil.domain.Product;
import com.gbh.movil.ui.main.list.HolderBinder;
import com.squareup.picasso.Picasso;

/**
 * TODO
 *
 * @author hecvasro
 */
class ProductHolderBinder implements HolderBinder<ProductItem, ProductHolder> {
  private final ResourceProvider resourceProvider;

  /**
   * TODO
   */
  ProductHolderBinder(@NonNull ResourceProvider resourceProvider) {
    this.resourceProvider = resourceProvider;
  }

  @Override
  public void bind(@NonNull ProductItem item, @NonNull ProductHolder holder) {
    final Product product = item.getProduct();
    final Bank bank = product.getBank();
    // TODO: Load bank's logo.
    Picasso.with(holder.getContext())
      .load(resourceProvider.getLogoUri(bank, ResourceProvider.STYLE_36_GRAY))
      .into(holder.bankLogoImageView);
    holder.productAliasTextView.setText(product.getAlias());
    holder.bankNameTextView.setText(bank.getName());
    final Balance balance = item.getBalance();
    if (balance != null) {
      holder.productBalanceAmountView.setVisibility(View.VISIBLE);
      holder.productBalanceAmountView.setCurrency(product.getCurrency());
      holder.productBalanceAmountView.setValue(balance.getValue());
      holder.queryActionButton.setVisibility(View.GONE);
    } else {
      holder.productBalanceAmountView.setVisibility(View.GONE);
      holder.queryActionButton.setVisibility(View.VISIBLE);
    }
  }
}
