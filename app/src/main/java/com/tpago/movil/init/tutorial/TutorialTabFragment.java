package com.tpago.movil.init.tutorial;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tpago.movil.R;
import com.tpago.movil.util.Preconditions;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author hecvasro
 */
public final class TutorialTabFragment extends Fragment {
  private static final String KEY_TITLE_ID = "titleId";
  private static final String KEY_DESCRIPTION_ID = "descriptionId";

  private Unbinder unbinder;

  @BindView(R.id.label_title) TextView titleTextView;
  @BindView(R.id.label_description) TextView descriptionTextView;

  static TutorialTabFragment create(int titleId, int descriptionId) {
    final Bundle args = new Bundle();
    args.putInt(KEY_TITLE_ID, titleId);
    args.putInt(KEY_DESCRIPTION_ID, descriptionId);
    final TutorialTabFragment fragment = new TutorialTabFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Nullable
  @Override
  public View onCreateView(
    LayoutInflater inflater,
    @Nullable ViewGroup container,
    @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_tutorial_tab, container, false);
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    // Binds all annotated views and methods.
    unbinder = ButterKnife.bind(this, view);
    // Populates the art, title and description fields.
    final Bundle args = Preconditions.checkNotNull(getArguments(), "getArguments() == null");
    titleTextView.setText(args.getInt(KEY_TITLE_ID));
    descriptionTextView.setText(args.getInt(KEY_DESCRIPTION_ID));
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    // Unbinds all annotated views and methods.
    unbinder.unbind();
  }
}
