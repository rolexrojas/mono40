package com.tpago.movil.data;

import android.net.Uri;

import com.tpago.movil.app.DisplayDensity;
import com.tpago.movil.domain.LogoStyle;

import java.util.HashMap;
import java.util.Map;

import static com.tpago.movil.util.Preconditions.assertNotNull;

/**
 * @author hecvasro
 */
public final class AssetUriBuilder {
  private static final String PLACEHOLDER_SIZE = "{size}";
  private static final String PLACEHOLDER_STYLE = "{style}";

  private final String displayDensityName;

  AssetUriBuilder(DisplayDensity displayDensity) {
    displayDensityName = assertNotNull(displayDensity, "displayDensity == null").name().toLowerCase();
  }

  public final Uri build(String template, @LogoStyle String style) {
    return InternalBuilder.create(template)
      .putReplacement(PLACEHOLDER_SIZE, displayDensityName)
      .putReplacement(PLACEHOLDER_STYLE, style)
      .build();
  }

  private static final class InternalBuilder {
    private static void assertPlaceholder(String template, String placeholder) {
      if (!template.contains(placeholder)) {
        throw new IllegalArgumentException("template.contains('" + placeholder + "') == false");
      }
    }

    static InternalBuilder create(String template) {
      return new InternalBuilder(template);
    }

    private final String template;
    private final Map<String, String> replacementMap = new HashMap<>();

    private InternalBuilder(String template) {
      this.template = assertNotNull(template, "template == null");
    }

    final InternalBuilder putReplacement(String placeholder, String replacement) {
      assertNotNull(placeholder, "placeholder == null");
      assertNotNull(replacement, "replacement == null");
      replacementMap.put(placeholder, replacement);
      return this;
    }

    final Uri build() {
      String templateCopy = template;
      for (String placeholder : replacementMap.keySet()) {
        assertPlaceholder(templateCopy, placeholder);
        templateCopy = templateCopy.replace(placeholder, replacementMap.get(placeholder));
      }
      return Uri.parse(templateCopy);
    }
  }
}
