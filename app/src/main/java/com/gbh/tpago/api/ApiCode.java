package com.gbh.tpago.api;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * TODO
 *
 * @author hecvasro
 */
@IntDef({ ApiCode.SUCCESS, ApiCode.BAD_REQUEST, ApiCode.UNAUTHORIZED, ApiCode.FORBIDDEN,
  ApiCode.NOT_FOUND, ApiCode.SERVER_ERROR })
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

  /**
   * TODO
   */
  int SERVER_ERROR = 500;
}
