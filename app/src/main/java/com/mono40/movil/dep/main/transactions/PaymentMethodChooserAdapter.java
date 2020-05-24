package com.mono40.movil.dep.main.transactions;

import com.mono40.movil.d.domain.Product;
import com.mono40.movil.util.ObjectHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hecvasro
 */
final class PaymentMethodChooserAdapter {

  private static final int POSITION_INVALID = -1;
  private static final int POSITION_SELECTED = 0;

  private final OnItemSelectedListener onItemSelectedListener;

  private List<Product> paymentMethodList;

  private int selectedItemPreviousPosition = POSITION_INVALID;

  private Observer observer;

  PaymentMethodChooserAdapter(OnItemSelectedListener onItemSelectedListener) {
    this.onItemSelectedListener = ObjectHelper
      .checkNotNull(onItemSelectedListener, "onItemSelectedChangedListener");
  }

  final void setObserver(Observer observer) {
    this.observer = observer;
  }

  final void setItemList(List<Product> paymentMethodList) {
    ObjectHelper.checkNotNull(paymentMethodList, "paymentMethodLister");
    this.selectedItemPreviousPosition = POSITION_INVALID;
    if (ObjectHelper.isNull(this.paymentMethodList)) {
      this.paymentMethodList = new ArrayList<>();
    } else {
      this.paymentMethodList.clear();
    }
    this.paymentMethodList.addAll(paymentMethodList);
    if (ObjectHelper.isNotNull(this.observer)) {
      this.observer.onDataSetChanged();
    }
    this.onItemSelectedListener.onItemSelectedChanged(this.paymentMethodList.get(POSITION_SELECTED));
  }

  final int getItemCount() {
    return paymentMethodList.size();
  }

  final Product getItem(int position) {
    return paymentMethodList.get(position);
  }

  final Product getSelectedItem() {
    return paymentMethodList.get(POSITION_SELECTED);
  }

  void setSelectedItem(int position) {
    final List<Product> paymentMethodListCopy = new ArrayList<>(paymentMethodList);
    if (selectedItemPreviousPosition != POSITION_INVALID) {
      final Product pm = paymentMethodListCopy.get(POSITION_SELECTED);
      paymentMethodList.remove(pm);
      paymentMethodList.add(selectedItemPreviousPosition, pm);
      selectedItemPreviousPosition = POSITION_INVALID;
    }
    selectedItemPreviousPosition = position;
    final Product pm = paymentMethodListCopy.get(selectedItemPreviousPosition);
    paymentMethodList.remove(pm);
    paymentMethodList.add(POSITION_SELECTED, pm);
    if (ObjectHelper.isNotNull(observer)) {
      observer.onDataSetChanged();
    }
    onItemSelectedListener.onItemSelectedChanged(pm);
  }

  interface Observer {

    void onDataSetChanged();
  }

  interface OnItemSelectedListener {

    void onItemSelectedChanged(Product product);
  }
}
