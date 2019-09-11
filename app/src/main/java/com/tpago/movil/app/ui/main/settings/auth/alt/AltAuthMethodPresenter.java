package com.tpago.movil.app.ui.main.settings.auth.alt;

import android.app.Activity;
import android.graphics.Point;
import androidx.annotation.Nullable;
import android.view.Display;

import com.tpago.movil.app.StringMapper;
import com.tpago.movil.app.ui.Presenter;
import com.tpago.movil.app.ui.alert.AlertManager;
import com.tpago.movil.app.ui.loader.takeover.TakeoverLoader;
import com.tpago.movil.app.ui.main.code.CodeCreator;
import com.tpago.movil.reactivex.DisposableUtil;
import com.tpago.movil.session.CodeMethodKeyGenerator;
import com.tpago.movil.session.FingerprintMethodKeyGenerator;
import com.tpago.movil.session.SessionManager;
import com.tpago.movil.session.UnlockMethod;
import com.tpago.movil.session.UnlockMethodKeyGenerator;
import com.tpago.movil.util.BuilderChecker;
import com.tpago.movil.util.ObjectHelper;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * @author hecvasro
 */
public final class AltAuthMethodPresenter extends Presenter<AltAuthMethodPresentation> {

    static Builder builder() {
        return new Builder();
    }

    private final SessionManager sessionManager;

    private final CodeMethodKeyGenerator.Creator codeAltAuthMethodKeyGeneratorCreator;
    private final FingerprintMethodKeyGenerator fingerprintAltAuthMethodKeyGenerator;

    private final StringMapper stringMapper;
    private final AlertManager alertManager;
    private final TakeoverLoader takeoverLoader;
    private final CodeCreator codeCreator;

    private Disposable disposable = Disposables.disposed();

    private AltAuthMethodPresenter(Builder builder) {
        super(builder.presentation);

        this.sessionManager = builder.sessionManager;

        this.codeAltAuthMethodKeyGeneratorCreator = builder.codeAltAuthMethodKeyGeneratorCreator;
        this.fingerprintAltAuthMethodKeyGenerator = builder.fingerprintAltAuthMethodKeyGenerator;

        this.stringMapper = builder.stringMapper;
        this.alertManager = builder.alertManager;
        this.takeoverLoader = builder.takeoverLoader;
        this.codeCreator = builder.codeCreator;
    }

    private void handleCompletion() {
        this.presentation.finish();
    }

    private void handleError(Throwable throwable, String message, Object... argList) {
        Timber.e(throwable, message, argList);
        this.alertManager.showAlertForGenericFailure();
    }

    private void onEnableButtonClicked(UnlockMethodKeyGenerator generator) {
        this.disposable = this.sessionManager.enableUnlockMethod(generator)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe((d) -> this.takeoverLoader.show())
                .doOnTerminate(this.takeoverLoader::hide)
                .subscribe(
                        this::handleCompletion,
                        (throwable) -> this.handleError(
                                throwable,
                                "onEnableButtonClicked(%1$s)",
                                generator.method()
                        )
                );
    }

    final void onEnableFingerprintButtonClicked() {
        if (this.sessionManager.isUnlockMethodEnabled(UnlockMethod.FINGERPRINT)) {
            this.handleCompletion();
        } else {
            this.onEnableButtonClicked(this.fingerprintAltAuthMethodKeyGenerator);
        }
    }

    final void onEnableCodeButtonClicked(Activity activity) {
        if (this.sessionManager.isUnlockMethodEnabled(UnlockMethod.CODE)) {
            this.handleCompletion();
        } else {
            final Display display = activity.getWindowManager().getDefaultDisplay();
            final Point size = new Point();
            display.getSize(size);

            final int x = size.x / 2;
            final int y = size.y / 2;
            this.codeCreator.create(
                    CodeCreator.RequestType.SESSION_OPENING_METHOD,
                    (code) -> this.onEnableButtonClicked(this.codeAltAuthMethodKeyGeneratorCreator.create(code)),
                    x,
                    y
            );
        }
    }

