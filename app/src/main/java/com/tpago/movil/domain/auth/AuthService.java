package com.tpago.movil.domain.auth;

import com.tpago.movil.domain.Email;
import com.tpago.movil.domain.PhoneNumber;
import com.tpago.movil.domain.user.User;
import com.tpago.movil.util.Result;

import io.reactivex.Single;

public interface AuthService {

  Single<Result<User>> signIn(
    PhoneNumber phoneNumber,
    Email username,
    String deviceId
  );
}
