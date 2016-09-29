package com.gbh.movil.domain.api;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * API code enumeration.
 *
 * @author hecvasro
 */
@IntDef({ ApiCode.SUCCESS, ApiCode.BAD_REQUEST, ApiCode.UNAUTHORIZED, ApiCode.FORBIDDEN,
  ApiCode.NOT_FOUND })
@Retention(RetentionPolicy.SOURCE)
public @interface ApiCode {
  int SUCCESS = 200;
  int BAD_REQUEST = 400;
  int UNAUTHORIZED = 401;
  int FORBIDDEN = 403;
  int NOT_FOUND = 404;
}
