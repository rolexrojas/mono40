package com.gbh.movil.ui.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gbh.movil.R;
import com.gbh.movil.ui.SubFragment;

/**
 * TODO
 *
 * @author hecvasro
 */
public class AddAnotherProductFragment extends SubFragment<MainContainer> {
  /**
   * TODO
   *
   * @return TODO
   */
  @NonNull
  public static AddAnotherProductFragment newInstance() {
    return new AddAnotherProductFragment();
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
    @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_add_another_account, container, false);
  }
}
