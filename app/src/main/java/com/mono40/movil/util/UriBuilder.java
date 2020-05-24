package com.mono40.movil.util;

import android.net.Uri;

import com.mono40.movil.DisplayDensity;
import com.mono40.movil.company.Company;
import com.mono40.movil.company.CompanyHelper;
import com.mono40.movil.company.partner.Partner;
import com.mono40.movil.company.partner.PartnerStore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Builds {@link Uri uris} from a given {@link String template} by finding and replacing
 * placeholders.
 *
 * @author hecvasro
 */
public final class UriBuilder {

  /**
   * Placeholder for the {@link DisplayDensity display density} of the device.
   */
  private static final String DISPLAY_DENSITY_PLACEHOLDER = wrapPlaceholder("size");

  private static String wrapPlaceholder(String placeholder) {
    return String.format("{%1$s}", StringHelper.checkIsNotNullNorEmpty(placeholder, "placeholder"));
  }

  private static boolean isContained(String template, String placeholder) {
    return StringHelper.checkIsNotNullNorEmpty(template, "template")
      .contains(placeholder);
  }

  private static void checkIsContained(String template, String wrappedPlaceholder) {
    if (!isContained(template, wrappedPlaceholder)) {
      throw new IllegalArgumentException(
        String.format(
          "!isContained(\"%1$s\",\"%2$s\")",
          template,
          wrappedPlaceholder
        )
      );
    }
  }

  /**
   * Constructs an instance for the given template.
   *
   * @return An instance for the given template.
   *
   * @throws IllegalArgumentException
   *   If {@code template} is {@code null} or empty.
   * @throws IllegalArgumentException
   *   If {@code template} does not containsType the {@link #DISPLAY_DENSITY_PLACEHOLDER placeholder}
   *   used for the {@link DisplayDensity display density} of the device.
   */
  public static UriBuilder create(String template) {
    return new UriBuilder(template);
  }

  /**
   * Construct a uri depending on the partner
   * @param partnerStore List where the partners are going to be taken
   * @param companyHelper Extract the uri of the company
   * @param partnerId Partner that is going to be used to obtain the uri
   * @return
   */
  public static Uri createFromPartners(PartnerStore partnerStore, CompanyHelper companyHelper, String partnerId) {
    List<Partner> partners = partnerStore.getProviders()
            .defaultIfEmpty(new ArrayList<>())
            .blockingGet();

    Partner currentPartner = null;
    for (Partner partner: partners) {
      if(partner.id().equals(partnerId)){
        currentPartner = partner;
      }
    }

    Uri uri = companyHelper.getLogoUri(currentPartner, Company.LogoStyle.GRAY_20);
    return uri;
  }

  private final String template;
  private final Map<String, String> replacements = new HashMap<>();

  /**
   * Constructs an instance for the given template.
   *
   * @throws IllegalArgumentException
   *   If {@code template} is {@code null} or empty.
   */
  private UriBuilder(String template) {
    this.template = StringHelper.checkIsNotNullNorEmpty(template, "template");
  }

  private void checkIsContained(String wrappedPlaceholder) {
    checkIsContained(this.template, wrappedPlaceholder);
  }

  /**
   * Adds the given string as a replacement the given placeholder.
   *
   * @throws IllegalArgumentException
   *   If {@code placeholder} is {@code null} or empty.
   * @throws IllegalArgumentException
   *   If {@code replacement} is {@code null} or empty.
   * @throws IllegalArgumentException
   *   If {@code placeholder} isn't contained in {@link #template template}.
   */
  private UriBuilder replacement(String placeholder, String replacement, boolean shouldWrap) {
    StringHelper.checkIsNotNullNorEmpty(placeholder, "placeholder");
    StringHelper.checkIsNotNullNorEmpty(replacement, "replacement");

    final String wrappedPlaceholder;
    if (shouldWrap) {
      wrappedPlaceholder = wrapPlaceholder(placeholder);
    } else {
      wrappedPlaceholder = placeholder;
    }

    this.checkIsContained(wrappedPlaceholder);

    this.replacements.put(wrappedPlaceholder, replacement);
    return this;
  }

  /**
   * Adds the given string as a replacement the given placeholder.
   *
   * @throws IllegalArgumentException
   *   If {@code placeholder} is {@code null} or empty.
   * @throws IllegalArgumentException
   *   If {@code replacement} is {@code null} or empty.
   * @throws IllegalArgumentException
   *   If {@code placeholder} isn't contained in {@link #template template}.
   */
  public final UriBuilder replacement(String placeholder, String replacement) {
    return this.replacement(placeholder, replacement, true);
  }

  /**
   * Adds the given as a replacement for the {@link #DISPLAY_DENSITY_PLACEHOLDER placeholder} used
   * for the {@link DisplayDensity display density} of the device.
   *
   * @throws NullPointerException
   *   If {@code displayDensity} is null.
   */
  public final UriBuilder replacement(DisplayDensity displayDensity) {
    final String replacement = ObjectHelper.checkNotNull(displayDensity, "displayDensity")
      .toString();
    return this.replacement(DISPLAY_DENSITY_PLACEHOLDER, replacement, false);
  }

  public final Uri build() {
    String s = this.template;
    for (String placeholder : this.replacements.keySet()) {
      s = s.replace(placeholder, this.replacements.get(placeholder));
    }
    return Uri.parse(s);
  }
}
