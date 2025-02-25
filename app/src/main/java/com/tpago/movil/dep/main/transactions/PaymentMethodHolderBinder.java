package com.tpago.movil.dep.main.transactions;

import android.content.Context;
import androidx.annotation.NonNull;

import com.tpago.movil.company.CompanyHelper;
import com.tpago.movil.d.domain.Product;

/**
 * @author hecvasro
 */
final class PaymentMethodHolderBinder extends BasePaymentMethodHolderBinder<PaymentMethodHolder> {

  PaymentMethodHolderBinder(Context context, CompanyHelper companyHelper) {
    super(context, companyHelper);
  }

  @Override
  public void bind(@NonNull Product product, @NonNull PaymentMethodHolder holder) {
    super.bind(product, holder);
    holder.getProductNumberTextView()
      .setText(product.getNumber()
        .replaceAll("[\\D]", ""));
  }
}
