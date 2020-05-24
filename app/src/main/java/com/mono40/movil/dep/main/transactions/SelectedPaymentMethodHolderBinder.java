package com.mono40.movil.dep.main.transactions;

import android.content.Context;

import com.mono40.movil.company.CompanyHelper;

/**
 * @author hecvasro
 */
final class SelectedPaymentMethodHolderBinder
  extends BasePaymentMethodHolderBinder<SelectedPaymentMethodHolder> {
  SelectedPaymentMethodHolderBinder(Context context, CompanyHelper companyHelper) {
    super(context, companyHelper);
  }
}
