package com.gbh.tpago.api;

import android.support.annotation.NonNull;

import com.gbh.tpago.Account;
import com.gbh.tpago.Balance;
import com.gbh.tpago.Bank;
import com.gbh.tpago.InitialData;

import java.util.List;

import retrofit2.http.Url;
import rx.Observable;

/**
 * TODO
 *
 * @author hecvasro
 */
public interface ApiBridge {
  /**
   * TODO
   *
   * @return TODO
   */
  @NonNull
  Observable<ApiResult<List<Bank>>> getAllBanks();

  /**
   * TODO
   *
   * @return TODO
   */
  @NonNull
  Observable<ApiResult<InitialData>> getInitialData();

  /**
   * TODO
   *
   * @param url TODO
   * @param account TODO
   * @param pin TODO
   *
   * @return TODO
   */
  @NonNull
  Observable<ApiResult<Balance>> getAccountBalance(@Url String url, @NonNull Account account,
    @NonNull String pin);
}
