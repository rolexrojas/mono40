package com.tpago.movil.dep;

import com.tpago.movil.d.domain.Bank;
import com.tpago.movil.d.domain.LogoStyle;
import com.tpago.movil.d.domain.LogoUriMap;
import com.tpago.movil.d.domain.Product;
import com.tpago.movil.d.domain.ProductCreator;
import com.tpago.movil.d.domain.ProductType;
import com.tpago.movil.d.domain.Transaction;
import com.tpago.movil.dep.data.AssetUriBuilder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class MockData {

  public static Set<Bank> BANK_SET;
  public static Set<Product> PRODUCT_SET;
  public static Set<Partner> PARTNER_SET;
  public static List<Transaction> TRANSACTION_LIST;

  private static LogoUriMap createLogoUriMap(AssetUriBuilder assetUriBuilder, String template) {
    return LogoUriMap.builder()
      .setUriForGray20(assetUriBuilder.build(template, LogoStyle.GRAY_20))
      .setUriForGray36(assetUriBuilder.build(template, LogoStyle.GRAY_36))
      .setUriForPrimary24(assetUriBuilder.build(template, LogoStyle.PRIMARY_24))
      .setUriForWhite36(assetUriBuilder.build(template, LogoStyle.WHITE_36))
      .build();
  }

  static {
    final AssetUriBuilder assetUriBuilder = new AssetUriBuilder(DisplayDensity.XXXHDPI);

    final String template1 = "https://demo.gcs-systems.com/appimages/{size}/102/102{style}.png";
    final Bank bank1 = Bank.builder()
      .setCode(5)
      .setId("102")
      .setName("Banco Popular")
      .setLogoUriTemplate(template1)
      .setLogoUriMap(createLogoUriMap(assetUriBuilder, template1))
      .build();
    final Product product1 = ProductCreator.create(
      ProductType.SAV,
      "S2-SAZ 9833",
      "*****9833",
      bank1,
      "DOP",
      BigDecimal.valueOf(5),
      true,
      false,
      null
    );
    final Product product2 = ProductCreator.create(
      ProductType.CC,
      "T1-BPD 8000",
      "************8000",
      bank1,
      "DOP",
      BigDecimal.valueOf(5),
      true,
      false,
      null
    );
    final String template2 = "https://demo.gcs-systems.com/appimages/{size}/BDP/BDP{style}.png";
    final Bank bank2 = Bank.builder()
      .setCode(24)
      .setId("BDP")
      .setName("Banco del Progreso")
      .setLogoUriTemplate(template2)
      .setLogoUriMap(createLogoUriMap(assetUriBuilder, template2))
      .build();
    final Product product3 = ProductCreator.create(
      ProductType.LOAN,
      "L1-BDP 6852",
      "*****6852",
      bank2,
      "DOP",
      BigDecimal.valueOf(5),
      true,
      false,
      null
    );

    final Partner partner1 = Partner.builder()
      .setCode(2)
      .setType(Partner.TYPE_CARRIER)
      .setId("200")
      .setName("CODETEL/Claro")
      .setImageUriTemplate("https://demo.gcs-systems.com/appimages/{size}/200/200{style}.png")
      .build();
    final Partner partner2 = Partner.builder()
      .setCode(7)
      .setType(Partner.TYPE_PROVIDER)
      .setId("200")
      .setName("CODETEL/Claro")
      .setImageUriTemplate("https://demo.gcs-systems.com/appimages/{size}/200/200{style}.png")
      .build();
    final Partner partner3 = Partner.builder()
      .setCode(8)
      .setType(Partner.TYPE_CARRIER)
      .setId("300")
      .setName("Orange")
      .setImageUriTemplate("https://demo.gcs-systems.com/appimages/{size}/300/300{style}.png")
      .build();
    final Partner partner4 = Partner.builder()
      .setCode(27)
      .setType(Partner.TYPE_PROVIDER)
      .setId("ORL")
      .setName("Orange")
      .setImageUriTemplate("https://demo.gcs-systems.com/appimages/{size}/ORL/ORL{style}.png")
      .build();

    BANK_SET = new HashSet<>();
    BANK_SET.add(bank1);
    BANK_SET.add(bank2);

    PRODUCT_SET = new HashSet<>();
    PRODUCT_SET.add(product1);
    PRODUCT_SET.add(product2);
    PRODUCT_SET.add(product3);

    PARTNER_SET = new HashSet<>();
    PARTNER_SET.add(partner1);
    PARTNER_SET.add(partner2);
    PARTNER_SET.add(partner3);
    PARTNER_SET.add(partner4);

    TRANSACTION_LIST = new ArrayList<>();
    TRANSACTION_LIST.add(
      Transaction.builder()
        .dateString("27/08/2017")
        .type("Pago de factura")
        .detail("Pago a factura de Orange")
        .amount(BigDecimal.valueOf(1825.00))
        .build()
    );
    TRANSACTION_LIST.add(
      Transaction.builder()
        .dateString("27/09/2017")
        .type("Pago de factura")
        .detail("Pago a factura de Claro")
        .amount(BigDecimal.valueOf(2250.00))
        .build()
    );
  }
}
