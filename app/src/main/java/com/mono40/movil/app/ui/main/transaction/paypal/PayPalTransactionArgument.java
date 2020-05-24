package com.mono40.movil.app.ui.main.transaction.paypal;

import com.google.auto.value.AutoValue;
import com.mono40.movil.app.ui.activity.toolbar.ActivityToolbar;
import com.mono40.movil.app.ui.activity.toolbar.FragmentToolbarRetained;
import com.mono40.movil.app.ui.fragment.base.FragmentBase;
import com.mono40.movil.app.ui.main.transaction.paypal.confirm.PayPalTransactionConfirmFragment;
import com.mono40.movil.paypal.PayPalAccount;

@AutoValue
public abstract class PayPalTransactionArgument extends ActivityToolbar.Argument {

  public static PayPalTransactionArgument create(PayPalAccount recipient) {
    return new AutoValue_PayPalTransactionArgument(recipient);
  }

  PayPalTransactionArgument() {
  }

  public abstract PayPalAccount recipient();

  @Override
  public FragmentToolbarRetained createFragmentRetained() {
    return PayPalTransactionFragment.create(this.recipient());
  }

  @Override
  public FragmentBase createFragment() {
    return PayPalTransactionConfirmFragment.create();
  }
}
