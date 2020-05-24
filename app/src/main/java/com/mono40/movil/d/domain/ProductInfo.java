package com.mono40.movil.d.domain;

import androidx.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.mono40.movil.company.bank.Bank;

import java.math.BigDecimal;

import static java.math.BigDecimal.ZERO;

/**
 * @author hecvasro
 */
@Deprecated
@AutoValue
public abstract class ProductInfo {

    public static ProductInfo create(Product product) {
        return new AutoValue_ProductInfo(
                product.getBank(),
                product.getType(),
                product.getAlias(),
                product.getNumber(),
                product.getCurrency(),
                Product.isPaymentOption(product),
                Product.isDefaultPaymentOption(product),
                null,
                product.getAltpanKey()
        );
    }

    public static ProductInfo create(Product product, String name) {
        return new AutoValue_ProductInfo(
                product.getBank(),
                product.getType(),
                product.getAlias(),
                product.getNumber(),
                product.getCurrency(),
                Product.isPaymentOption(product),
                Product.isDefaultPaymentOption(product),
                name,
                product.getAltpanKey()
        );
    }

    public static TypeAdapter<ProductInfo> typeAdapter(Gson gson) {
        return new AutoValue_ProductInfo.GsonTypeAdapter(gson);
    }

    @SerializedName("bank")
    public abstract Bank getBank();

    @SerializedName("account-type")
    public abstract ProductType getType();

    @SerializedName("account-alias")
    public abstract String getAlias();

    @SerializedName("account-number")
    public abstract String getNumber();

    @SerializedName("currency")
    public abstract String getCurrency();

    public BigDecimal getQueryFee() {
        return ZERO;
    }

    @SerializedName("payable")
    public abstract boolean isPaymentMethod();

    @SerializedName("default-account")
    public abstract boolean isDefaultPaymentMethod();

    @SerializedName("recipient-name")
    @Nullable
    public abstract String getRecipientName();

    @SerializedName("altpan-key")
    @Nullable
    public abstract String altpanKey();
}
