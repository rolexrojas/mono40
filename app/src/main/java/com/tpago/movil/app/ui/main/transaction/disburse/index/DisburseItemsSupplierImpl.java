package com.tpago.movil.app.ui.main.transaction.disburse.index;

import com.tpago.movil.app.ui.main.transaction.item.IndexItem;

import java.util.Set;

import io.reactivex.Observable;

/**
 * @author hecvasro
 */
final class DisburseItemsSupplierImpl implements DisburseItemsSupplier {

  static DisburseItemsSupplierImpl create(Set<DisburseItemsSupplier> itemsSuppliers) {
    return new DisburseItemsSupplierImpl(itemsSuppliers);
  }

  private final Observable<IndexItem> itemsSource;

  private DisburseItemsSupplierImpl(Set<DisburseItemsSupplier> itemsSuppliers) {
    this.itemsSource = Observable.fromIterable(itemsSuppliers)
      .flatMap(DisburseItemsSupplier::get);
  }

  @Override
  public Observable<IndexItem> get() {
    return this.itemsSource;
  }
}
