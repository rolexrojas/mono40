package com.tpago.movil.init.register;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.tpago.movil.Avatar;
import com.tpago.movil.R;
import com.tpago.movil.app.AvatarCreationDialogFragment;
import com.tpago.movil.graphics.CircleTransformation;

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

  @Inject
  Avatar avatar;

  @BindView(R.id.image_view_avatar)
  ImageView avatarImageView;

  static AvatarFormFragment create() {
    return new AvatarFormFragment();
  }

  @OnClick(R.id.button_move_to_next_screen)
  void onMoveToNextScreenButtonClicked() {
    // TODO
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
    @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_register_form_profile_picture, container, false);
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
    final File file = avatar.getFile();
    final Context context = getActivity();
    Picasso.with(context).invalidate(file);
    Picasso.with(context)
      .load(file)
      .resizeDimen(R.dimen.widget_image_avatar_large, R.dimen.widget_image_avatar_large)
      .transform(new CircleTransformation())
      .placeholder(R.drawable.widget_image_dark_avatar_placeholder_large)
      .error(R.drawable.widget_image_dark_avatar_placeholder_large)
      .noFade()
      .into(avatarImageView);
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    // Unbinds all the annotated resources, views and methods.
    unbinder.unbind();
  }
}
