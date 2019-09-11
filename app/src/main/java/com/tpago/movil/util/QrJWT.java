package com.tpago.movil.util;

import com.google.gson.annotations.SerializedName;

public class QrJWT {
    private String sub;
    @SerializedName("merchant-description")
    private String merchantDescription;

    public void setSub(String sub) {
        this.sub = sub;
    }

    public String getSub() {
        return sub;
    }

    public String getMerchantDescription() {
        return merchantDescription;
    }

    public void setMerchantDescription(String merchantDescription) {
        this.merchantDescription = merchantDescription;
    }
}
