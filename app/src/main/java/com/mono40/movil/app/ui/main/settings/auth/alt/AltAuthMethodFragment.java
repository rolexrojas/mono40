package com.mono40.movil.app.ui.main.settings.auth.alt;

import android.annotation.TargetApi;
import android.app.KeyguardManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import androidx.fragment.app.Fragment;
import androidx.core.hardware.fingerprint.FingerprintManagerCompat;
import androidx.appcompat.widget.Toolbar;
import android.view.View;

import com.google.auto.value.AutoValue;
import com.mono40.movil.R;
import com.mono40.movil.app.ui.alert.AlertManager;
import com.mono40.movil.app.ui.FragmentActivityBase;
import com.mono40.movil.app.ui.FragmentCreator;
import com.mono40.movil.app.ui.InjectableFragment;
import com.mono40.movil.app.ui.main.settings.SelectableSettingsOption;
import com.mono40.movil.app.StringMapper;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author hecvasro
 */
public final class AltAuthMethodFragment extends InjectableFragment
  implements AltAuthMethodPresentation {

  public static FragmentCreator creator() {
    return new AutoValue_AltAuthMethodFragment_Creator();
  }

  @BindView(R.id.codeOption) SelectableSettingsOption codeOption;
  @BindView(R.id.fingerprintOption) SelectableSettingsOption fingerprintOption;
  @BindView(R.id.fingerprintOptionDivider) View fingerprintOptionDivider;
  @BindView(R.id.noneOption) SelectableSettingsOption noneOption;

  @Inject AltAuthMethodPresenter presenter;

  @Inject KeyguardManager keyguardManager;
  @Inject FingerprintManagerCompat fingerprintManager;

  @Inject StringMapper stringMapper;
  @Inject AlertManager alertManager;

  @TargetApi(Build.VERSION_CODES.M)
  @OnClick(R.id.fingerprintOption)
  final void onFingerprintOptionClicked() {
    if (!this.keyguardManager.isDeviceSecure() || !this.fingerprintManager.hasEnrolledFingerprints()) {
      this.alertManager.builder()
        .message(
          getString(R.string.activatefingerprintmessage)
        )
        .negativeButtonText("Seguridad")
        .negativeButtonAction(
          () -> this.startActivity(new Intent(Settings.ACTION_SECURITY_SETTINGS))
        )
        .show();
    } else {
      this.presenter.onEnableFingerprintButtonClicked();
    }
  }

  @OnClick(R.id.codeOption)
  final void onCodeOptionClicked() {
    this.presenter.onEnableCodeButtonClicked(getActivity());
  }

  @OnClick(R.id.noneOption)
  final void onNoneOptionClicked() {
    this.presenter.onDisableButtonClicked();
  }

  @Override
  protected int layoutResId() {
    return R.layout.fragment_alt_auth_method;
  }

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    // Injects all annotated dependencies.
    this.parentComponentBuilderSupplier
      .get(AltAuthMethodFragment.class, AltAuthMethodComponent.Builder.class)
      .altAuthMethodModule(AltAuthMethodModule.create(this))
      .build()
      .inject(this);
  }

  @Override
  public void onStart() {
    super.onStart();

    // Sets the title.
    FragmentActivityBase.get(this.getActivity())
      .setTitle(R.string.altUnlockMethod);
  }

  @Override
  public void onResume() {
    super.onResume();

    final int visibility = this.fingerprintManager.isHardwareDetected() ? View.VISIBLE : View.GONE;
    this.fingerprintOption.setVisibility(visibility);
    this.fingerprintOptionDivider.setVisibility(visibility);

    // Resumes the presenter.
    this.presenter.onPresentationResumed();
  }

  @Override
  public void onPause() {
    // Pauses the presenter.
    this.presenter.onPresentationPaused();

    super.onPause();
  }

  @Override
  public void setFingerprintOptionSelection(boolean selected) {
    this.fingerprintOption.selected(selected);
  }

  @Override
  public void setCodeOptionSelection(boolean selected) {
    this.codeOption.selected(selected);
  }

  @Override
  public void setNoneOptionSelection(boolean selected) {
    this.noneOption.selected(selected);
  }

  @Override
  public void finish() {
    this.getActivity()
      .finish();
  }

  @AutoValue
  public static abstract class Creator extends FragmentCreator {

    Creator() {
    }

    @Override
    public Fragment create() {
      return new AltAuthMethodFragment();
    }
  }
}
