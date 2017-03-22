package com.tpago.movil.main.transactions;

import android.content.Context;

import com.squareup.picasso.Picasso;
import com.tpago.movil.Bank;
import com.tpago.movil.api.ApiImageUriBuilder;
import com.tpago.movil.dep.data.util.Binder;
import com.tpago.movil.dep.domain.Product;
import com.tpago.movil.dep.domain.ProductType;
import com.tpago.movil.util.Preconditions;

/**
 * @author hecvasro
 */
abstract class BasePaymentMethodHolderBinder<H extends BasePaymentMethodHolder> implements Binder<Product, H> {
  private final Context context;

  BasePaymentMethodHolderBinder(Context context) {
    this.context = Preconditions.checkNotNull(context, "context == null");
  }

  protected final String getString(int id) {
    return context.getString(id);
  }

  protected String formatIdentifier(Product product) {
    return Bank.getName(product.getBank()) + " " + getString(ProductType.findStringId(product));
  }

  @Override
  public void bind(Product product, H holder) {
    Picasso.with(context)
      .load(ApiImageUriBuilder.build(context, product.getBank(), ApiImageUriBuilder.Style.PRIMARY_24))
      .noFade()
      .into(holder.getBankLogoImageView());
    holder.getProductIdentifierTextView().setText(formatIdentifier(product));
  }
}
