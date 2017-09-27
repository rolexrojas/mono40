package com.tpago.movil.app.ui.main.settings.index;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;

import com.tpago.movil.R;
import com.tpago.movil.app.ui.main.BaseMainFragment;
import com.tpago.movil.app.ui.main.settings.MultiLineSettingsOption;
import com.tpago.movil.app.ui.main.settings.ToggleableSettingsOption;
import com.tpago.movil.d.ui.main.DepMainActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author hecvasro
 */
public final class SettingsIndexFragment extends BaseMainFragment
  implements SettingsIndexPresentation {

  public static SettingsIndexFragment create() {
    return new SettingsIndexFragment();
  }

  @Inject SettingsIndexPresenter presenter;

  @BindView(R.id.profileSettingsOption) MultiLineSettingsOption profileSettingsOption;
  @BindView(R.id.primaryPaymentMethodSettingsOption) MultiLineSettingsOption primaryPaymentMethodSettingsOption;
  @BindView(R.id.unlockMethodSettingsOption) MultiLineSettingsOption unlockMethodSettingsOption;
  @BindView(R.id.timeoutSettingsOption) MultiLineSettingsOption timeoutSettingsOption;
  @BindView(R.id.lockOnExitSettingsOption) ToggleableSettingsOption lockOnExitSettingsOption;

  @Override
  @LayoutRes
  protected int layoutResId() {
    return R.layout.settings_index;
  }

  @Override
  @StringRes
  protected int titleResId() {
    return R.string.settings;
  }

  @OnClick(R.id.profileSettingsOption)
  final void onProfileSettingsOptionClicked() {
    // TODO
  }

  @OnClick(R.id.primaryPaymentMethodSettingsOption)
  final void onPrimaryPaymentMethodSettingsOptionClicked() {
    // TODO
  }

  @OnClick(R.id.unlockMethodSettingsOption)
  final void onUnlockMethodSettingsOptionClicked() {
    // TODO
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
    // TODO
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Injects all annotated dependencies.
    DepMainActivity.get(this.getActivity())
      .getComponent()
      .create(SettingsIndexModule.create(this))
      .inject(this);
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
  public void setUnlockMethodSettingsOptionSecondaryText(String text) {
    this.unlockMethodSettingsOption.secondaryText(text);
  }

  @Override
  public void setTimeoutSettingsOptionSecondaryText(String text) {
    this.timeoutSettingsOption.secondaryText(text);
  }

  @Override
  public void setLockOnExitSettingsOptionChecked(boolean checked) {
    this.lockOnExitSettingsOption.checked(checked);
  }
}
