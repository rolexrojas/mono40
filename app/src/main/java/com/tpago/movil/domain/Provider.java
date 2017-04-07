package com.tpago.movil.domain;

import java.util.Set;

import io.reactivex.Observable;

/**
 * @author hecvasro
 */
interface Provider<T> {
  Observable<Result<Set<T>, ErrorCode>> getAll();
}
