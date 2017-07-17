package com.tpago.movil.d.data.api;

import com.tpago.movil.d.domain.Customer;

import rx.functions.Func1;

/**
 * @author hecvasro
 */
final class FetchCustomerStateResponseBody {
  public static Func1<FetchCustomerStateResponseBody, Customer.State> mapFunc() {
    return new Func1<FetchCustomerStateResponseBody, Customer.State>() {
      @Override
      public Customer.State call(FetchCustomerStateResponseBody fetchCustomerStateResponseBody) {
        return fetchCustomerStateResponseBody.status;
      }
    };
  }

  Customer.State status;
}
