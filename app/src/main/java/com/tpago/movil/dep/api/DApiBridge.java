package com.tpago.movil.dep.api;

import android.support.v4.util.Pair;
import com.tpago.movil.Email;
import com.tpago.movil.PhoneNumber;
import com.tpago.movil.dep.Pin;
import com.tpago.movil.dep.net.HttpResult;

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
