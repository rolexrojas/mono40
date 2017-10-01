package com.tpago.movil.data.auth.alt;

import com.tpago.movil.util.Result;
import com.tpago.movil.util.Supplier;

import java.security.KeyPair;

import io.reactivex.Single;

/**
 * @author hecvasro
 */
public interface AltAuthMethodKeyPairSupplier extends Supplier<Single<Result<KeyPair>>> {
}
