package com.tpago.movil.api;

import android.support.v4.util.Pair;
import com.tpago.movil.domain.Email;
import com.tpago.movil.domain.PhoneNumber;
import com.tpago.movil.Pin;
import com.tpago.movil.net.HttpResult;

import io.reactivex.Single;

/**
 * @author hecvasro
 */
@Deprecated
public interface DApiBridge {

  Single<HttpResult<DApiData<Integer>>> validatePhoneNumber(PhoneNumber phoneNumber);

  Single<HttpResult<DApiData<Pair<UserData, String>>>> signUp(
    PhoneNumber phoneNumber,
    Email email,
    String password,
    Pin pin);

  Single<HttpResult<DApiData<Pair<UserData, String>>>> signIn(
    PhoneNumber phoneNumber,
    Email email,
    String password,
    boolean shouldForce);
}
