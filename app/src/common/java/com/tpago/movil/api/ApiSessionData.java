package com.tpago.movil.api;

import com.google.auto.value.AutoValue;
import com.google.auto.value.extension.memoized.Memoized;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.tpago.movil.company.bank.Bank;
import com.tpago.movil.company.partner.Partner;
import com.tpago.movil.product.Product;
import com.tpago.movil.product.Products;
import com.tpago.movil.session.SessionData;
import com.tpago.movil.util.ObjectHelper;

import java.util.List;

/**
 * @author hecvasro
 */
@AutoValue
public abstract class ApiSessionData {

  public static TypeAdapter<ApiSessionData> typeAdapter(Gson gson) {
    return new AutoValue_ApiSessionData.GsonTypeAdapter(gson);
  }

  static SessionData zip(List<Bank> bs, List<Partner> ps, ApiSessionData sd) {
    ObjectHelper.checkNotNull(bs, "bs");
    ObjectHelper.checkNotNull(ps, "ps");
    ObjectHelper.checkNotNull(sd, "sd");
    return SessionData.builder()
      .banks(bs)
      .partners(ps)
      .products(sd.products())
      .build();
  }

  ApiSessionData() {
  }

  @SerializedName("query")
  abstract ProductsWrapper apiProducts();

  @Memoized
  Products products() {
    final ProductsWrapper wrapper = this.apiProducts();
    return Products.builder()
      .accounts(wrapper.accounts())
      .creditCards(wrapper.creditCards())
      .loans(wrapper.loans())
      .build();
  }

  @AutoValue
  public static abstract class ProductsWrapper {

    public static TypeAdapter<ProductsWrapper> typeAdapter(Gson gson) {
      return new AutoValue_ApiSessionData_ProductsWrapper.GsonTypeAdapter(gson);
    }

    ProductsWrapper() {
    }

    abstract List<Product> accounts();

    @SerializedName("credit-cards")
    abstract List<Product> creditCards();

    abstract List<Product> loans();
  }
}
