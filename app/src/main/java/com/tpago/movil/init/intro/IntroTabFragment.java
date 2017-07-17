package com.tpago.movil.init.intro;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tpago.movil.R;
import com.tpago.movil.graphics.Drawables;
import com.tpago.movil.util.Preconditions;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author hecvasro
 */
public final class IntroTabFragment extends Fragment {
  private static final String KEY_ART_ID = "artId";
  private static final String KEY_TITLE_ID = "titleId";
  private static final String KEY_DESCRIPTION_ID = "descriptionId";

  private Unbinder unbinder;

  @BindView(R.id.image_view_art) ImageView artImageView;
  @BindView(R.id.label_title) TextView titleTextView;
  @BindView(R.id.label_description) TextView descriptionTextView;

  static IntroTabFragment create(int artId, int titleId, int descriptionId) {
    final Bundle args = new Bundle();
    args.putInt(KEY_ART_ID, artId);
    args.putInt(KEY_TITLE_ID, titleId);
    args.putInt(KEY_DESCRIPTION_ID, descriptionId);
    final IntroTabFragment fragment = new IntroTabFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Nullable
  @Override
  public View onCreateView(
    LayoutInflater inflater,
    @Nullable ViewGroup container,
    @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_intro_tab, container, false);
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    // Binds all annotated views and methods.
    unbinder = ButterKnife.bind(this, view);
    // Populates the art, title and description fields.
    final Bundle args = Preconditions.assertNotNull(getArguments(), "getArguments() == null");
    artImageView.setImageResource(args.getInt(KEY_ART_ID));
    titleTextView.setText(args.getInt(KEY_TITLE_ID));
    descriptionTextView.setText(args.getInt(KEY_DESCRIPTION_ID));
  }

  @Override
  public void onResume() {
    super.onResume();
    Drawables.startAnimationDrawable(artImageView);
  }

  @Override
  public void onPause() {
    super.onPause();
    Drawables.stopAnimationDrawable(artImageView);
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    // Unbinds all annotated views and methods.
    unbinder.unbind();
  }
}
