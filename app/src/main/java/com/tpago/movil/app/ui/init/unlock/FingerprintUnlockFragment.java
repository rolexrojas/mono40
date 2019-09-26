package com.tpago.movil.app.ui.init.unlock;

import android.os.Build;
import android.os.Bundle;
import android.security.keystore.KeyPermanentlyInvalidatedException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.biometric.BiometricPrompt;
import androidx.core.hardware.fingerprint.FingerprintManagerCompat;
import androidx.core.os.CancellationSignal;

import android.view.View;
import android.widget.Toast;

import com.tpago.movil.R;
import com.tpago.movil.app.ui.fragment.FragmentReplacer;
import com.tpago.movil.dep.init.InitFragment;
import com.tpago.movil.session.FingerprintMethodSignatureSupplier;
import com.tpago.movil.dep.init.InitActivityBase;
import com.tpago.movil.session.SessionManager;
import com.tpago.movil.session.User;
import com.tpago.movil.util.FailureData;
import com.tpago.movil.util.Result;
import com.tpago.movil.util.StringHelper;

import java.security.Signature;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import butterknife.OnClick;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @author hecvasro
 */
public final class FingerprintUnlockFragment extends BaseUnlockFragment {
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo biometricPromptInfo;
    private Disposable disposableAuth;

    static FingerprintUnlockFragment create() {
        return new FingerprintUnlockFragment();
    }

    private CancellationSignal cancellationSignal;

    @Inject
    SessionManager sessionManager;
    @Inject
    FingerprintManagerCompat fingerprintManager;
    @Inject
    @Nullable
    FingerprintMethodSignatureSupplier.Creator fingerprintSignatureSupplierCreator;


    @Override
    protected int layoutResId() {
        return R.layout.unlock_fingerprint;
    }

    final void onUserPasswordTextViewClicked() {
        this.fragmentReplacer.begin(PasswordUnlockFragment.create())
                .transition(FragmentReplacer.Transition.FIFO)
                .commit();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        biometricPrompt = new BiometricPrompt(this,
                Executors.newSingleThreadExecutor(),
                new BiometricPrompt.AuthenticationCallback() {
                    @Override
                    public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                        super.onAuthenticationError(errorCode, errString);
                        onUserPasswordTextViewClicked();
                    }

                    @Override
                    public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                        super.onAuthenticationSucceeded(result);
                        try {
                            Signature signature = fingerprintSignatureSupplierCreator.create(new CancellationSignal()).getSignature();
                            disposableAuth = FingerprintUnlockFragment.this.sessionManager
                                    .openSession(signature, FingerprintUnlockFragment.this.deviceIdSupplier.get())
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .doOnSubscribe((disposable) -> takeoverLoader.show())
                                    .subscribe(FingerprintUnlockFragment.this::handleSuccess,
                                            FingerprintUnlockFragment.this::handleError);
                        } catch (Exception e) {
                            e.printStackTrace();
                            onUserPasswordTextViewClicked();
                        }
                    }

                    @Override
                    public void onAuthenticationFailed() {
                        super.onAuthenticationFailed();
                    }
                });
        biometricPromptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("titulo")
                .setSubtitle("Subtitulo")
                .setDescription("description")
                .setNegativeButtonText(getString(R.string.usePassword))
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();
        showBiometric();
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
                .positiveButtonAction(this::showBiometric)
                .show();

    }

    private void showBiometric() {
        biometricPrompt.authenticate(biometricPromptInfo);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
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
                    if (this.cancellationSignal != null) {
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

        if (this.cancellationSignal != null) {
            if (this.cancellationSignal.isCanceled()) {
                return;
            }
            this.cancellationSignal.cancel();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
