package com.tpago.movil.app.ui.main.settings.auth.alt;

import com.tpago.movil.app.ui.AlertData;
import com.tpago.movil.app.ui.AlertManager;
import com.tpago.movil.app.ui.Presenter;
import com.tpago.movil.app.ui.TakeoverLoader;
import com.tpago.movil.data.StringMapper;
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

  private final StringMapper stringMapper;
  private final AlertManager alertManager;
  private final TakeoverLoader takeoverLoader;

  private Disposable disposable = Disposables.disposed();

  private AltAuthMethodPresenter(Builder builder) {
    super(builder.presentation);

    this.altAuthMethodManager = builder.altAuthMethodManager;

    this.codeAltAuthMethodKeyGeneratorCreator = builder.codeAltAuthMethodKeyGeneratorCreator;

    this.stringMapper = builder.stringMapper;
    this.alertManager = builder.alertManager;
    this.takeoverLoader = builder.takeoverLoader;
  }

  private void handleCompletion() {
    this.updateSelection();

    // TODO: Go back
  }

  private void handleError(Throwable throwable, String message) {
    Timber.e(throwable, message);

    this.alertManager.show(AlertData.genericFailureData(this.stringMapper));
  }

  private void enableMethod(AltAuthMethodKeyGenerator generator) {
    final Completable completable;
    if (this.altAuthMethodManager.isEnabled()) {
      completable = this.altAuthMethodManager.disable()
        .concatWith(this.altAuthMethodManager.enable(generator));
    } else {
      completable = this.altAuthMethodManager.enable(generator);
    }
    this.disposable = completable
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(
        this::handleCompletion,
        (throwable) -> this.handleError(throwable, "Enabling alt auth method")
      );
  }

  final void onFingerprintOptionClicked() {
    if (this.altAuthMethodManager.getActiveMethod() == AltAuthMethod.FINGERPRINT) {
      this.handleCompletion();
    } else {
      // TODO: Ask for permissions, create key generator, and enable method.
    }
  }

  final void onCodeOptionClicked() {
    if (this.altAuthMethodManager.getActiveMethod() == AltAuthMethod.CODE) {
      this.handleCompletion();
    } else {
      // TODO: Ask for code, create key generator, and enable method.
    }
  }

  final void onNoneOptionClicked() {
    if (this.altAuthMethodManager.isEnabled()) {
      this.handleCompletion();
    } else {
      this.disposable = this.altAuthMethodManager.disable()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSubscribe((d) -> this.takeoverLoader.show())
        .doOnComplete(this.takeoverLoader::hide)
        .subscribe(
          this::handleCompletion,
          (throwable) -> this.handleError(throwable, "Disabling alt auth method")
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

    this.presentation.setFingerprintAlhAuthMethodOptionVisibility(false);

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

    private StringMapper stringMapper;
    private AlertManager alertManager;
    private TakeoverLoader takeoverLoader;

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
        .addPropertyNameIfMissing("presentation", ObjectHelper.isNull(this.presentation))
        .checkNoMissingProperties();

      return new AltAuthMethodPresenter(this);
    }
  }
}
