package com.tpago.movil.d.ui.main.recipient.index.disburse;

import static android.app.Activity.RESULT_OK;
import static com.tpago.movil.d.domain.Product.checkIfCreditCard;
import static com.tpago.movil.util.Objects.checkIfNotNull;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import com.tpago.movil.R;
import com.tpago.movil.UserStore;
import com.tpago.movil.d.data.StringHelper;
import com.tpago.movil.d.data.util.BinderFactory;
import com.tpago.movil.d.domain.Product;
import com.tpago.movil.d.domain.ProductManager;
import com.tpago.movil.d.ui.ChildFragment;
import com.tpago.movil.d.ui.Dialogs;
import com.tpago.movil.d.ui.main.MainContainer;
import com.tpago.movil.d.ui.main.list.ListItemAdapter;
import com.tpago.movil.d.ui.main.list.ListItemHolderCreatorFactory;
import com.tpago.movil.d.ui.main.recipient.index.category.TransactionSummaryDialogFragment;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import javax.inject.Inject;

/**
 * @author Hector Vasquez
 */
public final class DisbursementFragment extends ChildFragment<MainContainer> implements
  OwnProductListItemHolder.OnButtonClickedListener {

  private static final String KEY_TRANSACTION_ID = "transactionId";

  private static final int REQUEST_CODE_TRANSFER = 0;

  static Intent serializeResult(String transactionId) {
    final Intent intent = new Intent();
    intent.putExtra(KEY_TRANSACTION_ID, transactionId);
    return intent;
  }

  public static String deserializeResult(Intent intent) {
    return intent.getStringExtra(KEY_TRANSACTION_ID);
  }

  public static DisbursementFragment create() {
    return new DisbursementFragment();
  }

  private Unbinder unbinder;
  private ListItemAdapter adapter;

  @BindView(R.id.recycler_view)
  RecyclerView recyclerView;

  @Inject
  UserStore userStore;
  @Inject
  StringHelper stringHelper;
  @Inject
  ProductManager productManager;

  private String requestResult = null;

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if (requestCode == REQUEST_CODE_TRANSFER && resultCode == RESULT_OK) {
      this.requestResult = deserializeResult(data);
    }
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    this.getContainer()
      .getComponent()
      .inject(this);
  }

  @Nullable
  @Override
  public View onCreateView(
    LayoutInflater inflater,
    @Nullable ViewGroup container,
    @Nullable Bundle savedInstanceState
  ) {
    return inflater.inflate(R.layout.fragment_disbursement, container, false);
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    unbinder = ButterKnife.bind(this, view);

    // Prepares the actions and recipients list.
    final ListItemHolderCreatorFactory holderCreatorFactory = new ListItemHolderCreatorFactory
      .Builder()
      .addCreator(Product.class, new OwnProductListItemHolderCreator(this))
      .build();
    final BinderFactory binderFactory = new BinderFactory.Builder()
      .addBinder(
        Product.class,
        OwnProductListItemHolder.class,
        new OwnProductListItemHolderBinder(stringHelper)
      )
      .build();
    adapter = new ListItemAdapter(holderCreatorFactory, binderFactory);
    for (Product product : this.productManager.getProductList()) {
      if (checkIfCreditCard(product)) {
        adapter.add(product);
      }
    }
    recyclerView.setAdapter(adapter);
    recyclerView.setHasFixedSize(true);
    final Context context = getContext();
    recyclerView
      .setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
    final RecyclerView.ItemDecoration divider = new HorizontalDividerItemDecoration.Builder(context)
      .drawable(R.drawable.d_divider)
      .marginResId(R.dimen.space_horizontal_normal)
      .showLastDivider()
      .build();
    recyclerView.addItemDecoration(divider);
  }

  @Override
  public void onStart() {
    super.onStart();

    // Sets the title.
    this.getContainer()
      .setTitle(this.getString(R.string.disburse));
  }

  @Override
  public void onResume() {
    super.onResume();

    if (checkIfNotNull(this.requestResult)) {
      TransactionSummaryDialogFragment.create(null, true, requestResult)
        .show(getChildFragmentManager(), null);
      this.requestResult = null;
    }
  }

  @Override
  public void onDestroyView() {
    unbinder.unbind();

    super.onDestroyView();
  }

  @Override
  public void onButtonClicked(int position) {
    boolean hasAccounts = false;
    for (Product product : this.productManager.getProductList()) {
      if (Product.checkIfAccount(product)) {
        hasAccounts = true;
        break;
      }
    }
    if (hasAccounts) {
      final Product product = (Product) this.adapter.get(position);

      this.startActivityForResult(
        DisbursementActivity.createLaunchIntent(
          this.getContext(),
          product
        ),
        REQUEST_CODE_TRANSFER
      );
    } else {
      Dialogs.builder(this.getActivity())
        .setTitle(R.string.we_are_sorry)
        .setMessage(
          "No tiene cuentas bancarias afiliadas para acreditar su avance de efectivo desde su tarjeta. Favor enrole sus cuentas e intente de nuevo."
        )
        .setPositiveButton(R.string.ok, null)
        .show();
    }
  }
}
