package com.tpago.movil.d.domain;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tpago.movil.d.domain.util.StringUtils;
import com.tpago.movil.d.misc.Utils;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import java.io.Serializable;

/**
 * Phone number representation.
 *
 * @author hecvasro
 */
@Deprecated
public class PhoneNumber implements Serializable, Matchable {
  /**
   * TODO
   */
  private static final String REGION_DO = "DO"; // ISO 3166-1

  /**
   * TODO
   */
  private final String content;

  /**
   * TODO
   *
   * @param content
   *   TODO
   *
   * @throws NumberParseException
   *   TODO
   */
  public PhoneNumber(@NonNull String content) throws NumberParseException {
    this.content = StringUtils.sanitize(format(content));
  }

  /**
   * TODO
   *
   * @param content
   *   TODO
   *
   * @return TODO
   *
   * @throws NumberParseException
   *   TODO
   */
  private static Phonenumber.PhoneNumber parse(@NonNull String content)
    throws NumberParseException {
    return PhoneNumberUtil.getInstance().parse(content, REGION_DO);
  }

  /**
   * TODO
   *
   * @param content
   *   TODO
   *
   * @return TODO
   */
  public static boolean isValid(@NonNull String content) {
    try {
      return PhoneNumberUtil.getInstance().isValidNumberForRegion(parse(content), REGION_DO);
    } catch (NumberParseException exception) {
      // Ignored.
    }
    return false;
  }

  /**
   * TODO
   *
   * @param content
   *   TODO
   *
   * @return TODO
   *
   * @throws NumberParseException
   *   TODO
   */
  @NonNull
  public static String format(@NonNull String content) throws NumberParseException {
    return PhoneNumberUtil.getInstance().format(parse(content),
      PhoneNumberUtil.PhoneNumberFormat.NATIONAL);
  }

  @Override
  public boolean equals(Object object) {
    return super.equals(object) || (Utils.isNotNull(object) && object instanceof PhoneNumber
      && ((PhoneNumber) object).content.equals(content));
  }

  @Override
  public int hashCode() {
    return content.hashCode();
  }

  @Override
  public String toString() {
    return content;
  }

  @Override
  public boolean matches(@Nullable String query) {
    return StringUtils.matches(content, query);
  }
}
