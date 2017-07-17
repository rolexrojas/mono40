package com.tpago.movil.d.domain;

import android.net.Uri;
import android.os.Parcel;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tpago.movil.d.domain.util.StringUtils;

/**
 * @author Hector Vasquez
 */
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

  private final String phoneNumber;
  private final String name;
  private final Uri pictureUri;

  private UserRecipient(Parcel source) {
    super(source);

    this.phoneNumber = source.readString();
    this.name = source.readString();
    this.pictureUri = source.readParcelable(Uri.class.getClassLoader());
  }

  public UserRecipient(String phoneNumber, String name, Uri pictureUri) {
    super(RecipientType.USER, name);
    this.phoneNumber = phoneNumber;
    this.name = name;
    this.pictureUri = pictureUri;
  }

  public final Uri pictureUri() {
    return this.pictureUri;
  }

  @Override
  public String getId() {
    return this.phoneNumber;
  }

  @NonNull
  @Override
  public String getIdentifier() {
    return this.phoneNumber;
  }

  @Override
  public boolean matches(@Nullable String query) {
    return super.matches(query)
      || StringUtils.matches(phoneNumber, query)
      || StringUtils.matches(name, query);
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    super.writeToParcel(dest, flags);
    dest.writeString(phoneNumber);
    dest.writeString(name);
    dest.writeParcelable(pictureUri, flags);
  }
}
