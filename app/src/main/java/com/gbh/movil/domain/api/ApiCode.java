package com.gbh.movil.domain.api;

/**
 * API code enumeration.
 *
 * @author hecvasro
 */
public enum ApiCode {
  OK (200),
  BAD_REQUEST (400),
  UNAUTHORIZED (401),
  FORBIDDEN (403),
  NOT_FOUND (404),
  UNEXPECTED (500);

  private final int value;

  ApiCode(int value) {
    this.value = value;
  }

  public final int value() {
    return value;
  }
}
