package com.tpago.movil.company.bank;

import com.tpago.movil.product.Product;

public class BanreservasType {

  public String name;

  public String type;

  public String logoTemplate;

 public BanreservasType() {
  }

  public BanreservasType name(String name) {
    this.name = name;
    return this;
  }

  public BanreservasType type(String type) {
    this.type = type;
    return this;
  }

  public BanreservasType savType() {
    this.type = Product.Type.SAV;
    return this;
  }

  public BanreservasType ddaType() {
    this.type = Product.Type.DDA;
    return this;
  }


  public BanreservasType logoTemplate(String logoTemplate) {
    this.logoTemplate = logoTemplate;
    return this;
  }
}
