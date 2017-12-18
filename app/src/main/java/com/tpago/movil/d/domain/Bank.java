package com.tpago.movil.d.domain;

import android.net.Uri;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.auto.value.AutoValue;
import com.tpago.movil.company.LogoCatalog;
import com.tpago.movil.util.ObjectHelper;

import java.math.BigDecimal;

/**
 * @author hecvasro
 */
@Deprecated
@AutoValue
public abstract class Bank implements LogoUriProvider, Parcelable, Comparable<Bank> {

  private static final BigDecimal TRANSFER_COST_PERCENTAGE = BigDecimal.valueOf(0.015);

  public static Builder builder() {
    return new AutoValue_Bank.Builder();
  }

  public static Bank create(com.tpago.movil.bank.Bank bank) {
    return builder()
      .setCode(bank.code())
      .setId(bank.id())
      .setName(bank.name())
      .setLogoUriTemplate(bank.logoTemplate())
      .setLogoUriMap(LogoUriMap.create(bank.logoCatalog()))
      .build();
  }

  public static com.tpago.movil.bank.Bank create(Bank bank) {
    ObjectHelper.checkNotNull(bank, "bank");
    final LogoUriMap map = bank.getLogoUriMap();
    final LogoCatalog catalog = LogoCatalog.builder()
      .colored24(map.getUriForPrimary24())
      .gray20(map.getUriForGray20())
      .gray36(map.getUriForGray36())
      .white36(map.getUriForWhite36())
      .build();
    return com.tpago.movil.bank.Bank.builder()
      .code(bank.getCode())
      .id(bank.getId())
      .name(bank.getName())
      .logoTemplate(bank.getLogoUriTemplate())
      .logoCatalog(catalog)
      .transferCostRate(TRANSFER_COST_PERCENTAGE)
      .build();
  }

  public static BigDecimal calculateTransferCost(BigDecimal amount) {
    return ObjectHelper.checkNotNull(amount, "amount")
      .multiply(TRANSFER_COST_PERCENTAGE);
  }

  abstract LogoUriMap getLogoUriMap();

  public abstract int getCode();

  public abstract String getId();

  public abstract String getName();

  public abstract String getLogoUriTemplate();

  @Override
  public Uri getLogoUri(@LogoStyle String logoStyle) {
    return getLogoUriMap().getLogoUri(logoStyle);
  }

  @Override
  public int compareTo(@NonNull Bank bank) {
    return getName().compareTo(bank.getName());
  }

  @AutoValue.Builder
  public static abstract class Builder {

    public abstract Builder setCode(int code);

    public abstract Builder setId(String id);

    public abstract Builder setName(String name);

    public abstract Builder setLogoUriTemplate(String imageUriTemplate);

    public abstract Builder setLogoUriMap(LogoUriMap logoUriMap);

    public abstract Bank build();
  }
}
