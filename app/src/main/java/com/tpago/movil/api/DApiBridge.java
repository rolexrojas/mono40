package com.tpago.movil.api;

import com.tpago.movil.Email;
import com.tpago.movil.PhoneNumber;
import com.tpago.movil.Pin;
import com.tpago.movil.net.HttpResult;

import io.reactivex.Single;

/**
 * @author hecvasro
 */
public interface DApiBridge {
  Single<HttpResult<DApiData<PhoneNumber.State>>> validatePhoneNumber(PhoneNumber phoneNumber);

  Single<HttpResult<DApiData<String>>> signUp(
    PhoneNumber phoneNumber,
    Email email,
    String password,
    Pin pin);

  Single<HttpResult<DApiData<String>>> signIn(
    PhoneNumber phoneNumber,
    Email email,
    String password,
    boolean shouldForce);
}
