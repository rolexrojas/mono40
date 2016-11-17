package com.gbh.movil.domain;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.gbh.movil.Utils;
import com.gbh.movil.domain.util.StringHelper;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

/**
 * Phone number representation.
 *
 * @author hecvasro
 */
public class PhoneNumber implements Matchable {
  /**
   * TODO
   */
  private static final String REGION_DO = "DO"; // ISO 3166-1

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
    this.content = PhoneNumberUtil.getInstance().format(parse(content),
      PhoneNumberUtil.PhoneNumberFormat.NATIONAL);
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
    return PhoneNumber.class.getSimpleName() + ":{content=" + content + "}";
  }

  @Override
  public boolean matches(@Nullable String query) {
    return Utils.isNull(query) || StringHelper.matches(content, query);
  }
}
