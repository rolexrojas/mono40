package com.tpago.movil.app.ui.main;

import android.os.Parcelable;
import android.support.v4.app.Fragment;

/**
 * @author hecvasro
 */
public abstract class FragmentCreator implements Parcelable {

  public abstract Fragment create();
}
