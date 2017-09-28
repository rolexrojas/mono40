package com.tpago.movil.d.ui;

import com.tpago.movil.dep.App;
import com.tpago.movil.app.ui.BaseActivity;

/**
 * @author hecvasro
 */
@Deprecated
public abstract class DepBaseActivity extends BaseActivity {

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
