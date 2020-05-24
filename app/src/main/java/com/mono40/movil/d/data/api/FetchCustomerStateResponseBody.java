package com.mono40.movil.d.data.api;

import com.mono40.movil.d.domain.Customer;

import rx.functions.Func1;

/**
 * @author hecvasro
 */
@Deprecated
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
