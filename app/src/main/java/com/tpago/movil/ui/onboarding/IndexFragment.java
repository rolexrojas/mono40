package com.tpago.movil.ui.onboarding;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.tpago.movil.R;
import com.tpago.movil.ui.Backgrounds;
import com.tpago.movil.util.Objects;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import timber.log.Timber;

/**
 * @author hecvasro
 */
public final class IndexFragment extends Fragment {
  private static final float BACKGROUND_PERCENTAGE_RADIUS = 0.97F;

  private Unbinder unbinder;

  @BindColor(R.color.primary)
  int primaryColor;

  @BindView(R.id.relative_layout_logo)
  View rootView;
  @BindView(R.id.image_view_logo)
  ImageView logoImageView;

  public static IndexFragment newInstance() {
    return new IndexFragment();
  }

  @Nullable
  @Override
  public View onCreateView(
    LayoutInflater inflater,
    @Nullable ViewGroup container,
    @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_index, container, false);
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    unbinder = ButterKnife.bind(this, view);
    final ViewTreeObserver observer = rootView.getViewTreeObserver();
    if (observer.isAlive()) {
      observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
          if (Objects.isNotNull(rootView)) {
            final ViewTreeObserver observer = rootView.getViewTreeObserver();
            if (Objects.isNotNull(observer)) {
              observer.removeOnGlobalLayoutListener(this);
            }
            rootView.setBackground(Backgrounds.darkColoredRadialGradient(
              rootView.getContext(),
              rootView.getHeight()));
          }
        }
      });
    }
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    unbinder.unbind();
  }
}
