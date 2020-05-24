package com.mono40.movil.app.ui.main.transaction.paypal;

import android.os.Bundle;
import androidx.annotation.Nullable;

import com.mono40.movil.app.ui.activity.toolbar.ActivityToolbar;
import com.mono40.movil.app.ui.activity.toolbar.FragmentToolbarRetained;
import com.mono40.movil.paypal.PayPalAccount;
import com.mono40.movil.util.ObjectHelper;

public final class PayPalTransactionFragment extends FragmentToolbarRetained {

  private static final String KEY_RECIPIENT = "recipient";

  static PayPalTransactionFragment create(PayPalAccount recipient) {
    final Bundle arguments = new Bundle();
    arguments.putParcelable(KEY_RECIPIENT, recipient);
    final PayPalTransactionFragment fragment = new PayPalTransactionFragment();
    fragment.setArguments(arguments);
    return fragment;
  }

  private PayPalTransactionComponent component;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Loads all arguments.
    final Bundle arguments = ObjectHelper.checkNotNull(this.getArguments(), "arguments");
    if (!arguments.containsKey(KEY_RECIPIENT)) {
      throw new IllegalArgumentException("!arguments.containsKey(KEY_RECIPIENT)");
    }
    final PayPalAccount recipient = ObjectHelper
      .checkNotNull(arguments.getParcelable(KEY_RECIPIENT), "recipient");

    // Instantiates the dependency injector.
    this.component = ActivityToolbar.get(this.getContext())
      .componentBuilderSupplier()
      .get(PayPalTransactionFragment.class, PayPalTransactionComponent.Builder.class)
      .payPalTransactionModule(PayPalTransactionModule.create(recipient))
      .build();

    // Injects all annotated dependencies.
    this.component.inject(this);
  }
}
