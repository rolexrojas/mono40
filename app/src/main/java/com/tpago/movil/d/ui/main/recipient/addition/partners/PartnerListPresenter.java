package com.tpago.movil.d.ui.main.recipient.addition.partners;

import static com.tpago.movil.dep.Partner.TYPE_PROVIDER;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tpago.movil.dep.Partner;
import com.tpago.movil.d.data.SchedulerProvider;
import com.tpago.movil.d.domain.api.ApiResult;
import com.tpago.movil.d.domain.api.DepApiBridge;
import com.tpago.movil.d.domain.session.SessionManager;
import com.tpago.movil.d.misc.rx.RxUtils;
import com.tpago.movil.d.ui.main.recipient.addition.RecipientCandidateListPresenter;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;

/**
 * TODO
 *
 * @author hecvasro
 */
final class PartnerListPresenter extends RecipientCandidateListPresenter {

  private final SessionManager sessionManager;
  private final DepApiBridge apiBridge;

  PartnerListPresenter(
    @NonNull SchedulerProvider schedulerProvider,
    @NonNull SessionManager sessionManager,
    @NonNull DepApiBridge apiBridge
  ) {
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
  protected Observable<Object> search(@Nullable final String query) {
    return this.apiBridge.partners(
      this.sessionManager.getSession()
        .getAuthToken()
    )
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
      .filter(new Func1<Partner, Boolean>() {
        @Override
        public Boolean call(Partner partner) {
          return partner.getType()
            .equals(TYPE_PROVIDER);
        }
      })
      .filter(new Func1<Partner, Boolean>() {
        @Override
        public Boolean call(Partner partner) {
          return query == null
            || query.isEmpty()
            || partner.getName()
            .toUpperCase()
            .contains(query.toUpperCase());
        }
      })
      .toSortedList(new Func2<Partner, Partner, Integer>() {
        @Override
        public Integer call(Partner pA, Partner pB) {
          return pA.getName()
            .toUpperCase()
            .compareTo(
              pB.getName()
                .toUpperCase()
            );
        }
      })
      .compose(RxUtils.<Partner>fromCollection())
      .cast(Object.class);
  }
}
