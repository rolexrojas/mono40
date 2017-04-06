package com.tpago.movil.d.ui.main.products;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import com.squareup.picasso.Picasso;
import com.tpago.movil.d.data.Formatter;
import com.tpago.movil.d.data.StringHelper;
import com.tpago.movil.d.domain.Balance;
import com.tpago.movil.domain.Bank;
import com.tpago.movil.d.domain.Product;
import com.tpago.movil.d.domain.ProductType;
import com.tpago.movil.d.ui.main.list.ListItemHolderBinder;
import com.tpago.movil.domain.LogoStyle;

import static com.tpago.movil.util.Preconditions.assertNotNull;

/**
 * @author hecvasro
 */
class ProductListItemHolderBinder implements ListItemHolderBinder<ProductItem, ProductListItemHolder> {
  private final StringHelper stringHelper;

  ProductListItemHolderBinder(StringHelper stringHelper) {
    this.stringHelper = assertNotNull(stringHelper, "stringHelper == null");
  }

  @Override
  public void bind(@NonNull ProductItem item, @NonNull ProductListItemHolder holder) {
    final Product p = item.getProduct();
    final Bank b = p.getBank();
    final Context c = holder.getContext();
    Picasso.with(c)
      .load(b.getLogoUri(LogoStyle.GRAY_36))
      .into(holder.bankLogoImageView);
    holder.bankNameTextView.setText(c.getString(ProductType.findStringId(p)));
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
