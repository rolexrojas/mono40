package com.tpago.movil.api;

import com.tpago.movil.Email;
import com.tpago.movil.PhoneNumber;
import com.tpago.movil.Pin;
import com.tpago.movil.net.HttpResult;

import io.reactivex.Single;

/**
 * @author hecvasro
 */
public interface ApiBridge {
  Single<HttpResult<ApiData<PhoneNumber.State>>> validatePhoneNumber(PhoneNumber phoneNumber);

  Single<HttpResult<ApiData<String>>> signUp(
    PhoneNumber phoneNumber,
    Email email,
    String password,
    Pin pin);

  Single<HttpResult<ApiData<String>>> signIn(
    PhoneNumber phoneNumber,
    Email email,
    String password,
    boolean shouldForce);
}
