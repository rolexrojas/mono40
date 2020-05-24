package com.mono40.movil.d.domain;

import android.os.Parcel;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mono40.movil.d.domain.util.StringUtils;

/**
 * @author Hector Vasquez
 */
@Deprecated
public final class ProductRecipient extends Recipient {

  public static final Creator<ProductRecipient> CREATOR = new Creator<ProductRecipient>() {
    @Override
    public ProductRecipient createFromParcel(Parcel source) {
      return new ProductRecipient(source);
    }

    @Override
    public ProductRecipient[] newArray(int size) {
      return new ProductRecipient[size];
    }
  };

  private final Product product;

  private ProductBillBalance balance;

  private ProductRecipient(Parcel source) {
    super(source);
    this.product = source.readParcelable(Product.class.getClassLoader());
    if (Product.checkIfCreditCard(this.product)) {
      this.balance = source.readParcelable(CreditCardBillBalance.class.getClassLoader());
    } else {
      this.balance = source.readParcelable(LoanBillBalance.class.getClassLoader());
    }
  }

  public ProductRecipient(Product product, @Nullable String label) {
    super(RecipientType.PRODUCT, label);
    this.product = product;
  }

  public ProductRecipient(Product product) {
    super(RecipientType.PRODUCT);
    this.product = product;
  }

  public final Product getProduct() {
    return this.product;
  }

  public final void setBalance(ProductBillBalance balance) {
    this.balance = balance;
  }

  public final ProductBillBalance getBalance() {
    return this.balance;
  }

  @Override
  public String getId() {
    return product.getId();
  }

  @NonNull
  @Override
  public String getIdentifier() {
    return product.getAlias();
  }

  @Override
  public boolean matches(@Nullable String query) {
    return super.matches(query)
      || StringUtils.matches(
      product.getBank()
        .name(),
      query
    )
      || StringUtils.matches(product.getAlias(), query);
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    super.writeToParcel(dest, flags);
    dest.writeParcelable(this.product, flags);
    dest.writeParcelable(this.balance, flags);
  }
}
