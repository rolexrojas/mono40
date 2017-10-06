package com.tpago.movil.domain.auth.alt;

import com.tpago.movil.util.Result;
import com.tpago.movil.util.Supplier;

import java.security.PrivateKey;

import io.reactivex.Single;

/**
 * @author hecvasro
 */
public interface AltAuthMethodKeySupplier extends Supplier<Single<Result<PrivateKey>>> {
}
