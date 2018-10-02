package com.tpago.movil.app.ui.main.transaction.disburse.index;

import com.tpago.movil.R;
import com.tpago.movil.app.ui.Presenter;
import com.tpago.movil.app.ui.alert.AlertManager;
import com.tpago.movil.app.ui.main.transaction.item.IndexItem;
import com.tpago.movil.reactivex.DisposableUtil;
import com.tpago.movil.util.BuilderChecker;
import com.tpago.movil.util.ObjectHelper;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;
import io.reactivex.schedulers.Schedulers;

/**
 * @author hecvasro
 */
final class PresenterDisburseIndex extends Presenter<PresentationDisburseIndex> {

  static Builder builder() {
    return new Builder();
  }

  private final AlertManager alertManager;

  private final DisburseItemsSupplier itemsSupplier;

  private Disposable disposable = Disposables.disposed();

  private PresenterDisburseIndex(Builder builder) {
    super(builder.presentation);

    this.alertManager = builder.alertManager;

    this.itemsSupplier = builder.itemsSupplier;
  }

  @Override
  public void onPresentationResumed() {
    this.disposable = this.itemsSupplier.get()
      .subscribeOn(Schedulers.io())
      .toSortedList(IndexItem::compareTo)
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe((items) -> {
        if (items.isEmpty()) {
          this.alertManager.builder()
            .message(R.string.thereIsNoDisbursableItemsAvailable)
            .positiveButtonAction(this.presentation::goBack)
            .show();
        } else {
          this.presentation.setItems(items);
        }
      });
  }

  @Override
  public void onPresentationPaused() {
    DisposableUtil.dispose(this.disposable);
  }

  static final class Builder {

    private AlertManager alertManager;
    private DisburseItemsSupplier itemsSupplier;

    private PresentationDisburseIndex presentation;

    private Builder() {
    }

    final Builder alertManager(AlertManager alertManager) {
      this.alertManager = ObjectHelper.checkNotNull(alertManager, "alertManager");
      return this;
    }

    final Builder itemsSupplier(DisburseItemsSupplier itemSupplier) {
      this.itemsSupplier = ObjectHelper.checkNotNull(itemSupplier, "itemsSupplier");
      return this;
    }

    final Builder presentation(PresentationDisburseIndex presentation) {
      this.presentation = ObjectHelper.checkNotNull(presentation, "presentation");
      return this;
    }

    final PresenterDisburseIndex build() {
      BuilderChecker.create()
        .addPropertyNameIfMissing("alertManager", ObjectHelper.isNull(this.alertManager))
        .addPropertyNameIfMissing("itemsSupplier", ObjectHelper.isNull(this.itemsSupplier))
        .addPropertyNameIfMissing("presentation", ObjectHelper.isNull(this.presentation))
        .checkNoMissingProperties();
      return new PresenterDisburseIndex(this);
    }
  }
}
