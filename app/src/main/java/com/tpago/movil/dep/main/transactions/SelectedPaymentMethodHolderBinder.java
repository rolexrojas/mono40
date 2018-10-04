package com.tpago.movil.dep.main.transactions;

import android.content.Context;

import com.tpago.movil.company.CompanyHelper;

/**
 * @author hecvasro
 */
final class SelectedPaymentMethodHolderBinder
  extends BasePaymentMethodHolderBinder<SelectedPaymentMethodHolder> {
  SelectedPaymentMethodHolderBinder(Context context, CompanyHelper companyHelper) {
    super(context, companyHelper);
  }
}
