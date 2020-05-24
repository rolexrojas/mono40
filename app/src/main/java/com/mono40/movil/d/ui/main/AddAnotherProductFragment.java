package com.mono40.movil.d.ui.main;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mono40.movil.R;
import com.mono40.movil.d.ui.ChildFragment;

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
