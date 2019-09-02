package com.tpago.movil.d.domain;

import android.net.Uri;
import android.os.Parcel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tpago.movil.PhoneNumber;
import com.tpago.movil.company.partner.Partner;
import com.tpago.movil.d.domain.util.StringUtils;
import com.tpago.movil.dep.User;
import com.tpago.movil.dep.text.Texts;

/**
 * @author Hector Vasquez
 */
@Deprecated
public final class MerchantRecipient extends Recipient {

  public static final Creator<MerchantRecipient> CREATOR = new Creator<MerchantRecipient>() {
    @Override
    public MerchantRecipient createFromParcel(Parcel source) {
      return new MerchantRecipient(source);
    }

    @Override
    public MerchantRecipient[] newArray(int size) {
      return new MerchantRecipient[size];
    }
  };

  private final String id;
  private final String name;


  private MerchantRecipient(Parcel source) {
    super(source);

    this.id = source.readString();
    this.name = source.readString();
  }

  public MerchantRecipient(String name, String id) {
    super(RecipientType.MERCHANT);

    this.id = id;
    this.name = name;
  }



  @Override
  public String getId() {
    return id;
  }

  @NonNull
  @Override
  public String getIdentifier() {
    return name;
  }

  @Override
  public boolean matches(@Nullable String query) {
    return super.matches(query)
      || StringUtils.matches(this.id, query)
      || StringUtils.matches(this.name, query);
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    super.writeToParcel(dest, flags);
    dest.writeString(this.id);
    dest.writeString(this.name);
  }
}
