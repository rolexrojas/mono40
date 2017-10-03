package com.tpago.movil.data.api;

import com.tpago.movil.domain.auth.alt.AltAuthMethodVerifyData;
import com.tpago.movil.util.Placeholder;
import com.tpago.movil.util.Result;

import java.security.PublicKey;

import io.reactivex.Completable;
import io.reactivex.Single;

/**
 * @author hecvasro
 */
public interface Api {

  Completable enableAltAuthMethod(PublicKey publicKey);

  Single<Result<Placeholder>> verifyAltAuthMethod(AltAuthMethodVerifyData data, byte[] signedData);

  Completable disableAltAuthMethod();
}
