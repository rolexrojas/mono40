package com.tpago.movil.app.ui.main.transaction.insurance.micro.index;

import android.net.Uri;

import com.tpago.movil.R;
import com.tpago.movil.api.Api;
import com.tpago.movil.app.StringMapper;
import com.tpago.movil.app.ui.Presenter;
import com.tpago.movil.app.ui.alert.AlertManager;
import com.tpago.movil.app.ui.loader.takeover.TakeoverLoader;
import com.tpago.movil.app.ui.main.transaction.insurance.micro.purchase.MicroInsurancePurchase;
import com.tpago.movil.app.ui.main.transaction.item.IndexItem;
import com.tpago.movil.company.Company;
import com.tpago.movil.company.CompanyHelper;
import com.tpago.movil.company.partner.Partner;
import com.tpago.movil.company.partner.PartnerStore;
import com.tpago.movil.insurance.micro.MicroInsurancePartner;
import com.tpago.movil.insurance.micro.MicroInsurancePartnerStore;
import com.tpago.movil.reactivex.DisposableUtil;
import com.tpago.movil.util.BuilderChecker;
import com.tpago.movil.util.ObjectHelper;
import com.tpago.movil.util.UriBuilder;

import java.net.URI;
import java.util.ArrayList;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public final class MicroInsuranceIndexPresenter extends Presenter<MicroInsuranceIndexPresentation> {

  static Builder builder() {
    return new Builder();
  }

  private final MicroInsurancePartnerStore store;

  private final Api api;

  private PartnerStore partnerStore; // TODO: INJECT DIRECTLY HERE

  private CompanyHelper companyHelper;

  private final StringMapper stringMapper;
  private final AlertManager alertManager;
  private final TakeoverLoader takeoverLoader;

  private CompositeDisposable disposables;

  private MicroInsuranceIndexPresenter(Builder builder) {
    super(builder.presentation);

    this.store = builder.store;

    this.api = builder.api;

    this.stringMapper = builder.stringMapper;
    this.alertManager = builder.alertManager;
    this.takeoverLoader = builder.takeoverLoader;
  }

  private void handleError(Throwable throwable) {
    Timber.e(throwable);

    this.alertManager.showAlertForGenericFailure();
  }

  private void startTransaction(MicroInsurancePartner partner) {
    final Disposable disposable = this.api.fetchMicroInsurancePlans(partner)
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .doOnSubscribe((d) -> this.takeoverLoader.show())
      .doFinally(this.takeoverLoader::hide)
      .subscribe((plans) -> {
        final MicroInsurancePurchase purchase = MicroInsurancePurchase.builder()
          .partner(partner)
          .plans(plans)
          .build();
        this.presentation.startTransaction(purchase);
      }, this::handleError);
    this.disposables.add(disposable);
  }

  // TODO: DELETE WHEN THE INJECTION BECOME DIRECT
  public void onPresentationResumedWithPartners(PartnerStore partnerStore, CompanyHelper companyHelper) {
    this.partnerStore = partnerStore;
    this.companyHelper = companyHelper;
    this.onPresentationResumed();
  }

  @Override
  public void onPresentationResumed() {
    this.disposables = new CompositeDisposable();
    final Disposable disposable = this.store.getAll()
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .doOnSubscribe((d) -> this.takeoverLoader.show())
      .doFinally(this.takeoverLoader::hide)
      .flatMapObservable(Observable::fromIterable)
      .map((partner) ->
        IndexItem.builder()
          .titleText(partner.name())
          .actionText(this.stringMapper.apply(R.string.buy))
          .subtitleText(null)
          .pictureUri(UriBuilder.createFromPartners(this.partnerStore, this.companyHelper, partner.id()))
          .actionRunner(() -> this.startTransaction(partner))
          .build()
      )
      .toSortedList()
      .subscribe(this.presentation::setItems, this::handleError);
    this.disposables.add(disposable);
  }

  @Override
  public void onPresentationPaused() {
    DisposableUtil.dispose(this.disposables);
  }

  static final class Builder {

    private MicroInsurancePartnerStore store;

    private Api api;

    private StringMapper stringMapper;
    private AlertManager alertManager;
    private TakeoverLoader takeoverLoader;

    private MicroInsuranceIndexPresentation presentation;

    private Builder() {
    }

    final Builder store(MicroInsurancePartnerStore store) {
      this.store = ObjectHelper.checkNotNull(store, "store");
      return this;
    }

    final Builder api(Api api) {
      this.api = ObjectHelper.checkNotNull(api, "api");
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

    final Builder presentation(MicroInsuranceIndexPresentation presentation) {
      this.presentation = ObjectHelper.checkNotNull(presentation, "presentation");
      return this;
    }

    final MicroInsuranceIndexPresenter build() {
      BuilderChecker.create()
        .addPropertyNameIfMissing("store", ObjectHelper.isNull(this.store))
        .addPropertyNameIfMissing("api", ObjectHelper.isNull(this.api))
        .addPropertyNameIfMissing("stringMapper", ObjectHelper.isNull(this.stringMapper))
        .addPropertyNameIfMissing("alertManager", ObjectHelper.isNull(this.alertManager))
        .addPropertyNameIfMissing("takeoverLoader", ObjectHelper.isNull(this.takeoverLoader))
        .addPropertyNameIfMissing("presentation", ObjectHelper.isNull(this.presentation))
        .checkNoMissingProperties();
      return new MicroInsuranceIndexPresenter(this);
    }
  }
}
