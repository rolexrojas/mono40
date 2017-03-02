package com.tpago.movil.dep.ui.main.recipients.partners;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tpago.movil.Partner;
import com.tpago.movil.dep.data.SchedulerProvider;
import com.tpago.movil.dep.domain.api.ApiResult;
import com.tpago.movil.dep.domain.api.DepApiBridge;
import com.tpago.movil.dep.domain.session.SessionManager;
import com.tpago.movil.dep.misc.rx.RxUtils;
import com.tpago.movil.dep.ui.main.recipients.RecipientCandidateListPresenter;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscription;
import rx.functions.Func1;
import rx.subscriptions.Subscriptions;

/**
 * TODO
 *
 * @author hecvasro
 */
class PartnerListPresenter extends RecipientCandidateListPresenter {
  private final SessionManager sessionManager;
  private final DepApiBridge apiBridge;

  PartnerListPresenter(
    @NonNull SchedulerProvider schedulerProvider,
    @NonNull SessionManager sessionManager,
    @NonNull DepApiBridge apiBridge) {
    super(schedulerProvider);
    this.sessionManager = sessionManager;
    this.apiBridge = apiBridge;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected boolean canStartListeningQueryChangeEvents() {
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @NonNull
  @Override
  protected Observable<Object> search(@Nullable String query) {
    return apiBridge.partners(sessionManager.getSession().getAuthToken())
      .map(new Func1<ApiResult<List<Partner>>, List<Partner>>() {
        @Override
        public List<Partner> call(ApiResult<List<Partner>> result) {
          if (result.isSuccessful()) {
            return result.getData();
          } else {
            return new ArrayList<>();
          }
        }
      })
      .compose(RxUtils.<Partner>fromCollection())
      .cast(Object.class);
  }
}
