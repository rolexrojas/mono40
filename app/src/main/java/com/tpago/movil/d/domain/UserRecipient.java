package com.tpago.movil.d.domain;

import android.net.Uri;
import android.os.Parcel;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tpago.movil.dep.Partner;
import com.tpago.movil.PhoneNumber;
import com.tpago.movil.dep.User;
import com.tpago.movil.d.domain.util.StringUtils;
import com.tpago.movil.dep.text.Texts;

/**
 * @author Hector Vasquez
 */
@Deprecated
public final class UserRecipient extends Recipient {

  public static final Creator<UserRecipient> CREATOR = new Creator<UserRecipient>() {
    @Override
    public UserRecipient createFromParcel(Parcel source) {
      return new UserRecipient(source);
    }

    @Override
    public UserRecipient[] newArray(int size) {
      return new UserRecipient[size];
    }
  };

  private final String id;
  private final PhoneNumber phoneNumber;
  private final String name;
  private final Uri pictureUri;

  private Partner carrier;

  private UserRecipient(Parcel source) {
    super(source);

    this.id = source.readString();
    this.phoneNumber = source.readParcelable(PhoneNumber.class.getClassLoader());
    this.name = source.readString();
    this.pictureUri = source.readParcelable(Uri.class.getClassLoader());

    this.carrier = source.readParcelable(Partner.class.getClassLoader());
  }

  public UserRecipient(User user) {
    super(RecipientType.USER);

    this.id = user.phoneNumber()
      .value();
    this.phoneNumber = user.phoneNumber();
    this.name = user.name();
    this.pictureUri = user.avatar().exists() ? Uri.fromFile(user.avatar().getFile()) : Uri.EMPTY;
    this.carrier = user.carrier();
  }

  public final PhoneNumber phoneNumber() {
    return this.phoneNumber;
  }

  public final Uri pictureUri() {
    return this.pictureUri;
  }

  public final void setCarrier(Partner carrier) {
    this.carrier = carrier;
  }

  public final Partner getCarrier() {
    return this.carrier;
  }

  @Override
  public String getId() {
    return Texts.join("-", this.getType(), this.id);
  }

  @NonNull
  @Override
  public String getIdentifier() {
    return this.phoneNumber.formattedValued();
  }

  @Override
  public boolean matches(@Nullable String query) {
    return super.matches(query)
      || StringUtils.matches(this.phoneNumber.value(), query)
      || StringUtils.matches(this.name, query);
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    super.writeToParcel(dest, flags);

    dest.writeString(this.id);
    dest.writeParcelable(this.phoneNumber, flags);
    dest.writeString(this.name);
    dest.writeParcelable(this.pictureUri, flags);

    dest.writeParcelable(this.carrier, flags);
  }
}
