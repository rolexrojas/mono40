package com.tpago.movil.ui;

import com.tpago.movil.ui.widget.NumPad;
import com.tpago.movil.ui.widget.TextInput;
import com.tpago.movil.util.Objects;
import com.tpago.movil.util.Preconditions;

/**
 * @author hecvasro
 */
public abstract class NumPadTextInput implements NumPad.OnDotClickedListener,
  NumPad.OnDigitClickedListener,
  NumPad.OnDeleteClickedListener {
  private final TextInput textInput;

  private NumPad numPad;

  public NumPadTextInput(TextInput textInput) {
    this.textInput = Preconditions.checkNotNull(textInput, "textInput == null");
  }

  protected final TextInput getTextInput() {
    return textInput;
  }

  public final void setNumPad(NumPad numPad) {
    if (Objects.isNotNull(this.numPad)) {
      this.numPad.setOnDotClickedListener(null);
      this.numPad.setOnDigitClickedListener(null);
      this.numPad.setOnDeleteClickedListener(null);
    }
    this.numPad = numPad;
    if (Objects.isNotNull(this.numPad)) {
      this.numPad.setOnDotClickedListener(this);
      this.numPad.setOnDigitClickedListener(this);
      this.numPad.setOnDeleteClickedListener(this);
    }
  }

  @Override
  public void onDotClicked() {
  }
}
