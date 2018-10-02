package com.tpago.movil.dep.init;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tpago.movil.app.ui.alert.AlertManager;
import com.tpago.movil.SMSBroadCastReceiver;
import com.tpago.movil.util.function.Action;
import com.tpago.movil.util.function.Consumer;
import com.tpago.movil.util.digit.Digit;
import com.tpago.movil.R;
import com.tpago.movil.app.ui.activity.ActivityQualifier;
import com.tpago.movil.app.ui.fragment.FragmentReplacer;
import com.tpago.movil.dep.init.register.RegisterFragment;
import com.tpago.movil.dep.init.signin.SignInFragment;
import com.tpago.movil.dep.widget.EditableLabel;
import com.tpago.movil.app.ui.DNumPad;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public final class OneTimePasswordFragment
        extends BaseInitFragment
        implements OneTimePasswordPresenter.View {

  public static OneTimePasswordFragment create() {
    return new OneTimePasswordFragment();
  }

  private Unbinder unbinder;
  private OneTimePasswordPresenter presenter;
  private boolean shouldMoveToSignUpScreen;
  private String msisdn;
  private boolean autoComplete = true;

  @BindView(R.id.editable_label_otp_pin) EditableLabel otpEditableLabel;
  @BindView(R.id.num_pad) DNumPad DNumPad;

  @Inject @ActivityQualifier FragmentReplacer fragmentReplacer;
  @Inject LogoAnimator logoAnimator;
  @Inject AlertManager alertManager;

  private Consumer<Integer> numPadDigitConsumer;
  private Action numPadDeleteAction;

  private BroadcastReceiver receiver = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
      if (intent.getAction().equalsIgnoreCase(SMSBroadCastReceiver.OTP_IDENTIFIER)) {
        final String message = intent.getStringExtra(SMSBroadCastReceiver.MESSAGE);
        if(autoComplete) {
          presenter.setValue(message);
          autoComplete = false;
        }
      }
    }
  };

  @OnClick(R.id.sendcodeagain)
  void onSendCodeAgainClicked(){
    this.autoComplete = true;
    this.presenter.requestActivationCode(msisdn);
    this.alertManager.builder()
      .title(R.string.transactionSucceeded)
      .message("Se le ha enviado un nuevo código de activación")
      .show();
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // Injects all the annotated dependencies.
    getInitComponent().inject(this);
  }

  @Nullable
  @Override
  public View onCreateView(
          LayoutInflater inflater,
          @Nullable ViewGroup container,
          @Nullable Bundle savedInstanceState
  ) {
    return inflater.inflate(R.layout.d_fragment_init_one_time_password, container, false);
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    // Binds all annotated views, resources and methods.
    unbinder = ButterKnife.bind(this, view);
    // Creates presenter.
    presenter = new OneTimePasswordPresenter(this, getInitComponent());

    //TODO: Take things out from bundle
    Bundle bundle = this.getArguments();
    if(bundle != null){
      this.shouldMoveToSignUpScreen = bundle.getBoolean("shouldMoveToSignUpScreen");
      this.msisdn = bundle.getString("msisdn");
    }
    this.presenter.requestActivationCode(msisdn);
  }

  @Override
  public void onResume() {
    super.onResume();
    LocalBroadcastManager.getInstance(getActivity()).registerReceiver(receiver,
      new IntentFilter(SMSBroadCastReceiver.OTP_IDENTIFIER));
    // Moves the logo out of the screen.
    logoAnimator.moveOutOfScreen();

    // Adds a listener that gets notified each time a digit button of the num pad is clicked.
    this.numPadDigitConsumer = this::onDigitClicked;
    this.DNumPad.addDigitConsumer(this.numPadDigitConsumer);
    // Adds a listener that gets notified each time the delete button of the num pad is clicked.
    this.numPadDeleteAction = this::onDeleteClicked;
    this.DNumPad.addDeleteAction(this.numPadDeleteAction);

    // Starts the presenter.
    presenter.onViewStarted();
  }

  @Override
  public void onPause() {
    // Stops the presenter.
    presenter.onViewStopped();

    // Removes the listener that gets notified each time the delete button of the num pad is clicked.
    this.DNumPad.removeDeleteAction(this.numPadDeleteAction);
    this.numPadDeleteAction = null;
    // Removes the listener that gets notified each time a digit button of the num pad is clicked.
    this.DNumPad.removeDigitConsumer(this.numPadDigitConsumer);
    this.numPadDigitConsumer = null;

    LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(receiver);
    super.onPause();
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    // Destroys the presenter.
    presenter = null;
    // Binds all annotated views, resources and methods.
    unbinder.unbind();
  }

  @Override
  public void setTextInputContent(String text) {
    otpEditableLabel.setText(text);
  }

  @Override
  public void moveToSignInScreen() {
    fragmentReplacer.begin(SignInFragment.create())
      .addToBackStack()
      .transition(FragmentReplacer.Transition.SRFO)
      .commit();
  }

  @Override
  public void moveToSignUpScreen() {
    fragmentReplacer.begin(RegisterFragment.create())
      .addToBackStack()
      .transition(FragmentReplacer.Transition.SRFO)
      .commit();
  }

  @Override
  public String getMsisdn() { return this.msisdn; }

  @Override
  public boolean getShouldMoveToSignUpScreen() { return this.shouldMoveToSignUpScreen; }

  public final void onDigitClicked(@Digit int digit) {
    presenter.addDigit(digit);
  }

  public final void onDeleteClicked() {
    presenter.removeDigit();
  }
}
