package com.tpago.movil.session;

import androidx.annotation.IntDef;

import com.tpago.movil.util.Result;
import com.tpago.movil.util.function.Supplier;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.security.Signature;

import io.reactivex.Single;

/**
 * @author hecvasro
 */
public interface UnlockMethodSignatureSupplier extends Supplier<Single<Result<Signature>>> {

  @IntDef({
    FailureCode.UNAUTHORIZED
  })
  @Retention(RetentionPolicy.SOURCE)
  @interface FailureCode {

    int UNAUTHORIZED = 401;
    int UNEXPECTED = 500;
  }
}
