package com.tpago.movil.api;

import com.google.gson.annotations.SerializedName;
import com.tpago.movil.d.domain.Bank;

import java.util.Set;

import io.reactivex.functions.Function;

/**
 * @author hecvasro
 */
final class FetchBankSetResponseBody {
  static Function<FetchBankSetResponseBody, Set<Bank>> mapperFunc() {
    return new Function<FetchBankSetResponseBody, Set<Bank>>() {
      @Override
      public Set<Bank> apply(FetchBankSetResponseBody fetchBankSetResponseBody) throws Exception {
        return fetchBankSetResponseBody.embedded.set;
      }
    };
  }

  @SerializedName("_embedded") Embedded embedded;

  static final class Embedded {
    @SerializedName("banks") Set<Bank> set;
  }
}
