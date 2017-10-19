package com.tpago.movil.app.ui.init.unlock;

import android.os.Bundle;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.support.annotation.Nullable;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v4.os.CancellationSignal;
import android.view.View;

import com.tpago.movil.R;
import com.tpago.movil.app.ui.AlertData;
import com.tpago.movil.app.ui.FragmentReplacer;
import com.tpago.movil.data.auth.alt.AltAuthMethodConfigData;
import com.tpago.movil.data.auth.alt.FingerprintAltAuthMethodKeySupplier;
import com.tpago.movil.dep.init.InitActivity;
import com.tpago.movil.domain.auth.alt.AltAuthMethodManager;
import com.tpago.movil.domain.auth.alt.AltOpenSessionSignatureData;
import com.tpago.movil.util.Result;

import java.security.PrivateKey;
import java.security.Signature;

import javax.inject.Inject;

import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * @author hecvasro
 */
public final class FingerprintUnlockFragment extends BaseUnlockFragment {

  static FingerprintUnlockFragment create() {
    return new FingerprintUnlockFragment();
  }

  private CancellationSignal cancellationSignal;

  @Inject AltAuthMethodConfigData altAuthMethodConfigData;
  @Inject AltAuthMethodManager altAuthMethodManager;
  @Inject FingerprintManagerCompat fingerprintManager;
  @Inject @Nullable FingerprintAltAuthMethodKeySupplier fingerprintAltAuthMethodKeySupplier;

  @Override
  protected int layoutResId() {
    return R.layout.unlock_fingerprint;
  }

  @OnClick(R.id.usePasswordTextView)
  final void onUserPasswordTextViewClicked() {
    this.fragmentReplacer.begin(PasswordUnlockFragment.create())
      .transition(FragmentReplacer.Transition.FIFO)
      .commit();
  }

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    InitActivity.get(this.getActivity())
      .getInitComponent()
      .inject(this);
  }

  @Override
  public void onResume() {
    super.onResume();

    this.logoAnimator.moveTopAndScaleDown();

    this.cancellationSignal = new CancellationSignal();

    try {
      final PrivateKey privateKey = this.fingerprintAltAuthMethodKeySupplier.get()
        .map(Result::successData)
        .blockingGet();

      final Signature signature = Signature.getInstance(this.altAuthMethodConfigData.signAlgName());
      signature.initSign(privateKey);

      this.fingerprintManager.authenticate(
        new FingerprintManagerCompat.CryptoObject(signature),
        0,
        this.cancellationSignal,
        new FingerprintManagerCompat.AuthenticationCallback() {
          @Override
          public void onAuthenticationError(int errMsgId, CharSequence errString) {
            Timber.i("onAuthenticationError(%1$s, %2$s)", errMsgId, errString);

            alertManager.show(AlertData.createForGenericFailure(stringMapper));
          }

          @Override
          public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
            Timber.i("onAuthenticationHelp(%1$s, %2$s)", helpMsgId, helpString);

            alertManager.show(AlertData.createForGenericFailure(stringMapper));
          }

          @Override
          public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {
            Timber.i("onAuthenticationSucceeded(%1$s)", result);

            final AltOpenSessionSignatureData data = AltOpenSessionSignatureData.builder()
              .user(sessionManager.getUser())
              .deviceId(deviceIdSupplier.get())
              .build();
            disposable = altAuthMethodManager.verify(
              data,
              result.getCryptoObject()
                .getSignature()
            )
              .subscribeOn(Schedulers.io())
              .observeOn(AndroidSchedulers.mainThread())
              .doOnSubscribe((disposable) -> takeoverLoader.show())
              .doFinally(takeoverLoader::hide)
              .subscribe(
                FingerprintUnlockFragment.this::handleSuccess,
                FingerprintUnlockFragment.this::handleError
              );
          }

          @Override
          public void onAuthenticationFailed() {
            Timber.i("onAuthenticationFailed()");

            final AlertData alertData = AlertData.builder(stringMapper)
              .message("La huella digital no fue reconocida, favor intentar nuevamente.")
              .build();
            alertManager.show(alertData);
          }
        },
        null
      );
    } catch (Exception exception) {
      Timber.e(exception, "Loading private key for fingerprint authentication");

      String message = null;
      AlertData.ButtonAction positiveButtonAction = null;
      if (exception instanceof KeyPermanentlyInvalidatedException) {
        message
          = "Como la seguridad del dispositivo fue desactivada o una nueva huella fue enrolada es requerido volver a configurar el desbloqueo rápido con huellas digitales, pero primero debe desbloquear la aplicación usando su contraseña.";
        positiveButtonAction = this::onUserPasswordTextViewClicked;
      } else {
        positiveButtonAction = this.getActivity()::finish;
      }

      final AlertData alertData = AlertData.builder(this.stringMapper)
        .message(message)
        .positiveButtonAction(positiveButtonAction)
        .build();
      this.alertManager.show(alertData);
    }
  }

  @Override
  public void onPause() {
    this.cancellationSignal.cancel();
    this.cancellationSignal = null;

    super.onPause();
  }
}
