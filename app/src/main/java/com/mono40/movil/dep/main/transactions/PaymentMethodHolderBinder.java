package com.mono40.movil.dep.main.transactions;

import android.content.Context;
import androidx.annotation.NonNull;

import com.mono40.movil.company.CompanyHelper;
import com.mono40.movil.d.domain.Product;

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
