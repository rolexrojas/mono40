package com.mono40.movil.d.ui;

import com.mono40.movil.app.ui.activity.base.ActivityBase;
import com.mono40.movil.dep.App;

/**
 * @author hecvasro
 */
@Deprecated
public abstract class DepActivityBase extends ActivityBase {

  @Override
  protected void onResume() {
    super.onResume();

    App.get(this)
      .setVisible(true);
  }

  @Override
  protected void onPause() {
    App.get(this)
      .setVisible(false);

    super.onPause();
  }
}
