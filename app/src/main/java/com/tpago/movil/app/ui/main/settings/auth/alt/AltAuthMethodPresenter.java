package com.tpago.movil.app.ui.main.settings.auth.alt;

import com.tpago.movil.app.ui.AlertData;
import com.tpago.movil.app.ui.AlertManager;
import com.tpago.movil.app.ui.Presenter;
import com.tpago.movil.app.ui.loader.takeover.TakeoverLoader;
import com.tpago.movil.app.ui.main.code.CodeCreator;
import com.tpago.movil.data.StringMapper;
import com.tpago.movil.data.auth.alt.FingerprintAltAuthMethodKeyGenerator;
import com.tpago.movil.domain.auth.alt.AltAuthMethodKeyGenerator;
import com.tpago.movil.data.auth.alt.CodeAltAuthMethodKeyGenerator;
import com.tpago.movil.domain.auth.alt.AltAuthMethodManager;
import com.tpago.movil.domain.auth.alt.AltAuthMethod;
import com.tpago.movil.reactivex.DisposableHelper;
import com.tpago.movil.util.BuilderChecker;
import com.tpago.movil.util.ObjectHelper;

import io.reactivex.Completable;
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

  private final AltAuthMethodManager altAuthMethodManager;

  private final CodeAltAuthMethodKeyGenerator.Creator codeAltAuthMethodKeyGeneratorCreator;
  private final FingerprintAltAuthMethodKeyGenerator fingerprintAltAuthMethodKeyGenerator;

  private final StringMapper stringMapper;
  private final AlertManager alertManager;
  private final TakeoverLoader takeoverLoader;
  private final CodeCreator codeCreator;

  private Disposable disposable = Disposables.disposed();

  private AltAuthMethodPresenter(Builder builder) {
    super(builder.presentation);

    this.altAuthMethodManager = builder.altAuthMethodManager;

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

  private void onEnableButtonClicked(AltAuthMethodKeyGenerator generator) {
    this.disposable = this.altAuthMethodManager.enable(generator)
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
    if (this.altAuthMethodManager.getActiveMethod() == AltAuthMethod.FINGERPRINT) {
      this.handleCompletion();
    } else {
      this.onEnableButtonClicked(this.fingerprintAltAuthMethodKeyGenerator);
    }
  }

  final void onEnableCodeButtonClicked() {
    if (this.altAuthMethodManager.getActiveMethod() == AltAuthMethod.CODE) {
      this.handleCompletion();
    } else {
      this.codeCreator.create(
        CodeCreator.RequestType.ALT_AUTH,
        (code) -> this.onEnableButtonClicked(this.codeAltAuthMethodKeyGeneratorCreator.create(code))
      );
    }
  }

  final void onDisableButtonClicked() {
    if (!this.altAuthMethodManager.isEnabled()) {
      this.handleCompletion();
    } else {
      this.disposable = this.altAuthMethodManager.disable()
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
    final AltAuthMethod method = this.altAuthMethodManager.getActiveMethod();

    boolean isFingerprintSelected = false;
    boolean isCodeSelected = false;

    if (method == AltAuthMethod.FINGERPRINT) {
      isFingerprintSelected = true;
    } else if (method == AltAuthMethod.CODE) {
      isCodeSelected = true;
    }

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

    private AltAuthMethodManager altAuthMethodManager;
    private CodeAltAuthMethodKeyGenerator.Creator codeAltAuthMethodKeyGeneratorCreator;
    private FingerprintAltAuthMethodKeyGenerator fingerprintAltAuthMethodKeyGenerator;

    private StringMapper stringMapper;
    private AlertManager alertManager;
    private TakeoverLoader takeoverLoader;
    private CodeCreator codeCreator;

    private AltAuthMethodPresentation presentation;

    private Builder() {
    }

    final Builder altAuthMethodManager(AltAuthMethodManager manager) {
      this.altAuthMethodManager = ObjectHelper.checkNotNull(manager, "manager");
      return this;
    }

    final Builder codeAltAuthMethodKeyGeneratorCreator(CodeAltAuthMethodKeyGenerator.Creator creator) {
      this.codeAltAuthMethodKeyGeneratorCreator = ObjectHelper.checkNotNull(creator, "creator");
      return this;
    }

    final Builder fingerprintAltAuthMethodKeyGenerator(FingerprintAltAuthMethodKeyGenerator generator) {
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
        .addPropertyNameIfMissing(
          "altAuthMethodManager",
          ObjectHelper.isNull(this.altAuthMethodManager)
        )
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
