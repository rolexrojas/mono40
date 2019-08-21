package com.tpago.movil.company;

import android.os.Parcelable;
import androidx.annotation.NonNull;
import androidx.annotation.StringDef;

import com.tpago.movil.util.ComparisonChain;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Company representation
 * <p>
 *
 * @author hecvasro
 */
public abstract class Company implements Comparable<Company>, Parcelable {

  protected Company() {
  }

  public abstract int code();

  public abstract String id();

  public abstract String name();

  public abstract String logoTemplate();

  @Override
  public int compareTo(@NonNull Company that) {
    return ComparisonChain.create()
      .compare(this.name(), that.name())
      .result();
  }

  @StringDef({
    LogoStyle.COLORED_24,
    LogoStyle.GRAY_20,
    LogoStyle.GRAY_36,
    LogoStyle.WHITE_36
  })
  @Retention(RetentionPolicy.SOURCE)
  public @interface LogoStyle {

    /**
     * Colored of 24 x 24
     */
    String COLORED_24 = "_24";

    /**
     * Gray of 20 x 20
     */
    String GRAY_20 = "_20";

    /**
     * Gray of 36 x 36
     */
    String GRAY_36 = "_36";

    /**
     * White of 36 x 26
     */
    String WHITE_36 = "_36_bln";
  }

}
