package com.gbh.movil.ui.main.products;

import android.support.annotation.NonNull;
import android.view.View;

import com.gbh.movil.domain.Balance;
import com.gbh.movil.domain.Bank;
import com.gbh.movil.domain.Product;
import com.gbh.movil.ui.main.list.ItemHolderBinder;

/**
 * TODO
 *
 * @author hecvasro
 */
class ProductItemHolderBinder implements ItemHolderBinder<ProductItem, ProductItemHolder> {
  /**
   * TODO
   */
  ProductItemHolderBinder() {
  }

  @Override
  public void bind(@NonNull ProductItem item, @NonNull ProductItemHolder holder) {
    final Product product = item.getProduct();
    final Bank bank = product.getBank();
    // TODO: Load bank's logo.
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
