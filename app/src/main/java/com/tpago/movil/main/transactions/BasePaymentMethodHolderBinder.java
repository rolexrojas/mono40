package com.tpago.movil.main.transactions;

import android.content.Context;

import com.squareup.picasso.Picasso;
import com.tpago.Banks;
import com.tpago.movil.d.data.util.Binder;
import com.tpago.movil.d.domain.Product;
import com.tpago.movil.d.domain.ProductType;
import com.tpago.movil.domain.LogoStyle;
import com.tpago.movil.util.Preconditions;

/**
 * @author hecvasro
 */
abstract class BasePaymentMethodHolderBinder<H extends BasePaymentMethodHolder> implements Binder<Product, H> {
  private final Context context;

  BasePaymentMethodHolderBinder(Context context) {
    this.context = Preconditions.assertNotNull(context, "context == null");
  }

  protected final String getString(int id) {
    return context.getString(id);
  }

  protected String formatIdentifier(Product product) {
    return Banks.getName(product.getBank()) + " " + getString(ProductType.findStringId(product));
  }

  @Override
  public void bind(Product product, H holder) {
    Picasso.with(context)
      .load(product.getBank().getLogoUri(LogoStyle.PRIMARY_24))
      .noFade()
      .into(holder.getBankLogoImageView());
    holder.getProductIdentifierTextView().setText(formatIdentifier(product));
  }
}
