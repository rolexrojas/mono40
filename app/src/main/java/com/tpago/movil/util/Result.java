package com.tpago.movil.util;

/**
 * @author hecvasro
 */
public abstract class Result<T> {
  public abstract T getData();

  public abstract boolean isSuccessful();
}
