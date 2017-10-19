package com.tpago.movil.app.ui.main.settings.auth.alt;

import com.tpago.movil.app.ui.AlertData;
import com.tpago.movil.app.ui.AlertManager;
import com.tpago.movil.app.ui.Presenter;
import com.tpago.movil.app.ui.loader.takeover.TakeoverLoader;
import com.tpago.movil.app.ui.main.code.CodeCreator;
import com.tpago.movil.data.StringMapper;
import com.tpago.movil.session.CodeSessionOpeningMethodKeyGenerator;
import com.tpago.movil.session.FingerprintSessionOpeningMethodKeyGenerator;
import com.tpago.movil.session.SessionManager;
import com.tpago.movil.session.SessionOpeningMethodKeyGenerator;
import com.tpago.movil.session.SessionOpeningMethod;
import com.tpago.movil.reactivex.DisposableHelper;
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

  private final CodeSessionOpeningMethodKeyGenerator.Creator codeAltAuthMethodKeyGeneratorCreator;
  private final FingerprintSessionOpeningMethodKeyGenerator fingerprintAltAuthMethodKeyGenerator;

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
    this.alertManager.show(AlertData.createForGenericFailure(this.stringMapper));
  }

  private void onEnableButtonClicked(SessionOpeningMethodKeyGenerator generator) {
    this.disposable = this.sessionManager.enableSessionOpeningMethod(generator)
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
    if (this.sessionManager.isSessionOpeningMethodEnabled(SessionOpeningMethod.FINGERPRINT)) {
      this.handleCompletion();
    } else {
      this.onEnableButtonClicked(this.fingerprintAltAuthMethodKeyGenerator);
    }
  }

  final void onEnableCodeButtonClicked() {
    if (this.sessionManager.isSessionOpeningMethodEnabled(SessionOpeningMethod.CODE)) {
      this.handleCompletion();
    } else {
      this.codeCreator.create(
        CodeCreator.RequestType.ALT_AUTH,
        (code) -> this.onEnableButtonClicked(this.codeAltAuthMethodKeyGeneratorCreator.create(code))
      );
    }
  }

  final void onDisableButtonClicked() {
    if (!this.sessionManager.isSessionOpeningMethodEnabled()) {
      this.handleCompletion();
    } else {
      this.disposable = this.sessionManager.disableSessionOpeningMethod()
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
      .isSessionOpeningMethodEnabled(SessionOpeningMethod.FINGERPRINT);
    boolean isCodeSelected = this.sessionManager
      .isSessionOpeningMethodEnabled(SessionOpeningMethod.CODE);

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
    DisposableHelper.dispose(this.disposable);

    super.onPresentationPaused();
  }

  static final class Builder {

    private SessionManager sessionManager;

    private CodeSessionOpeningMethodKeyGenerator.Creator codeAltAuthMethodKeyGeneratorCreator;
    private FingerprintSessionOpeningMethodKeyGenerator fingerprintAltAuthMethodKeyGenerator;

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

    final Builder codeAltAuthMethodKeyGeneratorCreator(CodeSessionOpeningMethodKeyGenerator.Creator creator) {
      this.codeAltAuthMethodKeyGeneratorCreator = ObjectHelper.checkNotNull(creator, "creator");
      return this;
    }

    final Builder fingerprintAltAuthMethodKeyGenerator(FingerprintSessionOpeningMethodKeyGenerator generator) {
      this.fingerprintAltAuthMethodKeyGenerator = ObjectHelper.checkNotNull(generator, "generator");
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
