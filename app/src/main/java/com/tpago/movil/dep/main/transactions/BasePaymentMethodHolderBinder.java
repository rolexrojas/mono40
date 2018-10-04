package com.tpago.movil.dep.main.transactions;

import android.content.Context;

import com.squareup.picasso.Picasso;
import com.tpago.movil.company.Company;
import com.tpago.movil.company.CompanyHelper;
import com.tpago.movil.d.data.util.Binder;
import com.tpago.movil.d.domain.Product;
import com.tpago.movil.d.domain.ProductType;
import com.tpago.movil.util.ObjectHelper;

/**
 * @author hecvasro
 */
abstract class BasePaymentMethodHolderBinder<H extends BasePaymentMethodHolder>
  implements Binder<Product, H> {

  private final Context context;
  private final CompanyHelper companyHelper;

  BasePaymentMethodHolderBinder(Context context, CompanyHelper companyHelper) {
    this.context = ObjectHelper.checkNotNull(context, "context");
    this.companyHelper = ObjectHelper.checkNotNull(companyHelper, "companyHelper");
  }

  protected final String getString(int id) {
    return context.getString(id);
  }

  protected String formatIdentifier(Product product) {
    return this.getString(ProductType.findStringId(product)) + " " + product.getNumberSanitized();
  }

  @Override
  public void bind(Product product, H holder) {
    Picasso.with(this.context)
      .load(this.companyHelper.getLogoUri(product.getBank(), Company.LogoStyle.COLORED_24))
      .noFade()
      .into(holder.getBankLogoImageView());
    holder.getProductIdentifierTextView()
      .setText(formatIdentifier(product));
  }
}
