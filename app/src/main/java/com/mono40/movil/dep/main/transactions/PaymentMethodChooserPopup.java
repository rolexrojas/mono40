package com.mono40.movil.dep.main.transactions;

import android.content.Context;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.mono40.movil.R;
import com.mono40.movil.company.CompanyHelper;
import com.mono40.movil.util.ObjectHelper;

/**
 * @author hecvasro
 */
final class PaymentMethodChooserPopup {

  private final View parentView;
  private final PaymentMethodChooserAdapter paymentMethodChooserAdapter;
  private final CompanyHelper companyHelper;

  private PopupWindow popupWindow;

  PaymentMethodChooserPopup(
    View parentView,
    PaymentMethodChooserAdapter paymentMethodChooserAdapter,
    CompanyHelper companyHelper
  ) {
    this.parentView = ObjectHelper.checkNotNull(parentView, "parentView");
    this.paymentMethodChooserAdapter = ObjectHelper
      .checkNotNull(paymentMethodChooserAdapter, "paymentMethodChooserAdapter");
    this.companyHelper = ObjectHelper.checkNotNull(companyHelper, "companyHelper");
  }

  final void show() {
    if (ObjectHelper.isNull(popupWindow)) {
      final Context context = parentView.getContext();
      final RecyclerView recyclerView = (RecyclerView) LayoutInflater
        .from(context)
        .inflate(R.layout.widget_payment_method_chooser_popup, null);
      recyclerView.setLayoutManager(new LinearLayoutManager(
        context,
        LinearLayoutManager.VERTICAL,
        false
      ));
      recyclerView.setAdapter(new PaymentMethodChooserRecyclerViewAdapter(
        context,
        paymentMethodChooserAdapter,
        this.companyHelper
      ));
      popupWindow = new PopupWindow(
        recyclerView,
        parentView.getWidth(),
        WindowManager.LayoutParams.WRAP_CONTENT
      );
    }
    popupWindow.showAsDropDown(parentView, 0, -parentView.getHeight());
  }

  final void dismiss() {
    if (ObjectHelper.isNotNull(popupWindow)) {
      popupWindow.dismiss();
    }
  }
}
