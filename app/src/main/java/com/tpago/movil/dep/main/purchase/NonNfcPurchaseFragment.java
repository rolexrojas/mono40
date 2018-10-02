package com.tpago.movil.dep.main.purchase;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tpago.movil.R;
import com.tpago.movil.d.ui.ChildFragment;
import com.tpago.movil.d.ui.main.AddAnotherProductFragment;
import com.tpago.movil.d.ui.main.MainContainer;
import com.tpago.movil.dep.graphics.Drawables;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @author hecvasro
 */
public final class NonNfcPurchaseFragment extends ChildFragment<MainContainer> {

  public static NonNfcPurchaseFragment create() {
    return new NonNfcPurchaseFragment();
  }

  private Unbinder unbinder;

  @BindView(R.id.image_view_art) ImageView artImageView;

  @OnClick(R.id.button_learn_more)
  final void onLearnMoreButtonClicked() {
    this.getContainer()
      .setChildFragment(AddAnotherProductFragment.newInstance(), true, true);
  }

  @Nullable
  @Override
  public View onCreateView(
    LayoutInflater inflater,
    @Nullable ViewGroup container,
    @Nullable Bundle savedInstanceState
  ) {
    return inflater.inflate(R.layout.d_fragment_purchase_fragment_non_nfc, container, false);
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    // Binds all the annotated resources, views and methods.
    unbinder = ButterKnife.bind(this, view);
  }

  @Override
  public void onStart() {
    super.onStart();
    // Sets the title.
    getContainer().setTitle(getString(R.string.screen_payments_commerce_title));
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

    // Unbinds all the annotated resources, views and methods.
    unbinder.unbind();
  }
}
