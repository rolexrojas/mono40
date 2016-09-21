package com.tpago.movil.ui.main.payments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tpago.movil.R;
import com.tpago.movil.ui.main.MainScreen;

/**
 * TODO
 *
 * @author hecvasro
 */
public class PaymentsFragment extends Fragment {
  private MainScreen parentScreen;

  /**
   * TODO
   *
   * @return TODO
   */
  @NonNull
  public static PaymentsFragment newInstance() {
    return new PaymentsFragment();
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    final Activity activity = getActivity();
    if (activity instanceof MainScreen) {
      parentScreen = (MainScreen) activity;
    }
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
    @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_payments, container, false);
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    if (parentScreen != null) {
      parentScreen.setTitle(getString(R.string.payments));
    }
  }

  @Override
  public void onDetach() {
    super.onDetach();
    parentScreen = null;
  }
}
