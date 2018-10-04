package com.tpago.movil.app.ui.main.transaction.disburse.index;

import com.tpago.movil.app.ui.Presentation;
import com.tpago.movil.app.ui.main.transaction.item.IndexItem;
import com.tpago.movil.product.Product;
import com.tpago.movil.product.disbursable.DisbursableProduct;

import java.util.List;

/**
 * @author hecvasro
 */
public interface PresentationDisburseIndex extends Presentation {

  void goBack();

  void setItems(List<IndexItem> items);

  void startProductTransaction(Product product);

  void startDisbursableProductTransaction(DisbursableProduct product);
}
