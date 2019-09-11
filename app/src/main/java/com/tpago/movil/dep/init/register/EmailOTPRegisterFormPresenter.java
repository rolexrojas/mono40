package com.tpago.movil.dep.init.register;

import com.tpago.movil.Code;
import com.tpago.movil.R;
import com.tpago.movil.api.Api;
import com.tpago.movil.api.RetrofitApiEmailRequestVerificationCodeResponse;
import com.tpago.movil.app.StringMapper;
import com.tpago.movil.app.ui.alert.AlertManager;
import com.tpago.movil.app.ui.loader.takeover.TakeoverLoader;
import com.tpago.movil.data.DeviceIdSupplier;
import com.tpago.movil.dep.Presenter;
import com.tpago.movil.dep.reactivex.Disposables;
import com.tpago.movil.reactivex.DisposableUtil;
import com.tpago.movil.session.SessionManager;
import com.tpago.movil.util.ObjectHelper;
import com.tpago.movil.util.Result;
import com.tpago.movil.util.digit.Digit;
import com.tpago.movil.util.digit.DigitUtil;
import com.tpago.movil.util.digit.DigitValueCreator;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * @author hecvasro
 */
public final class EmailOTPRegisterFormPresenter extends Presenter<EmailOTPRegisterFormPresenter.View> {

    @Inject
    AlertManager alertManager;
    @Inject
    DeviceIdSupplier deviceIdSupplier;
    @Inject
    RegisterData registerData;
    @Inject
    SessionManager sessionManager;
    @Inject
    StringMapper stringMapper;
    @Inject
    TakeoverLoader takeoverLoader;
    @Inject
    Api api;

    private List<Integer> oneTimePasswordDigits = new ArrayList<>();
    private DigitValueCreator<Code> pinCreator;
    private Disposable disposable = Disposables.disposed();
    private boolean isOneTimePasswordValid = false;

    EmailOTPRegisterFormPresenter(View view, RegisterComponent component) {
        super(view);

        // Injects all annotated dependencies.
        ObjectHelper.checkNotNull(component, "component")
                .inject(this);
    }

    private void handleSuccess(Result result) {
        if (result.isSuccessful()) {
            this.registerData.setSubmitted(true);

            this.view.moveToNextScreen();
        } else {
            this.registerData.setSubmitted(false);

            final com.tpago.movil.util.FailureData failureData = result.failureData();
            this.alertManager.builder()
                    .message(failureData.description())
                    .show();
        }
    }

    private void handleError(Throwable throwable) {
        Timber.e(throwable, "Signing up");
        this.registerData.setSubmitted(false);
        this.alertManager.showAlertForGenericFailure();
    }

    private void updateView() {
        final String oneTimePassword = DigitUtil.toDigitString(this.oneTimePasswordDigits);
        this.isOneTimePasswordValid = oneTimePassword.length() == 6;
        if (ObjectHelper.isNotNull(this.view)) {
            this.view.setTextInputContent(oneTimePassword);
            if (isOneTimePasswordValid) {
                this.api.verifyEmailOneTimePasswordActivationCode(this.registerData.getPhoneNumber().value(), this.registerData.getEmail().value(), oneTimePassword)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(this::showTakeoverLoader)
                        .doFinally(this::hideTakeoverLoader)
                        .subscribe((s) -> handleSuccess(s), this::handleError);
            }
        }
    }

    final void onDigitButtonClicked(@Digit int digit) {
        if (oneTimePasswordDigits.size() < 6) {
            oneTimePasswordDigits.add(digit);
            this.updateView();
        }
    }

    final void onDeleteButtonClicked() {
        if (!oneTimePasswordDigits.isEmpty()) {
            oneTimePasswordDigits.remove(oneTimePasswordDigits.size() - 1);
            this.updateView();
        }
    }


    private void handleRequest(Result<RetrofitApiEmailRequestVerificationCodeResponse> response) {
        if (response.isSuccessful()) {
            this.alertManager.builder()
                    .title(response.successData().title())
                    .message(response.successData().message())
                    .positiveButtonText("Ok")
                    .build()
                    .show();
        } else {
            this.alertManager.builder()
                    .title(this.stringMapper.apply(R.string.weAreSorry))
                    .message(response.failureData().description())
                    .positiveButtonText("Ok")
                    .build()
                    .show();
        }
    }

    public void requestVerificationCode() {
        this.api.requestEmailOneTimePasswordActivationCode(this.registerData.getPhoneNumber().value(), this.registerData.getEmail().value())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(this::showTakeoverLoader)
                .doFinally(this::hideTakeoverLoader)
                .subscribe((s) -> handleRequest(s), this::handleError);
    }


    private void showTakeoverLoader(Disposable disposable) {
        this.takeoverLoader.show();
    }

    private void hideTakeoverLoader() {
        this.takeoverLoader.hide();
    }

    @Override
    public void onViewStarted() {
        super.onViewStarted();
        this.updateView();
    }

    @Override
    public void onViewStopped() {
        DisposableUtil.dispose(this.disposable);

        super.onViewStopped();
    }

    interface View extends Presenter.View {

        void setTextInputContent(String content);

        void moveToNextScreen();
    }
}
