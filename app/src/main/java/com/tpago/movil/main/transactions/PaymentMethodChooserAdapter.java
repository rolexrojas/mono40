package com.tpago.movil.main.transactions;

import com.tpago.movil.d.domain.Product;
import com.tpago.movil.util.Objects;
import com.tpago.movil.util.Preconditions;

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
    this.onItemSelectedListener = Preconditions
      .checkNotNull(onItemSelectedListener, "onItemSelectedChangedListener == null");
  }

  final void setObserver(Observer observer) {
    this.observer = observer;
  }

  final void setItemList(List<Product> paymentMethodList) {
    Preconditions.checkNotNull(paymentMethodList, "paymentMethodLister == null");
    this.selectedItemPreviousPosition = POSITION_INVALID;
    if (Objects.isNull(this.paymentMethodList)) {
      this.paymentMethodList = new ArrayList<>();
    } else {
      this.paymentMethodList.clear();
    }
    this.paymentMethodList.addAll(paymentMethodList);
    if (Objects.isNotNull(this.observer)) {
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
    if (Objects.isNotNull(observer)) {
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
