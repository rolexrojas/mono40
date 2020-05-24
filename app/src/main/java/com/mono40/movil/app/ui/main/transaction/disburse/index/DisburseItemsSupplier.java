package com.mono40.movil.app.ui.main.transaction.disburse.index;

import com.mono40.movil.app.ui.main.transaction.item.IndexItem;
import com.mono40.movil.util.function.Supplier;

import io.reactivex.Observable;

/**
 * @author hecvasro
 */
interface DisburseItemsSupplier extends Supplier<Observable<IndexItem>> {
}
