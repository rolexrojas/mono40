package com.gbh.movil.domain;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.gbh.movil.Utils;

/**
 * Bank representation.
 *
 * @author hecvasro
 */
public class Bank {
  /**
   * Bank's code.
   */
  private final int code;
  /**
   * Bank's identifier.
   */
  private final String id;
  /**
   * Bank's name.
   */
  private String name;
  /**
   * Bank's state. True if it is active, false otherwise.
   */
  private boolean state;

  /**
   * Constructs a new bank.
   *
   * @param code
   *   Bank's code.
   * @param id
   *   Bank's identifier.
   * @param name
   *   Bank's name.
   */
  public Bank(int code, @NonNull String id, @NonNull String name) {
    this.code = code;
    this.id = id;
    this.name = name;
  }

  /**
   * Gets the code of the bank.
   *
   * @return Bank's code.
   */
  public final int getCode() {
    return code;
  }

  /**
   * Gets the identifier of the bank.
   *
   * @return Bank's identifier.
   */
  @NonNull
  public final String getId() {
    return id;
  }

  /**
   * Gets the name of the bank.
   *
   * @return Bank's name.
   */
  @NonNull
  public String getName() {
    return !TextUtils.isEmpty(name) ? name : id;
  }

  /**
   * Sets the name of the bank.
   *
   * @param name
   *   Bank's name.
   */
  public void setName(@NonNull String name) {
    this.name = name;
  }

  /**
   * Gets the state of the bank.
   *
   * @return True if it is active, false otherwise.
   */
  public boolean getState() {
    return state;
  }

  /**
   * Sets the state of the bank.
   *
   * @param state
   *   True if it is active, false otherwise.
   */
  public void setState(boolean state) {
    this.state = state;
  }

  @Override
  public boolean equals(Object object) {
    return super.equals(object) || (Utils.isNotNull(object) && object instanceof Bank
      && ((Bank) object).code == code && ((Bank) object).id.equals(id));
  }

  @Override
  public int hashCode() {
    return Utils.hashCode(code, id);
  }

  @Override
  public String toString() {
    return Bank.class.getSimpleName() + ":{code='" + code + "',id='" + id + "',name='" + name
      + "',state=" + state + "'}";
  }
}
