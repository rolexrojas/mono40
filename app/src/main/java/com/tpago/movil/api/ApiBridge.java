package com.tpago.movil.api;

import com.tpago.movil.PhoneNumber;
import com.tpago.movil.net.HttpResult;

import io.reactivex.Single;

/**
 * @author hecvasro
 */
public interface ApiBridge {
  Single<HttpResult<ApiData<PhoneNumber.State>>> validatePhoneNumber(PhoneNumber phoneNumber);
}
