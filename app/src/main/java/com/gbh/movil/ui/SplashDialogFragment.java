package com.gbh.movil.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gbh.movil.R;

/**
 * TODO
 *
 * @author hecvasro
 */
public class SplashDialogFragment extends FullScreenDialogFragment {
  /**
   * TODO
   *
   * @return TODO
   */
  @NonNull
  public static SplashDialogFragment newInstance() {
    return new SplashDialogFragment();
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
    @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_splash, container, false);
  }
}
