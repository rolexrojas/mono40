package com.gbh.movil.data.pos;

import android.support.annotation.NonNull;

import com.gbh.movil.domain.PhoneNumber;
import com.gbh.movil.domain.pos.PosBridge;
import com.gbh.movil.domain.pos.PosCode;
import com.gbh.movil.domain.pos.PosResult;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * TODO
 *
 * @author hecvasro
 */
class FakePosBridge implements PosBridge {
  private final List<String> aliases;

  FakePosBridge() {
    aliases = new ArrayList<>();
  }

  private static int getLastDigit(@NonNull String s) {
    return Integer.parseInt(s.substring(s.length() - 1, s.length()));
  }

  private static boolean isLastDigitEven(@NonNull String s) {
    return getLastDigit(s) % 2 == 0;
  }

  @NonNull
  @Override
  public Observable<PosResult<List<String>>> getCards() {
    return Observable.just(aliases)
      .map(new Func1<List<String>, PosResult<List<String>>>() {
        @Override
        public PosResult<List<String>> call(List<String> strings) {
          return new PosResult<>(PosCode.OK, strings);
        }
      });
  }

  @NonNull
  @Override
  public Observable<PosResult<String>> addCard(@NonNull PhoneNumber phoneNumber,
    @NonNull String pin, @NonNull String alias) {
    if (isLastDigitEven(pin)) {
      if (!aliases.contains(alias)) {
        aliases.add(alias);
      }
      return Observable.just(new PosResult<>(PosCode.OK, alias));
    } else {
      return Observable.just(new PosResult<String>(PosCode.INCORRECT_AUTH_CODE));
    }
  }

  @NonNull
  @Override
  public Observable<PosResult<Void>> selectCard(@NonNull String alias) {
    return Observable.error(new UnsupportedOperationException());
  }

  @NonNull
  @Override
  public Observable<PosResult<String>> removeCard(@NonNull String alias) {
    if (aliases.contains(alias)) {
      aliases.remove(alias);
    }
    return Observable.just(new PosResult<>(PosCode.OK, alias));
  }
}
