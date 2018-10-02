package com.tpago.movil.d.ui;

import com.tpago.movil.app.ui.activity.base.ActivityBase;
import com.tpago.movil.dep.App;

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
