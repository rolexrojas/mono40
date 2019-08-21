package com.tpago.movil.dep.init.intro;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tpago.movil.R;
import com.tpago.movil.dep.graphics.Drawables;
import com.tpago.movil.util.Memory;
import com.tpago.movil.util.ObjectHelper;

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
  private Boolean animationStarted = false;

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
    @Nullable Bundle savedInstanceState
  ) {
    return inflater.inflate(R.layout.fragment_intro_tab, container, false);
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    // Binds all annotated views and methods.
    unbinder = ButterKnife.bind(this, view);
    // Populates the art, title and description fields.
    final Bundle args = ObjectHelper.checkNotNull(this.getArguments(), "this.getArguments()");
    artImageView.setImageResource(args.getInt(KEY_ART_ID));
    titleTextView.setText(args.getInt(KEY_TITLE_ID));
    descriptionTextView.setText(args.getInt(KEY_DESCRIPTION_ID));
  }

  @Override
  public void onResume() {
    super.onResume();
    if(Memory.canDisplayImageAnimation(getContext())) {
      Drawables.startAnimationDrawable(artImageView);
      animationStarted = true;
    }
  }

  @Override
  public void onPause() {
    super.onPause();
    if(animationStarted) {
      Drawables.stopAnimationDrawable(artImageView);
    }
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    // Unbinds all annotated views and methods.
    unbinder.unbind();
  }
}
