package com.tpago.movil.app.ui.main.transaction.disburse.index;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tpago.movil.R;
import com.tpago.movil.api.Api;
import com.tpago.movil.app.ui.activity.ActivityQualifier;
import com.tpago.movil.app.ui.activity.toolbar.ActivityToolbar;
import com.tpago.movil.app.ui.fragment.FragmentReplacer;
import com.tpago.movil.app.ui.item.ItemAdapter;
import com.tpago.movil.app.ui.item.ItemHelper;
import com.tpago.movil.app.ui.item.ItemTypeFactory;
import com.tpago.movil.app.ui.main.BaseMainFragment;
import com.tpago.movil.app.ui.main.transaction.disburse.product.ArgumentDisburseProduct;
import com.tpago.movil.app.ui.main.transaction.item.IndexItem;
import com.tpago.movil.app.ui.main.transaction.item.IndexItemHolder;
import com.tpago.movil.app.ui.main.transaction.item.IndexItemHolderBinder;
import com.tpago.movil.app.ui.main.transaction.summary.TransactionSummaryUtil;
import com.tpago.movil.app.ui.main.transaction.summary.TransactionSummaryDialogFragment;
import com.tpago.movil.d.ui.main.DepMainActivityBase;
import com.tpago.movil.d.ui.main.recipient.index.disburse.DisbursementActivity;
import com.tpago.movil.product.Product;
import com.tpago.movil.product.disbursable.DisbursableProduct;
import com.tpago.movil.product.disbursable.DisbursableProductStore;
import com.tpago.movil.util.ObjectHelper;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author hecvasro
 */
public final class FragmentDisburseIndex extends BaseMainFragment
  implements PresentationDisburseIndex {

  private static final int REQUEST_CODE = 42;

  public static FragmentDisburseIndex create() {
    return new FragmentDisburseIndex();
  }

  @BindView(R.id.recyclerView) RecyclerView recyclerView;

  @Inject
  @ActivityQualifier
  FragmentReplacer fragmentReplacer;

  @Inject PresenterDisburseIndex presenter;
  @Inject DisbursableProductStore disbursableProductStore;
  @Inject Api api;

  private ItemAdapter adapter;

  @LayoutRes
  @Override
  protected int layoutResId() {
    return R.layout.main_transaction_index;
  }

  @Override
  protected int titleResId() {
    return R.string.withdraw;
  }

  @Override
  protected String subTitle() {
    return "";
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Injects all annotated dependencies.
    final FragmentComponentDisburseIndex component = DepMainActivityBase.get(this.getActivity())
      .componentBuilderSupplier()
      .get(FragmentDisburseIndex.class, FragmentComponentDisburseIndex.Builder.class)
      .disburseModule(FragmentModuleDisburseIndex.create(this))
      .build();
    component.inject(this);
  }

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    final Context context = this.getContext();

    final ItemTypeFactory typeFactory = ItemTypeFactory.builder()
      .type(IndexItem.class, IndexItem.type())
      .build();
    this.adapter = ItemAdapter.builder(typeFactory)
      .itemType(
        IndexItem.class,
        IndexItemHolder::create,
        IndexItemHolderBinder.create()
      )
      .build();

    this.api.fetchDisbursableProducts()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSubscribe((d) -> {})
        .doFinally(() -> {})
        .subscribe((products) -> {
          this.disbursableProductStore.sync(products).subscribeOn(Schedulers.io())
              .observeOn(AndroidSchedulers.mainThread())
              .doOnSubscribe((d) -> {})
              .doFinally(() -> {})
              .subscribe(() -> {
                this.adapter.notifyDataSetChanged();
                this.onResume();
              }, (e) -> {});
        }, (e) -> {});

    this.recyclerView.setAdapter(this.adapter);
    this.recyclerView.setHasFixedSize(true);
    this.recyclerView.setLayoutManager(ItemHelper.layoutManagerLinearVertical(context));
    this.recyclerView.addItemDecoration(ItemHelper.dividerLineHorizontal(context));
  }

  @Override
  public void onResume() {
    super.onResume();
    this.presenter.onPresentationResumed();
  }

  @Override
  public void onPause() {
    this.presenter.onPresentationPaused();
    super.onPause();
  }

  @Override
  public void goBack() {
    this.fragmentReplacer.manager()
      .popBackStack();
  }

  @Override
  public void setItems(List<IndexItem> items) {
    ObjectHelper.checkNotNull(items, "items");
    this.adapter.clear();
    for (IndexItem item : items) {
      this.adapter.add(item);
    }
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == REQUEST_CODE) {
      if (resultCode == Activity.RESULT_OK) {
        TransactionSummaryDialogFragment.create(TransactionSummaryUtil.unwrap(data))
          .show(this.getFragmentManager(), null);
      }
    }
  }

  @Override
  public void startProductTransaction(Product product) {
    final Intent intent = DisbursementActivity.intentBuilder()
      .context(this.getContext())
      .creditCard(product)
      .build();
    this.startActivityForResult(intent, REQUEST_CODE);
  }

  @Override
  public void startDisbursableProductTransaction(DisbursableProduct product) {
    final ActivityToolbar.Argument argument = ArgumentDisburseProduct.builder()
      .product(product)
      .build();
    final Intent intent = ActivityToolbar.intentBuilder()
      .context(this.getContext())
      .argument(argument)
      .build();
    this.startActivityForResult(intent, REQUEST_CODE);
  }
}
