package com.tpago.movil.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tpago.movil.R;

/**
 * TODO
 *
 * @author hecvasro
 */
public class SplashFragment extends Fragment {
  /**
   * TODO
   *
   * @return TODO
   */
  @NonNull
  public static SplashFragment newInstance() {
    return new SplashFragment();
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
    @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_splash, container, false);
  }
}
