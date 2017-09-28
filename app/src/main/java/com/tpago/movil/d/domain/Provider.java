package com.tpago.movil.d.domain;

import java.util.Set;

import io.reactivex.Observable;

/**
 * @author hecvasro
 */
@Deprecated
interface Provider<T> {
  Observable<Result<Set<T>, ErrorCode>> getAll();
}
