package com.tpago.movil.d.ui.main.purchase;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tpago.movil.R;
import com.tpago.movil.d.data.StringHelper;
import com.tpago.movil.d.data.util.BinderFactory;
import com.tpago.movil.d.domain.Product;
import com.tpago.movil.d.misc.Utils;
import com.tpago.movil.d.ui.ChildFragment;
import com.tpago.movil.d.ui.Dialogs;
import com.tpago.movil.d.ui.main.MainContainer;
import com.tpago.movil.d.ui.main.PinConfirmationDialogFragment;
import com.tpago.movil.d.ui.main.list.ListItemAdapter;
import com.tpago.movil.d.ui.main.list.ListItemHolder;
import com.tpago.movil.d.ui.main.list.ListItemHolderCreatorFactory;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author hecvasro
 */
@Deprecated
public class PurchaseFragment
  extends ChildFragment<MainContainer>
  implements PurchaseContainer,
  PurchaseScreen,
  ListItemHolder.OnClickListener,
  SelectedItemDecoration.Provider,
  PurchasePaymentDialogFragment.OnDismissedListener {
  private static final String TAG_PAYMENT_SCREEN = "paymentScreen";

  private static final String KEY_ACTIVATE_AUTOMATICALLY = "activeAutomatically";

  PurchaseComponent component;

  private Unbinder unbinder;
  private ListItemAdapter adapter;
  private boolean activateAutomatically;

  @Inject StringHelper stringHelper;
  @Inject PurchasePaymentOptionBinder paymentOptionBinder;
  @Inject PurchasePresenter presenter;

  @BindString(R.string.commerce_payment_option_ready_text_value) String readyMessage;

  @BindView(R.id.recycler_view) RecyclerView recyclerView;

  @NonNull
  public static PurchaseFragment newInstance(boolean activeAutomatically) {
    final Bundle bundle = new Bundle();
    bundle.putBoolean(KEY_ACTIVATE_AUTOMATICALLY, activeAutomatically);
    final PurchaseFragment fragment = new PurchaseFragment();
    fragment.setArguments(bundle);
    return fragment;
  }

  @NonNull
  public static PurchaseFragment newInstance() {
    return newInstance(false);
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // Injects all the annotated dependencies.
    component = DaggerPurchaseComponent.builder()
      .depMainComponent(getContainer().getComponent())
      .build();
    component.inject(this);
    final Bundle bundle = Utils.isNotNull(savedInstanceState) ? savedInstanceState : getArguments();
    activateAutomatically = Utils.isNotNull(bundle) && bundle.getBoolean(KEY_ACTIVATE_AUTOMATICALLY, false);
  }

  @Nullable
  @Override
  public View onCreateView(
    LayoutInflater inflater,
    @Nullable ViewGroup container,
    @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.d_fragment_payments_commerce, container, false);
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
      .addCreator(Product.class, new PurchasePaymentOptionListItemHolderCreator(this))
      .addCreator(String.class, new TextListItemHolderCreator())
      .build();
    final BinderFactory holderBinderFactory = new BinderFactory.Builder()
      .addBinder(Product.class, PurchasePaymentOptionHolder.class, paymentOptionBinder)
      .addBinder(String.class, TextListItemHolder.class, new TextListItemBinder())
      .build();
    adapter = new ListItemAdapter(holderCreatorFactory, holderBinderFactory);
    recyclerView.setAdapter(adapter);
    recyclerView.setHasFixedSize(true);
    recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL,
      false));
    final Resources resources = getResources();
    recyclerView.addItemDecoration(
      new SpaceDividerItemDecoration(
        resources.getDimensionPixelSize(R.dimen.commerce_payment_option_margin)));
    final int borderWidth = resources.getDimensionPixelOffset(
      R.dimen.commerce_payment_option_border_width);
    final int borderColor = ContextCompat.getColor(context,
      R.color.d_commerce_payment_option_border);
    final int borderRadius = resources.getDimensionPixelOffset(
      R.dimen.commerce_payment_option_border_radius);
    recyclerView.addItemDecoration(new SelectedItemDecoration(this, borderWidth, borderColor,
      borderRadius));
    // Attaches the screen to the presenter.
    presenter.attachScreen(this);
  }

  @Override
  public void onStart() {
    super.onStart();
    // Sets the title.
    getContainer().setTitle(getString(R.string.screen_payments_commerce_title));
    // Starts the presenter.
    presenter.start();
    if (activateAutomatically) {
      requestPin();
      activateAutomatically = false;
    }
  }

  @Override
  public void onResume() {
    super.onResume();
    presenter.resume();
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

  @Nullable
  @Override
  public PurchaseComponent getComponent() {
    return component;
  }

  @Override
  public void clearPaymentOptions() {
    adapter.clear();
  }

  @Override
  public void addPaymentOption(@NonNull Product product) {
    adapter.add(product);
  }

  @Override
  public void markAsSelected(@NonNull Product product) {
    int index = adapter.indexOf(readyMessage);
    if (index >= 0) {
      adapter.remove(index);
    }
    index = adapter.indexOf(product);
    adapter.add(index + 1, readyMessage);
  }

  @Override
  public void openPaymentScreen(@NonNull Product paymentOption) {
    final FragmentTransaction transaction = getChildFragmentManager()
      .beginTransaction()
      .setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out);
    PurchasePaymentDialogFragment.newInstance(paymentOption)
      .show(transaction, TAG_PAYMENT_SCREEN);
  }

  @Override
  public void requestPin() {
    final Display display = getActivity().getWindowManager().getDefaultDisplay();
    final Point size = new Point();
    display.getSize(size);
    final int originX = size.x / 2;
    final int originY = size.y / 2;
    PinConfirmationDialogFragment.show(
      getChildFragmentManager(),
      getString(R.string.activate_payment_methods),
      new PinConfirmationDialogFragment.Callback() {
        @Override
        public void confirm(String pin) {
          presenter.activateCards(pin);
        }
      },
      originX,
      originY);
  }

  @Override
  public void onActivationFinished(boolean succeeded) {
    PinConfirmationDialogFragment.dismiss(getChildFragmentManager(), succeeded);
  }

  @Override
  public void showGenericErrorDialog(String message) {
    Dialogs.builder(getContext())
      .setTitle(R.string.error_title)
      .setMessage(message)
      .setPositiveButton(R.string.error_positive_button_text, null)
      .show();
  }

  @Override
  public void showGenericErrorDialog() {
    showGenericErrorDialog(getString(R.string.error_generic));
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

  @Override
  public void onDismissed() {
    presenter.resume();
  }
}
