package com.gbh.movil.ui.main.payments.purchase;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gbh.movil.R;
import com.gbh.movil.Utils;
import com.gbh.movil.domain.Product;
import com.gbh.movil.ui.FullScreenDialogFragment;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * TODO
 *
 * @author hecvasro
 */
public class CommercePaymentDialogFragment extends FullScreenDialogFragment {
  /**
   * TODO
   */
  private static final String EXTRA_PAYMENT_OPTION = "paymentOption";

  /**
   * TODO
   */
  private Product paymentOption;

  private Unbinder unbinder;

  @Inject
  CommercePaymentOptionBinder paymentOptionBinder;

  @BindView(R.id.commerce_payment_option)
  View paymentOptionContainerView;

  /**
   * TODO
   *
   * @param paymentOption
   *   TODO
   *
   * @return TODO
   */
  @NonNull
  public static CommercePaymentDialogFragment newInstance(@NonNull Product paymentOption) {
    final Bundle bundle = new Bundle();
    bundle.putSerializable(EXTRA_PAYMENT_OPTION, paymentOption);
    final CommercePaymentDialogFragment fragment = new CommercePaymentDialogFragment();
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
      paymentOption = (Product) bundle.getSerializable(EXTRA_PAYMENT_OPTION);
      // Injects all the dependencies.
      // TODO: Modify Container and SubFragment interfaces in order to allow containers be fragments.
      ((PurchaseFragment) getParentFragment()).component.inject(this);
    }
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
    @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.dialog_fragment_commerce_payment, container, false);
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    // Binds all the annotated views and methods.
    unbinder = ButterKnife.bind(this, view);
    // Binds the selected payment option to its container.
    paymentOptionBinder.bind(paymentOption, new CommercePaymentOptionItemHolder(
      paymentOptionContainerView));
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    // Unbinds all the annotated views and methods.
    unbinder.unbind();
  }
}
