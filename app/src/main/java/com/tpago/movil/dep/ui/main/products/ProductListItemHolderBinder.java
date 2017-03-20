package com.tpago.movil.dep.ui.main.products;

import android.support.annotation.NonNull;
import android.view.View;

import com.tpago.movil.dep.data.Formatter;
import com.tpago.movil.dep.data.StringHelper;
import com.tpago.movil.dep.data.res.DepAssetProvider;
import com.tpago.movil.dep.domain.Balance;
import com.tpago.movil.Bank;
import com.tpago.movil.dep.domain.Product;
import com.tpago.movil.dep.ui.main.list.ListItemHolderBinder;
import com.squareup.picasso.Picasso;
import com.tpago.movil.text.Texts;

/**
 * @author hecvasro
 */
class ProductListItemHolderBinder implements ListItemHolderBinder<ProductItem, ProductListItemHolder> {
  private final StringHelper stringHelper;
  private final DepAssetProvider assetProvider;

  ProductListItemHolderBinder(
    @NonNull StringHelper stringHelper,
    @NonNull DepAssetProvider assetProvider) {
    this.stringHelper = stringHelper;
    this.assetProvider = assetProvider;
  }

  @Override
  public void bind(@NonNull ProductItem item, @NonNull ProductListItemHolder holder) {
    final Product p = item.getProduct();
    final Bank b = p.getBank();
    Picasso.with(holder.getContext())
      .load(assetProvider.getLogoUri(b, DepAssetProvider.STYLE_36_GRAY))
      .noFade()
      .into(holder.bankLogoImageView);
    holder.bankNameTextView.setText(Texts.join(" ", Bank.getName(b), stringHelper.productTypeName(p)));
    final String productIdentifier;
    if (Product.checkIfCreditCard(p)) {
      productIdentifier = stringHelper.maskedProductNumber(p);
    } else {
      productIdentifier = p.getAlias();
    }
    holder.productIdentifierTextView.setText(productIdentifier);
    final Balance balance = item.getBalance();
    if (balance != null) {
      holder.productBalanceTextView.setVisibility(View.VISIBLE);
      holder.productBalanceTextView.setPrefix(p.getCurrency());
      holder.productBalanceTextView.setContent(Formatter.amount(balance.getValue()));
      holder.queryBalanceButton.setVisibility(View.GONE);
    } else {
      holder.productBalanceTextView.setVisibility(View.GONE);
      holder.queryBalanceButton.setVisibility(View.VISIBLE);
    }
  }
}
