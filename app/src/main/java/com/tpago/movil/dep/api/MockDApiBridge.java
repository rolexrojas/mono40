package com.tpago.movil.dep.api;

import android.support.v4.util.Pair;

import com.tpago.movil.dep.Pin;
import com.tpago.movil.dep.net.HttpCode;
import com.tpago.movil.dep.net.HttpResult;
import com.tpago.movil.Email;
import com.tpago.movil.PhoneNumber;
import com.tpago.movil.util.DigitHelper;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import io.reactivex.Single;

final class MockDApiBridge implements DApiBridge {

  static MockDApiBridge create() {
    return new MockDApiBridge();
  }

  private MockDApiBridge() {
  }

  @Override
  public Single<HttpResult<DApiData<Integer>>> validatePhoneNumber(PhoneNumber phoneNumber) {
    final int state;
    final List<Integer> digitList = DigitHelper.toDigitList(phoneNumber.value());
    final int digitListSize = digitList.size();
    if (digitList.get(digitListSize - 1) % 2 == 0) {
      if (digitList.get(digitListSize - 2) % 2 == 0) {
        state = PhoneNumber.State.REGISTERED;
      } else {
        state = PhoneNumber.State.AFFILIATED;
      }
    } else {
      state = PhoneNumber.State.NONE;
    }
    return Single.just(HttpResult.create(HttpCode.OK, DApiData.create(state)))
      .delay(1L, TimeUnit.SECONDS);
  }

  private Single<HttpResult<DApiData<Pair<UserData, String>>>> sign(
    PhoneNumber phoneNumber,
    Email email
  ) {
    final UserData userData = UserData.createBuilder()
      .id(1)
      .phoneNumber(phoneNumber.value())
      .email(email.value())
      .firstName("Nelson")
      .lastName("Mandela")
      .pictureUri(
        "https://www.biography.com/.image/t_share/MTIwNjA4NjMzOTAyODkyNTU2/nelson-mandela-9397017-1-402.jpg"
      )
      .build();
    final String authToken = UUID.randomUUID()
      .toString();
    return Single.just(HttpResult.create(
      HttpCode.OK,
      DApiData.create(Pair.create(userData, authToken))
    ))
      .delay(1L, TimeUnit.SECONDS);
  }

  @Override
  public Single<HttpResult<DApiData<Pair<UserData, String>>>> signUp(
    PhoneNumber phoneNumber,
    Email email,
    String password,
    Pin pin
  ) {
    return sign(phoneNumber, email);
  }

  @Override
  public Single<HttpResult<DApiData<Pair<UserData, String>>>> signIn(
    PhoneNumber phoneNumber,
    Email email,
    String password,
    boolean shouldForce
  ) {
    return sign(phoneNumber, email);
  }
}
