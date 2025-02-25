package com.tpago.movil.app.ui.item;

/**
 * @author hecvasro
 */
public interface ItemHolderBinder<I extends Item, IH extends ItemHolder> {

  void bind(I item, IH holder);
}
