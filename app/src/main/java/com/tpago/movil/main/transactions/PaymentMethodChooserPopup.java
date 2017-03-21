package com.tpago.movil.main.transactions;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.tpago.movil.R;
import com.tpago.movil.util.Objects;
import com.tpago.movil.util.Preconditions;

/**
 * @author hecvasro
 */
final class PaymentMethodChooserPopup {
  private final View parentView;
  private final PaymentMethodChooserAdapter paymentMethodChooserAdapter;

  private PopupWindow popupWindow;

  PaymentMethodChooserPopup(
    View parentView,
    PaymentMethodChooserAdapter paymentMethodChooserAdapter) {
    this.parentView = Preconditions.checkNotNull(parentView, "parentView == null");
    this.paymentMethodChooserAdapter = Preconditions
      .checkNotNull(paymentMethodChooserAdapter, "paymentMethodChooserAdapter == null");
  }

  final void show() {
    if (Objects.isNull(popupWindow)) {
      final Context context = parentView.getContext();
      final RecyclerView recyclerView = (RecyclerView) LayoutInflater
        .from(context)
        .inflate(R.layout.payment_method_chooser_popup, null);
      recyclerView.setLayoutManager(new LinearLayoutManager(
        context,
        LinearLayoutManager.VERTICAL,
        false));
      recyclerView.setAdapter(new PaymentMethodChooserRecyclerViewAdapter(
        context,
        paymentMethodChooserAdapter));
      popupWindow = new PopupWindow(
        recyclerView,
        parentView.getWidth(),
        WindowManager.LayoutParams.WRAP_CONTENT);
    }
    popupWindow.showAsDropDown(parentView, 0, -parentView.getHeight());
  }

  final void dismiss() {
    if (Objects.isNotNull(popupWindow)) {
      popupWindow.dismiss();
    }
  }
}
