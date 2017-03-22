package com.tpago.movil.dep.ui.main.purchase;

import android.animation.Animator;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tpago.movil.R;
import com.tpago.movil.dep.misc.Utils;
import com.tpago.movil.dep.domain.Product;
import com.tpago.movil.dep.ui.FullScreenChildDialogFragment;
import com.tpago.movil.dep.ui.view.BaseAnimatorListener;

import javax.inject.Inject;

import butterknife.BindInt;
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

  @BindInt(android.R.integer.config_shortAnimTime)
  int terminateDuration;

  @BindView(R.id.commerce_payment_option)
  View paymentOptionContainerView;
  @BindView(R.id.commerce_payment_message)
  TextView commercePaymentMessageTextView;
  @BindView(R.id.purchase_payment_indicator_confirmation)
  ImageView purchasePaymentIndicatorConfirmationImageView;

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
    return R.style.PurchasePaymentTheme;
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
    return inflater.inflate(R.layout.d_dialog_fragment_purchase_payment, container, false);
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
    getDialog().getWindow().setWindowAnimations(R.style.PurchasePaymentAnimation);
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

  @Override
  public void animateAndTerminate() {
    purchasePaymentIndicatorConfirmationImageView.animate()
      .alpha(1F)
      .setDuration(terminateDuration)
      .setListener(new BaseAnimatorListener() {
        @Override
        public void onAnimationEnd(Animator animator) {
          dismiss();
        }
      })
      .start();
  }
}
