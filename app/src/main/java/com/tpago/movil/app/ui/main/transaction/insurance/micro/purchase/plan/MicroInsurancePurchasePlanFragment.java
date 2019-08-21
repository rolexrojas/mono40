package com.tpago.movil.app.ui.main.transaction.insurance.micro.purchase.plan;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.tpago.movil.R;
import com.tpago.movil.app.ui.activity.ActivityQualifier;
import com.tpago.movil.app.ui.fragment.FragmentReplacer;
import com.tpago.movil.app.ui.fragment.base.FragmentBase;
import com.tpago.movil.app.ui.item.ItemAdapter;
import com.tpago.movil.app.ui.item.ItemHelper;
import com.tpago.movil.app.ui.item.ItemTypeFactory;
import com.tpago.movil.app.ui.main.transaction.insurance.micro.purchase.MicroInsurancePurchaseActivity;
import com.tpago.movil.app.ui.main.transaction.insurance.micro.purchase.MicroInsurancePurchaseComponent;
import com.tpago.movil.app.ui.main.transaction.insurance.micro.purchase.term.MicroInsurancePurchaseTermFragment;
import com.tpago.movil.app.ui.main.transaction.item.IndexItem;
import com.tpago.movil.app.ui.main.transaction.item.IndexItemHolder;
import com.tpago.movil.app.ui.main.transaction.item.IndexItemHolderBinder;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public final class MicroInsurancePurchasePlanFragment extends FragmentBase
  implements MicroInsurancePurchasePlanPresentation {

  public static MicroInsurancePurchasePlanFragment create() {
    return new MicroInsurancePurchasePlanFragment();
  }

  @Inject
  @ActivityQualifier
  FragmentReplacer fragmentReplacer;

  private MicroInsurancePurchasePlanPresenter presenter;

  @BindView(R.id.recyclerView) RecyclerView recyclerView;

  private ItemAdapter adapter;

  @Override
  protected int layoutResId() {
    return R.layout.main_transaction_index;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Retrieves the dependency injector.
    final MicroInsurancePurchaseComponent component = MicroInsurancePurchaseActivity
      .get(this.getContext())
      .component();

    // Injects all annotated dependencies.
    component.inject(this);

    // Instantiates the presenter.
    this.presenter = MicroInsurancePurchasePlanPresenter.create(this, component);
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

    this.recyclerView.setAdapter(this.adapter);
    this.recyclerView.setHasFixedSize(true);
    this.recyclerView.setLayoutManager(ItemHelper.layoutManagerLinearVertical(context));
    this.recyclerView.addItemDecoration(ItemHelper.dividerLineHorizontal(context));
  }

  @Override
  public void onResume() {
    super.onResume();

    // Resumes the presenter.
    this.presenter.onPresentationResumed();
  }

  @Override
  public void onPause() {
    // Pauses the presenter.
    this.presenter.onPresentationPaused();

    super.onPause();
  }


  @Override
  public void setTitle(String text) {
    MicroInsurancePurchaseActivity.get(this.getContext())
      .toolbarManager()
      .setTitleText(text);
  }

  @Override
  public void setSubtitle(String text) {
    MicroInsurancePurchaseActivity.get(this.getContext())
      .toolbarManager()
      .setSubtitleText(text);
  }

  @Override
  public void setItems(List<IndexItem> items) {
    this.adapter.clear();
    for (IndexItem item : items) {
      this.adapter.add(item);
    }
  }

  @Override
  public void moveToNextScreen() {
    this.fragmentReplacer.begin(MicroInsurancePurchaseTermFragment.create())
      .transition(FragmentReplacer.Transition.SRFO)
      .addToBackStack()
      .commit();
  }
}
