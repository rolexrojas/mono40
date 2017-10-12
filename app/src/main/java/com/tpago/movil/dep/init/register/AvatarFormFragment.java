package com.tpago.movil.dep.init.register;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.tpago.movil.dep.Avatar;
import com.tpago.movil.R;
import com.tpago.movil.dep.AvatarCreationDialogFragment;
import com.tpago.movil.app.ui.FragmentQualifier;
import com.tpago.movil.app.ui.FragmentReplacer;
import com.tpago.movil.data.picasso.CircleTransformation;

import java.io.File;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @author hecvasro
 */
public final class AvatarFormFragment extends BaseRegisterFragment {

  private Unbinder unbinder;

  @Inject Avatar avatar;
  @Inject
  @FragmentQualifier
  FragmentReplacer fragmentReplacer;

  @BindView(R.id.image_view_avatar) ImageView avatarImageView;
  @BindView(R.id.button_move_to_next_screen) Button moveToNextScreenButton;

  static AvatarFormFragment create() {
    return new AvatarFormFragment();
  }

  @OnClick(R.id.button_move_to_next_screen)
  void onMoveToNextScreenButtonClicked() {
    fragmentReplacer.begin(EmailRegisterFormFragment.create())
      .addToBackStack()
      .transition(FragmentReplacer.Transition.SRFO)
      .commit();
  }

  @OnClick(R.id.image_view_avatar)
  void onAvatarImageViewClicked() {
    AvatarCreationDialogFragment.create(avatar.getFile())
      .show(getChildFragmentManager(), null);
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // Injects all the annotated dependencies.
    getRegisterComponent().inject(this);
  }

  @Nullable
  @Override
  public View onCreateView(
    LayoutInflater inflater,
    @Nullable ViewGroup container,
    @Nullable Bundle savedInstanceState
  ) {
    return inflater.inflate(R.layout.fragment_register_form_avatar, container, false);
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    // Binds all the annotated resources, views and methods.
    unbinder = ButterKnife.bind(this, view);
  }

  @Override
  public void onResume() {
    super.onResume();
    if (avatar.exists()) {
      final File file = avatar.getFile();
      final Context context = getActivity();
      moveToNextScreenButton.setText(R.string.next);
      Picasso.with(context)
        .invalidate(file);
      Picasso.with(context)
        .load(file)
        .resizeDimen(R.dimen.normalImageSize, R.dimen.normalImageSize)
        .transform(new CircleTransformation())
        .placeholder(R.drawable.profile_picture_placeholder_dark)
        .error(R.drawable.profile_picture_placeholder_dark)
        .noFade()
        .into(avatarImageView);
    } else {
      avatarImageView.setImageResource(R.drawable.profile_picture_placeholder_dark);
      moveToNextScreenButton.setText(R.string.laterAsVerb);
    }
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    // Unbinds all the annotated resources, views and methods.
    unbinder.unbind();
  }
}
