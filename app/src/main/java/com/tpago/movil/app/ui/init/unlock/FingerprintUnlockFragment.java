package com.tpago.movil.app.ui.init.unlock;

import android.os.Bundle;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.support.annotation.Nullable;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v4.os.CancellationSignal;
import android.view.View;

import com.tpago.movil.R;
import com.tpago.movil.app.ui.fragment.FragmentReplacer;
import com.tpago.movil.dep.init.InitFragment;
import com.tpago.movil.session.FingerprintMethodSignatureSupplier;
import com.tpago.movil.dep.init.InitActivityBase;
import com.tpago.movil.session.SessionManager;
import com.tpago.movil.util.FailureData;
import com.tpago.movil.util.Result;
import com.tpago.movil.util.StringHelper;

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
  @Inject @Nullable FingerprintMethodSignatureSupplier.Creator fingerprintSignatureSupplierCreator;

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

    InitActivityBase.get(this.getActivity())
      .getInitComponent()
      .inject(this);
  }

  @Override
  protected void handleSuccess(Result<?> result) {
    if (result.isSuccessful()) {
      this.fragmentReplacer.begin(InitFragment.create())
          .commit();
      return;
    }
    final FailureData failureData = result.failureData();
    final String errorMessage = StringHelper.isNullOrEmpty(failureData.description()) ?
            getString(R.string.error_generic) : failureData.description();

    this.cancelFingerprintAfterRead();

    this.alertManager.builder()
        .message(errorMessage)
        .positiveButtonAction(this::startToListenFingerprint)
        .show();

  }

  @Override
  protected void handleError(Throwable throwable) {
    if ((throwable instanceof KeyPermanentlyInvalidatedException)) {
      this.alertManager.builder()
        .message(getString(R.string.unregisteredfingerprint))
        .positiveButtonAction(this::onUserPasswordTextViewClicked)
        .show();
      return;
    }

    this.cancelFingerprintAfterRead();

    this.alertManager.builder()
      .title(R.string.error_generic_title)
      .message(R.string.error_generic)
      .positiveButtonAction(this::startToListenFingerprint)
      .build()
      .show();

  }

  @Override
  public void onResume() {
    super.onResume();

    this.logoAnimator.moveTopAndScaleDown();
    this.startToListenFingerprint();
  }


  private void startToListenFingerprint() {
    if (!this.disposable.isDisposed()) {
      this.disposable.dispose();
    }
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
      .doOnDispose(() -> {
        if(this.cancellationSignal != null) {
          if (this.cancellationSignal.isCanceled()) {
            return;
          }
          this.cancellationSignal.cancel();
        }
      })
      .subscribe(
        this::handleSuccess,
        this::handleError
      );
  }

  private void cancelFingerprintAfterRead() {
    if (!this.disposable.isDisposed()) {
      this.disposable.dispose();
    }

    if(this.cancellationSignal != null) {
      if (this.cancellationSignal.isCanceled()) {
        return;
      }
      this.cancellationSignal.cancel();
    }
  }
  @Override
  public void onPause() {
    this.cancellationSignal.cancel();
    this.cancellationSignal = null;
    super.onPause();
  }
}
