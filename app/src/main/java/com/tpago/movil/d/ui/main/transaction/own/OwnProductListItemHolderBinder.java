package com.tpago.movil.d.ui.main.transaction.own;

import android.content.Context;
import androidx.annotation.NonNull;

import com.squareup.picasso.Picasso;
import com.tpago.movil.R;
import com.tpago.movil.company.Company;
import com.tpago.movil.company.CompanyHelper;
import com.tpago.movil.company.bank.Bank;
import com.tpago.movil.d.data.StringHelper;
import com.tpago.movil.d.domain.Product;
import com.tpago.movil.d.domain.ProductType;
import com.tpago.movil.d.ui.main.list.ListItemHolderBinder;
import com.tpago.movil.util.ObjectHelper;

/**
 * @author hecvasro
 */
class OwnProductListItemHolderBinder
  implements ListItemHolderBinder<Product, OwnProductListItemHolder> {

  private final StringHelper stringHelper;
  private final CompanyHelper companyHelper;

  OwnProductListItemHolderBinder(StringHelper stringHelper, CompanyHelper companyHelper) {
    this.stringHelper = ObjectHelper.checkNotNull(stringHelper, "stringHelper");
    this.companyHelper = ObjectHelper.checkNotNull(companyHelper, "companyHelper");
  }

  @Override
  public void bind(@NonNull Product item, @NonNull OwnProductListItemHolder holder) {
    final Context c = holder.getContext();

    final Bank b = item.getBank();
    Picasso.get()
      .load(this.companyHelper.getLogoUri(b, Company.LogoStyle.WHITE_36))
      .into(holder.bankLogoImageView);
    holder.productTypeTextView.setText(c.getString(ProductType.findStringId(item)));
    final String productIdentifier;
    final String buttonText;
    if (Product.checkIfCreditCard(item)) {
      productIdentifier = item.getNumberSanitized();
      buttonText = stringHelper.resolve(R.string.forward);
    } else {
      productIdentifier = item.getAlias();
      buttonText = stringHelper.resolve(R.string.transfer);
    }
    holder.productIdentifierTextView.setText(productIdentifier);
    holder.button.setText(buttonText);
  }
}
