package com.tpago.movil.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.tpago.movil.R;
import com.tpago.movil.ui.widget.NumPadTextInput;
import com.tpago.movil.ui.widget.TextInput;
import com.tpago.movil.util.Strings;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author hecvasro
 */
public final class PlaygroundActivity extends AppCompatActivity {
  @BindView(R.id.text_input)
  TextInput textInput;
  @BindView(R.id.num_pad_text_input)
  NumPadTextInput numPadTextInput;

  @OnClick(R.id.button_content)
  void onContentButtonClicked() {
    textInput.setText(Strings.isEmpty(textInput.getText()) ? "809-882-9887" : null);
    numPadTextInput.setText(Strings.isEmpty(numPadTextInput.getText()) ? "809-882-9887" : null);
  }

  @OnClick(R.id.button_enabled)
  void onEnabledButtonClicked() {
    textInput.setEnabled(!textInput.isEnabled());
    numPadTextInput.setEnabled(!numPadTextInput.isEnabled());
  }

  @OnClick(R.id.button_error)
  void onErrorButtonClicked() {
    textInput.setErrorStateEnabled(!textInput.isErrorStateEnabled());
    numPadTextInput.setErrorStateEnabled(!numPadTextInput.isErrorStateEnabled());
  }

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_playground);
    ButterKnife.bind(this);
  }
}
