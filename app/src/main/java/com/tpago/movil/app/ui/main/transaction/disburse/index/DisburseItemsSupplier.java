package com.tpago.movil.app.ui.main.transaction.disburse.index;

import com.tpago.movil.app.ui.main.transaction.item.IndexItem;
import com.tpago.movil.util.function.Supplier;

import io.reactivex.Observable;

/**
 * @author hecvasro
 */
interface DisburseItemsSupplier extends Supplier<Observable<IndexItem>> {
}
