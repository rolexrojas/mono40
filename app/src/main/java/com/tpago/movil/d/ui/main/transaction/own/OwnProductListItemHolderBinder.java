package com.tpago.movil.d.ui.main.transaction.own;

import android.content.Context;
import android.support.annotation.NonNull;

import com.squareup.picasso.Picasso;
import com.tpago.movil.R;
import com.tpago.movil.d.data.StringHelper;
import com.tpago.movil.d.domain.Product;
import com.tpago.movil.d.domain.ProductType;
import com.tpago.movil.d.ui.main.list.ListItemHolderBinder;
import com.tpago.movil.d.domain.Bank;
import com.tpago.movil.d.domain.LogoStyle;
import com.tpago.movil.util.ObjectHelper;

/**
 * @author hecvasro
 */
class OwnProductListItemHolderBinder
  implements ListItemHolderBinder<Product, OwnProductListItemHolder> {

  private final StringHelper stringHelper;

  OwnProductListItemHolderBinder(StringHelper stringHelper) {
    this.stringHelper = ObjectHelper.checkNotNull(stringHelper, "stringHelper");
  }

  @Override
  public void bind(@NonNull Product item, @NonNull OwnProductListItemHolder holder) {
    final Context c = holder.getContext();

    final Bank b = item.getBank();
    Picasso.with(c)
      .load(b.getLogoUri(LogoStyle.GRAY_36))
      .into(holder.bankLogoImageView);
    holder.productTypeTextView.setText(c.getString(ProductType.findStringId(item)));
    final String productIdentifier;
    final String buttonText;
    if (Product.checkIfCreditCard(item)) {
      productIdentifier = stringHelper.maskedProductNumber(item);
      buttonText = stringHelper.resolve(R.string.forward);
    } else {
      productIdentifier = item.getAlias();
      buttonText = stringHelper.resolve(R.string.transfer);
    }
    holder.productIdentifierTextView.setText(productIdentifier);
    holder.button.setText(buttonText);
  }
}
