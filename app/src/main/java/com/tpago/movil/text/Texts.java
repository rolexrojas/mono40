package com.tpago.movil.text;

import android.content.Context;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;

import com.tpago.movil.util.Objects;

import uk.co.chrisjenx.calligraphy.CalligraphyTypefaceSpan;

/**
 * @author hecvasro
 */
public final class Texts {
  public static boolean isEmpty(CharSequence s) {
    return Objects.checkIfNull(s) || s.length() == 0;
  }

  public static boolean isNotEmpty(CharSequence s) {
    return !isEmpty(s);
  }

  public static String join(String delimiter, Object... tokens) {
    boolean firstTime = true;
    final StringBuilder builder = new StringBuilder();
    for (Object token : tokens) {
      if (firstTime) {
        firstTime = false;
      } else {
        builder.append(delimiter);
      }
      builder.append(token.toString());
    }
    return builder.toString();
  }

  public static CharSequence createContent(
    Context context,
    String content,
    String fontPath,
    int textColor,
    int textSize) {
    if (Texts.isEmpty(content)) {
      return null;
    } else {
      return Truss.create()
        .pushSpan(new ForegroundColorSpan(textColor))
        .pushSpan(new AbsoluteSizeSpan(textSize))
        .pushSpan(new CalligraphyTypefaceSpan(Typefaces.load(context, fontPath)))
        .append(content)
        .build();
    }
  }

  private Texts() {
    throw new AssertionError("Cannot be instantiated");
  }
}
