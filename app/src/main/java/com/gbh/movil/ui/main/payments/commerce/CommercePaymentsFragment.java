package com.gbh.movil.ui.main.payments.commerce;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gbh.movil.R;
import com.gbh.movil.data.util.BinderFactory;
import com.gbh.movil.domain.Product;
import com.gbh.movil.ui.SubFragment;
import com.gbh.movil.ui.main.MainContainer;
import com.gbh.movil.ui.main.list.ListItemAdapter;
import com.gbh.movil.ui.main.list.ListItemHolder;
import com.gbh.movil.ui.main.list.ListItemHolderCreatorFactory;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * TODO
 *
 * @author hecvasro
 */
public class CommercePaymentsFragment extends SubFragment<MainContainer>
  implements CommercePaymentsScreen, ListItemHolder.OnClickListener,
  SelectedItemDecoration.Provider {
  private Unbinder unbinder;

  private ListItemAdapter adapter;

  @Inject
  PaymentOptionBinder paymentOptionBinder;
  @Inject
  CommercePaymentsPresenter presenter;

  @BindString(R.string.commerce_payment_option_ready_text_value)
  String readyMessage;

  @BindView(R.id.recycler_view)
  RecyclerView recyclerView;

  /**
   * TODO
   *
   * @return TODO
   */
  @NonNull
  public static CommercePaymentsFragment newInstance() {
    return new CommercePaymentsFragment();
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // Injects all the annotated dependencies.
    final CommercePaymentsComponent component = DaggerCommercePaymentsComponent.builder()
      .mainComponent(container.getComponent())
      .build();
    component.inject(this);
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
    @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_payments_commerce, container, false);
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    // Initializes all reusable variables.
    final Context context = getContext();
    // Binds all the annotated views and methods.
    unbinder = ButterKnife.bind(this, view);
    // Prepares the payment options list.
    final ListItemHolderCreatorFactory holderCreatorFactory = new ListItemHolderCreatorFactory
      .Builder()
      .addCreator(Product.class, new PaymentOptionListItemHolderCreator(this))
      .addCreator(String.class, new TextListItemHolderCreator())
      .build();
    final BinderFactory holderBinderFactory = new BinderFactory.Builder()
      .addBinder(Product.class, PaymentOptionHolder.class, paymentOptionBinder)
      .addBinder(String.class, TextListItemHolder.class, new TextListItemBinder())
      .build();
    adapter = new ListItemAdapter(holderCreatorFactory, holderBinderFactory);
    recyclerView.setAdapter(adapter);
    recyclerView.setHasFixedSize(true);
    recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL,
      false));
    final Resources resources = getResources();
    recyclerView.addItemDecoration(new SpaceDividerItemDecoration(resources.getDimensionPixelSize(
      R.dimen.commerce_payment_option_container_default_margin)));
    final int borderWidth = resources.getDimensionPixelOffset(
      R.dimen.commerce_payment_option_container_selected_border_width);
    final int borderColor = ContextCompat.getColor(context,
      R.color.commerce_payment_option_container_selected_border);
    final int borderRadius = resources.getDimensionPixelOffset(
      R.dimen.commerce_payment_option_container_default_border_radius);
    recyclerView.addItemDecoration(new SelectedItemDecoration(this, borderWidth, borderColor,
      borderRadius));
    // Attaches the screen to the presenter.
    presenter.attachScreen(this);
  }

  @Override
  public void onStart() {
    super.onStart();
    // Sets the title.
    container.setTitle(getString(R.string.screen_payments_commerce_title));
    // Starts the presenter.
    presenter.start();
  }

  @Override
  public void onStop() {
    super.onStop();
    // Stops the presenter.
    presenter.stop();
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    // Detaches the screen from the presenter.
    presenter.detachScreen();
    // Unbinds all the annotated views and methods.
    unbinder.unbind();
  }

  @Override
  public void clearPaymentOptions() {
    final int count = adapter.getItemCount();
    if (count > 0) {
      adapter.clear();
      adapter.notifyItemRangeRemoved(0, count);
    }
  }

  @Override
  public void addPaymentOption(@NonNull Product product) {
    adapter.add(product);
    adapter.notifyItemInserted(adapter.getItemCount());
  }

  @Override
  public void markAsSelected(@NonNull Product product) {
    int index = adapter.indexOf(readyMessage);
    if (index >= 0) {
      adapter.remove(index);
      adapter.notifyItemRemoved(index);
    }
    index = adapter.indexOf(product);
    adapter.add(index + 1, readyMessage);
    adapter.notifyItemChanged(index);
    adapter.notifyItemInserted(index + 1);
  }

  @Override
  public void onClick(int position) {
    final Object item = adapter.get(position);
    if (item instanceof Product) {
      presenter.onPaymentOptionSelected((Product) item);
    }
  }

  @Override
  public int getSelectedItemPosition() {
    return adapter.indexOf(presenter.getSelectedPaymentOption());
  }
}
