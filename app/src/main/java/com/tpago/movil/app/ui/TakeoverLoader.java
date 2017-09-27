package com.tpago.movil.app.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;

import com.google.auto.value.AutoValue;
import com.tpago.movil.R;

/**
 * @author Hector Vasquez
 */
public final class TakeoverLoader extends BaseDialogFragment {

  public static final String TAG = TakeoverLoader.class.getSimpleName();

  public static TakeoverLoader create() {
    return new TakeoverLoader();
  }

  @Override
  protected int layoutResId() {
    return R.layout.loader_takeover;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Sets the style of the dialog fragment.
    this.setStyle(DialogFragment.STYLE_NO_FRAME, R.style.TakeoverLoaderTheme);
  }

  @AutoValue
  public static abstract class ShowEvent {

    public static ShowEvent create() {
      return new AutoValue_TakeoverLoader_ShowEvent();
    }
  }

  @AutoValue
  public static abstract class HideEvent {

    public static HideEvent create() {
      return new AutoValue_TakeoverLoader_HideEvent();
    }
  }
}
