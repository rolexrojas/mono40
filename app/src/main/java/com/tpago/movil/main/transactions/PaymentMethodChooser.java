package com.tpago.movil.main.transactions;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.tpago.movil.R;
import com.tpago.movil.d.domain.Product;
import com.tpago.movil.util.Objects;

import java.util.List;

/**
 * @author hecvasro
 */
public class PaymentMethodChooser
  extends RelativeLayout
  implements PaymentMethodChooserAdapter.OnItemSelectedListener {
  private PaymentMethodChooserAdapter paymentMethodChooserAdapter;
  private PaymentMethodChooserPopup paymentMethodChooserPopup;

  private SelectedPaymentMethodHolder paymentMethodHolder;
  private SelectedPaymentMethodHolderBinder paymentMethodHolderBinder;

  private OnPaymentMethodChosenListener onPaymentMethodChosenListener;

  public PaymentMethodChooser(Context context) {
    super(context);
    initializePaymentMethodChooser(context);
  }

  public PaymentMethodChooser(Context context, AttributeSet attrs) {
    super(context, attrs);
    initializePaymentMethodChooser(context);
  }

  public PaymentMethodChooser(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    initializePaymentMethodChooser(context);
  }

  private void initializePaymentMethodChooser(Context context) {
    LayoutInflater.from(context).inflate(R.layout.widget_payment_method_chooser, this);
  }

  public final void setPaymentMethodList(List<Product> paymentMethodList) {
    paymentMethodChooserAdapter.setItemList(paymentMethodList);
  }

  public final Product getSelectedItem() {
    return paymentMethodChooserAdapter.getSelectedItem();
  }

  public final void setOnPaymentMethodChosenListener(OnPaymentMethodChosenListener listener) {
    onPaymentMethodChosenListener = listener;
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();
    paymentMethodHolder = new SelectedPaymentMethodHolder(this);
    paymentMethodHolderBinder = new SelectedPaymentMethodHolderBinder(getContext());
    paymentMethodChooserAdapter = new PaymentMethodChooserAdapter(this);
    paymentMethodChooserPopup = new PaymentMethodChooserPopup(this, paymentMethodChooserAdapter);
    setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        if (paymentMethodChooserAdapter.getItemCount() > 1) {
          paymentMethodChooserPopup.show();
        }
      }
    });
  }

  @Override
  public void onItemSelectedChanged(Product product) {
    paymentMethodHolderBinder.bind(product, paymentMethodHolder);
    if (Objects.isNotNull(onPaymentMethodChosenListener)) {
      onPaymentMethodChosenListener.onPaymentMethodChosen(product);
    }
    paymentMethodChooserPopup.dismiss();
  }

  public interface OnPaymentMethodChosenListener {
    void onPaymentMethodChosen(Product product);
  }
}
