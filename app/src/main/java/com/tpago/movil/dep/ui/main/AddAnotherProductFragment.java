package com.tpago.movil.dep.ui.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tpago.movil.R;
import com.tpago.movil.dep.ui.ChildFragment;

/**
 * TODO
 *
 * @author hecvasro
 */
public class AddAnotherProductFragment extends ChildFragment<MainContainer> {
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
    return inflater.inflate(R.layout.d_fragment_add_another_account, container, false);
  }
}
