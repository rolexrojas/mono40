package com.tpago.movil.payment;

import com.tpago.movil.company.LogoCatalog;

/**
 * @author hecvasro
 */
public interface PartnerBuilder {

  PartnerBuilder id(String id);

  PartnerBuilder code(int code);

  PartnerBuilder name(String name);

  PartnerBuilder logoTemplate(String logoTemplate);

  PartnerBuilder logoCatalog(LogoCatalog logoCatalog);

  Partner build();
}
