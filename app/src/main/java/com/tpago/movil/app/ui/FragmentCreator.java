package com.tpago.movil.app.ui;

import android.os.Parcelable;
import android.support.v4.app.Fragment;

/**
 * @author hecvasro
 */
@Deprecated
public abstract class FragmentCreator implements Parcelable {

  public abstract Fragment create();

}
