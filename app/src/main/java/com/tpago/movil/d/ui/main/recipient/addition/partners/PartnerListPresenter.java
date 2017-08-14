package com.tpago.movil.d.ui.main.recipient.addition.partners;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tpago.movil.Partner;
import com.tpago.movil.d.data.SchedulerProvider;
import com.tpago.movil.d.domain.api.ApiResult;
import com.tpago.movil.d.domain.api.DepApiBridge;
import com.tpago.movil.d.domain.session.SessionManager;
import com.tpago.movil.d.misc.rx.RxUtils;
import com.tpago.movil.d.ui.main.recipient.addition.RecipientCandidateListPresenter;
import com.tpago.movil.util.Objects;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

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
  protected Observable<Object> search(@Nullable final String query) {
    return apiBridge.partners(sessionManager.getSession().getAuthToken())
      .map(new Func1<ApiResult<List<Partner>>, List<Partner>>() {
        @Override
        public List<Partner> call(ApiResult<List<Partner>> result) {
          final List<Partner> filteredList = new ArrayList<>();
          if (result.isSuccessful()) {
            for (Partner partner : result.getData()) {
              if (Objects.checkIfNull(query)
                || partner.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(partner);
              }
            }
            Partner.sort(filteredList);
          }
          return filteredList;
        }
      })
      .compose(RxUtils.<Partner>fromCollection())
      .cast(Object.class);
  }
}
