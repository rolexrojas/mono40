package com.mono40.movil.dep.main.transactions;

import android.content.Context;

import com.squareup.picasso.Picasso;
import com.mono40.movil.company.Company;
import com.mono40.movil.company.CompanyHelper;
import com.mono40.movil.d.data.util.Binder;
import com.mono40.movil.d.domain.Product;
import com.mono40.movil.d.domain.ProductType;
import com.mono40.movil.util.ObjectHelper;

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
    Picasso.get()
      .load(this.companyHelper.getLogoUri(product.getBank(), Company.LogoStyle.COLORED_24))
      .noFade()
      .into(holder.getBankLogoImageView());
    holder.getProductIdentifierTextView()
      .setText(formatIdentifier(product));
  }
}
