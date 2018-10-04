package com.tpago.movil.paypal;

import android.os.Parcel;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tpago.movil.d.domain.Recipient;
import com.tpago.movil.d.domain.RecipientType;
import com.tpago.movil.d.domain.util.StringUtils;
import com.tpago.movil.util.ObjectHelper;
import com.tpago.movil.util.StringHelper;

@Deprecated
public final class PayPalAccountRecipient extends Recipient {

  public static final Creator<PayPalAccountRecipient> CREATOR
    = new Creator<PayPalAccountRecipient>() {
    @Override
    public PayPalAccountRecipient createFromParcel(Parcel source) {
      return new PayPalAccountRecipient(source);
    }

    @Override
    public PayPalAccountRecipient[] newArray(int size) {
      return new PayPalAccountRecipient[size];
    }
  };

  public static PayPalAccountRecipient create(PayPalAccount reference) {
    return new PayPalAccountRecipient(reference);
  }

  private final PayPalAccount reference;

  private PayPalAccountRecipient(Parcel source) {
    super(source);

    this.reference = source.readParcelable(PayPalAccount.class.getClassLoader());
  }

  private PayPalAccountRecipient(PayPalAccount reference) {
    super(RecipientType.PAY_PAL_ACCOUNT);

    this.reference = ObjectHelper.checkNotNull(reference, "reference");
  }

  public final PayPalAccount reference() {
    return this.reference;
  }

  @Override
  public String getId() {
    return StringHelper.builder()
      .append(this.getType())
      .append("-")
      .append(this.reference.email())
      .toString();
  }

  @Nullable
  @Override
  public String getLabel() {
    return this.reference.alias();
  }

  @NonNull
  @Override
  public String getIdentifier() {
    return this.reference.email();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    super.writeToParcel(dest, flags);

    dest.writeParcelable(this.reference, flags);
  }

  @Override
  public boolean matches(@Nullable String query) {
    return super.matches(query)
      || StringUtils.matches(this.reference.email(), query)
      || StringUtils.matches(this.reference.alias(), query);
  }
}
