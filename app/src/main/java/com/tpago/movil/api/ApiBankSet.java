package com.tpago.movil.api;

import com.google.gson.annotations.SerializedName;
import com.tpago.movil.domain.Bank;

import java.util.Set;

import io.reactivex.functions.Function;

/**
 * @author hecvasro
 */
final class ApiBankSet {
  static Function<ApiBankSet, Set<Bank>> mapperFunc() {
    return new Function<ApiBankSet, Set<Bank>>() {
      @Override
      public Set<Bank> apply(ApiBankSet apiBankSet) throws Exception {
        return apiBankSet.embedded.set;
      }
    };
  }

  @SerializedName("_embedded") Embedded embedded;

  static final class Embedded {
    @SerializedName("banks") Set<Bank> set;
  }
}
