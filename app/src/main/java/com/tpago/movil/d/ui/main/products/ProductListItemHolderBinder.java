package com.tpago.movil.d.ui.main.products;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.RelativeLayout;

import com.squareup.picasso.Picasso;
import com.tpago.movil.company.Company;
import com.tpago.movil.company.CompanyHelper;
import com.tpago.movil.company.bank.Bank;
import com.tpago.movil.dep.api.DCurrencies;
import com.tpago.movil.d.data.Formatter;
import com.tpago.movil.d.data.StringHelper;
import com.tpago.movil.d.domain.Balance;
import com.tpago.movil.d.domain.Product;
import com.tpago.movil.d.domain.ProductType;
import com.tpago.movil.d.ui.main.list.ListItemHolderBinder;
import com.tpago.movil.dep.Dates;
import com.tpago.movil.util.ObjectHelper;

/**
 * @author hecvasro
 */
class ProductListItemHolderBinder
  implements ListItemHolderBinder<ProductItem, ProductListItemHolder> {

  private final StringHelper stringHelper;
  private final CompanyHelper companyHelper;

  ProductListItemHolderBinder(StringHelper stringHelper, CompanyHelper companyHelper) {
    this.stringHelper = stringHelper;
    this.companyHelper = ObjectHelper.checkNotNull(companyHelper, "companyHelper");
  }

  @Override
  public void bind(@NonNull ProductItem item, @NonNull ProductListItemHolder holder) {
    final Product p = item.getProduct();
    final Bank b = p.getBank();
    final Context c = holder.getContext();
    Picasso.with(c)
      .load(this.companyHelper.getLogoUri(b, Company.LogoStyle.GRAY_36))
      .into(holder.bankLogoImageView);
    holder.productTypeTextView.setText(c.getString(ProductType.findStringId(p)));
    final String productIdentifier;
    if (Product.checkIfCreditCard(p)) {
      productIdentifier = p.getNumberSanitized();
    } else {
      productIdentifier = p.getAlias();
    }
    holder.productIdentifierTextView.setText(productIdentifier);
    final Balance balance = item.getBalance();
    final int productTypeAnchorId;
    final RelativeLayout.LayoutParams productTypeLayoutParams
      = (RelativeLayout.LayoutParams) holder.productTypeTextView.getLayoutParams();
    final int productIdentifierAnchorId;
    final RelativeLayout.LayoutParams productIdentifierLayoutParams
      = (RelativeLayout.LayoutParams) holder.productIdentifierTextView.getLayoutParams();
    if (ObjectHelper.isNull(balance)) {
      holder.productBalanceTextView.setVisibility(View.GONE);
      holder.queryTimeTextView.setVisibility(View.GONE);
      holder.queryBalanceButton.setVisibility(View.VISIBLE);
      productTypeAnchorId = holder.queryBalanceButton.getId();
      productIdentifierAnchorId = holder.queryBalanceButton.getId();
    } else {
      holder.productBalanceTextView.setVisibility(View.VISIBLE);
      holder.productBalanceTextView.setPrefix(DCurrencies.map(p.getCurrency()));
      holder.productBalanceTextView.setContent(Formatter.amount(balance.valueForWalletScreen()));
      productTypeAnchorId = holder.productBalanceTextView.getId();
      holder.queryTimeTextView.setVisibility(View.VISIBLE);
      holder.queryTimeTextView.setText(Dates.createRelativeTimeString(c, item.getQueryTime()));
      productIdentifierAnchorId = holder.queryTimeTextView.getId();
      holder.queryBalanceButton.setVisibility(View.GONE);
    }
    productTypeLayoutParams.addRule(RelativeLayout.START_OF, productTypeAnchorId);
    holder.productTypeTextView.setLayoutParams(productTypeLayoutParams);
    productIdentifierLayoutParams.addRule(RelativeLayout.START_OF, productIdentifierAnchorId);
    holder.productIdentifierTextView.setLayoutParams(productIdentifierLayoutParams);
  }
}
