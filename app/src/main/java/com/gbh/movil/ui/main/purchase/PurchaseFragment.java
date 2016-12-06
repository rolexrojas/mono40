package com.gbh.movil.ui.main.purchase;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gbh.movil.R;
import com.gbh.movil.data.StringHelper;
import com.gbh.movil.data.util.BinderFactory;
import com.gbh.movil.domain.Product;
import com.gbh.movil.misc.Utils;
import com.gbh.movil.ui.ChildFragment;
import com.gbh.movil.ui.main.MainContainer;
import com.gbh.movil.ui.main.PinConfirmationDialogFragment;
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
public class PurchaseFragment extends ChildFragment<MainContainer>
  implements PurchaseContainer, PurchaseScreen, ListItemHolder.OnClickListener,
  SelectedItemDecoration.Provider {
  private static final String TAG_PAYMENT_SCREEN = "paymentScreen";
  private static final String TAG_PIN_CONFIRMATION = "pinConfirmation";

  /**
   * TODO
   */
  private static final String KEY_ACTIVATE_AUTOMATICALLY = "activeAutomatically";

  PurchaseComponent component;

  private Unbinder unbinder;
  private ListItemAdapter adapter;
  private boolean activateAutomatically;

  @Inject
  StringHelper stringHelper;
  @Inject
  PurchasePaymentOptionBinder paymentOptionBinder;
  @Inject
  PurchasePresenter presenter;

  @BindString(R.string.commerce_payment_option_ready_text_value)
  String readyMessage;

  @BindView(R.id.recycler_view)
  RecyclerView recyclerView;

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
      .mainComponent(getContainer().getComponent())
      .build();
    component.inject(this);
    final Bundle bundle = Utils.isNotNull(savedInstanceState) ? savedInstanceState : getArguments();
    if (Utils.isNotNull(bundle)) {
      activateAutomatically = bundle.getBoolean(KEY_ACTIVATE_AUTOMATICALLY, false);
    } else  {
      activateAutomatically = false;
    }
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
    recyclerView.addItemDecoration(new SpaceDividerItemDecoration(resources.getDimensionPixelSize(
      R.dimen.commerce_payment_option_margin)));
    final int borderWidth = resources.getDimensionPixelOffset(
      R.dimen.commerce_payment_option_border_width);
    final int borderColor = ContextCompat.getColor(context,
      R.color.commerce_payment_option_border);
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
    final FragmentTransaction transaction = getChildFragmentManager().beginTransaction()
      .setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out);
    PurchasePaymentDialogFragment.newInstance(paymentOption)
      .show(transaction, TAG_PAYMENT_SCREEN);
  }

  @Override
  public void requestPin() {
    final FragmentManager manager = getChildFragmentManager();
    final Fragment fragment = manager.findFragmentByTag(TAG_PIN_CONFIRMATION);
    if (Utils.isNotNull(fragment) && fragment instanceof PinConfirmationDialogFragment) {
      ((PinConfirmationDialogFragment) fragment).dismiss();
    }
    final Display display = getActivity().getWindowManager().getDefaultDisplay();
    final Point size = new Point();
    display.getSize(size);
    PinConfirmationDialogFragment.newInstance((size.x / 2), (size.y / 2),
      "Activar cuenta para comprar", new PinConfirmationDialogFragment.Callback() {
        @Override
        public void confirm(@NonNull String pin) {
          presenter.activeCards(pin);
        }
      })
      .show(manager, TAG_PIN_CONFIRMATION);
  }

  @Override
  public void onActivationFinished(boolean succeeded) {
    final Fragment fragment = getChildFragmentManager().findFragmentByTag(TAG_PIN_CONFIRMATION);
    if (Utils.isNotNull(fragment) && fragment instanceof PinConfirmationDialogFragment) {
      ((PinConfirmationDialogFragment) fragment).resolve(succeeded);
    }
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
