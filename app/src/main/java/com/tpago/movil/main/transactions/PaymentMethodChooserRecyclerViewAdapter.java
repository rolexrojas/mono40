package com.tpago.movil.main.transactions;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.tpago.movil.R;
import com.tpago.movil.dep.domain.Product;
import com.tpago.movil.util.Preconditions;

/**
 * @author hecvasro
 */
final class PaymentMethodChooserRecyclerViewAdapter
  extends RecyclerView.Adapter<BasePaymentMethodRecyclerViewHolder<? extends BasePaymentMethodHolder>>
  implements PaymentMethodChooserAdapter.Observer,
    BasePaymentMethodRecyclerViewHolder.OnPaymentMethodViewHolderClickedListener {
  private static final int TYPE_ITEM = 0;
  private static final int TYPE_ITEM_SELECTED = 1;

  private final PaymentMethodChooserAdapter paymentMethodChooserAdapter;

  private final PaymentMethodHolderBinder paymentMethodHolderBinder;
  private final SelectedPaymentMethodHolderBinder selectedPaymentMethodHolderBinder;

  PaymentMethodChooserRecyclerViewAdapter(
    Context context,
    PaymentMethodChooserAdapter paymentMethodChooserAdapter) {
    Preconditions.checkNotNull(context, "context == null");
    this.paymentMethodHolderBinder = new PaymentMethodHolderBinder(context);
    this.selectedPaymentMethodHolderBinder = new SelectedPaymentMethodHolderBinder(context);
    Preconditions.checkNotNull(paymentMethodChooserAdapter, "paymentMethodChooserAdapter == null");
    this.paymentMethodChooserAdapter = paymentMethodChooserAdapter;
    this.paymentMethodChooserAdapter.setObserver(this);
  }

  @Override
  public BasePaymentMethodRecyclerViewHolder<? extends BasePaymentMethodHolder> onCreateViewHolder(
    ViewGroup parent,
    int viewType) {
    final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
    if (viewType == TYPE_ITEM) {
      return new PaymentMethodRecyclerViewHolder(
        inflater.inflate(R.layout.widget_payment_method_chooser_popup_item, parent, false),
        this);
    } else {
      return new SelectedPaymentMethodRecyclerViewHolder(
        inflater.inflate(R.layout.widget_payment_method_chooser_popup_item_selected, parent, false),
        this);
    }
  }

  @Override
  public void onBindViewHolder(
    BasePaymentMethodRecyclerViewHolder<? extends BasePaymentMethodHolder> holder,
    int position) {
    final Product item = paymentMethodChooserAdapter.getItem(position);
    if (getItemViewType(position) == TYPE_ITEM) {
      paymentMethodHolderBinder.bind(
        item,
        ((PaymentMethodRecyclerViewHolder) holder).internalHolder);
    } else {
      selectedPaymentMethodHolderBinder.bind(
        item,
        ((SelectedPaymentMethodRecyclerViewHolder) holder).internalHolder);
    }
  }

  @Override
  public int getItemViewType(int position) {
    return position == 0 ? TYPE_ITEM_SELECTED : TYPE_ITEM;
  }

  @Override
  public int getItemCount() {
    return paymentMethodChooserAdapter.getItemCount();
  }

  @Override
  public void onDataSetChanged() {
    notifyDataSetChanged();
  }

  @Override
  public void onPaymentMethodViewHolderClicked(int position) {
    paymentMethodChooserAdapter.setSelectedItem(position);
  }
}
