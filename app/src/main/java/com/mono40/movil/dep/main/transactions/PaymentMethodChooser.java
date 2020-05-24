package com.mono40.movil.dep.main.transactions;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.mono40.movil.R;
import com.mono40.movil.app.App;
import com.mono40.movil.company.CompanyHelper;
import com.mono40.movil.d.domain.Product;
import com.mono40.movil.util.ObjectHelper;

import java.util.List;

import javax.inject.Inject;

/**
 * @author hecvasro
 */
public class PaymentMethodChooser
  extends RelativeLayout
  implements PaymentMethodChooserAdapter.OnItemSelectedListener {

  @Inject
  CompanyHelper companyHelper;

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
    App.get(context)
      .component()
      .inject(this);

    LayoutInflater.from(context)
      .inflate(R.layout.widget_payment_method_chooser, this);
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
    paymentMethodHolderBinder = new SelectedPaymentMethodHolderBinder(
      this.getContext(),
      this.companyHelper
    );
    paymentMethodChooserAdapter = new PaymentMethodChooserAdapter(this);
    paymentMethodChooserPopup = new PaymentMethodChooserPopup(
      this,
      this.paymentMethodChooserAdapter,
      this.companyHelper
    );
    setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        if (paymentMethodChooserAdapter.getItemCount() > 1) {
          paymentMethodChooserPopup.show();
        }
      }
    });
  }

  public void onPause() {
    paymentMethodChooserPopup.dismiss();
  }

  @Override
  public void onItemSelectedChanged(Product product) {
    paymentMethodHolderBinder.bind(product, paymentMethodHolder);
    if (ObjectHelper.isNotNull(onPaymentMethodChosenListener)) {
      onPaymentMethodChosenListener.onPaymentMethodChosen(product);
    }
    paymentMethodChooserPopup.dismiss();
  }

  public interface OnPaymentMethodChosenListener {

    void onPaymentMethodChosen(Product product);
  }
}
