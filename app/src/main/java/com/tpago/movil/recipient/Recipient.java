package com.tpago.movil.recipient;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author hecvasro
 */
public abstract class Recipient {

  Recipient() {
  }

  @Type
  public abstract int type();

  public abstract String name();

  @IntDef({
    Type.USER
  })
  @Retention(RetentionPolicy.SOURCE)
  public @interface Type {

    int USER = 0;
  }
}
