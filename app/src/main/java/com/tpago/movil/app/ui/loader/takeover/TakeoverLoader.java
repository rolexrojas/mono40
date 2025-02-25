package com.tpago.movil.app.ui.loader.takeover;

import androidx.fragment.app.FragmentManager;

import com.tpago.movil.app.ui.fragment.FragmentHelper;
import com.tpago.movil.app.ui.loader.Loader;
import com.tpago.movil.util.ObjectHelper;

/**
 * @author hecvasro
 */
public final class TakeoverLoader implements Loader {

  private static final String TAG = TakeoverLoader.class.getCanonicalName();

  public static TakeoverLoader create(FragmentManager fragmentManager) {
    return new TakeoverLoader(fragmentManager);
  }

  private final FragmentManager fragmentManager;

  public TakeoverLoader(FragmentManager fragmentManager) {
    this.fragmentManager = ObjectHelper.checkNotNull(fragmentManager, "fragmentManager");
  }

  @Override
  public void show() {
    this.hide();

    TakeoverLoaderDialogFragment.create()
      .show(this.fragmentManager, TAG);
  }

  @Override
  public void hide() {
    FragmentHelper.dismissByTag(this.fragmentManager, TAG);
  }
}
