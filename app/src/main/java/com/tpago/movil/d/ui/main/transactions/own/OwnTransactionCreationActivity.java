package com.tpago.movil.d.ui.main.transactions.own;

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
import com.tpago.movil.User;
import com.tpago.movil.UserStore;
import com.tpago.movil.app.App;
import com.tpago.movil.d.data.StringHelper;
import com.tpago.movil.d.data.util.BinderFactory;
import com.tpago.movil.d.domain.Product;
import com.tpago.movil.d.domain.ProductManager;
import com.tpago.movil.d.ui.main.list.ListItemAdapter;
import com.tpago.movil.d.ui.main.list.ListItemHolderCreatorFactory;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * @author Hector Vasquez
 */
public final class OwnTransactionCreationActivity extends AppCompatActivity implements OwnProductListItemHolder.OnButtonClickedListener {
  public static Intent createLaunchIntent(Context context) {
    return new Intent(context, OwnTransactionCreationActivity.class);
  }

  private Unbinder unbinder;
  private ListItemAdapter adapter;

  @BindView(R.id.toolbar) Toolbar toolbar;
  @BindView(R.id.recycler_view) RecyclerView recyclerView;

  @Inject UserStore userStore;
  @Inject StringHelper stringHelper;
  @Inject ProductManager productManager;

  @Override
  protected void attachBaseContext(Context newBase) {
    super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
  }

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.d_activity_user_space);

    unbinder = ButterKnife.bind(this);

    App.get(this)
      .getComponent()
      .inject(this);

    setSupportActionBar(toolbar);
    final ActionBar actionBar = getSupportActionBar();
    actionBar.setTitle("Transacción entre cuentas");
    final User user = userStore.get();
    actionBar.setSubtitle(user.getPhoneNumber().formattedValued());
    actionBar.setDisplayHomeAsUpEnabled(true);

    final ListItemHolderCreatorFactory holderCreatorFactory = new ListItemHolderCreatorFactory.Builder()
      .addCreator(Product.class, new OwnProductListItemHolderCreator(this))
      .build();
    final BinderFactory holderBinderFactory = new BinderFactory.Builder()
      .addBinder(Product.class, OwnProductListItemHolder.class, new OwnProductListItemHolderBinder(stringHelper))
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
    recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    final RecyclerView.ItemDecoration divider = new HorizontalDividerItemDecoration.Builder(this)
      .drawable(R.drawable.d_divider)
      .marginResId(R.dimen.space_horizontal_normal)
      .build();
    recyclerView.addItemDecoration(divider);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == android.R.id.home) {
      onBackPressed();
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
    // TODO
  }
}
