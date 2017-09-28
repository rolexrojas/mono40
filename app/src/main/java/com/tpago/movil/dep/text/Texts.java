package com.tpago.movil.dep.text;

import android.content.Context;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;

import uk.co.chrisjenx.calligraphy.CalligraphyTypefaceSpan;

import static com.tpago.movil.dep.Objects.checkIfNull;

/**
 * @author hecvasro
 */
@Deprecated
public final class Texts {
  public static boolean checkIfEmpty(CharSequence s) {
    return checkIfNull(s) || s.length() == 0;
  }

  public static boolean checkIfNotEmpty(CharSequence s) {
    return !checkIfEmpty(s);
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
    if (Texts.checkIfEmpty(content)) {
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
