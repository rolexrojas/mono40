package com.tpago.movil.app.ui.main.settings;

import android.app.AlertDialog;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.view.Display;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.tpago.movil.R;
import com.tpago.movil.api.Api;
import com.tpago.movil.app.ui.FragmentActivityBase;
import com.tpago.movil.app.ui.activity.ActivityQualifier;
import com.tpago.movil.app.ui.fragment.FragmentReplacer;
import com.tpago.movil.app.ui.main.BaseMainFragment;
import com.tpago.movil.app.ui.main.settings.help.FragmentHelp;
import com.tpago.movil.app.ui.main.settings.primaryPaymentMethod.PrimaryPaymentMethodFragment;
import com.tpago.movil.app.ui.main.settings.profile.ProfileFragment;
import com.tpago.movil.app.ui.main.settings.auth.alt.AltAuthMethodFragment;
import com.tpago.movil.d.ui.Dialogs;
import com.tpago.movil.d.ui.main.DepMainActivityBase;
import com.tpago.movil.d.ui.main.PinConfirmationDialogFragment;
import com.tpago.movil.session.SessionManager;
import com.tpago.movil.util.ObjectHelper;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author hecvasro
 */
public final class SettingsFragment extends BaseMainFragment implements SettingsPresentation {

  public static SettingsFragment create() {
    return new SettingsFragment();
  }

  @Inject
  @ActivityQualifier
  FragmentReplacer fragmentReplacer;

  @Inject SettingsPresenter presenter;
  @Inject Api api;
  @Inject SessionManager sessionManager;

  @BindView(R.id.profileSettingsOption) MultiLineSettingsOption profileSettingsOption;
  @BindView(R.id.primaryPaymentMethodSettingsOption) MultiLineSettingsOption primaryPaymentMethodSettingsOption;
  @BindView(R.id.altAuthMethodSettingsOption) MultiLineSettingsOption altAuthMethodSettingsOption;
  @BindView(R.id.timeoutSettingsOption) MultiLineSettingsOption timeoutSettingsOption;
  @BindView(R.id.lockOnExitSettingsOption) ToggleableSettingsOption lockOnExitSettingsOption;

  @OnClick(R.id.setting_option_help)
  final void onHelpSettingOptionPressed() {
    this.fragmentReplacer.begin(FragmentHelp.create())
      .transition(FragmentReplacer.Transition.FIFO)
      .addToBackStack()
      .commit();
  }

  @OnClick(R.id.setting_option_about)
  final void onAboutSettingOptionPressed() {
    this.fragmentReplacer.begin(FragmentAbout.create())
      .transition(FragmentReplacer.Transition.FIFO)
      .addToBackStack()
      .commit();
  }

  @Override
  @LayoutRes
  protected int layoutResId() {
    return R.layout.settings;
  }

  @Override
  @StringRes
  protected int titleResId() {
    return R.string.settings;
  }

  @Override
  protected String subTitle() {
    return "";
  }

  @OnClick(R.id.profileSettingsOption)
  final void onProfileSettingsOptionClicked() {
    this.fragmentReplacer.begin(ProfileFragment.create())
      .transition(FragmentReplacer.Transition.FIFO)
      .addToBackStack()
      .commit();
  }

  @OnClick(R.id.primaryPaymentMethodSettingsOption)
  final void onPrimaryPaymentMethodSettingsOptionClicked() {
    this.startActivity(
      FragmentActivityBase.createLaunchIntent(
        this.getContext(),
          PrimaryPaymentMethodFragment.creator()
      )
    );
  }

  @OnClick(R.id.altAuthMethodSettingsOption)
  final void onAltAuthMethodSettingsOptionClicked() {
    this.startActivity(
      FragmentActivityBase.createLaunchIntent(
        this.getContext(),
        AltAuthMethodFragment.creator()
      )
    );
  }

  @OnClick(R.id.timeoutSettingsOption)
  final void onTimeoutSettingsOptionClicked() {
    // TODO
  }

  @OnClick(R.id.lockOnExitSettingsOption)
  final void onLockOnExitSettingsOptionClicked() {
    // TODO
  }

