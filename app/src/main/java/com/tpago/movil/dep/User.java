package com.tpago.movil.dep;

import com.google.auto.value.AutoValue;
import com.tpago.movil.Email;
import com.tpago.movil.PhoneNumber;
import com.tpago.movil.util.ObjectHelper;
import com.tpago.movil.util.StringHelper;

/**
 * @author hecvasro
 */
@Deprecated
@AutoValue
public abstract class User {

  public static Builder createBuilder() {
    return new AutoValue_User.Builder();
  }

  private int id;
  private OnIdChangedListener onIdChangedListener;

  private String firstName;
  private String lastName;
  private String name;
  private OnNameChangedListener onNameChangedListener;

  private Partner carrier;
  private OnCarrierChangedListener onCarrierChangedListener;

  User() {
  }

  public abstract PhoneNumber phoneNumber();

  public final Partner carrier() {
    return this.carrier;
  }

  public final void carrier(Partner carrier) {
    this.carrier = carrier;

    if (ObjectHelper.isNotNull(this.onCarrierChangedListener)) {
      this.onCarrierChangedListener.onCarrierChanged(this.carrier);
    }
  }

  final void onCarrierChangedListener(OnCarrierChangedListener listener) {
    this.onCarrierChangedListener = listener;
  }

  public abstract Email email();

  final void onIdChangedListener(OnIdChangedListener listener) {
    this.onIdChangedListener = listener;
  }

  final void id(int id) {
    this.id = id;

    if (ObjectHelper.isNotNull(this.onIdChangedListener)) {
      this.onIdChangedListener.onIdChanged(this.id);
    }
  }

  final int id() {
    return this.id;
  }

  final void onNameChangedListener(OnNameChangedListener listener) {
    this.onNameChangedListener = listener;
  }

  public final void name(String firstName, String lastName) {
    ObjectHelper.checkNotNull(firstName, "firstName");
    if (StringHelper.isNullOrEmpty(firstName)) {
      throw new IllegalArgumentException("StringHelper.isNullOrEmpty(firstName)");
    }
    this.firstName = firstName;

    ObjectHelper.checkNotNull(lastName, "lastName");
    if (StringHelper.isNullOrEmpty(lastName)) {
      throw new IllegalArgumentException("StringHelper.isNullOrEmpty(lastName)");
    }
    this.lastName = lastName;

    this.name = this.firstName + " " + this.lastName;

    if (ObjectHelper.isNotNull(this.onNameChangedListener)) {
      this.onNameChangedListener.onNameChanged(this.firstName, this.lastName);
    }
  }

  public final String firstName() {
    return this.firstName;
  }

  public final String lastName() {
    return this.lastName;
  }

  public final String name() {
    return this.name;
  }

  public abstract Avatar avatar();

  interface OnIdChangedListener {

    void onIdChanged(int id);
  }

  interface OnNameChangedListener {

    void onNameChanged(String firstName, String lastName);
  }

  interface OnCarrierChangedListener {

    void onCarrierChanged(Partner carrier);
  }

  @AutoValue.Builder
  public static abstract class Builder {

    public abstract Builder phoneNumber(PhoneNumber phoneNumber);

    public abstract Builder email(Email email);

    public abstract Builder avatar(Avatar avatar);

    public abstract User build();
  }
}
