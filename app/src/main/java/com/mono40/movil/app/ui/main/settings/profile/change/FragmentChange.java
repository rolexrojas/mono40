package com.mono40.movil.app.ui.main.settings.profile.change;

import android.os.Bundle;
import androidx.annotation.Nullable;

import com.mono40.movil.R;
import com.mono40.movil.app.di.ComponentBuilderSupplier;
import com.mono40.movil.app.di.ComponentBuilderSupplierContainer;
import com.mono40.movil.app.ui.activity.toolbar.ActivityToolbar;
import com.mono40.movil.app.ui.activity.toolbar.FragmentToolbarRetained;

/**
 * @author hecvasro
 */
public final class FragmentChange extends FragmentToolbarRetained
  implements ComponentBuilderSupplierContainer {

  public static FragmentChange create() {
    return new FragmentChange();
  }

  private FragmentComponentChange component;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Instantiates the dependency injector.
    this.component = ActivityToolbar.get(this.getContext())
      .componentBuilderSupplier()
      .get(FragmentChange.class, FragmentComponentChange.Builder.class)
      .build();
  }

  @Override
  public void onStart() {
    super.onStart();

    // Sets the title of the fragment.
    ActivityToolbar.get(this.getContext())
      .toolbarManager()
      .setHomeButtonDrawable(R.drawable.ic_clear_white_24dp)
      .setTitleText("Cambiar mi contraseña");
  }

  @Override
  public ComponentBuilderSupplier componentBuilderSupplier() {
    return this.componentBuilderSupplier;
  }
}
