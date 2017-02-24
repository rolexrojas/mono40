package com.tpago.movil.widget;

import android.text.SpannableString;
import android.text.Spanned;

import com.tpago.movil.text.style.AlphaForegroundColorSpan;
import com.tpago.movil.util.Preconditions;

/**
 * @author hecvasro
 */
public class TextInputLoadIndicator implements LoadIndicator {
  private final TextInput textInput;

  public TextInputLoadIndicator(TextInput textInput) {
    this.textInput = Preconditions.checkNotNull(textInput, "textInput == null");
  }

  private void setAlpha(float alpha) {
    final CharSequence text = textInput.getText();
    final SpannableString spannableString = new SpannableString(text);
    spannableString.setSpan(
      new AlphaForegroundColorSpan(textInput.getCurrentTextColor(), alpha),
      0,
      text.length(),
      Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
    textInput.setText(spannableString);
  }

  @Override
  public void start() {
    setAlpha(0.5F);
  }

  @Override
  public void stop() {
    setAlpha(1.0F);
  }
}
