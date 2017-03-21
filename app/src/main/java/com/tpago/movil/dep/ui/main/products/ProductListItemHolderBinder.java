package com.tpago.movil.dep.ui.main.products;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import com.tpago.movil.api.ApiImageUriBuilder;
import com.tpago.movil.dep.data.Formatter;
import com.tpago.movil.dep.data.StringHelper;
import com.tpago.movil.dep.domain.Balance;
import com.tpago.movil.Bank;
import com.tpago.movil.dep.domain.Product;
import com.tpago.movil.dep.domain.ProductType;
import com.tpago.movil.dep.ui.main.list.ListItemHolderBinder;
import com.squareup.picasso.Picasso;

/**
 * @author hecvasro
 */
class ProductListItemHolderBinder implements ListItemHolderBinder<ProductItem, ProductListItemHolder> {
  private final StringHelper stringHelper;

  ProductListItemHolderBinder(StringHelper stringHelper) {
    this.stringHelper = stringHelper;
  }

  @Override
  public void bind(@NonNull ProductItem item, @NonNull ProductListItemHolder holder) {
    final Product p = item.getProduct();
    final Bank b = p.getBank();
    final Context c = holder.getContext();
    Picasso.with(c)
      .load(ApiImageUriBuilder.build(c, b, ApiImageUriBuilder.Style.GRAY_36))
      .noFade()
      .into(holder.bankLogoImageView);
    holder.bankNameTextView.setText(Bank.getName(b) + " " + c.getString(ProductType.findStringId(p)));
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
