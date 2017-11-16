package com.tpago.movil.company;

import java.util.List;

/**
 * @author hecvasro
 */
public abstract class CompanyList<T extends Company> {

  protected CompanyList() {
  }

  public abstract long queryTime();

  public abstract List<T> value();
}
