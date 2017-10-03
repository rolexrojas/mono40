package com.tpago.movil.domain.auth.alt;

import android.support.annotation.IntDef;

import com.tpago.movil.util.Result;
import com.tpago.movil.util.Supplier;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.security.PrivateKey;

import io.reactivex.Single;

/**
 * @author hecvasro
 */
public interface AltAuthMethodKeySupplier extends Supplier<Single<Result<PrivateKey>>> {

  @IntDef({
    FailureCode.UNAUTHORIZED
  })
  @Retention(RetentionPolicy.SOURCE)
  @interface FailureCode {

    int UNAUTHORIZED = 0;
  }
}