    final void onDisableButtonClicked() {
        if (!this.sessionManager.isUnlockMethodEnabled()) {
            this.handleCompletion();
        } else {
            this.disposable = this.sessionManager.disableUnlockMethod()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe((d) -> this.takeoverLoader.show())
                    .doOnTerminate(this.takeoverLoader::hide)
                    .subscribe(
                            this::handleCompletion,
                            (throwable) -> this.handleError(throwable, "onDisableButtonClicked()")
                    );
        }
    }

    private void updateSelection() {
        final boolean isFingerprintSelected = this.sessionManager
                .isUnlockMethodEnabled(UnlockMethod.FINGERPRINT);
        boolean isCodeSelected = this.sessionManager
                .isUnlockMethodEnabled(UnlockMethod.CODE);

        this.presentation.setFingerprintOptionSelection(isFingerprintSelected);
        this.presentation.setCodeOptionSelection(isCodeSelected);
        this.presentation.setNoneOptionSelection(!isFingerprintSelected && !isCodeSelected);
    }

    @Override
    public void onPresentationResumed() {
        super.onPresentationResumed();

        this.updateSelection();
    }

    @Override
    public void onPresentationPaused() {
        DisposableUtil.dispose(this.disposable);

        super.onPresentationPaused();
    }

    static final class Builder {

        private SessionManager sessionManager;

        private CodeMethodKeyGenerator.Creator codeAltAuthMethodKeyGeneratorCreator;
        private FingerprintMethodKeyGenerator fingerprintAltAuthMethodKeyGenerator;

        private StringMapper stringMapper;
        private AlertManager alertManager;
        private TakeoverLoader takeoverLoader;
        private CodeCreator codeCreator;

        private AltAuthMethodPresentation presentation;

        private Builder() {
        }

        final Builder sessionManager(SessionManager sessionManager) {
            this.sessionManager = ObjectHelper.checkNotNull(sessionManager, "sessionManager");
            return this;
        }

        final Builder codeAltAuthMethodKeyGeneratorCreator(CodeMethodKeyGenerator.Creator creator) {
            this.codeAltAuthMethodKeyGeneratorCreator = ObjectHelper.checkNotNull(creator, "creator");
            return this;
        }

        final Builder fingerprintAltAuthMethodKeyGenerator(@Nullable FingerprintMethodKeyGenerator generator) {
            this.fingerprintAltAuthMethodKeyGenerator = generator;
            return this;
        }

        final Builder stringMapper(StringMapper stringMapper) {
            this.stringMapper = ObjectHelper.checkNotNull(stringMapper, "stringMapper");
            return this;
        }

        final Builder alertManager(AlertManager alertManager) {
            this.alertManager = ObjectHelper.checkNotNull(alertManager, "alertManager");
            return this;
        }

        final Builder takeoverLoader(TakeoverLoader takeoverLoader) {
            this.takeoverLoader = ObjectHelper.checkNotNull(takeoverLoader, "takeoverLoader");
            return this;
        }

        final Builder codeCreator(CodeCreator codeCreator) {
            this.codeCreator = ObjectHelper.checkNotNull(codeCreator, "codeCreator");
            return this;
        }

        final Builder presentation(AltAuthMethodPresentation presentation) {
            this.presentation = ObjectHelper.checkNotNull(presentation, "presentation");
            return this;
        }

        final AltAuthMethodPresenter build() {
            BuilderChecker.create()
                    .addPropertyNameIfMissing("sessionManager", ObjectHelper.isNull(this.sessionManager))
                    .addPropertyNameIfMissing(
                            "codeAltAuthMethodKeyGeneratorCreator",
                            ObjectHelper.isNull(this.codeAltAuthMethodKeyGeneratorCreator)
                    )
                    .addPropertyNameIfMissing("stringMapper", ObjectHelper.isNull(this.stringMapper))
                    .addPropertyNameIfMissing("alertManager", ObjectHelper.isNull(this.alertManager))
                    .addPropertyNameIfMissing("takeoverLoader", ObjectHelper.isNull(this.takeoverLoader))
                    .addPropertyNameIfMissing("codeCreator", ObjectHelper.isNull(this.codeCreator))
                    .addPropertyNameIfMissing("presentation", ObjectHelper.isNull(this.presentation))
                    .checkNoMissingProperties();

            return new AltAuthMethodPresenter(this);
        }
    }
}
