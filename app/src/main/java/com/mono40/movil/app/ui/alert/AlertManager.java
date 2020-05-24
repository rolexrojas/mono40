package com.mono40.movil.app.ui.alert;

import android.content.Context;

import com.mono40.movil.R;
import com.mono40.movil.app.StringMapper;
import com.mono40.movil.util.ObjectHelper;

/**
 * @author hecvasro
 */
public final class AlertManager {

  public static AlertManager create(Context context, StringMapper stringMapper) {
    return new AlertManager(context, stringMapper);
  }

  private final Context context;
  private final StringMapper stringMapper;

  private AlertManager(Context context, StringMapper stringMapper) {
    this.context = ObjectHelper.checkNotNull(context, "context");
    this.stringMapper = ObjectHelper.checkNotNull(stringMapper, "stringMapper");
  }

  public final Alert.Builder builder() {
    return Alert.builder(this.context, this.stringMapper)
      .title(this.stringMapper.apply(R.string.weAreSorry))
      .message(this.stringMapper.apply(R.string.anUnexpectedErrorOccurred))
      .positiveButtonText(this.stringMapper.apply(R.string.ok));
  }

  public final void showAlertForGenericFailure() {
    this.builder()
      .show();
  }
}
