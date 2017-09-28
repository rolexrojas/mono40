package com.tpago.movil.d.ui.main.recipient.addition.banks;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tpago.movil.d.data.SchedulerProvider;
import com.tpago.movil.d.domain.api.ApiResult;
import com.tpago.movil.d.domain.api.DepApiBridge;
import com.tpago.movil.d.misc.rx.RxUtils;
import com.tpago.movil.d.ui.main.recipient.addition.RecipientCandidateListPresenter;
import com.tpago.movil.d.domain.Bank;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;


/**
 * @author hecvasro
 */
final class BankListPresenter extends RecipientCandidateListPresenter {

  private final DepApiBridge apiBridge;

  BankListPresenter(@NonNull SchedulerProvider schedulerProvider, @NonNull DepApiBridge apiBridge) {
    super(schedulerProvider);
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
    return this.apiBridge.banks()
      .map(new Func1<ApiResult<List<Bank>>, List<Bank>>() {
        @Override
        public List<Bank> call(ApiResult<List<Bank>> result) {
          if (result.isSuccessful()) {
            return result.getData();
          } else {
            return new ArrayList<>();
          }
        }
      })
      .compose(RxUtils.fromCollection())
      .filter(
        (bank) -> query == null
          || query.isEmpty()
          || bank.getName()
          .toUpperCase()
          .contains(query)
      )
      .toSortedList(Bank::compareTo)
      .compose(RxUtils.fromCollection())
      .cast(Object.class);
  }
}
