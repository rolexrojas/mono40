package com.tpago.movil.domain.auth;

import com.tpago.movil.util.Placeholder;
import com.tpago.movil.util.Result;

import io.reactivex.Single;

/**
 * @author hecvasro
 */
public interface Unlocker {

  Single<Result<Placeholder>> unlock();
}
