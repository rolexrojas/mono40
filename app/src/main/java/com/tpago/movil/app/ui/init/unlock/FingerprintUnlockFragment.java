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
import com.tpago.movil.session.FingerprintSessionOpeningMethodSignatureSupplier;
import com.tpago.movil.dep.init.InitActivity;
import com.tpago.movil.session.SessionManager;
import com.tpago.movil.util.Result;

import javax.inject.Inject;

import butterknife.OnClick;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author hecvasro
 */
public final class FingerprintUnlockFragment extends BaseUnlockFragment {

  static FingerprintUnlockFragment create() {
    return new FingerprintUnlockFragment();
  }

  private CancellationSignal cancellationSignal;

  @Inject SessionManager sessionManager;
  @Inject FingerprintManagerCompat fingerprintManager;
  @Inject @Nullable FingerprintSessionOpeningMethodSignatureSupplier.Creator fingerprintSignatureSupplierCreator;

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
  protected void handleError(Throwable throwable) {
    if (!(throwable instanceof KeyPermanentlyInvalidatedException)) {
      super.handleError(throwable);
    } else {
      final AlertData alertData = AlertData.builder(this.stringMapper)
        .message("Como la seguridad del dispositivo fue desactivada o una nueva huella fue enrolada es requerido volver a configurar el desbloqueo rápido con huellas digitales, pero primero debe desbloquear la aplicación usando su contraseña.")
        .positiveButtonAction(this::onUserPasswordTextViewClicked)
        .build();
      this.alertManager.show(alertData);
    }
  }

  @Override
  public void onResume() {
    super.onResume();

    this.logoAnimator.moveTopAndScaleDown();

    this.cancellationSignal = new CancellationSignal();
    this.disposable = this.fingerprintSignatureSupplierCreator.create(this.cancellationSignal)
      .get()
      .flatMap((result) -> {
        if (result.isSuccessful()) {
          return this.sessionManager
            .openSession(result.successData(), this.deviceIdSupplier.get())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe((disposable) -> takeoverLoader.show())
            .doFinally(takeoverLoader::hide);
        } else {
          return Single.just(Result.create(result.failureData()));
        }
      })
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(
        this::handleSuccess,
        this::handleError
      );
  }

  @Override
  public void onPause() {
    this.cancellationSignal.cancel();
    this.cancellationSignal = null;

    super.onPause();
  }
}
