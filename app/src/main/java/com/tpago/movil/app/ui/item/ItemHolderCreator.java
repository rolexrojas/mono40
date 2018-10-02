package com.tpago.movil.app.ui.item;

import android.view.ViewGroup;

/**
 * @author hecvasro
 */
public interface ItemHolderCreator<IH extends ItemHolder> {

  IH create(ViewGroup parent);
}
