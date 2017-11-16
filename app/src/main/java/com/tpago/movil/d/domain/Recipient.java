package com.tpago.movil.d.domain;

import static com.tpago.movil.d.domain.RecipientType.ACCOUNT;
import static com.tpago.movil.d.domain.RecipientType.BILL;
import static com.tpago.movil.d.domain.RecipientType.NON_AFFILIATED_PHONE_NUMBER;
import static com.tpago.movil.d.domain.RecipientType.PHONE_NUMBER;
import static com.tpago.movil.d.domain.RecipientType.PRODUCT;
import static com.tpago.movil.d.domain.RecipientType.USER;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tpago.movil.d.domain.util.StringUtils;
import com.tpago.movil.util.ObjectHelper;

import java.util.Comparator;

/**
 * Abstract recipient representation.
 *
 * @author hecvasro
 */
@Deprecated
public abstract class Recipient implements Parcelable, Matchable {

  @Deprecated
  public static Comparator<Recipient> comparator() {
    return new Comparator<Recipient>() {
      @Override
      public int compare(Recipient ra, Recipient rb) {
        if (ra instanceof UserRecipient) {
          return -1;
        } else if (rb instanceof UserRecipient) {
          return 1;
        } else {
          return ra.getIdentifier()
            .compareTo(rb.getIdentifier());
        }
      }
    };
  }

  @Deprecated
  public static boolean checkIfBill(Recipient recipient) {
    return recipient.type == BILL;
  }

  public static boolean acceptsPayments(Recipient recipient) {
    return ObjectHelper.isNotNull(recipient) && (recipient.type == BILL || recipient.type == PRODUCT);
  }

  public static boolean acceptsTransfers(Recipient recipient) {
    return ObjectHelper.isNotNull(recipient)
      && (
      recipient.type == PHONE_NUMBER
        || recipient.type == NON_AFFILIATED_PHONE_NUMBER
        || recipient.type == ACCOUNT
    );
  }

  public static boolean acceptsRecharges(Recipient recipient) {
    return ObjectHelper.isNotNull(recipient)
      && (
      recipient.type == USER
        || recipient.type == PHONE_NUMBER
        || recipient.type == NON_AFFILIATED_PHONE_NUMBER
    );
  }

  public static boolean acceptsDisburses(Recipient recipient) {
    return ObjectHelper.isNotNull(recipient)
      && recipient.type == PRODUCT
      && Product.checkIfCreditCard(((ProductRecipient) recipient).getProduct());
  }

  /**
   * Recipient's {@link RecipientType type}.
   */
  private final RecipientType type;

  /**
   * Recipient's label.
   */
  private String label;

  private boolean selected = false;

  protected Recipient(Parcel source) {
    type = RecipientType.valueOf(source.readString());
    label = source.readString();
  }

  /**
   * Constructs a new recipient.
   *
   * @param type
   *   Recipient's {@link RecipientType type}.
   * @param label
   *   Recipient's label.
   */
  protected Recipient(@NonNull RecipientType type, @Nullable String label) {
    this.type = type;
    this.label = label;
  }

  /**
   * Constructs a new recipient.
   *
   * @param type
   *   Recipient's {@link RecipientType type}.
   */
  protected Recipient(@NonNull RecipientType type) {
    this(type, null);
  }

  public abstract String getId();

  /**
   * Gets the {@link RecipientType type} of the recipient.
   *
   * @return Recipient's {@link RecipientType type}.
   */
  @NonNull
  public final RecipientType getType() {
    return type;
  }

  /**
   * Gets the label of the recipient.
   *
   * @return Recipient's label.
   */
  @Nullable
  public String getLabel() {
    return label;
  }

  /**
   * Gets the identifier of the recipient.
   *
   * @return Recipient's identifier.
   */
  @NonNull
  public abstract String getIdentifier();

  /**
   * Sets the label of the recipient.
   *
   * @param label
   *   Recipient's label.
   */
  public void setLabel(@Nullable String label) {
    this.label = label;
  }

  public boolean isSelected() {
    return selected;
  }

  public void setSelected(boolean selected) {
    this.selected = selected;
  }

  @Override
  public boolean equals(Object o) {
    return o != null && o instanceof Recipient && ((Recipient) o).getId()
      .equals(getId());
  }

  @Override
  public int hashCode() {
    return getId().hashCode();
  }

  @Override
  public String toString() {
    return "Recipient:{type='" + type + "',label='" + label + "'}";
  }

  @Override
  public boolean matches(@Nullable String query) {
    return ObjectHelper.isNotNull(label) && StringUtils.matches(label, query);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(type.name());
    dest.writeString(label);
  }
}
