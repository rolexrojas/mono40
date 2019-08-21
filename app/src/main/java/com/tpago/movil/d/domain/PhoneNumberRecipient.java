package com.tpago.movil.d.domain;

import static com.tpago.movil.d.domain.RecipientType.PHONE_NUMBER;

import android.os.Parcel;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tpago.movil.company.partner.Partner;
import com.tpago.movil.d.ui.main.recipient.addition.Contact;
import com.tpago.movil.PhoneNumber;
import com.tpago.movil.d.domain.util.StringUtils;
import com.tpago.movil.dep.text.Texts;

/**
 * Phone number recipient representation.
 *
 * @author hecvasro
 */
@Deprecated
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

  private final PhoneNumber phoneNumber;
  private Partner carrier;

  protected PhoneNumberRecipient(Parcel source) {
    super(source);
    this.phoneNumber = source.readParcelable(PhoneNumber.class.getClassLoader());
    this.carrier = source.readParcelable(Partner.class.getClassLoader());
  }

  public PhoneNumberRecipient(
    @NonNull PhoneNumber phoneNumber,
    @Nullable Partner carrier,
    @Nullable String label
  ) {
    super(PHONE_NUMBER, label);

    this.phoneNumber = phoneNumber;
    this.carrier = carrier;
  }

  public PhoneNumberRecipient(@NonNull PhoneNumber phoneNumber, @Nullable String label) {
    this(phoneNumber, null, label);
  }

  public PhoneNumberRecipient(@NonNull PhoneNumber phoneNumber) {
    this(phoneNumber, null, null);
  }

  public PhoneNumberRecipient(Contact contact) {
    this(contact.phoneNumber(), null, contact.name());
  }

  @NonNull
  public final PhoneNumber getPhoneNumber() {
    return this.phoneNumber;
  }

  public final void setCarrier(Partner carrier) {
    this.carrier = carrier;
  }

  public final Partner getCarrier() {
    return this.carrier;
  }

  @Override
  public String getId() {
    return Texts.join("-", this.getType(), this.phoneNumber.value());
  }

  @NonNull
  @Override
  public String getIdentifier() {
    return this.phoneNumber.formattedValued();
  }

  @Override
  public String toString() {
    return PhoneNumberRecipient.class.getSimpleName() + ":{super=" + super.toString()
      + ",phoneNumber='" + phoneNumber + ",carrier=" + carrier.toString() + "'}";
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean matches(@Nullable String query) {
    return super.matches(query) || StringUtils.matches(this.phoneNumber.value(), query);
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    super.writeToParcel(dest, flags);

    dest.writeParcelable(this.phoneNumber, flags);
    dest.writeParcelable(this.carrier, flags);
  }
}
