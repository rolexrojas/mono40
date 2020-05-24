package com.mono40.movil.app.ui.main.transaction.disburse.index;

import com.mono40.movil.app.ui.Presentation;
import com.mono40.movil.app.ui.main.transaction.item.IndexItem;
import com.mono40.movil.product.Product;
import com.mono40.movil.product.disbursable.DisbursableProduct;

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
