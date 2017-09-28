package com.tpago.movil.dep.api;

import com.tpago.movil.d.domain.Bank;
import com.tpago.movil.d.domain.Result;
import com.tpago.movil.dep.MockData;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import io.reactivex.Single;

final class MockApiService implements ApiService {

  public static MockApiService create() {
    return new MockApiService();
  }

  private MockApiService() {
  }

  @Override
  public Single<Result<Set<Bank>, ApiCode>> fetchBankSet() {
    return Single.just(Result.<Set<Bank>, ApiCode>create(MockData.BANK_SET))
      .delay(1L, TimeUnit.SECONDS);
  }
}
