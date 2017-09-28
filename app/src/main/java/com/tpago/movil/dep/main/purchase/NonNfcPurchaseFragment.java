package com.tpago.movil.dep.main.purchase;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.tpago.movil.R;
import com.tpago.movil.d.ui.ChildFragment;
import com.tpago.movil.d.ui.main.MainContainer;
import com.tpago.movil.dep.graphics.Drawables;
import com.tpago.movil.dep.graphics.RadialGradientDrawable;

import butterknife.BindColor;
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

  @BindColor(R.color.common_background_gradient_dark_end)
  int backgroundEndColor;
  @BindColor(R.color.common_background_gradient_dark_start)
  int backgroundStartColor;

  @BindView(R.id.view_root)
  View rootView;
  @BindView(R.id.image_view_art)
  ImageView artImageView;

  @OnClick(R.id.button_learn_more)
  void onLearnMoreButtonClicked() {
    // TODO: Redirect to the about screen.
  }

  @Nullable
  @Override
  public View onCreateView(
    LayoutInflater inflater,
    @Nullable ViewGroup container,
    @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.d_fragment_purchase_fragment_non_nfc, container, false);
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    // Binds all the annotated resources, views and methods.
    unbinder = ButterKnife.bind(this, view);
    // Adds a listener that gets notified when the root view has been laid out.
    final ViewTreeObserver observer = rootView.getViewTreeObserver();
    if (observer.isAlive()) {
      observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
          // Removes the listener that gets notified when the root view has been laid out.
          final ViewTreeObserver observer = rootView.getViewTreeObserver();
          if (observer.isAlive()) {
            observer.removeOnGlobalLayoutListener(this);
          }
          // Sets the background of the fragment.
          rootView.setBackground(new RadialGradientDrawable(
            backgroundStartColor,
            backgroundEndColor,
            rootView.getHeight()));
        }
      });
    }
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
