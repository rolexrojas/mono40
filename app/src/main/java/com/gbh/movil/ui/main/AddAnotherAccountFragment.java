package com.gbh.movil.ui.main;

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
public class AddAnotherAccountFragment extends SubFragment {
  /**
   * TODO
   *
   * @return TODO
   */
  @NonNull
  public static AddAnotherAccountFragment newInstance() {
    return new AddAnotherAccountFragment();
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
    @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_add_another_account, container, false);
  }
}
