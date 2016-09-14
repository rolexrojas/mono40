package com.gbh.tpago.domain.api;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * TODO
 *
 * @author hecvasro
 */
@IntDef({ ApiCode.SUCCESS, ApiCode.BAD_REQUEST, ApiCode.UNAUTHORIZED, ApiCode.FORBIDDEN,
  ApiCode.NOT_FOUND })
@Retention(RetentionPolicy.SOURCE)
public @interface ApiCode {
  /**
   * TODO
   */
  int SUCCESS = 200;

  /**
   * TODO
   */
  int BAD_REQUEST = 400;

  /**
   * TODO
   */
  int UNAUTHORIZED = 401;

  /**
   * TODO
   */
  int FORBIDDEN = 403;

  /**
   * TODO
   */
  int NOT_FOUND = 404;
}
