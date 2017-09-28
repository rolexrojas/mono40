package com.tpago.movil.d.ui.main.transaction.own;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.tpago.movil.R;
import com.tpago.movil.dep.UserStore;
import com.tpago.movil.dep.App;
import com.tpago.movil.d.data.StringHelper;
import com.tpago.movil.d.data.util.BinderFactory;
import com.tpago.movil.d.domain.Product;
import com.tpago.movil.d.domain.ProductManager;
import com.tpago.movil.d.ui.Dialogs;
import com.tpago.movil.d.ui.main.list.ListItemAdapter;
import com.tpago.movil.d.ui.main.list.ListItemHolderCreatorFactory;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.tpago.movil.d.domain.Product.checkIfAccount;

/**
 * @author Hector Vasquez
 */
public final class OwnTransactionCreationActivity extends AppCompatActivity implements
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

  public static Intent createLaunchIntent(Context context) {
    return new Intent(context, OwnTransactionCreationActivity.class);
  }

  private Unbinder unbinder;
  private ListItemAdapter adapter;

  @BindView(R.id.toolbar)
  Toolbar toolbar;
  @BindView(R.id.recycler_view)
  RecyclerView recyclerView;

  @Inject
  UserStore userStore;
  @Inject
  StringHelper stringHelper;
  @Inject
  ProductManager productManager;

  @Override
  protected void attachBaseContext(Context newBase) {
    super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if (requestCode == REQUEST_CODE_TRANSFER && resultCode == RESULT_OK) {
      this.setResult(RESULT_OK, data);
      this.finish();
    }
  }

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.d_activity_user_space);

    unbinder = ButterKnife.bind(this);

    App.get(this)
      .getComponent()
      .inject(this);

    setSupportActionBar(this.toolbar);
    final ActionBar actionBar = getSupportActionBar();
    actionBar.setDisplayHomeAsUpEnabled(true);

    this.toolbar.post(new Runnable() {
      @Override
      public void run() {
        toolbar.setTitle("Transacción entre cuentas");
        toolbar.setSubtitle(
          userStore.get()
            .phoneNumber()
            .formattedValued()
        );
      }
    });

    final ListItemHolderCreatorFactory holderCreatorFactory
      = new ListItemHolderCreatorFactory.Builder()
      .addCreator(Product.class, new OwnProductListItemHolderCreator(this))
      .build();
    final BinderFactory holderBinderFactory = new BinderFactory.Builder()
      .addBinder(
        Product.class,
        OwnProductListItemHolder.class,
        new OwnProductListItemHolderBinder(stringHelper)
      )
      .build();
    adapter = new ListItemAdapter(holderCreatorFactory, holderBinderFactory);
    for (Product product : productManager.getProductList()) {
      if (!Product.checkIfCreditCard(product) && !Product.checkIfLoan(product)) {
        adapter.add(product);
      }
    }
    recyclerView.setAdapter(adapter);
    recyclerView.setHasFixedSize(true);
    recyclerView.setItemAnimator(null);
    recyclerView
      .setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    final RecyclerView.ItemDecoration divider = new HorizontalDividerItemDecoration.Builder(this)
      .drawable(R.drawable.d_divider)
      .marginResId(R.dimen.space_horizontal_normal)
      .build();
    recyclerView.addItemDecoration(divider);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == android.R.id.home) {
      this.onBackPressed();
      return true;
    } else {
      return super.onOptionsItemSelected(item);
    }
  }

  @Override
  protected void onDestroy() {
    unbinder.unbind();

    super.onDestroy();
  }

  @Override
  public void onButtonClicked(int position) {
    boolean hasAccounts = false;
    for (Product product : this.productManager.getProductList()) {
      if (checkIfAccount(product)) {
        hasAccounts = true;
        break;
      }
    }
    if (hasAccounts) {
      this.startActivityForResult(
        OwnTransferActivity.createLaunchIntent(
          this,
          (Product) this.adapter.get(position)
        ),
        REQUEST_CODE_TRANSFER
      );
    } else {
      Dialogs.builder(this)
        .setTitle(R.string.we_are_sorry)
        .setMessage(
          "No tiene cuentas bancarias afiliadas para acreditar su avance de efectivo desde su tarjeta. Favor enrole sus cuentas e intente de nuevo."
        )
        .setPositiveButton(R.string.ok, null)
        .show();
    }
  }
}
