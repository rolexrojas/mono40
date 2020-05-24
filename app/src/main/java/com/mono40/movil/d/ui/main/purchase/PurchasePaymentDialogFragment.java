package com.mono40.movil.d.ui.main.purchase;

import android.animation.Animator;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.mono40.movil.R;
import com.mono40.movil.d.domain.Product;
import com.mono40.movil.d.ui.FullScreenChildDialogFragment;
import com.mono40.movil.d.ui.view.BaseAnimatorListener;
import com.mono40.movil.util.ObjectHelper;

import javax.inject.Inject;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author hecvasro
 */
public class PurchasePaymentDialogFragment
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
  @BindView(R.id.purchase_payment_indicator_confirmation) ImageView purchasePaymentIndicatorConfirmationImageView;

  private OnDismissedListener onDismissedListener;

  @NonNull
  public static PurchasePaymentDialogFragment newInstance(@NonNull Product paymentOption) {
    final Bundle bundle = new Bundle();
    bundle.putParcelable(EXTRA_PAYMENT_OPTION, paymentOption);
    final PurchasePaymentDialogFragment fragment = new PurchasePaymentDialogFragment();
    fragment.setArguments(bundle);
    return fragment;
  }

  @Override
  protected int getCustomTheme() {
    return R.style.PurchasePaymentTheme;
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    if (ObjectHelper.isNotNull(getTargetFragment()) && getTargetFragment() instanceof OnDismissedListener) {
      onDismissedListener = (OnDismissedListener) getTargetFragment();
    } else if (ObjectHelper.isNotNull(getParentFragment()) && getParentFragment() instanceof OnDismissedListener) {
      onDismissedListener = (OnDismissedListener) getParentFragment();
    } else if (getActivity() instanceof OnDismissedListener) {
      onDismissedListener = (OnDismissedListener) getActivity();
    }
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
      final PurchasePaymentComponent component = DaggerPurchasePaymentComponent.builder()
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
      .setDuration(5000L)
      .setListener(new BaseAnimatorListener() {
        @Override
        public void onAnimationEnd(Animator animator) {
          dismiss();
        }
      })
      .start();
  }

  @Override
  public void onDismiss(DialogInterface dialog) {
    super.onDismiss(dialog);
    if (ObjectHelper.isNotNull(onDismissedListener)) {
      onDismissedListener.onDismissed();
    }
  }

  interface OnDismissedListener {

    void onDismissed();
  }
}
