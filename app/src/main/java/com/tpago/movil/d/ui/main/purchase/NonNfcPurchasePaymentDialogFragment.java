package com.tpago.movil.d.ui.main.purchase;

import android.animation.Animator;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.tpago.movil.R;
import com.tpago.movil.d.domain.Product;
import com.tpago.movil.d.ui.FullScreenChildDialogFragment;
import com.tpago.movil.d.ui.view.BaseAnimatorListener;
import com.tpago.movil.util.ObjectHelper;

import javax.inject.Inject;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author hecvasro
 */
public class NonNfcPurchasePaymentDialogFragment
  extends FullScreenChildDialogFragment<PurchaseContainer>
  implements PurchasePaymentScreen {

  private static final String EXTRA_PAYMENT_OPTION = "paymentOption";

  private Unbinder unbinder;

  private PurchasePaymentOptionHolder paymentOptionHolder;

  @Inject PurchasePaymentOptionBinder paymentOptionBinder;
  @Inject PurchasePaymentPresenter presenter;

  @BindInt(android.R.integer.config_shortAnimTime) int terminateDuration;

  @BindView(R.id.commerce_payment_option) View paymentOptionContainerView;
  @BindView(R.id.commerce_payment_message) TextView commercePaymentMessageTextView;

  private OnDismissedListener onDismissedListener;

  @NonNull
  public static NonNfcPurchasePaymentDialogFragment newInstance(@NonNull Product paymentOption, OnDismissedListener onDismissedListener) {
    final Bundle bundle = new Bundle();
    bundle.putParcelable(EXTRA_PAYMENT_OPTION, paymentOption);
    final NonNfcPurchasePaymentDialogFragment fragment = new NonNfcPurchasePaymentDialogFragment();
    fragment.setArguments(bundle);
    fragment.setOnDismissedListener(onDismissedListener);
    return fragment;
  }

  public void setOnDismissedListener(OnDismissedListener onDismissedListener) {
    this.onDismissedListener = onDismissedListener;
  }

  @Override
  protected int getCustomTheme() {
    return R.style.PurchasePaymentTheme;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // Asserts the payment option.
    final Bundle bundle
      = ObjectHelper.isNotNull(savedInstanceState) ? savedInstanceState : getArguments();
    if (ObjectHelper.isNull(bundle) || !bundle.containsKey(EXTRA_PAYMENT_OPTION)) {
      throw new NullPointerException("Argument " + EXTRA_PAYMENT_OPTION + " is missing");
    } else {
      // Retrieves the payment option from the arguments.
      final Product paymentOption = bundle.getParcelable(EXTRA_PAYMENT_OPTION);
      // Injects all the dependencies.
      final NonNfcPurchasePaymentComponent component = DaggerNonNfcPurchasePaymentComponent.builder()
        .purchaseComponent(getContainer().getComponent())
        .purchasePaymentModule(new PurchasePaymentModule(paymentOption))
        .build();
      component.inject(this);
    }
  }

  @Nullable
  @Override
  public View onCreateView(
    LayoutInflater inflater, @Nullable ViewGroup container,
    @Nullable Bundle savedInstanceState
  ) {
    return inflater.inflate(R.layout.d_dialog_fragment_non_nfc_purchase_payment, container, false);
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    // Binds all the annotated views and methods.
    unbinder = ButterKnife.bind(this, view);
    // Creates the payment option representation.
    paymentOptionHolder = new PurchasePaymentOptionItemHolder(paymentOptionContainerView);
    // Attaches the screen to the presenter.
    presenter.attachScreen(this);
  }

  @Override
  public void onResume() {
    super.onResume();
    final Window window = getDialog().getWindow();
    if (ObjectHelper.isNotNull(window)) {
      window.setWindowAnimations(R.style.PurchasePaymentAnimation);
    }
    this.presenter.resume();
  }

  @Override
  public void onPause() {
    this.presenter.pause();
    super.onPause();
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    // Detaches the screen from the presenter.
    presenter.detachScreen();
    // Unbinds all the annotated views and methods.
    unbinder.unbind();
  }

  @Override
  public void setMessage(@NonNull String message) {
  }

  @Override
  public void setPaymentOption(@NonNull Product paymentOption) {
    paymentOptionBinder.bind(paymentOption, paymentOptionHolder);
  }

  @Override
  public void animateAndTerminate() {
  }

  @Override
  public void onDismiss(DialogInterface dialog) {
    super.onDismiss(dialog);
    if (ObjectHelper.isNotNull(onDismissedListener)) {
      onDismissedListener.onDismissed();
    }
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == android.R.id.home) {
      dismiss();
      return true;
    } else {
      return super.onOptionsItemSelected(item);
    }
  }

  interface OnDismissedListener {

    void onDismissed();
  }
}
