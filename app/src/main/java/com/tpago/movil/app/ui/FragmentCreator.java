package com.tpago.movil.app.ui;

import android.os.Parcelable;

import androidx.fragment.app.Fragment;

/**
 * @author hecvasro
 */
@Deprecated
public abstract class FragmentCreator implements Parcelable {

  public abstract Fragment create();

}