  @OnClick(R.id.pinSettingsOption)
  final void onPinSettingsOptionClicked() {
    PinConfirmation pin = (actualPin) -> this.requestPin(getString(R.string.newPin),
        (newPin) ->  this.requestPin(getString(R.string.newPinConfirm), (newPinConfirmation) -> {
          if (newPin.equals(newPinConfirmation)) {
            final String msisdn = sessionManager.getUser().phoneNumber().value();
            api.ChangePin(msisdn, newPin, actualPin)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((result) -> {
                  if(result.isSuccessful()){
                    handleSuccess(result.successData().getMessage());
                  }else{
                    if(ObjectHelper.isNull(result.failureData())){
                      handleError(getString(R.string.error_generic));
                    }else{
                      handleError(result.failureData().description());
                    }
                  }
                });
            PinConfirmationDialogFragment.dismiss(getChildFragmentManager(), true);
          } else {
            this.showErrorMessage();
          }
        }));
    this.requestPin(getString(R.string.pinActual), pin);
  }

  private void showErrorMessage() {
    AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this.getActivity());
    builder.setTitle(getString(R.string.errorNewPin));
    builder.setMessage(getString(R.string.errorNewPinMessage));
    builder.setPositiveButton(getString(R.string.register_form_password_error_positive_button_text),
        (dialog, which) -> dialog.dismiss());
    AlertDialog alertDialog = builder.create();
    alertDialog.show();
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Injects all annotated dependencies.
    DepMainActivityBase.get(this.getActivity())
      .componentBuilderSupplier()
      .get(SettingsFragment.class, SettingsComponent.Builder.class)
      .settingsModule(SettingsModule.create(this))
      .build()
      .inject(this);
  }

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    WebView webView = new WebView(getContext());
    webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
    webView.loadUrl(getString(R.string.tpagotermconditions));
  }

  @Override
  public void onResume() {
    super.onResume();

    this.presenter.onPresentationResumed();
  }

  @Override
  public void onPause() {
    this.presenter.onPresentationPaused();

    super.onPause();
  }

  @Override
  public void setProfileSettingsOptionSecondaryText(String text) {
    this.profileSettingsOption.secondaryText(text);
  }

  @Override
  public void setPrimaryPaymentMethodSettingsOptionSecondaryText(String text) {
    this.primaryPaymentMethodSettingsOption.secondaryText(text);
  }

  @Override
  public void setAltAuthMethodOption(String text) {
    this.altAuthMethodSettingsOption.secondaryText(text);
  }

  @Override
  public void setTimeoutSettingsOptionSecondaryText(String text) {
    this.timeoutSettingsOption.secondaryText(text);
  }

  @Override
  public void setLockOnExitSettingsOptionChecked(boolean checked) {
    this.lockOnExitSettingsOption.checked(checked);
  }

  public void requestPin(String message, PinConfirmation method) {
    final Display display = getActivity().getWindowManager().getDefaultDisplay();
    final Point size = new Point();
    display.getSize(size);

    final int x = size.x / 2;
    final int y = size.y / 2;
    PinConfirmationDialogFragment.show(
        getChildFragmentManager(),
        message,
        (PinConfirmationDialogFragment.Callback) pin -> {
          PinConfirmationDialogFragment.dismiss(getChildFragmentManager(), true);
          method.call(pin);
        },
        x,
        y
    );
  }

  private void handleSuccess(String message) {
    Dialogs.builder(getContext())
        .setTitle(R.string.done_with_exclamation_mark)
        .setMessage(message)
        .setPositiveButton(R.string.ok, (dialog, which) -> {})
        .create()
        .show();
  }

  private void handleError(String errorDescription) {
    Dialogs.builder(getContext())
      .setTitle(R.string.error_generic_title)
      .setMessage(errorDescription)
      .setPositiveButton(R.string.error_positive_button_text, (dialog, which) -> dialog.dismiss())
      .create()
      .show();
  }

  interface PinConfirmation {
    void call(String pin);
  }
}
