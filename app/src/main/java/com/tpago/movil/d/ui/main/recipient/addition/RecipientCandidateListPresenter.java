package com.tpago.movil.d.ui.main.recipient.addition;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tpago.movil.d.ui.Presenter;
import com.tpago.movil.d.ui.misc.UiUtils;
import com.tpago.movil.d.ui.main.list.NoResultsListItemItem;
import com.tpago.movil.reactivex.DisposableUtil;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;
import io.reactivex.schedulers.Schedulers;

/**
 * @author hecvasro
 */
public abstract class RecipientCandidateListPresenter
  extends Presenter<RecipientCandidateListScreen> {

  private static final long DEFAULT_TIME_SPAN_QUERY = 300L; // 0.3 seconds.

  private Disposable queryDisposable = Disposables.disposed();
  private Disposable searchDisposable = Disposables.disposed();

  public RecipientCandidateListPresenter() {
  }

  protected abstract boolean canStartListeningQueryChangeEvents();

  @NonNull
  protected abstract Observable<Object> search(@Nullable String query);

  protected final void startListeningQueryChangeEvents() {
    if (this.queryDisposable.isDisposed()) {
      this.queryDisposable = this.screen.onQueryChanged()
        .debounce(DEFAULT_TIME_SPAN_QUERY, TimeUnit.MILLISECONDS)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe((query) -> {
          DisposableUtil.dispose(this.searchDisposable);
          this.searchDisposable = this.search(query)
            .subscribeOn(Schedulers.io())
            .switchIfEmpty(Observable.just(new NoResultsListItemItem(query)))
            .observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())
            .doOnSubscribe((disposable) -> {
              this.screen.clear();
              UiUtils.showRefreshIndicator(this.screen);
            })
            .doFinally(() -> UiUtils.hideRefreshIndicator(this.screen))
            .subscribe(this.screen::add);
        });
    }
  }

  final void start() {
    if (this.canStartListeningQueryChangeEvents()) {
      this.startListeningQueryChangeEvents();
    }
  }

  final void stop() {
    DisposableUtil.dispose(this.searchDisposable);
    DisposableUtil.dispose(this.queryDisposable);
  }
}
