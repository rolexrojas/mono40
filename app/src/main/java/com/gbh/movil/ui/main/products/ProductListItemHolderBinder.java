package com.gbh.movil.ui.main.products;

import android.support.annotation.NonNull;
import android.view.View;

import com.gbh.movil.data.Formatter;
import com.gbh.movil.data.StringHelper;
import com.gbh.movil.data.res.AssetProvider;
import com.gbh.movil.domain.Balance;
import com.gbh.movil.domain.Bank;
import com.gbh.movil.domain.Product;
import com.gbh.movil.ui.main.list.ListItemHolderBinder;
import com.squareup.picasso.Picasso;

/**
 * TODO
 *
 * @author hecvasro
 */
class ProductListItemHolderBinder implements ListItemHolderBinder<ProductItem, ProductListItemHolder> {
  private final StringHelper stringHelper;
  private final AssetProvider assetProvider;

  /**
   * TODO
   */
  ProductListItemHolderBinder(@NonNull StringHelper stringHelper,
    @NonNull AssetProvider assetProvider) {
    this.stringHelper = stringHelper;
    this.assetProvider = assetProvider;
  }

  @Override
  public void bind(@NonNull ProductItem item, @NonNull ProductListItemHolder holder) {
    final Product product = item.getProduct();
    final Bank bank = product.getBank();
    Picasso.with(holder.getContext())
      .load(assetProvider.getLogoUri(bank, AssetProvider.STYLE_36_GRAY))
      .into(holder.bankLogoImageView);
    holder.productNumberTextView.setText(stringHelper.productNumber(product));
    holder.bankNameTextView.setText(bank.getName());
    final Balance balance = item.getBalance();
    if (balance != null) {
      holder.productBalanceTextView.setVisibility(View.VISIBLE);
      holder.productBalanceTextView.setPrefix(product.getCurrency());
      holder.productBalanceTextView.setContent(Formatter.amount(balance.getValue()));
      holder.queryActionButton.setVisibility(View.GONE);
    } else {
      holder.productBalanceTextView.setVisibility(View.GONE);
      holder.queryActionButton.setVisibility(View.VISIBLE);
    }
  }
}
