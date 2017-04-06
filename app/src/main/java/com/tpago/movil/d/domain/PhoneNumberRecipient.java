package com.tpago.movil.d.domain;

import android.os.Parcel;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tpago.movil.d.domain.util.StringUtils;
import com.tpago.movil.text.Texts;

/**
 * Phone number recipient representation.
 *
 * @author hecvasro
 */
public class PhoneNumberRecipient extends Recipient {
  public static final Creator<PhoneNumberRecipient> CREATOR = new Creator<PhoneNumberRecipient>() {
    @Override
    public PhoneNumberRecipient createFromParcel(Parcel source) {
      return new PhoneNumberRecipient(source);
    }

    @Override
    public PhoneNumberRecipient[] newArray(int size) {
      return new PhoneNumberRecipient[size];
    }
  };

  private final String phoneNumber;

  protected PhoneNumberRecipient(Parcel source) {
    super(source);
    phoneNumber = source.readString();
  }

  /**
   * Constructs a new phone number recipient.
   */
  public PhoneNumberRecipient(@NonNull String phoneNumber, @Nullable String label) {
    super(RecipientType.PHONE_NUMBER, label);
    this.phoneNumber = phoneNumber;
  }

  /**
   * Constructs a new phone number recipient.
   */
  public PhoneNumberRecipient(@NonNull String phoneNumber) {
    super(RecipientType.PHONE_NUMBER);
    this.phoneNumber = phoneNumber;
  }

  @NonNull
  public final String getPhoneNumber() {
    return phoneNumber;
  }

  @Override
  public String getId() {
    return Texts.join("-", getType(), phoneNumber);
  }

  @NonNull
  @Override
  public String getIdentifier() {
    return com.tpago.movil.PhoneNumber.format(phoneNumber);
  }

  @Override
  public String toString() {
    return PhoneNumberRecipient.class.getSimpleName() + ":{super=" + super.toString()
      + ",phoneNumber='" + phoneNumber + "'}";
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean matches(@Nullable String query) {
    return super.matches(query) || StringUtils.matches(phoneNumber, query);
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    super.writeToParcel(dest, flags);
    dest.writeString(phoneNumber);
  }
}
