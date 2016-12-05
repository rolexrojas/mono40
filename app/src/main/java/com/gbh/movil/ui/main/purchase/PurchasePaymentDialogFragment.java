package com.gbh.movil.ui.main.purchase;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gbh.movil.R;
import com.gbh.movil.Utils;
import com.gbh.movil.domain.Product;
import com.gbh.movil.ui.FullScreenChildDialogFragment;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * TODO
 *
 * @author hecvasro
 */
public class PurchasePaymentDialogFragment
  extends FullScreenChildDialogFragment<PurchaseContainer> implements PurchasePaymentScreen {
  /**
   * TODO
   */
  private static final String EXTRA_PAYMENT_OPTION = "paymentOption";

  private Unbinder unbinder;

  private PurchasePaymentOptionHolder paymentOptionHolder;

  @Inject
  PurchasePaymentOptionBinder paymentOptionBinder;
  @Inject
  PurchasePaymentPresenter presenter;

  @BindView(R.id.commerce_payment_option)
  View paymentOptionContainerView;
  @BindView(R.id.commerce_payment_message)
  TextView commercePaymentMessageTextView;

  /**
   * TODO
   *
   * @param paymentOption
   *   TODO
   *
   * @return TODO
   */
  @NonNull
  public static PurchasePaymentDialogFragment newInstance(@NonNull Product paymentOption) {
    final Bundle bundle = new Bundle();
    bundle.putSerializable(EXTRA_PAYMENT_OPTION, paymentOption);
    final PurchasePaymentDialogFragment fragment = new PurchasePaymentDialogFragment();
    fragment.setArguments(bundle);
    return fragment;
  }

  @Override
  protected int getCustomTheme() {
    return R.style.CommercePaymentTheme;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // Asserts the payment option.
    final Bundle bundle = Utils.isNotNull(savedInstanceState) ? savedInstanceState : getArguments();
    if (Utils.isNull(bundle) || !bundle.containsKey(EXTRA_PAYMENT_OPTION)) {
      throw new NullPointerException("Argument " + EXTRA_PAYMENT_OPTION + " is missing");
    } else {
      // Retrieves the payment option from the arguments.
      final Product paymentOption = (Product) bundle.getSerializable(EXTRA_PAYMENT_OPTION);
      // Injects all the dependencies.
      final PurchasePaymentComponent component = DaggerPurchasePaymentComponent.builder()
        .purchaseComponent(getContainer().getComponent())
        .purchasePaymentModule(new PurchasePaymentModule(paymentOption))
        .build();
      component.inject(this);
    }
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
    @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.dialog_fragment_purchase_payment, container, false);
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
  public void onStart() {
    super.onStart();
    // Starts the presenter.
    presenter.start();
  }

  @Override
  public void onStop() {
    super.onStop();
    // Stops the presenter.
    presenter.stop();
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
    commercePaymentMessageTextView.setText(message);
  }

  @Override
  public void setPaymentOption(@NonNull Product paymentOption) {
    paymentOptionBinder.bind(paymentOption, paymentOptionHolder);
  }
}
